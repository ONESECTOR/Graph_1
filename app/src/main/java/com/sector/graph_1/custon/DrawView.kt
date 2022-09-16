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
import androidx.core.content.ContextCompat
import com.sector.graph_1.R
import java.lang.Float.max
import kotlin.math.abs
import kotlin.math.roundToInt

class DrawView(context: Context, attrs: AttributeSet): View(context, attrs) {

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
            color = ContextCompat.getColor(context, R.color.primary)
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
        else if(count > 2) {
            if (count % 2 == 0) { // здесь мы считаем, какая по счету идет итерация, так как если итерация нечетная, то мы забываем про предыдующую нечетную итерацию. Точно так же и с нечетной
                x2 = x
                y2 = y
            } else {
                x1 = x
                y1 = y
            }
        }
        else {
            x2 = x
            y2 = y
        }
    }

    private fun actionUp() {
        // рисует линию от текущей точки до указанной, следующее рисование пойдет уже от указанной точки
        drawPoint(mCurX, mCurY)

        if (count == 2 || count > 2) {
            algorithmBresenham(x1, y1, x2, y2)
        }
    }

    fun clearCanvas() {
        resetValues()
        mPath.reset()
        invalidate()
    }

    private fun resetValues() {
        count = 0
        x1 = 0f
        y1 = 0f
        x2 = 0f
        y2 = 0f
    }

    private fun algorithmBresenham(x1: Float, y1: Float, x2: Float, y2: Float) {

        // Определение четверти
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
                mPath.lineTo(x, y)

                x+=dx
                y+= dy * lengthY / lengthX
            }
        }
        else {
            var x = x1
            var y = y1

            length++
            repeat(length.toInt()) {
                mPath.lineTo(x, y)

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