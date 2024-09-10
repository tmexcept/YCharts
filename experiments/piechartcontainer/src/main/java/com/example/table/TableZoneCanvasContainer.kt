package com.example.table

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.graphics.Typeface
import android.text.TextPaint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.sp
import com.example.piechartcontainer.ui.theme.color_0xFF1D2129
import com.example.piechartcontainer.ui.theme.color_0xFFFFFFFF


@OptIn(ExperimentalTextApi::class)
@Composable
fun TableZoneCanvasContainer(
    modifier: Modifier = Modifier,
    combineBitmap: ImageBitmap,
    layoutSize: LayoutSize,
    widgets: List<TableWidget>,
) {
    val textMeasurer = rememberTextMeasurer()
    Box(
        modifier = modifier.clipToBounds()
    ) {
        Canvas(modifier = modifier
            .align(Alignment.Center)
            .fillMaxHeight()
            .fillMaxWidth()
            .background(Color(0xFFC9CDD4)),
            onDraw = {
                widgets.forEach {
                    when(it.sizeType) {
                        SizeType.SMALL_CIRCLE -> {
                            drawSmallCircleTable("A02", this,
                                circleTable = it,
                                tableColor = color_0xFFFFFFFF,
                                layoutSize = layoutSize,
                                combineBitmap = combineBitmap,
                                )
                            drawSmallCircleTable2("A02", textMeasurer, this,
                                circleTable = it,
                                tableColor = color_0xFFFFFFFF,
                                layoutSize = layoutSize,
                                combineBitmap = combineBitmap,
                                )
                        }
                        else -> {}
                    }
                }
            }
        )
    }
}


fun drawSmallCircleTable(
    text: String,
    drawScope: DrawScope,
    circleTable: TableWidget,
    tableColor: Color,
    layoutSize: LayoutSize,
    combineBitmap: ImageBitmap,
) {
    val drawOffset = circleTable.offset
    val radius = circleTable.radius
    with(drawScope) {
        drawCircle(
            color = tableColor,
            center = drawOffset,
            radius = radius,
        )

        val paint = Paint().apply {
            color = color_0xFF1D2129.toArgb()
            textSize = layoutSize.smallTableNameTextSize.toPx()
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true  //开启抗锯齿
        }
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)

        val textHeight = rect.height().toFloat()

        // 计算居中位置
        val textStartY = drawOffset.y + textHeight / 2

        drawContext.canvas.nativeCanvas.drawText(
            text,
            drawOffset.x,
            textStartY,
            paint
        )

        val imagePaint = Paint().apply {
            color = android.graphics.Color.BLACK
        }
        val rectF = RectF(
            drawOffset.x - layoutSize.iconSize.toPx() / 2,
            drawOffset.y - radius,
            drawOffset.x + layoutSize.iconSize.toPx() / 2,
            drawOffset.y - radius + layoutSize.iconSize.toPx() / 2
        )
        drawImage(
            image = combineBitmap,
            dstSize = IntSize(layoutSize.iconSize.toPx().toInt(), layoutSize.iconSize.toPx().toInt()),
            dstOffset = IntOffset((drawOffset.x - layoutSize.iconSize.toPx() / 2).toInt(), (drawOffset.y - radius).toInt()),
            colorFilter = ColorFilter.tint(color = Color(0xFF3E6BF6)),
        )
    }
}

fun drawSmallCircleTable(
    text: String,
    drawScope: DrawScope,
    circleTable: TableWidget,
    tableColor: Color,
    layoutSize: LayoutSize,
    combineBitmap: ImageBitmap,
) {
    val drawOffset = circleTable.offset
    val radius = circleTable.radius
    with(drawScope) {
        drawCircle(
            color = tableColor,
            center = drawOffset,
            radius = radius,
        )

        val paint = Paint().apply {
            color = color_0xFF1D2129.toArgb()
            textSize = layoutSize.smallTableNameTextSize.toPx()
            textAlign = Paint.Align.CENTER
            typeface = Typeface.DEFAULT_BOLD
            isAntiAlias = true  //开启抗锯齿
        }
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)

        val textHeight = rect.height().toFloat()

        // 计算居中位置
        val textStartY = drawOffset.y + textHeight / 2

        drawContext.canvas.nativeCanvas.drawText(
            text,
            drawOffset.x,
            textStartY,
            paint
        )

        val imagePaint = Paint().apply {
            color = android.graphics.Color.BLACK
        }
        val rectF = RectF(
            drawOffset.x - layoutSize.iconSize.toPx() / 2,
            drawOffset.y - radius,
            drawOffset.x + layoutSize.iconSize.toPx() / 2,
            drawOffset.y - radius + layoutSize.iconSize.toPx() / 2
        )
        drawImage(
            image = combineBitmap,
            dstSize = IntSize(layoutSize.iconSize.toPx().toInt(), layoutSize.iconSize.toPx().toInt()),
            dstOffset = IntOffset((drawOffset.x - layoutSize.iconSize.toPx() / 2).toInt(), (drawOffset.y - radius).toInt()),
            colorFilter = ColorFilter.tint(color = Color(0xFF3E6BF6)),
        )
    }
}


@OptIn(ExperimentalTextApi::class)
fun drawSmallCircleTable2(
    text: String,
    textMeasurer: TextMeasurer,
    drawScope: DrawScope,
    circleTable: TableWidget,
    tableColor: Color,
    layoutSize: LayoutSize,
    combineBitmap: ImageBitmap,
) {
    val drawOffset = circleTable.offset.copy(
        y = circleTable.offset.y + 200
    )
    val radius = circleTable.radius
    with(drawScope) {
        drawCircle(
            color = tableColor,
            center = drawOffset,
            radius = radius,
        )

        val paint = Paint().apply {
            color = android.graphics.Color.BLACK
            textSize = layoutSize.smallTableNameTextSize.toPx()
            textAlign = Paint.Align.CENTER
        }
        val rect = Rect()
        paint.getTextBounds(text, 0, text.length, rect)

        val textPaint = TextPaint().apply {
            textSize = layoutSize.smallTableNameTextSize.toPx()
        }
        val textLength = textPaint.measureText(text)

        drawText(
            textMeasurer = textMeasurer,
            text = text,
            topLeft = Offset(drawOffset.x - textLength/2, drawOffset.y - textPaint.textSize/2 - textPaint.textSize/6),
            style = TextStyle(
                fontSize = layoutSize.smallTableNameTextSize,
                lineHeight = layoutSize.smallTableNameTextSize,
                fontWeight = FontWeight.W700,
                color = color_0xFF1D2129,
            ),
            size = Size(circleTable.radius * 2, circleTable.radius * 2) //此处必须设置size，否则可能崩溃【maxWidth < minWidth】
        )

        drawImage(
            image = combineBitmap,
            dstSize = IntSize(layoutSize.iconSize.toPx().toInt(), layoutSize.iconSize.toPx().toInt()),
            dstOffset = IntOffset((drawOffset.x - layoutSize.iconSize.toPx() / 2).toInt(), (drawOffset.y - radius).toInt()),
            colorFilter = ColorFilter.tint(color = Color(0xFF3E6BF6)),
        )
    }
}
