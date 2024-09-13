package com.example.table

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import com.example.piechartcontainer.ui.theme.color_0xFF14CABF
import com.example.piechartcontainer.ui.theme.color_0xFF212121
import com.example.piechartcontainer.ui.theme.color_0xFF4E8AFF
import com.example.piechartcontainer.ui.theme.color_0xFFFFFFFF


fun drawCircleTable(
    drawScope: DrawScope,
    widget: TableWidget,
    pathClip: Path,
    paint: Paint,
    layoutSize: LayoutSize,
    drawables: Drawables,
    sizeType: SizeType,

    combineBgColor: Int = 0,
    combineText: String? = null,
    combineContentColor: Int = color_0xFF4E8AFF.toArgb(),

    tableName: String,
    tableNameTextColor: Int = color_0xFFFFFFFF.toArgb(),

    bookTime: String? = null,
    bookTimeContentColor: Int = color_0xFFFFFFFF.toArgb(),

    guestZoneText: String = "40",
    guestZoneBgColor: Int = 0,
    guestZoneContentColor: Int = color_0xFF212121.toArgb(),
) {
    pathClip.apply {
        val offsetX = widget.offset.x
        val offsetY = widget.offset.y
        addOval(
            androidx.compose.ui.geometry.Rect(
                offsetX, offsetY,
                offsetX + widget.radius * 2, offsetY + widget.radius * 2
            )
        )
    }
    with(drawScope) {
        clipPath(path = pathClip) {
            paint.setColor(color_0xFF14CABF.toArgb())
            drawContext.canvas.nativeCanvas.apply {
                drawCircle(
                    widget.offset.x + widget.radius,
                    widget.offset.y + widget.radius,
                    widget.radius,
                    paint
                )

                drawables.combine?.setTint(combineContentColor)
                drawCombineInfo(
                    canvas = this,
                    widget = widget,
                    paint = paint,
                    layoutSize = layoutSize,
                    drawable = drawables.combine,
                    combineBgColor = combineBgColor,
                    sizeType = sizeType,
                    combineText = combineText,
                    combineContentColor = combineContentColor,
                )

                if (sizeType != SizeType.SMALL_CIRCLE) {
                    drawGuestNum(
                        canvas = this,
                        widget = widget,
                        paint = paint,
                        layoutSize = layoutSize,
                        drawable = drawables.chair,
                        guestZoneBgColor = guestZoneBgColor,
                        guestText = guestZoneText,
                        guestContentColor = guestZoneContentColor
                    )
                }

                drawTableName(
                    canvas = this,
                    widget = widget,
                    layoutSize = layoutSize,
                    drawables = drawables,
                    tableName = tableName,
                    tableNameTextSize = if (sizeType == SizeType.SMALL_CIRCLE) {
                        layoutSize.smallTableNameTextSizePx
                    } else {
                        layoutSize.largeTableNameTextSizePx
                    },
                    tableNameTextColor = tableNameTextColor,
                    paint = paint,
                    bookTime = if (sizeType != SizeType.LARGE_CIRCLE) null else bookTime,
                    bookTimeContentColor = bookTimeContentColor,
                )
            }
        }
    }
}

private fun drawCombineInfo(
    canvas: Canvas,
    widget: TableWidget,
    layoutSize: LayoutSize,
    drawable: Drawable? = null,
    paint: Paint,
    sizeType: SizeType,
    combineBgColor: Int = 0,
    combineText: String? = null,
    combineContentColor: Int = color_0xFF4E8AFF.toArgb(),
) {
    var contentWidth = layoutSize.iconSizePx
    var textWidth = 0f
    if (combineText != null) {
        paint.typeface = Typeface.DEFAULT
        paint.textSize = layoutSize.combineTextSizePx
        textWidth = paint.measureText(combineText)
        contentWidth += layoutSize.iconDiv + textWidth
    }
    val padding = if (sizeType == SizeType.SMALL_CIRCLE) {
        layoutSize.smallCombinePaddingPx
    } else {
        layoutSize.largeCombinePaddingPx
    }
    val left = widget.offset.x + widget.radius - contentWidth / 2
    val top = widget.offset.y + padding

    if (combineBgColor != 0) {
        paint.setColor(combineBgColor)
        canvas.drawRoundRect(
            left - padding,
            top - padding - 10, //此处是让顶部圆角被clip掉
            left + contentWidth + padding,
            top + layoutSize.iconSizePx + padding,
            layoutSize.combineBackgroundCorner,
            layoutSize.combineBackgroundCorner,
            paint
        )
    }

    drawIconWithText(
        canvas = canvas,
        layoutSize = layoutSize,
        drawable = drawable,
        paint = paint,
        contentColor = combineContentColor,
        text = combineText,
        textWidth = textWidth,
        left = left,
        top = top,
        div = layoutSize.iconDiv,
    )
}

private fun drawGuestNum(
    canvas: Canvas,
    widget: TableWidget,
    layoutSize: LayoutSize,
    drawable: Drawable? = null,
    paint: Paint,
    guestZoneBgColor: Int = 0,
    guestText: String,
    guestContentColor: Int = color_0xFF4E8AFF.toArgb(),
) {

    val left = widget.offset.x
    val bottom = widget.offset.y + widget.radius * 2

    if (guestZoneBgColor != 0) {
        paint.setColor(guestZoneBgColor)
        canvas.drawRect(
            left,
            bottom,
            left + widget.radius * 2,
            bottom - layoutSize.guestZoneHeightPx,
            paint
        )
    }

    //需要整体计算 icon与文本的总宽度，再绘制
    var contentWidth = layoutSize.iconSizePx
    paint.typeface = Typeface.DEFAULT
    paint.textSize = layoutSize.guestTextSizePx
    val textWidth = paint.measureText(guestText)
    contentWidth += layoutSize.iconDiv + textWidth

    drawIconWithText(
        canvas = canvas,
        layoutSize = layoutSize,
        drawable = drawable,
        paint = paint,
        contentColor = guestContentColor,
        text = guestText,
        textWidth = textWidth,
        left = left + widget.radius - contentWidth / 2,
        top = bottom - layoutSize.guestZoneHeightPx / 2 - layoutSize.iconSizePx / 2,
        div = layoutSize.iconDiv,
    )
}

fun drawIconWithText(
    canvas: Canvas,
    layoutSize: LayoutSize,
    drawable: Drawable? = null,
    paint: Paint,
    contentColor: Int,
    text: String? = null,
    textWidth: Float,
    left: Float,
    top: Float,
    div: Float,
) {

    drawable?.setTint(contentColor)
    drawable?.setBounds(
        left.toInt(),
        top.toInt(),
        (left + layoutSize.iconSizePx).toInt(),
        (top + layoutSize.iconSizePx).toInt()
    )
    drawable?.draw(canvas)

    if (text != null) {
        paint.color = contentColor
        val textStartY = top + layoutSize.iconSizePx - layoutSize.combineTextSizePx / 6
        canvas.drawText(
            text,
            left + div + layoutSize.iconSizePx + textWidth / 2,
            textStartY,
            paint
        )
    }
}
