package com.example.table

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
    val radius: Float,
    val width: Float,
    val height: Float,
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
)

enum class SizeType{
    SMALL_CIRCLE,
    MEDIUM_CIRCLE,
    LARGE_CIRCLE,
    SMALL_RECT,
    MEDIUM_RECT,
    LARGE_RECT,
    WALL,
    BAR,
    CHAIR,
}