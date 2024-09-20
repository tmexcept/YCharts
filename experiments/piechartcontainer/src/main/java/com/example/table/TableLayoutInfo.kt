package com.example.table

import android.graphics.drawable.Drawable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.piechartcontainer.ui.theme.color_0xFF14CABF
import com.example.piechartcontainer.ui.theme.color_0xFF1D2129
import com.example.piechartcontainer.ui.theme.color_0xFF4E5969
import com.example.piechartcontainer.ui.theme.color_0xFF4E8AFF
import com.example.piechartcontainer.ui.theme.color_0xFF9195A3
import com.example.piechartcontainer.ui.theme.color_0xFFB1EEEA
import com.example.piechartcontainer.ui.theme.color_0xFFF7F8FA
import com.example.piechartcontainer.ui.theme.color_0xFFFDFDFD
import com.example.piechartcontainer.ui.theme.color_0xFFFFFFFF

data class Drawables(
    val combine: Drawable? = null,
    val chair: Drawable? = null,
    val guest: Drawable? = null,
    val book: Drawable? = null,
    val wall: Drawable? = null,
)

data class TableWidget(
    val offset: Offset,
    val angle: Float = 0f,
    val radius: Float = 0f,
    val width: Float = 0f,
    val height: Float = 0f,
    val sizeType: SizeType,
    val widgetColor: WidgetColor = WidgetColor.Empty(),
)

sealed class WidgetColor {
    abstract val tableColor: Int
    abstract val combineContentColor:Int
    abstract val combineBgColor:Int?
    abstract val tableNameColor:Int
    abstract val bottomBgColor:Int?
    abstract val bottomContentColor:Int

    data class Empty(
        override val tableColor:Int = color_0xFFF7F8FA.toArgb(),
        override val combineContentColor:Int = color_0xFF4E8AFF.toArgb(),
        override val combineBgColor:Int? = color_0xFFFFFFFF.toArgb(),
        override val tableNameColor:Int = color_0xFF1D2129.toArgb(),
        override val bottomBgColor:Int? = null,
        override val bottomContentColor:Int = color_0xFF4E5969.toArgb(),
    ) : WidgetColor()

    data class Used(
        override val tableColor:Int = color_0xFF14CABF.toArgb(),
        override val combineContentColor:Int = color_0xFF4E8AFF.toArgb(),
        override val combineBgColor:Int? = color_0xFFFFFFFF.toArgb(),
        override val tableNameColor:Int = color_0xFFFFFFFF.toArgb(),
        override val bottomBgColor:Int? = null,
        override val bottomContentColor:Int = color_0xFFFFFFFF.toArgb(),
    ) : WidgetColor()

    data class Booked(
        override val tableColor:Int = color_0xFFF7F8FA.toArgb(),
        override val combineContentColor:Int = color_0xFF4E8AFF.toArgb(),
        override val combineBgColor:Int? = color_0xFFFFFFFF.toArgb(),
        override val tableNameColor:Int = color_0xFF1D2129.toArgb(),
        override val bottomBgColor:Int? = color_0xFFB1EEEA.toArgb(),
        override val bottomContentColor:Int = color_0xFF4E5969.toArgb(),
    ) : WidgetColor()

    data class NeedClean(
        override val tableColor:Int = color_0xFF9195A3.toArgb(),
        override val combineContentColor:Int = color_0xFF4E8AFF.toArgb(),
        override val combineBgColor:Int? = color_0xFFFFFFFF.toArgb(),
        override val tableNameColor:Int = color_0xFFFFFFFF.toArgb(),
        override val bottomBgColor:Int? = null,
        override val bottomContentColor:Int = color_0xFFFFFFFF.toArgb(),
    ) : WidgetColor()

    data class Opened(
        override val tableColor:Int = color_0xFF9195A3.toArgb(),
        override val combineContentColor:Int = color_0xFF4E8AFF.toArgb(),
        override val combineBgColor:Int? = color_0xFFFFFFFF.toArgb(),
        override val tableNameColor:Int = color_0xFFFFFFFF.toArgb(),
        override val bottomBgColor:Int = color_0xFFB1EEEA.toArgb(),
        override val bottomContentColor:Int = color_0xFFFFFFFF.toArgb(),
    ) : WidgetColor()
}

data class LayoutSize(
    val iconSizePx: Float = 16f,
    val iconDiv: Float = 2f,
    //    val tableNameSmallHeightPx: Float = 26f,
    //    val tableNameMediumHeightPx: Float = 40f,
    val tableNameLargeHeightPx: Float = 48f,
    val tableNameSmallTextSizePx: Float = 20f,
    val tableNameLargeTextSizePx: Float = 30f,

    val bookingTimeTextSizePx: Float = 18f,
    val bookingTimeZoneHeight: Float = 32f,

    val combineTextSizePx: Float = 16f,
    val combineSmallPaddingPx: Float = 2f,
    val combineLargePaddingPx: Float = 4.5f,
    val combineBgCornerPx: Float = 8f,

    val guestTextSizePx: Float = 18f,
    val bottomBgHeightPx: Float = 32f,
    val guestZoneHeightPxLittleRect: Float = 36f,

    val rectRadius: Float = 12f,
    val rectBottomPaddingLtr: Float = 12f,
    val wallWidth: Float = 81f,
    val wallHeight: Float = 81f,
)

enum class SizeType {
    SMALL_CIRCLE,
    MEDIUM_CIRCLE,
    LARGE_CIRCLE,
    SMALL_RECT,
    MEDIUM_RECT,
    LARGE_RECT,
    MEDIUM_RECT_WIDTH,    //- 矩形桌台超宽（宽度 >=160px）时，展示金额。
    LARGE_RECT_WIDTH,     //- 矩形桌台超宽（宽度 >=160px）时，展示金额。
    WALL,
    BAR,
    CHAIR,
}

data class LayoutSizeDp(
    val iconSize: Dp = 16.dp,
    val combineTextSize: TextUnit = 16.sp,
    val smallTableNameTextSize: TextUnit = 20.sp,
    val bigTableNameTextSize: TextUnit = 30.sp,
    val guestNumberTextSize: TextUnit = 18.sp,
    val bookingTimeTextSize: TextUnit = 18.sp,
)