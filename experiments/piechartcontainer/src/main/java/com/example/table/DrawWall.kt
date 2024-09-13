package com.example.table

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Path.Direction
import android.graphics.Rect
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.example.piechartcontainer.ui.theme.color_0xFFE79315


fun drawWall(
    drawScope: DrawScope,
    widget: TableWidget,
    layoutSize: LayoutSize,
    drawable: Drawable,
) {
    drawable.setBounds(
        0,
        0,
        layoutSize.wallWidth.toInt(),
        layoutSize.wallHeight.toInt()
    )
    with(drawScope) {
        drawContext.canvas.nativeCanvas.apply {
            // 计算需要绘制的列和行数
            if(widget.angle == 0f) {
                drawNormalDrawable(canvas = this, widget, layoutSize, drawable)
            } else {
                drawRotateDrawable(canvas = this, widget, layoutSize, drawable)
            }
        }
    }
}

private fun drawNormalDrawable(
    canvas: Canvas,
    widget: TableWidget,
    layoutSize: LayoutSize,
    drawable: Drawable,
){
    canvas.save()
    canvas.clipRect(
        widget.offset.x, widget.offset.y,
        widget.offset.x + widget.width, widget.offset.y + widget.height
    )
    val numColumns = (widget.width.toInt() / 100) + 1
    val numRows = (widget.height.toInt() / 100) + 1
    for (i in 0 until numColumns) {
        for (j in 0 until numRows) {
            // 设置 drawable 的位置
            val x = widget.offset.x + i * layoutSize.wallWidth
            val y = widget.offset.y + j * layoutSize.wallHeight
            canvas.save()
            canvas.translate(x, y)
            drawable.draw(canvas)
            canvas.restore()
        }
    }
    canvas.restore()
}

private fun drawRotateDrawable(
    canvas: Canvas,
    widget: TableWidget,
    layoutSize: LayoutSize,
    drawable: Drawable,
){
    //旋转的需要根据实际数据及bo效果修改
    canvas.save()
    canvas.translate(widget.offset.x, widget.offset.y)
    canvas.rotate(widget.angle)
    canvas.clipRect(0f, 0f,widget.width, widget.height)
    val numColumns = (widget.width.toInt() / 100) + 1
    val numRows = (widget.height.toInt() / 100) + 1
    for (i in 0 until numColumns) {
        for (j in 0 until numRows) {
            // 设置 drawable 的位置
            val x = i * layoutSize.wallWidth
            val y = j * layoutSize.wallHeight
            canvas.save()
            canvas.translate(x, y)
            drawable.draw(canvas)
            canvas.restore()
        }
    }
    canvas.restore()
}
