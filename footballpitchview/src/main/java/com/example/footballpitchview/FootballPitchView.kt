package com.example.footballpitchview

/**
 * Created by anweshmishra on 04/05/18.
 */

import android.graphics.*
import android.content.Context
import android.view.View
import android.view.MotionEvent

class FootballPitchView (ctx : Context) : View(ctx) {
    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                
            }
        }
        return true
    }
}