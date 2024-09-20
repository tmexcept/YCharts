package com.example.table

import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
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


fun drawRectTable(
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
    chairNum: String = "40",
    guestNum: String? = null,
    payAmountText: String? = null,
) {
    pathClip.apply {
        reset()
        val offsetX = widget.offset.x
        val offsetY = widget.offset.y
        addRoundRect(
            androidx.compose.ui.geometry.RoundRect(
                offsetX, offsetY,
                offsetX + widget.width, offsetY + widget.height,
                layoutSize.rectRadius,
                layoutSize.rectRadius,
            )
        )
    }
    with(drawScope) {
        clipPath(path = pathClip) {
            drawContext.canvas.nativeCanvas.apply {
                paint.setColor(widget.widgetColor.tableColor)
                drawRoundRect(
                    widget.offset.x,
                    widget.offset.y,
                    widget.offset.x + widget.width,
                    widget.offset.y + widget.height,
                    layoutSize.rectRadius,
                    layoutSize.rectRadius,
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

                drawBottomContent(
                    canvas = this,
                    widget = widget,
                    paint = paint,
                    layoutSize = layoutSize,
                    sizeType = sizeType,
                    drawables = drawables,
                    bgColor = widget.widgetColor.bottomBgColor,
                    guestNum = guestNum,
                    chairNum = chairNum,
                    contentColor = widget.widgetColor.bottomContentColor,
                    payAmountText = payAmountText,
                )

                drawTableName(
                    canvas = this,
                    widget = widget,
                    layoutSize = layoutSize,
                    drawables = drawables,
                    tableName = tableName,
                    tableNameTextSize = if (sizeType == SizeType.SMALL_RECT) {
                        layoutSize.tableNameSmallTextSizePx
                    } else {
                        layoutSize.tableNameLargeTextSizePx
                    },
                    tableNameTextColor = widget.widgetColor.tableNameColor,
                    paint = paint,
                    bookTime = if (sizeType != SizeType.LARGE_RECT && sizeType != SizeType.LARGE_RECT_WIDTH) null else bookTime,
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

    paint.textSize = tableNameTextSize
    paint.setColor(tableNameTextColor)
    paint.typeface = Typeface.DEFAULT_BOLD

    if (bookTime == null) {
        val textStartY = widget.offset.y + widget.height / 2 + paint.textSize / 2 - paint.textSize / 6
        canvas.drawText(   //绘制tableName
            tableName,
            widget.offset.x + widget.width / 2,
            textStartY,
            paint
        )
    } else {
        val combineHeight = layoutSize.iconSizePx + layoutSize.combineLargePaddingPx * 2
        //计算上下四个控件的间距
        var div =
            (widget.height - combineHeight - layoutSize.bottomBgHeightPx
            - layoutSize.tableNameLargeHeightPx - layoutSize.bookingTimeZoneHeight)
        if(div > 0 ) {
            div /= 3
        } else {
            div = 0f
        }

        val startY =
            (widget.offset.y + widget.height - layoutSize.bottomBgHeightPx - div
                - (layoutSize.tableNameLargeHeightPx - layoutSize.tableNameLargeTextSizePx)/2
                - layoutSize.tableNameLargeTextSizePx / 6)
        canvas.drawText(   //绘制tableName
            tableName,
            widget.offset.x + widget.width / 2,
            startY,
            paint
        )

        var contentWidth = layoutSize.iconSizePx
        paint.typeface = Typeface.DEFAULT
        paint.textSize = layoutSize.bookingTimeTextSizePx
        val textWidth = paint.measureText(bookTime)
        contentWidth += layoutSize.iconDiv + textWidth

        val left = widget.offset.x + widget.width / 2 - contentWidth / 2
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
    if (combineText != null && sizeType != SizeType.SMALL_RECT) {
        paint.typeface = Typeface.DEFAULT
        paint.textSize = layoutSize.combineTextSizePx
        textWidth = paint.measureText(combineText)
        contentWidth += layoutSize.iconDiv + textWidth
    }
    val padding = if (sizeType == SizeType.SMALL_RECT) {
        layoutSize.combineSmallPaddingPx
    } else {
        layoutSize.combineLargePaddingPx
    }
    val left = if(sizeType == SizeType.SMALL_RECT) {
        widget.offset.x + widget.width / 2 - contentWidth / 2
    } else {
        widget.offset.x + widget.width - contentWidth - padding
    }
    val top = widget.offset.y + padding

    if (combineBgColor != null && sizeType != SizeType.SMALL_RECT) {
        paint.setColor(combineBgColor)
        drawCustomRoundRect(canvas,
            left - padding,
            top - padding,
            contentWidth + padding * 2,
            layoutSize.iconSizePx + padding * 2,
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
        text = if(sizeType == SizeType.SMALL_RECT) null else combineText,
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
    sizeType: SizeType,
    drawables: Drawables,
    paint: Paint,
    bgColor: Int? = null,
    guestNum: String? = null,
    chairNum: String,
    contentColor: Int,
    payAmountText: String? = null,
) {

    if (bgColor != null) {
        val left = widget.offset.x
        val bottom = widget.offset.y + widget.height
        val zoneHeight = if(sizeType == SizeType.SMALL_RECT) {
            layoutSize.guestZoneHeightPxLittleRect
        } else {
            layoutSize.bottomBgHeightPx
        }
        paint.setColor(bgColor)
        canvas.drawRect(
            left,
            bottom,
            left + widget.width,
            bottom - zoneHeight,
            paint
        )
    }

    if(sizeType != SizeType.SMALL_RECT) {
        paint.typeface = Typeface.DEFAULT
        paint.textSize = layoutSize.guestTextSizePx
        if (payAmountText == null || (sizeType != SizeType.LARGE_RECT_WIDTH && sizeType != SizeType.MEDIUM_RECT_WIDTH)) {
            val text = guestNum ?: chairNum
            val drawable = if(guestNum == null) {
                drawables.chair
            } else {
                drawables.guest
            }
            val textWidth = paint.measureText(text)
            //需要整体计算 icon与文本的总宽度，再绘制
            var contentWidth = layoutSize.iconSizePx
            contentWidth += layoutSize.iconDiv + textWidth

            drawIconWithText(
                canvas = canvas,
                layoutSize = layoutSize,
                drawable = drawable,
                paint = paint,
                contentColor = contentColor,
                text = text,
                textWidth = textWidth,
                left = widget.offset.x + widget.width / 2 - contentWidth / 2,
                top = widget.offset.y + widget.height - layoutSize.bottomBgHeightPx / 2 - layoutSize.iconSizePx / 2,
                div = layoutSize.iconDiv,
            )
        } else {
            paint.color = contentColor
            var textWidth = paint.measureText(guestNum)
            var contentWidth = layoutSize.iconSizePx
            contentWidth += layoutSize.iconDiv + textWidth
            val top = widget.offset.y + widget.height - layoutSize.bottomBgHeightPx / 2 - layoutSize.iconSizePx / 2

            drawIconWithText(
                canvas = canvas,
                layoutSize = layoutSize,
                drawable = drawables.guest,
                paint = paint,
                contentColor = contentColor,
                text = guestNum,
                textWidth = textWidth,
                left = widget.offset.x + layoutSize.rectBottomPaddingLtr,
                top = top,
                div = layoutSize.iconDiv,
            )

            textWidth = paint.measureText(payAmountText)

            val textStartY = top + layoutSize.iconSizePx - layoutSize.combineTextSizePx / 6
            canvas.drawText(
                payAmountText,
                widget.offset.x + widget.width - layoutSize.rectBottomPaddingLtr - textWidth / 2,
                textStartY,
                paint
            )
        }
    }
}

private fun drawCustomRoundRect(
    canvas: Canvas,
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    radius: Float,
    paint: Paint,
) {
    // 定义全矩形
    val fullRect = RectF(x, y, x + width, y + height)
    // 绘制完整的圆角矩形
    canvas.drawRoundRect(fullRect, radius, radius, paint)
    // 覆盖右下角为尖角
    val rightTopRect = RectF(x + width/2, y + height / 2, x + width, y + height)
    canvas.drawRect(rightTopRect, paint)
    // 覆盖左上角为尖角
    val leftBottomRect = RectF(x, y, x + width / 2, y + height/2)
    canvas.drawRect(leftBottomRect, paint)
}
