package com.example.table

import android.graphics.drawable.Drawable
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class Drawables(
    val combine: Drawable? = null,
    val chair: Drawable? = null,
    val guest: Drawable? = null,
    val book: Drawable? = null,
)

data class TableLayoutInfo(
    val xOffset: Float,
    val yOffset: Float,
    val width: Float,
    val height: Float,
    val circleRadius: Int = 12,
    val rectRadius: Int = 12,
    val angle: Float,
    val clickBorder: Int = 12,
)

data class TableWidget(
    val offset: Offset,
    val angle: Float,
    val radius: Float = 0f,
    val width: Float = 0f,
    val height: Float = 0f,
    val sizeType: SizeType,
)

//data class LayoutSize(
//    val iconSize: Float = 16f,
//    val combineTextSize: Float = 16f,
//    val smallTableNameTextSize: Float = 20f,
//    val bigTableNameTextSize: Float = 30f,
//    val guestNumberTextSize: Float = 18f,
//    val bookingTimeTextSize: Float = 18f,
//)

data class LayoutSize(
    val iconSize: Dp = 16.dp,
    val combineTextSize: TextUnit = 16.sp,
    val smallTableNameTextSize: TextUnit = 20.sp,
    val bigTableNameTextSize: TextUnit = 30.sp,
    val guestNumberTextSize: TextUnit = 18.sp,
    val bookingTimeTextSize: TextUnit = 18.sp,

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
)

enum class SizeType{
    SMALL_CIRCLE,
    MEDIUM_CIRCLE,
    LARGE_CIRCLE,
    SMALL_SQUARE,
    MEDIUM_SQUARE,
    LARGE_SQUARE,
    MEDIUM_RECT,
    LARGE_RECT,
    WALL,
    BAR,
    CHAIR,
}