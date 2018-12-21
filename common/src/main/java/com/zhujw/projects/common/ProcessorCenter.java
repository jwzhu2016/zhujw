package com.zhujw.projects.common;

import com.zhujw.projects.processor.IProcessor;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

/**
 * author: zhujw
 * date: on 2018-12-8.
 */

public class ProcessorCenter {
    private static Map<String, IProcessor> mCenterMap;
    private static volatile ProcessorCenter sProcessorCenter;

    public static ProcessorCenter getInstance() {
        if (sProcessorCenter == null) {
            synchronized (ProcessorCenter.class) {
                if (sProcessorCenter == null) {
                    sProcessorCenter = new ProcessorCenter();
                }
            }
        }
        return sProcessorCenter;
    }

    public static void register(Object object) {
        if (mCenterMap == null) {
            mCenterMap = new HashMap<>();
        }
        mCenterMap.put(object.getClass().getSimpleName(), getProcessor(object));
    }

    private static IProcessor getProcessor(Object service) {
        String clsName = service.getClass().getName() + "_MsgHandle";
        try {
            Class clazz = Class.forName(clsName);
            Constructor<? extends IProcessor> bindingCtor = (Constructor<? extends IProcessor>) clazz.getConstructor(service.getClass());
            return bindingCtor.newInstance(service);
        } catch (Exception e) {
            return null;
        }
    }

    public static IProcessor getProcessor(String name) {
        if (mCenterMap != null) {
            return mCenterMap.get(name);
        } else {
            return null;
        }
    }

    public static void unRegister(String name) {
        mCenterMap.remove(name);
    }
}
