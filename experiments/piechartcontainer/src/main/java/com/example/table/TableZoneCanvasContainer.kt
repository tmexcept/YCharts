package com.example.table

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.text.TextPaint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.toArgb
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
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.piechartcontainer.R
import com.example.piechartcontainer.ui.theme.color_0xFF14CABF
import com.example.piechartcontainer.ui.theme.color_0xFF1D2129
import com.example.piechartcontainer.ui.theme.color_0xFF4E5969
import com.example.piechartcontainer.ui.theme.color_0xFF9195A3
import com.example.piechartcontainer.ui.theme.color_0xFF9195A4
import com.example.piechartcontainer.ui.theme.color_0xFFFDFDFD
import com.example.piechartcontainer.ui.theme.color_0xFFFFFFFF

val widgets = listOf(
    TableWidget(
        offset = Offset(100f, 100f),
        angle = 0f,
        radius = 60f,
        sizeType = SizeType.SMALL_CIRCLE,
    ),
    TableWidget(
        offset = Offset(300f, 100f),
        angle = 0f,
        radius = 100f,
        sizeType = SizeType.MEDIUM_CIRCLE,
    ),
    TableWidget(
        offset = Offset(500f, 100f),
        angle = 0f,
        radius = 140f,
        sizeType = SizeType.LARGE_CIRCLE,
    ),
    TableWidget(
        offset = Offset(800f, 100f),
        angle = 0f,
        width = 120f,
        height = 120f,
        sizeType = SizeType.SMALL_SQUARE,
    ),
    TableWidget(
        offset = Offset(1000f, 100f),
        angle = 0f,
        width = 200f,
        height = 200f,
        sizeType = SizeType.MEDIUM_SQUARE,
    ),
    TableWidget(
        offset = Offset(1300f, 100f),
        angle = 0f,
        width = 280f,
        height = 280f,
        sizeType = SizeType.LARGE_SQUARE,
    ),
    TableWidget(
        offset = Offset(1000f, 400f),
        angle = 0f,
        width = 260f,
        height = 200f,
        sizeType = SizeType.MEDIUM_RECT,
    ),
    TableWidget(
        offset = Offset(1300f, 400f),
        angle = 0f,
        width = 360f,
        height = 280f,
        sizeType = SizeType.LARGE_RECT,
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

@Composable
fun TableZoneCanvasContainer() {

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

    val zoneHeight = 1080f
    val zoneWidth = 1440f

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color_0xFF4E5969)
            .onGloballyPositioned { coordinates ->
                if (width != 0.dp && height != 0.dp) {
                    return@onGloballyPositioned
                }
                val rect = coordinates.boundsInWindow()
                Log.e("huhu", "rect.width=${rect.width},  rect.height=${rect.height}")
                if (rect.height / rect.width > 0.75f) { //上下居中
                    val destHeightPx = rect.width * 0.75f
                    density.run {
                        height = destHeightPx
                            .toInt()
                            .toDp()
                        width = rect.width
                            .toInt()
                            .toDp()
                    }

                    zoneScale = zoneWidth / rect.width
                } else { //左右居中
                    val destWidthPx = rect.height / 0.75f
                    density.run {
                        width = destWidthPx.toDp()
                        height = rect.height
                            .toInt()
                            .toDp()
                    }

                    zoneScale = zoneHeight / rect.height
                }
                Log.e("huhu", "width=$width,  height=$height")
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
                    .background(color_0xFFFDFDFD)
            ) {
                TableZoneCanvas(
                    modifier = Modifier
                        .width(width)
                        .height(height),
                    widgets = widgets,
                    zoneScale = zoneScale
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
) {
    val context = LocalContext.current
    val density = LocalDensity.current
    val layoutSize by with(density) {
        remember {
            mutableStateOf(
                LayoutSize(
                    iconSizePx = 16.dp.toPx() * zoneScale,
                    tableNameSmallTextSizePx = 20.sp.toPx() * zoneScale,
                    tableNameLargeTextSizePx = 30.sp.toPx() * zoneScale,
                    tableNameLargeHeightPx = 48.dp.toPx() * zoneScale,

                    combineTextSizePx = 16.sp.toPx() * zoneScale,
                    combineSmallPaddingPx = 2.dp.toPx() * zoneScale,
                    combineLargePaddingPx = 4.5.dp.toPx() * zoneScale,
                    combineBgCornerPx = 8.dp.toPx() * zoneScale,
                    iconDiv = 2.dp.toPx() * zoneScale,

                    guestTextSizePx = 16.sp.toPx() * zoneScale,
                    bottomBgHeightPx = 32.dp.toPx() * zoneScale,
                    guestZoneHeightPxLittleRect = 16.dp.toPx() * zoneScale,

                    bookingTimeTextSizePx = 16.sp.toPx() * zoneScale,
                    bookingTimeZoneHeight = 32.dp.toPx() * zoneScale,

                    rectRadius = 12.dp.toPx() * zoneScale,
                    rectBottomPaddingLtr = 12.dp.toPx() * zoneScale,

                    wallWidth = 32.dp.toPx() * zoneScale,
                    wallHeight = 32.dp.toPx() * zoneScale,
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

    Canvas(modifier = modifier,
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
                            tableBgColor = color_0xFF14CABF.toArgb(),
                            combineBgColor = color_0xFFFFFFFF.toArgb(),
                            combineText = "20",
                            bookTime = "1h25m",
                            guestZoneText = "40",
                        )
                    }

                    SizeType.LARGE_RECT,
                    SizeType.MEDIUM_RECT,
                    SizeType.SMALL_SQUARE,
                    SizeType.MEDIUM_SQUARE,
                    SizeType.LARGE_SQUARE,
                    -> {
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