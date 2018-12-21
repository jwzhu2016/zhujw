package com.zhujw.projects;

import com.zhujw.projects.common.MsgId;
import com.zhujw.projects.processor.ProtoAnnotation;
import com.zhujw.projects.common.ProcessorCenter;

/**
 * author: zhujw
 * date: on 2018-12-10.
 */
 class ProcessorTest {

    ProcessorTest() {
        ProcessorCenter.register(this);
    }

    @ProtoAnnotation(cmdId = MsgId.HANDLER_TEST)
    public String onTest(String msg) {
        return "test_" + msg;
    }
}
