package com.zhujw.projects.kotlin

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_module1.*

/**
 * author: zhujw
 * date: on 2018-12-8.
 */

class Module1Activity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_module1)
        module1_tv.text = "welcome to kotlin"
    }
}