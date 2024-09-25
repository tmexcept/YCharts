package com.example.table

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.widget.Toast
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.piechartcontainer.R
import com.example.piechartcontainer.ui.theme.color_0xFF1D2129
import com.example.piechartcontainer.ui.theme.color_0xFF4E5969
import com.example.piechartcontainer.ui.theme.color_0xFFFDFDFD
import com.example.piechartcontainer.ui.theme.color_0xFFFFFFFF

val widgets = listOf(
    TableWidget(
        offset = Offset(80f, 20f),
        angle = 0f,
        radius = 50f,
        sizeType = SizeType.MEDIUM_CIRCLE,
    ),
    TableWidget(
        offset = Offset(200f, 20f),
        angle = 0f,
        radius = 70f,
        sizeType = SizeType.LARGE_CIRCLE,
    ),
    TableWidget(
        offset = Offset(350f, 20f),
        angle = 0f,
        width = 60f,
        height = 60f,
        sizeType = SizeType.SMALL_RECT,
    ),
    TableWidget(
        offset = Offset(420f, 20f),
        angle = 0f,
        width = 100f,
        height = 100f,
        sizeType = SizeType.MEDIUM_RECT,
    ),
    TableWidget(
        offset = Offset(600f, 20f),
        angle = 0f,
        width = 140f,
        height = 140f,
        sizeType = SizeType.LARGE_RECT,
    ),
    TableWidget(
        offset = Offset(420f, 200f),
        angle = 0f,
        width = 130f,
        height = 100f,
        sizeType = SizeType.MEDIUM_RECT_WIDTH,
    ),
    TableWidget(
        offset = Offset(600f, 200f),
        angle = 0f,
        width = 180f,
        height = 140f,
        sizeType = SizeType.LARGE_RECT_WIDTH,
    ),
    TableWidget(
        offset = Offset(100f, 600f),
        angle = 0f,
        width = 320f,
        height = 100f,
        sizeType = SizeType.WALL,
    ),
    TableWidget(
        offset = Offset(200f, 700f),
        angle = 90f,
        width = 320f,
        height = 100f,
        sizeType = SizeType.WALL,
    ),
)
//圆桌(直径) 小：[60~100px） 中：[100-140px） 大：[>=140px，∞)
//矩形(高度) 小：[60~100px） 中：[100-140px） 大：[>=140px）
//- 矩形桌台超宽（宽度 >=160px）时，展示金额。
private const val DESIGN_H_W_SCALE = 10f/16f //设计的高宽比例3:4
//4. 默认画布尺寸1280: 960
private const val zoneHeight = 800f
private const val zoneWidth = 1280f

@Composable
fun TableZoneCanvasContainer(
    widgets: List<TableWidget>,
) {

    val context = LocalContext.current
    val density = LocalDensity.current
    var width by remember {
        mutableStateOf(0.dp)
    }

    var height by remember {
        mutableStateOf(0.dp)
    }

    var zoneScale by remember {
        mutableStateOf(1f)
    }

    var widgetRealSize by remember {
        mutableStateOf(widgets)
    }

    fun changeWidgetSize(scale: Float){
        widgetRealSize = widgets.map {
            it.copy(
                offset = it.offset.copy(
                    x = it.offset.x * scale,
                    y = it.offset.y * scale
                ),
                radius = it.radius * scale,
                width = it.width * scale,
                height = it.height * scale,
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color_0xFF4E5969)
            .onGloballyPositioned { coordinates ->
                if (width != 0.dp && height != 0.dp) {
                    return@onGloballyPositioned
                }
                val rect = coordinates.boundsInWindow()
                if (rect.height / rect.width > DESIGN_H_W_SCALE) { //上下居中
                    val destHeightPx = rect.width * DESIGN_H_W_SCALE
                    density.run {
                        height = destHeightPx
                            .toInt()
                            .toDp()
                        width = rect.width
                            .toInt()
                            .toDp()
                    }

                    zoneScale = rect.width / zoneWidth
                    changeWidgetSize(zoneScale)
                } else { //左右居中
                    val destWidthPx = rect.height / DESIGN_H_W_SCALE
                    density.run {
                        width = destWidthPx.toDp()
                        height = rect.height
                            .toInt()
                            .toDp()
                    }

                    zoneScale = rect.height / zoneHeight
                    changeWidgetSize(zoneScale)
                }
            },
        contentAlignment = Alignment.Center
    ) {
        //- POS 区域长宽比大于4:3时（POS 屏幕更宽）可固定BO预定义平面图的高度，等比缩放平面图并在POS上居中；
        //- POS 区域长宽比小于4:3时（POS 屏幕更宽）可固定BO预定义平面图的宽度，等比缩放平面图高度并在屏幕中上下居中；
        if (width > 0.dp && height > 0.dp) {
            Box(
                modifier = Modifier
                    .width(width)
                    .height(height)
                    .background(color_0xFFFFFFFF)
            ) {
                TableZoneCanvas(
                    modifier = Modifier
                        .width(width)
                        .height(height),
                    widgets = widgetRealSize,
                    zoneScale = zoneScale,
                    onPointClick = {
                        Toast.makeText(context, "click index=$it", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
}

@Composable
fun TableZoneCanvas(
    modifier: Modifier = Modifier,
    widgets: List<TableWidget>,
    zoneScale: Float = 1.0f,
    onPointClick: (Int) -> Unit,
) {
    val context = LocalContext.current
    val layoutSize by remember {
        mutableStateOf(
            LayoutSize(
                iconSizePx = 16f * zoneScale,
                tableNameSmallTextSizePx = 20f * zoneScale,
                tableNameLargeTextSizePx = 30f * zoneScale,
                tableNameLargeHeightPx = 48f * zoneScale,

                combineTextSizePx = 16f * zoneScale,
                combineSmallPaddingPx = 2f * zoneScale,
                combineLargePaddingPx = 4.5f * zoneScale,
                combineBgCornerPx = 8f * zoneScale,
                iconDiv = 2f * zoneScale,

                guestTextSizePx = 16f * zoneScale,
                bottomBgHeightPx = 32f * zoneScale,
                guestZoneHeightPxLittleRect = 16f * zoneScale,

                bookingTimeTextSizePx = 16f * zoneScale,
                bookingTimeZoneHeight = 32f * zoneScale,

                rectRadius = 12f * zoneScale,
                rectBottomPaddingLtr = 12f * zoneScale,

                wallWidth = 32f * zoneScale,
                wallHeight = 32f * zoneScale,
            )
        )
    }

    val drawables by remember {
        mutableStateOf(
            Drawables(
                combine = ContextCompat.getDrawable(context, R.drawable.ic_combine_empty),
                chair = ContextCompat.getDrawable(context, R.drawable.icon_chair),
                guest = ContextCompat.getDrawable(context, R.drawable.icon_customer),
                book = ContextCompat.getDrawable(context, R.drawable.ic_hourglass),
                wall = ContextCompat.getDrawable(context, R.drawable.ic_pay_ali),
            )
        )
    }

    val paint by remember {
        mutableStateOf(
            Paint().apply {
                textAlign = Paint.Align.CENTER
                isAntiAlias = true  //开启抗锯齿
            }
        )
    }
    val pathClip by remember {
        mutableStateOf(Path())
    }

    Canvas(modifier = modifier
        .pointerInput(true) {
            detectTapGestures {
                widgets.forEachIndexed { index, item ->
                    if (contains(it.x, it.y, item)) {
                        onPointClick(index)
                        return@detectTapGestures
                    }
                }
            }
        },
        onDraw = {
            widgets.forEach {
                when (it.sizeType) {
                    SizeType.MEDIUM_CIRCLE,
                    SizeType.SMALL_CIRCLE,
                    SizeType.LARGE_CIRCLE,
                    -> {
                        drawCircleTable(
                            this,
                            pathClip = pathClip,
                            widget = it,
                            paint = paint,
                            layoutSize = layoutSize,
                            drawables = drawables,
                            sizeType = it.sizeType,
                            tableName = "A02",
                            combineText = "20",
                            bookTime = "1h25m",
                            guestZoneText = "40",
                        )
                    }

                    SizeType.LARGE_RECT_WIDTH,
                    SizeType.MEDIUM_RECT_WIDTH,
                    SizeType.SMALL_RECT,
                    SizeType.MEDIUM_RECT,
                    SizeType.LARGE_RECT,
                    -> {
                        drawRectTable(
                            this,
                            pathClip = pathClip,
                            widget = it,
                            paint = paint,
                            layoutSize = layoutSize,
                            drawables = drawables,
                            sizeType = it.sizeType,
                            tableName = "A02",
                            combineText = "20",
                            bookTime = "1h25m",
                            guestNum = "40",
                            payAmountText = "$80.00",
                        )
                    }

                    SizeType.WALL -> {
                        drawables.wall?.let { wall ->
                            drawWall(
                                this,
                                widget = it,
                                layoutSize = layoutSize,
                                drawable = wall,
                            )
                        }
                    }


                    else -> {}
                }
            }
        }
    )
}

private fun contains(x: Float, y: Float, widget: TableWidget): Boolean {
    // check for empty first
    return !(x < widget.offset.x || y < widget.offset.y
        || x > widget.width + widget.offset.x || y > widget.height + widget.offset.y)
}

@OptIn(ExperimentalTextApi::class)
fun drawSmallCircleTable2(
    text: String,
    textMeasurer: TextMeasurer,
    drawScope: DrawScope,
    yOffset: Int = 400,
    circleTable: TableWidget,
    tableColor: Color,
    layoutSize: LayoutSizeDp,
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