package com.example.footballpitchview

/**
 * Created by anweshmishra on 04/05/18.
 */

import android.graphics.*
import android.content.Context
import android.view.View
import android.view.MotionEvent

class FootballPitchView (ctx : Context) : View(ctx) {

    private val renderer : Renderer = Renderer(this)

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {
        renderer.render(canvas, paint)
    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                renderer.handleTap()
            }
        }
        return true
    }

    data class State (var prevScale : Float = 0f, var dir : Float = 0f, var j : Int = 0) {

        val scales : Array<Float> = arrayOf(0f, 0f, 0f)

        fun update(stopcb : (Float) -> Unit) {
            scales[j] += 0.1f * dir
            if (Math.abs(scales[j] - prevScale) > 1) {
                scales[j] = prevScale + dir
                j += dir.toInt()
                if (j == scales.size || j == -1) {
                    j -= dir.toInt()
                    dir = 0f
                    prevScale = scales[j]
                    stopcb(prevScale)
                }
            }
        }

        fun startUpdating(startcb : () -> Unit) {
            if (dir == 0f) {
                dir = 1 - 2 * prevScale
                startcb()
            }
        }
    }

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(updatecb : () -> Unit) {
            if (animated) {
                updatecb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start() {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class FootballPitch(var i : Int, val state : State = State()) {

        fun draw(canvas : Canvas, paint : Paint) {
            val w : Float = canvas.width.toFloat()
            val h : Float = canvas.height.toFloat()
            val r : Float = Math.min(w, h)/10
            val updatedH : Float = (h/2) * state.scales[0]
            canvas.save()
            canvas.translate(w/2, h/2)
            paint.style = Paint.Style.FILL
            paint.color = Color.parseColor("#558B2F")
            canvas.drawRect(RectF(-w/2, -updatedH, w/2, updatedH), paint)
            paint.color = Color.WHITE
            paint.style = Paint.Style.STROKE
            canvas.drawLine(-w/2 * state.scales[1], 0f, w/2 * state.scales[1], 0f, paint)
            canvas.drawArc(RectF(-r, -r, r, r), 0f, 360f * state.scales[2], false, paint)
            canvas.restore()
        }

        fun startUpdating(startcb : () -> Unit) {
            state.startUpdating(startcb)
        }

        fun update(stopcb : (Float) -> Unit) {
            state.update(stopcb)
        }
    }

    data class Renderer(var view : FootballPitchView) {

        private val animator : Animator = Animator(view)

        private val footballPitch : FootballPitch = FootballPitch(0)

        fun render(canvas : Canvas, paint : Paint) {
            canvas.drawColor(Color.parseColor("#212121"))
            footballPitch.draw(canvas, paint)
            animator.animate {
                footballPitch.update {
                    animator.stop()
                }
            }
        }

        fun handleTap() {
            footballPitch.startUpdating {
                animator.start()
            }
        }
    }
}