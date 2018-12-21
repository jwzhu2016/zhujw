package com.zhujw.projects.processor;

/**
 * author: zhujw
 * date: on 2018-12-8.
 */

public interface IProcessor {

    Object onMsgHandle(int cmdId, Object... arg);
}
