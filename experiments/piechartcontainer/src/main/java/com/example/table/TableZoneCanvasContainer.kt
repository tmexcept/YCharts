package com.example.table

import android.graphics.Bitmap
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.piechartcontainer.R
import com.example.piechartcontainer.ui.theme.color_0xFF14CABF
import com.example.piechartcontainer.ui.theme.color_0xFF1D2129
import com.example.piechartcontainer.ui.theme.color_0xFF9195A3
import com.example.piechartcontainer.ui.theme.color_0xFFFFFFFF


@OptIn(ExperimentalTextApi::class)
@Composable
fun TableZoneCanvasContainer(
    modifier: Modifier = Modifier,
    widgets: List<TableWidget>,
) {
    val context = LocalContext.current
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val layoutSize by with(density) {
        remember {
            mutableStateOf(
                LayoutSize(
                    iconSizePx = 16.dp.toPx(),
                    tableNameSmallTextSizePx = 20.sp.toPx(),
                    tableNameLargeTextSizePx = 30.sp.toPx(),
                    tableNameLargeHeightPx = 48.dp.toPx(),

                    combineTextSizePx = 16.sp.toPx(),
                    combineSmallPaddingPx = 2.dp.toPx(),
                    combineLargePaddingPx = 4.5.dp.toPx(),
                    combineBgCornerPx = 8.dp.toPx(),
                    iconDiv = 2.dp.toPx(),

                    guestTextSizePx = 16.sp.toPx(),
                    bottomBgHeightPx = 32.dp.toPx(),
                    guestZoneHeightPxLittleRect = 16.dp.toPx(),

                    bookingTimeTextSizePx = 16.sp.toPx(),
                    bookingTimeZoneHeight = 32.dp.toPx(),

                    rectRadius = 12.dp.toPx(),
                    rectBottomPaddingLtr = 12.dp.toPx(),
                )
            )
        }
    }
    val drawables by remember {
        mutableStateOf(
            Drawables(
                combine = ContextCompat.getDrawable(context, R.drawable.ic_combine_empty),
                chair = ContextCompat.getDrawable(context, R.drawable.icon_chair),
                guest = ContextCompat.getDrawable(context, R.drawable.icon_customer),
                book = ContextCompat.getDrawable(context, R.drawable.ic_hourglass),
            )
        )
    }

    val combineBitmap by remember {
        mutableStateOf(
            drawables.combine!!.toBitmap(
                layoutSize.iconSizePx.toInt(),
                layoutSize.iconSizePx.toInt()
            )
        )
    }

    val paint by remember {
        mutableStateOf(
            Paint().apply {
                color = color_0xFF1D2129.toArgb()
                textSize = layoutSize.tableNameSmallTextSizePx
                textAlign = Paint.Align.CENTER
                typeface = Typeface.DEFAULT_BOLD
                isAntiAlias = true  //开启抗锯齿
            }
        )
    }
    val pathClip by remember {
        mutableStateOf(Path())
    }

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
                    when (it.sizeType) {
                        SizeType.MEDIUM_CIRCLE,
                        SizeType.SMALL_CIRCLE,
                        SizeType.LARGE_CIRCLE -> {
                            drawCircleTable(
                                this,
                                pathClip = pathClip,
                                widget = it,
                                paint = paint,
                                layoutSize = layoutSize,
                                drawables = drawables,
                                sizeType = it.sizeType,
                                tableName = "A02",
                                tableBgColor = color_0xFF14CABF.toArgb(),
                                combineBgColor = color_0xFFFFFFFF.toArgb(),
                                combineText = "20",
                                bookTime = "1h25m",
                                guestZoneText = "40",
                            )

                            if(it.sizeType == SizeType.SMALL_CIRCLE) {
                                drawSmallCircleTable2(
                                    "A02", textMeasurer, this,
                                    yOffset = 400,
                                    circleTable = it,
                                    tableColor = color_0xFF14CABF,
                                    layoutSize = layoutSize,
                                    combineBitmap = combineBitmap.asImageBitmap(),
                                )
                            }
                        }

                        SizeType.LARGE_RECT,
                        SizeType.MEDIUM_RECT,
                        SizeType.SMALL_SQUARE,
                        SizeType.MEDIUM_SQUARE,
                        SizeType.LARGE_SQUARE -> {
                            drawRectTable(
                                this,
                                pathClip = pathClip,
                                widget = it,
                                paint = paint,
                                layoutSize = layoutSize,
                                drawables = drawables,
                                sizeType = it.sizeType,
                                tableBgColor = color_0xFF9195A3.toArgb(),
                                tableName = "A02",
                                combineBgColor = color_0xFFFFFFFF.toArgb(),
                                combineText = "20",
                                bookTime = "1h25m",
                                guestNum = "40",
                                payAmountText = "$80.00",
                            )
                        }



                        else -> {}
                    }
                }
            }
        )
    }
}

@OptIn(ExperimentalTextApi::class)
fun drawSmallCircleTable2(
    text: String,
    textMeasurer: TextMeasurer,
    drawScope: DrawScope,
    yOffset: Int = 400,
    circleTable: TableWidget,
    tableColor: Color,
    layoutSize: LayoutSize,
    combineBitmap: ImageBitmap,
) {
    val drawOffset = circleTable.offset.copy(
        y = circleTable.offset.y + yOffset
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
            topLeft = Offset(
                drawOffset.x - textLength / 2,
                drawOffset.y - textPaint.textSize / 2 - textPaint.textSize / 6
            ),
            style = TextStyle(
                fontSize = layoutSize.smallTableNameTextSize,
                lineHeight = layoutSize.smallTableNameTextSize,
                fontWeight = FontWeight.W700,
                color = color_0xFF1D2129,
            ),
            size = Size(
                circleTable.radius * 2,
                circleTable.radius * 2
            ) //此处必须设置size，否则可能崩溃【maxWidth < minWidth】
        )

        drawImage(
            image = combineBitmap,
            dstSize = IntSize(
                layoutSize.iconSize.toPx().toInt(),
                layoutSize.iconSize.toPx().toInt()
            ),
            dstOffset = IntOffset(
                (drawOffset.x - layoutSize.iconSize.toPx() / 2).toInt(),
                (drawOffset.y - radius).toInt()
            ),
            colorFilter = ColorFilter.tint(color = Color(0xFF3E6BF6)),
        )
    }
}

//绘制bitmap
fun drawSmallCombineInfo(
    drawScope: DrawScope,
    circleTable: TableWidget,
    layoutSize: LayoutSize,
    combineBitmap: Bitmap,
    paint: Paint,
    padding: Float,
) {
    with(drawScope) {
        drawContext.canvas.nativeCanvas.apply {
            val left = circleTable.offset.x + circleTable.radius - layoutSize.iconSizePx / 2
            val top = circleTable.offset.y + padding
            drawBitmap(
                combineBitmap,
                null,
                RectF(left, top, left + layoutSize.iconSizePx, top + layoutSize.iconSizePx),
                paint
            )
        }
    }
}