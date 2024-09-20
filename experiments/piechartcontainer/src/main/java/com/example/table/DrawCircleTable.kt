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
    combineText: String? = null,
    tableName: String,
    bookTime: String? = null,
    guestZoneText: String = "40",
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
            drawContext.canvas.nativeCanvas.apply {
                paint.setColor(widget.widgetColor.tableColor)
                drawCircle(
                    widget.offset.x + widget.radius,
                    widget.offset.y + widget.radius,
                    widget.radius,
                    paint
                )

                drawables.combine?.setTint(widget.widgetColor.combineContentColor)
                drawCombineInfo(
                    canvas = this,
                    widget = widget,
                    paint = paint,
                    layoutSize = layoutSize,
                    drawable = drawables.combine,
                    combineBgColor = widget.widgetColor.combineBgColor,
                    sizeType = sizeType,
                    combineText = combineText,
                    combineContentColor = widget.widgetColor.combineContentColor,
                )

                if (sizeType != SizeType.SMALL_CIRCLE) {
                    drawBottomContent(
                        canvas = this,
                        widget = widget,
                        paint = paint,
                        layoutSize = layoutSize,
                        drawable = drawables.chair,
                        guestZoneBgColor = widget.widgetColor.bottomBgColor,
                        guestText = guestZoneText,
                        guestContentColor = widget.widgetColor.bottomContentColor
                    )
                }

                drawTableName(
                    canvas = this,
                    widget = widget,
                    layoutSize = layoutSize,
                    drawables = drawables,
                    tableName = tableName,
                    tableNameTextSize = if (sizeType == SizeType.SMALL_CIRCLE) {
                        layoutSize.tableNameSmallTextSizePx
                    } else {
                        layoutSize.tableNameLargeTextSizePx
                    },
                    tableNameTextColor = widget.widgetColor.tableNameColor,
                    paint = paint,
                    bookTime = if (sizeType != SizeType.LARGE_CIRCLE) null else bookTime,
                )
            }
        }
    }
}

private fun drawTableName(
    canvas: Canvas,
    widget: TableWidget,
    layoutSize: LayoutSize,
    drawables: Drawables,
    tableName: String,
    tableNameTextSize: Float,
    tableNameTextColor: Int,
    paint: Paint,
    bookTime: String? = null,
) {

    paint.setColor(tableNameTextColor)
    paint.textSize = tableNameTextSize
    paint.typeface = Typeface.DEFAULT_BOLD

    if (bookTime == null) {
        val textStartY = widget.offset.y + widget.radius + paint.textSize / 2 - paint.textSize / 6
        canvas.drawText(   //绘制tableName
            tableName,
            widget.offset.x + widget.radius,
            textStartY,
            paint
        )
    } else {
        val combineHeight = layoutSize.iconSizePx + layoutSize.combineLargePaddingPx * 2
        val height = widget.radius * 2
        //计算上下四个控件的间距
        val div = (height - combineHeight
            - layoutSize.bookingTimeZoneHeight - layoutSize.tableNameLargeTextSizePx - layoutSize.bottomBgHeightPx) / 3

        val startY =
            widget.offset.y + height - layoutSize.bottomBgHeightPx - div - layoutSize.tableNameLargeTextSizePx / 6
        canvas.drawText(   //绘制tableName
            tableName,
            widget.offset.x + widget.radius,
            startY,
            paint
        )

        var contentWidth = layoutSize.iconSizePx
        paint.typeface = Typeface.DEFAULT
        paint.textSize = layoutSize.bookingTimeTextSizePx
        val textWidth = paint.measureText(bookTime)
        contentWidth += layoutSize.iconDiv + textWidth

        val left = widget.offset.x + widget.radius - contentWidth / 2
        //需去除底部Guest区域的高度、div、bookingTimeZone高度与booking文本差值的一半
        val top = widget.offset.y + combineHeight + div + (layoutSize.bookingTimeZoneHeight - layoutSize.iconSizePx) / 2

        drawIconWithText(
            canvas = canvas,
            layoutSize = layoutSize,
            drawable = drawables.book,
            paint = paint,
            contentColor = tableNameTextColor,
            text = bookTime,
            textWidth = textWidth,
            left = left,
            top = top,
            div = layoutSize.iconDiv,
        )
    }
}

private fun drawCombineInfo(
    canvas: Canvas,
    widget: TableWidget,
    layoutSize: LayoutSize,
    drawable: Drawable? = null,
    paint: Paint,
    sizeType: SizeType,
    combineBgColor: Int? = null,
    combineText: String? = null,
    combineContentColor: Int,
) {
    var contentWidth = layoutSize.iconSizePx
    var textWidth = 0f
    if (combineText != null && sizeType != SizeType.SMALL_CIRCLE) {
        paint.typeface = Typeface.DEFAULT
        paint.textSize = layoutSize.combineTextSizePx
        textWidth = paint.measureText(combineText)
        contentWidth += layoutSize.iconDiv + textWidth
    }
    val padding = if (sizeType == SizeType.SMALL_CIRCLE) {
        layoutSize.combineSmallPaddingPx
    } else {
        layoutSize.combineLargePaddingPx
    }
    val left = widget.offset.x + widget.radius - contentWidth / 2
    val top = widget.offset.y + padding

    if (combineBgColor != null && sizeType != SizeType.SMALL_CIRCLE) {
        paint.setColor(combineBgColor)
        canvas.drawRoundRect(
            left - padding,
            top - padding - 10, //此处是让顶部圆角被clip掉
            left + contentWidth + padding,
            top + layoutSize.iconSizePx + padding,
            layoutSize.combineBgCornerPx,
            layoutSize.combineBgCornerPx,
            paint
        )
    }

    drawIconWithText(
        canvas = canvas,
        layoutSize = layoutSize,
        drawable = drawable,
        paint = paint,
        contentColor = combineContentColor,
        text = if (sizeType == SizeType.SMALL_CIRCLE) null else combineText,
        textWidth = textWidth,
        left = left,
        top = top,
        div = layoutSize.iconDiv,
    )
}

private fun drawBottomContent(
    canvas: Canvas,
    widget: TableWidget,
    layoutSize: LayoutSize,
    drawable: Drawable? = null,
    paint: Paint,
    guestZoneBgColor: Int? = null,
    guestText: String,
    guestContentColor: Int,
) {

    val left = widget.offset.x
    val bottom = widget.offset.y + widget.radius * 2

    if (guestZoneBgColor != null) {
        paint.setColor(guestZoneBgColor)
        canvas.drawRect(
            left,
            bottom,
            left + widget.radius * 2,
            bottom - layoutSize.bottomBgHeightPx,
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
        top = bottom - layoutSize.bottomBgHeightPx / 2 - layoutSize.iconSizePx / 2,
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
