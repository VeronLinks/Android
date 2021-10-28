package com.example.session201fragments

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import com.example.session201fragments.databinding.ActivitySixthBinding

class CustomCanvas(context: Context?) : View(context)
{
    val brush = Paint()
    var path = Path()

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas : Canvas) {
        canvas.drawRGB(255, 255, 255)
        var width = canvas.width
        var height = canvas.height
        brush.setARGB(255, 0, 0, 0)
        brush.strokeWidth = 5.0f
        brush.style = Paint.Style.STROKE
        var rectangle = RectF(0f, 0f,
            width.toFloat(),
            height.toFloat())
        canvas.drawOval(rectangle, brush)
        var smallerMeasure: Int
        smallerMeasure = if (width < height) width else height
        brush.style = Paint.Style.FILL
        brush.setARGB(255, 255, 255, 0)
        canvas.drawCircle((width / 2).toFloat(),
            (height / 2).toFloat(),
            (smallerMeasure / 2).toFloat(),
            brush)
        path.moveTo(20f, (height / 2).toFloat());
        path.lineTo(60f, (height / 2 - 30).toFloat())
        path.lineTo(100f, (height / 2 - 60).toFloat())
        path.lineTo(140f, (height / 2 - 90).toFloat())
        path.lineTo(180f, (height / 2 - 120).toFloat())
        path.lineTo(240f, (height / 2 - 150).toFloat())
        path.lineTo(300f, (height / 2 - 180).toFloat())
        path.lineTo(360f, (height / 2 - 210).toFloat())
        brush.setARGB(255, 255, 0, 0)
        brush.textSize = 30f
        canvas.drawTextOnPath(resources.getString(R.string.hello_world),
            path, 0f, 0f, brush)
    }
}

class SixthActivity : AppCompatActivity() {

    private val bindings : ActivitySixthBinding by lazy {
        ActivitySixthBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(bindings.root)
        var canvas = CustomCanvas(this)
        bindings.canvasLayout.addView(canvas)
        supportActionBar?.hide()
    }

}