package com.example.table

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.toArgb
import com.example.piechartcontainer.ui.theme.color_0xFFFFFFFF


val circleSmall = listOf(
    TableWidget(
        offset = Offset(10f, 20f),
        radius = 30f,
        sizeType = SizeType.SMALL_CIRCLE,
        widgetColor = WidgetColor.Empty().copy(
            combineBgColor = null,
        )
    ),
    TableWidget(
        offset = Offset(200f, 20f),
        radius = 30f,
        sizeType = SizeType.SMALL_CIRCLE,
        widgetColor = WidgetColor.Used().copy(
            combineBgColor = null,
            combineContentColor = color_0xFFFFFFFF.toArgb(),
        )
    ),
    TableWidget(
        offset = Offset(400f, 20f),
        radius = 30f,
        sizeType = SizeType.SMALL_CIRCLE,
        widgetColor = WidgetColor.Booked().copy(
            combineBgColor = null,
        )
    ),
    TableWidget(
        offset = Offset(600f, 20f),
        radius = 30f,
        sizeType = SizeType.SMALL_CIRCLE,
        widgetColor = WidgetColor.NeedClean().copy(
            combineBgColor = null,
            combineContentColor = color_0xFFFFFFFF.toArgb(),
        )
    ),
)

val circleMedium = listOf(
    TableWidget(
        offset = Offset(10f, 100f),
        radius = 50f,
        sizeType = SizeType.MEDIUM_CIRCLE,
        widgetColor = WidgetColor.Empty()
    ),
    TableWidget(
        offset = Offset(200f, 100f),
        radius = 50f,
        sizeType = SizeType.MEDIUM_CIRCLE,
        widgetColor = WidgetColor.Used()
    ),
    TableWidget(
        offset = Offset(400f, 100f),
        radius = 50f,
        sizeType = SizeType.MEDIUM_CIRCLE,
        widgetColor = WidgetColor.Booked()
    ),
    TableWidget(
        offset = Offset(600f, 100f),
        radius = 50f,
        sizeType = SizeType.MEDIUM_CIRCLE,
        widgetColor = WidgetColor.NeedClean()
    ),
)

val circleLarge = listOf(
    TableWidget(
        offset = Offset(10f, 240f),
        radius = 70f,
        sizeType = SizeType.LARGE_CIRCLE,
        widgetColor = WidgetColor.Empty()
    ),
    TableWidget(
        offset = Offset(200f, 240f),
        radius = 70f,
        sizeType = SizeType.LARGE_CIRCLE,
        widgetColor = WidgetColor.Used()
    ),
    TableWidget(
        offset = Offset(400f, 240f),
        radius = 70f,
        sizeType = SizeType.LARGE_CIRCLE,
        widgetColor = WidgetColor.Booked()
    ),
    TableWidget(
        offset = Offset(600f, 240f),
        radius = 70f,
        sizeType = SizeType.LARGE_CIRCLE,
        widgetColor = WidgetColor.NeedClean()
    ),
)

val rectSmall = listOf(
    TableWidget(
        offset = Offset(10f, 400f),
        width = 60f,
        height = 60f,
        sizeType = SizeType.SMALL_RECT,
        widgetColor = WidgetColor.Empty().copy(
            combineBgColor = null,
        )
    ),
    TableWidget(
        offset = Offset(200f, 400f),
        width = 60f,
        height = 60f,
        sizeType = SizeType.SMALL_RECT,
        widgetColor = WidgetColor.Used().copy(
            combineBgColor = null,
        )
    ),
    TableWidget(
        offset = Offset(400f, 400f),
        width = 60f,
        height = 60f,
        sizeType = SizeType.SMALL_RECT,
        widgetColor = WidgetColor.Booked().copy(
            combineBgColor = null,
        )
    ),
    TableWidget(
        offset = Offset(600f, 400f),
        width = 60f,
        height = 60f,
        sizeType = SizeType.SMALL_RECT,
        widgetColor = WidgetColor.NeedClean().copy(
            combineBgColor = null,
        )
    ),
)

val rectMedium = listOf(
    TableWidget(
        offset = Offset(10f, 500f),
        width = 100f,
        height = 100f,
        sizeType = SizeType.MEDIUM_RECT,
        widgetColor = WidgetColor.Empty()
    ),
    TableWidget(
        offset = Offset(200f, 500f),
        width = 100f,
        height = 100f,
        sizeType = SizeType.MEDIUM_RECT,
        widgetColor = WidgetColor.Used()
    ),
    TableWidget(
        offset = Offset(400f, 500f),
        width = 100f,
        height = 100f,
        sizeType = SizeType.MEDIUM_RECT,
        widgetColor = WidgetColor.Booked()
    ),
    TableWidget(
        offset = Offset(600f, 500f),
        width = 100f,
        height = 100f,
        sizeType = SizeType.MEDIUM_RECT,
        widgetColor = WidgetColor.NeedClean()
    ),
)

val rectLarge = listOf(
    TableWidget(
        offset = Offset(10f, 640f),
        width = 140f,
        height = 140f,
        sizeType = SizeType.LARGE_RECT,
        widgetColor = WidgetColor.Empty()
    ),
    TableWidget(
        offset = Offset(200f, 640f),
        width = 140f,
        height = 140f,
        sizeType = SizeType.LARGE_RECT,
        widgetColor = WidgetColor.Used()
    ),
    TableWidget(
        offset = Offset(400f, 640f),
        width = 140f,
        height = 140f,
        sizeType = SizeType.LARGE_RECT,
        widgetColor = WidgetColor.Booked()
    ),
    TableWidget(
        offset = Offset(600f, 640f),
        width = 140f,
        height = 140f,
        sizeType = SizeType.LARGE_RECT,
        widgetColor = WidgetColor.NeedClean()
    ),
)