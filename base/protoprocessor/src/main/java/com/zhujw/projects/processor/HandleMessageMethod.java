package com.zhujw.projects.processor;

import javax.lang.model.element.ExecutableElement;

public class HandleMessageMethod {

    private ExecutableElement mElement;

    private int mCmdId;

    public ExecutableElement getElement() {
        return mElement;
    }

    public void setElement(ExecutableElement element) {
        mElement = element;
    }

    public int getCmdId() {
        return mCmdId;
    }

    public void setCmdId(int cmdId) {
        mCmdId = cmdId;
    }
}
