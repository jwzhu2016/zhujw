package com.zhujw.projects;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zhujw.projects.msghandle.MsgHandleActivity;

import me.andrew.xhook.HookCallback;
import me.andrew.xhook.HookManager;
import me.andrew.xhook.Param;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new ProcessorTest();
        Button button = findViewById(R.id.handle_msg);
        TextView hook = findViewById(R.id.hook);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MsgHandleActivity.class);
                startActivity(intent);
            }
        });
//        hook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                HookManager.addHookMethod(MainActivity.class, "test", null, new HookCallback() {
//                    @Override
//                    protected void beforeHookedMethod(Param param) throws Throwable {
//                        param.setCallOrigin(false);
//                    }
//
//                    @Override
//                    protected void afterHookedMethod(Param param) throws Throwable {
//                        Log.d("mainactivity", "afterHookedMethod test");
//                    }
//                });
//                HookManager.startHook();
//            }
//        });
    }

    private void test() {
        Log.d("mainactivity", " test");
    }
}
