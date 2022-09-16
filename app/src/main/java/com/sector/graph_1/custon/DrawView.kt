package com.sector.graph_1.custon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import java.lang.Float.max
import kotlin.math.abs
import kotlin.math.roundToInt

class DrawView(context: Context, attrs: AttributeSet): View(context, attrs) {

    // Для устранения ошибок из версии 1, когда появлялся угол,
    // нужно вычислить приращения по осям x и y (dy и dx)

    private var mPaint = Paint()
    private var mPath = Path()

    private var mCurX = 0f
    private var mCurY = 0f

    private var x1 = 0f
    private var y1 = 0f

    private var x2 = 0f
    private var y2 = 0f

    private var count = 0

    init {
        mPaint.apply {
            color = Color.RED
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            strokeCap = Paint.Cap.ROUND
            strokeWidth = 10f
            isAntiAlias = true
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawPath(mPath, mPaint)
    }

    private fun actionDown(x: Float, y: Float) {
        // ставит "курсор" на место нажатия, откуда пойдет рисование
        mPath.moveTo(x, y)

        // запоминаем координаты места, куда нажали
        mCurX = x
        mCurY = y

        count++
        if (count < 2) {
            x1 = x
            y1 = y
        }
        else {
            x2 = x
            y2 = y
        }
    }

    private fun actionUp() {
        // рисует линию от текущей точки до указанной, следующее рисование пойдет уже от указанной точки
        mPath.lineTo(mCurX, mCurY)
        mPath.lineTo(mCurX, mCurY + 1)
        mPath.lineTo(mCurX + 1, mCurY + 1)
        mPath.lineTo(mCurX + 1, mCurY)

        Log.d("sex20", "x1: ${x1.roundToInt()}, y1: ${y1.roundToInt()}, x2: ${x2.roundToInt()}, y2: ${y2.roundToInt()}")

        if (count == 2) {
            algVersion2(x1, y1, x2, y2)
            //alg(x1, y1, x2, y2)
        }
    }

    fun clearCanvas() {
        count = 0
        x1 = 0f
        y1 = 0f
        x2 = 0f
        y2 = 0f
        mPath.reset()
        invalidate()
    }

    private fun algVersion2(x1: Float, y1: Float, x2: Float, y2: Float) {
        var dx = if ((x2 - x1) >= 0) 1 else -1
        var dy = if ((y2 - y1) >= 0) 1 else -1

        val lengthX = abs(x2 - x1)
        val lengthY = abs(y2 - y1)

        var length = max(lengthX, lengthY)

        if (lengthY <= lengthX) {
            var x = x1
            var y = y1

            length++

            repeat(length.toInt()) {
                drawPoint(x, y)
                //mPath.lineTo(x, y)

                Log.d("sex", "x: ${x.roundToInt()}, y: ${y.roundToInt()}")

                x+=dx
                y+= dy * lengthY / lengthX
            }
        }
        else {
            var x = x1
            var y = y1

            length++
            repeat(length.toInt()) {
                drawPoint(x, y)
                x+= dx * lengthX / lengthY
                y+= dy
            }
        }
    }

    private fun drawPoint(x: Float, y: Float) {
        mPath.lineTo(x, y)
        mPath.lineTo(x, y + 1)
        mPath.lineTo(x + 1, y + 1)
        mPath.lineTo(x + 1, y)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x
        val y = event.y

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                actionDown(x, y)
            }
            MotionEvent.ACTION_UP -> actionUp()
        }
        invalidate()
        return true
    }
}