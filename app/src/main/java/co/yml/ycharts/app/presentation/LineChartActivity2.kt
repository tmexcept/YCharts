package co.yml.ycharts.app.presentation

import android.os.Bundle
import android.text.TextPaint
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.axis.AxisConfig
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.common.utils.DataUtils
import co.yml.charts.ui.linechart.model.GridLines
import co.yml.charts.ui.linechart.model.IntersectionPoint
import co.yml.charts.ui.linechart.model.Line
import co.yml.charts.ui.linechart.model.LineChartData
import co.yml.charts.ui.linechart.model.LinePlotData
import co.yml.charts.ui.linechart.model.LineStyle
import co.yml.charts.ui.linechart.model.SelectionHighlightPoint
import co.yml.charts.ui.linechart.model.SelectionHighlightPopUp
import co.yml.charts.ui.linechart.model.ShadowUnderLine
import co.yml.ycharts.app.R
import co.yml.ycharts.app.ui.compositions.AppBarWithBackButton
import co.yml.ycharts.app.ui.theme.YChartsTheme

/**
 * Line chart activity
 *
 * @constructor Create empty Line chart activity
 */
class LineChartActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            YChartsTheme {
                Scaffold(modifier = Modifier.fillMaxSize(),
                    backgroundColor = YChartsTheme.colors.background,
                    topBar = {
                        AppBarWithBackButton(
                            stringResource(id = R.string.title_line_chart),
                            onBackPressed = {
                                onBackPressed()
                            })
                    })
                {
                    val color = resources.getColor(R.color.dark_grey)
                    val whiteBackground = resources.getColor(R.color.white)
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it),
                    ) {
                        CombinedLineChart(color, whiteBackground,
                            DataUtils.getLineChartData(
                                200,
                                start = -50,
                                maxRange = 50
                            )
                        )

                        CurveLine2()
                    }
                }
            }
        }
    }
}

@Composable
fun CurveLine() {
    val drawScope = remember { DrawScope }
    val canvasWidth = 800f
    val canvasHeight = 600f
    val modifier = Modifier
        .size(canvasWidth.dp, canvasHeight.dp)
        .drawWithContent {
            drawScope.apply {
                val path = Path().apply {
                    reset()
                    moveTo(0f, size.height / 2)
                    cubicTo(
                        size.width / 4, 0f,
                        size.width / 2, size.height,
                        size.width, size.height / 2
                    )
                }
                drawPath(path = path,
                    color = Color.Red,
                    style = Stroke(width = 4.dp.toPx()),
                )
            }
        }
    Box(modifier = modifier)
}

@Composable
fun CurveLine2() {
    val canvasWidth = 800f
    val canvasHeight = 600f
    val modifier = Modifier
        .size(canvasWidth.dp, canvasHeight.dp)
        .background(color = Color.White)
        .drawWithContent {
            val paint = Paint().asFrameworkPaint().apply {
                color = android.graphics.Color.RED
                style = android.graphics.Paint.Style.STROKE
                strokeWidth = 10f
                setShadowLayer(10f, 2f, 20f, android.graphics.Color.GRAY)
            }
            val path = android.graphics.Path().apply {
                moveTo(0f, size.height / 2)
                cubicTo(
                    size.width / 4, 0f,
                    size.width / 2, size.height,
                    size.width, size.height / 2
                )
            }
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawPath(path, paint)
            }
        }
    Box(modifier = modifier)
}




/**
 * Combined linechart
 *
 * @param pointsData
 */
@OptIn(ExperimentalTextApi::class)
@Composable
private fun CombinedLineChart(colorShadow: Int, colorBackground: Int, pointsData: List<Point>) {
    val textMeasurer = rememberTextMeasurer()
    val pointSmallRadius = 6.dp
    val pointBigRadius = 10.dp
    val pointTopMore = 40.dp
    val pointHighLightWidth = 36.dp


    val paintTriangle = Paint().apply {
        color = Color.White
    }
    val path = Path()

    val steps = 5
    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .steps(pointsData.size - 1)
        .labelData { i -> i.toString() }
        .labelAndAxisLinePadding(15.dp)
        .build()
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(20.dp)
        .axisConfig(AxisConfig(isAxisLineRequired = false))
        .labelData { i ->
            "${i * 10000}"
        }.build()
    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = DataUtils.getLineChartData(
                        50,
                        start = 0,
                        maxRange = 50
                    ),
                    LineStyle(
                        color = Color(0xFF4340F4),
                        width = 4f
                    ),
                    intersectionPoint = null,
                    selectionHighlightPoint = SelectionHighlightPoint(
                        draw = { point ->
                            drawCircle(
                                Color.White,
                                pointBigRadius.toPx(),
                                point,
                            )
                            drawCircle(
                                Color(0xFF4340F4),
                                pointSmallRadius.toPx(),
                                point,
                            )
                        },
                        drawHighlightLine = { start, end ->
//                            drawLine(
//                                Color(0x5AFF0000),
//                                start,
//                                end.copy(
//                                    end.x,
//                                    y = end.y - pointTopMore.toPx()
//                                ),
//                                pointHighLightWidth.toPx(),
//                            )
                        }
                    ),
                    shadowUnderLine = null,
                    selectionHighlightPopUp = SelectionHighlightPopUp(
                        draw = { selectedOffset, identifiedPoint ->
                            val roundRectWidth = 178.dp.toPx()
                            val roundRectHeight = 100.dp.toPx()
                            val roundRectTop = 10.dp.toPx()


                            val top = selectedOffset.y - roundRectHeight - roundRectTop
                            val paddingHorizontal = 12.dp.toPx()
                            val paddingTop = 5.dp.toPx()
                            val paint = Paint().apply {
                                color = Color.White
                            }

                            drawContext.canvas.apply {
                                /**调用底层fragment Paint绘制*/
                                val frameworkPaint = paint.asFrameworkPaint()
                                frameworkPaint.color = colorBackground
                                /**绘制阴影*/
                                frameworkPaint.setShadowLayer(
                                    8.dp.toPx(),
                                    0f,
                                    0f,
                                    colorShadow
                                )

                                drawRoundRect(
                                    left = selectedOffset.x - roundRectWidth / 2,
                                    top = top,
                                    right = selectedOffset.x + roundRectWidth / 2,
                                    bottom = selectedOffset.y - 20.dp.toPx(),
                                    radiusX = 8.dp.toPx(),
                                    radiusY = 8.dp.toPx(),
                                    paint = paint
                                )

                                path.reset()
                                path.moveTo(selectedOffset.x, selectedOffset.y - 14.dp.toPx())
                                path.lineTo(selectedOffset.x - 6.dp.toPx(), selectedOffset.y - 21.dp.toPx())
                                path.lineTo(selectedOffset.x + 6.dp.toPx(), selectedOffset.y - 21.dp.toPx())
                                path.close()
                                drawPath(path, paintTriangle)

                                var left = selectedOffset.x + roundRectWidth / 2 - paddingHorizontal
                                var y = top + paddingTop

                                val text = "19M"
                                val textPaint = TextPaint().apply {
                                    textSize = 14.sp.toPx()
                                }
                                val textLength = textPaint.measureText(text)

                                drawText(
                                    textMeasurer = textMeasurer,
                                    text = text,
                                    topLeft = Offset(left - textLength, y),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W400,
                                        color = Color(0xFF4A4A4A),
                                    )
                                )

                                left = selectedOffset.x - roundRectWidth / 2 + paddingHorizontal

                                drawText(
                                    textMeasurer = textMeasurer,
                                    text = "2017-11-30",
                                    topLeft = Offset(left, y),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.W400,
                                        color = Color(0xFF4A4A4A),
                                    )
                                )

                                y = top + 38.dp.toPx()

                                drawText(
                                    textMeasurer = textMeasurer,
                                    text = "Net sales",
                                    topLeft = Offset(left, y),
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        lineHeight = 22.sp,
                                        fontWeight = FontWeight.W400,
                                        color = Color(0xFF4A4A4A),
                                    )
                                )
                                y = top + 56.dp.toPx()

                                drawText(
                                    textMeasurer = textMeasurer,
                                    text = "201730",
                                    topLeft = Offset(left, y),
                                    style = TextStyle(
                                        fontSize = 28.sp,
                                        lineHeight = 28.sp,
                                        fontWeight = FontWeight.W700,
                                        color = Color(0xFF000000),
                                    )
                                )
                            }
                        }
                    )
                ),
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(
            drawVerticalLines = { _, _, _ -> }
        )
    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        lineChartData = data
    )
}


/**
 * Combined linechart
 *
 * @param pointsData
 */
@Composable
private fun CombinedLineChart2(pointsData: List<Point>) {
    val steps = 6
    val xAxisData = AxisData.Builder()
        .axisStepSize(100.dp)
        .steps(pointsData.size - 1)
        .axisLabelFontSize(16.sp)
        .labelData { i -> "$i AM" }
        .labelAndAxisLinePadding(15.dp)
        .build()
    val yAxisData = AxisData.Builder()
        .steps(steps)
        .labelAndAxisLinePadding(50.dp)
        //        .axisConfig(AxisConfig(isAxisLineRequired = false))//不显示左侧的第一个竖线
        .labelData { i ->
            "${i * 10000}"
        }.build()

    val data = LineChartData(
        linePlotData = LinePlotData(
            lines = listOf(
                Line(
                    dataPoints = pointsData,
                    lineStyle = LineStyle(
                        width = 4f,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    IntersectionPoint(),
                    SelectionHighlightPoint(),
                    ShadowUnderLine(),
                    SelectionHighlightPopUp()
                ),
                Line(
                    dataPoints = pointsData.map {
                        it.copy(y = it.y - 10)
                    },
                    LineStyle(
                        width = 12f,
                        color = Color(0x0A4340F4)
                    ),
                    intersectionPoint = null,
                    selectionHighlightPoint = null,
                    shadowUnderLine = null,
                    selectionHighlightPopUp = null
                ),
            )
        ),
        xAxisData = xAxisData,
        yAxisData = yAxisData,
        gridLines = GridLines(
            drawVerticalLines = { _, _, _ -> }
        )
    )

    LineChart(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp),
        lineChartData = data
    )
}