package com.zhujw.projects.processor;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class ProtoProcessor extends AbstractProcessor {
    private Filer mFiler;
    private Elements mElementUtils;
    private Messager mMessager;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        mFiler = processingEnv.getFiler();
        mElementUtils = processingEnv.getElementUtils();
        mMessager = processingEnv.getMessager();
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Collections.singleton(ProtoAnnotation.class.getCanonicalName());
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        LinkedHashMap<TypeElement, List<HandleMessageMethod>> targetClassMap = findAllMethod(roundEnvironment);
        if (targetClassMap.size() == 0) {
            return false;
        }
        for (Map.Entry<TypeElement, List<HandleMessageMethod>> item : targetClassMap.entrySet()) {

            TypeElement closeTypeElement = item.getKey();
            String packageName = mElementUtils.getPackageOf(closeTypeElement).getQualifiedName().toString();
            String className = item.getKey().getSimpleName().toString();

            GenerationClass generationClass = new GenerationClass(packageName, className);
            TypeSpec.Builder classGenBuilder = generationClass.createClass();

            MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("onMsgHandle")
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(int.class, "cmdId")
                    .addParameter(Object[].class, "value")
                    .varargs()
                    .returns(Object.class);

            methodBuilder.addCode("switch($N) {\n", "cmdId");

            // all annotation method in class
            List<HandleMessageMethod> methods = item.getValue();
            for (HandleMessageMethod method : methods) {
                methodBuilder.addCode("case $L:\n", method.getCmdId());
                ExecutableElement element = method.getElement();
                List<? extends VariableElement> parameters = element.getParameters();

                if (parameters.size() > 0) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append(String.format("    mTarget.%s(", element.getSimpleName()));
                    for (int i = 0; i < parameters.size(); i++) {
                        if (i > 0) {
                            stringBuilder.append(", ");
                        }
                        stringBuilder.append(String.format("(%s) value[%d]", parameters.get(i).asType().toString(), i));
                    }
                    stringBuilder.append(")");
                    if (element.getReturnType().getKind() != TypeKind.VOID) {
                        methodBuilder.addStatement("return " + stringBuilder.toString());
                    } else {
                        methodBuilder.addStatement(stringBuilder.toString());
                        methodBuilder.addStatement("return null");
                    }
                } else {
                    if (element.getReturnType().getKind() != TypeKind.VOID) {
                        methodBuilder.addStatement("return $N.$N()", "mTarget", element.getSimpleName());
                    } else {
                        methodBuilder.addStatement("$N.$N()", "mTarget", element.getSimpleName());
                        methodBuilder.addStatement("return null");
                    }

                }
            }

            methodBuilder.addCode("default: \n" +
                    "   return false;\n" +
                    "}\n");
            classGenBuilder.addMethod(methodBuilder.build());

            //create java file
            JavaFile javaFile = JavaFile.builder(packageName, classGenBuilder.build()).build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException ex) {
                error(closeTypeElement, "Unable to write binding for type %s: %s", closeTypeElement, ex.getMessage());
            }
        }
        return true;
    }

    private void error(Element element, String message, Object... args) {
        printMessage(Diagnostic.Kind.ERROR, element, message, args);
    }

    private void printMessage(Diagnostic.Kind kind, Element element, String message, Object[] args) {
        if (args.length > 0) {
            message = String.format(message, args);
        }
        processingEnv.getMessager().printMessage(kind, message, element);
    }

    private LinkedHashMap<TypeElement, List<HandleMessageMethod>> findAllMethod(RoundEnvironment roundEnvironment) {
        LinkedHashMap<TypeElement, List<HandleMessageMethod>> targetClassMap = new LinkedHashMap<>();
        Collection<? extends Element> elements = roundEnvironment.getElementsAnnotatedWith(ProtoAnnotation.class);
        for (Element element : elements) {
            if (element.getKind() != ElementKind.METHOD) {
                mMessager.printMessage(Diagnostic.Kind.ERROR, "only support class");
                continue;
            }

            TypeElement enclosingElement = (TypeElement) element.getEnclosingElement();
            ExecutableElement executableElement = (ExecutableElement) element;

            List<HandleMessageMethod> findLogList = targetClassMap.get(enclosingElement);
            if (findLogList == null) {
                findLogList = new ArrayList<>();
                targetClassMap.put(enclosingElement, findLogList);
            }

            ProtoAnnotation annotation = executableElement.getAnnotation(ProtoAnnotation.class);
            HandleMessageMethod changeFuncInfo = new HandleMessageMethod();
            changeFuncInfo.setElement(executableElement);
            changeFuncInfo.setCmdId(annotation.cmdId());
            findLogList.add(changeFuncInfo);
        }

        return targetClassMap;
    }
}
