package com.example.table

import android.graphics.Bitmap
import android.graphics.Canvas
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
                    smallTableNameTextSizePx = 20.sp.toPx(),
                    largeTableNameTextSizePx = 30.sp.toPx(),

                    combineTextSizePx = 16.sp.toPx(),
                    smallCombinePaddingPx = 2.dp.toPx(),
                    largeCombinePaddingPx = 4.5.dp.toPx(),
                    combineBackgroundCorner = 8.dp.toPx(),
                    iconDiv = 2.dp.toPx(),

                    guestTextSizePx = 16.sp.toPx(),
                    guestZoneHeightPx = 32.dp.toPx(),

                    bookingTimeTextSizePx = 16.sp.toPx(),
                    bookingTimeZoneHeight = 32.dp.toPx(),
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
                textSize = layoutSize.smallTableNameTextSizePx
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
                        SizeType.SMALL_CIRCLE -> {
                            drawCircleTable(
                                this,
                                pathClip = pathClip,
                                widget = it,
                                paint = paint,
                                layoutSize = layoutSize,
                                drawables = drawables,
                                sizeType = it.sizeType,
                                tableName = "A02",
                            )

                            drawSmallCircleTable2(
                                "A02", textMeasurer, this,
                                yOffset = 400,
                                circleTable = it,
                                tableColor = color_0xFF14CABF,
                                layoutSize = layoutSize,
                                combineBitmap = combineBitmap.asImageBitmap(),
                            )
                        }

                        SizeType.MEDIUM_CIRCLE -> {
                            drawCircleTable(
                                this,
                                pathClip = pathClip,
                                widget = it,
                                paint = paint,
                                layoutSize = layoutSize,
                                drawables = drawables,
                                sizeType = it.sizeType,
                                tableName = "A02",
                                combineBgColor = color_0xFFFFFFFF.toArgb(),
                                combineText = "2",
                            )
                        }

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
                                combineBgColor = color_0xFFFFFFFF.toArgb(),
                                combineText = "20",
                                bookTime = "1h25m",
                                guestZoneText = "40",
                            )
                        }

                        else -> {}
                    }
                }
            }
        )
    }
}

fun drawTableName(
    canvas: Canvas,
    widget: TableWidget,
    layoutSize: LayoutSize,
    drawables: Drawables,
    tableName: String,
    tableNameTextSize: Float,
    tableNameTextColor: Int,
    paint: Paint,
    bookTime: String? = null,
    bookTimeContentColor: Int = 0,
) {

    paint.textSize = tableNameTextSize
    paint.setColor(tableNameTextColor)
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
        val height = widget.radius * 2
        //计算上下四个控件的间距
        val div = (height - layoutSize.iconSizePx - layoutSize.largeCombinePaddingPx * 2
            - layoutSize.bookingTimeZoneHeight - layoutSize.largeTableNameTextSizePx - layoutSize.guestZoneHeightPx) / 3

        val startY =
            widget.offset.y + height - layoutSize.guestZoneHeightPx - div - paint.textSize / 6
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
        //需去除底部Guest区域高度，div，bookingTimeZone高度一半
        val top =
            widget.offset.y + widget.radius * 2 - layoutSize.guestZoneHeightPx - layoutSize.largeTableNameTextSizePx - div * 2 - layoutSize.bookingTimeZoneHeight / 2 - layoutSize.iconSizePx / 2

        drawIconWithText(
            canvas = canvas,
            layoutSize = layoutSize,
            drawable = drawables.book,
            paint = paint,
            contentColor = bookTimeContentColor,
            text = bookTime,
            textWidth = textWidth,
            left = left,
            top = top,
            div = layoutSize.iconDiv,
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

fun drawCustomRoundRect(
    canvas: Canvas,
    x: Float,
    y: Float,
    width: Float,
    height: Float,
    paint: Paint,
) {
    // 定义全矩形
    val fullRect = RectF(x, y, x + width, y + height)
    // 圆角大小
    val cornerRadius = 50f
    // 绘制完整的圆角矩形
    canvas.drawRoundRect(fullRect, cornerRadius, cornerRadius, paint)
    // 覆盖右上角为尖角
    val rightTopRect = RectF(x + width / 2, y, x + width, y + height / 2)
    canvas.drawRect(rightTopRect, paint)
    // 覆盖左下角为尖角
    val leftBottomRect = RectF(x, y + height / 2, x + width / 2, y + height)
    canvas.drawRect(leftBottomRect, paint)
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