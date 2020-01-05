package com.gorillamo.ui.choosablelist

import android.app.Activity
import android.os.Bundle
import android.widget.TextView

class MockActivity :Activity(){

    lateinit var textView:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layout)

        textView = findViewById<TextView>(com.gorillamo.ui.choosablelist.test.R.id.result)

        val chooseable = findViewById<ChoosableList>(com.gorillamo.ui.choosablelist.test.R.id.choosable)
        chooseable.setItemClickcallback {
            textView.text = it.toString()
        }
    }

    companion object{
        var layout:Int = 0
    }

}