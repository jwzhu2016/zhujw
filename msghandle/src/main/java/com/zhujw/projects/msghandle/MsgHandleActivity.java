package com.zhujw.projects.msghandle;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.zhujw.projects.common.MsgId;
import com.zhujw.projects.processor.IProcessor;
import com.zhujw.projects.common.ProcessorCenter;
import com.zhujw.projects.route.RouteBoard;

/**
 * author: zhujw
 * date: on 2018-12-8.
 */
@Route(path = "/msghandle/msg")
public class MsgHandleActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_handle);
        IProcessor processor = ProcessorCenter.getProcessor("ProcessorTest");
        if (processor != null) {
            String result = (String) processor.onMsgHandle(MsgId.HANDLER_TEST, "MsgHandle");
            TextView textView = findViewById(R.id.msg);
            textView.setText(result);
        }
    }
}
