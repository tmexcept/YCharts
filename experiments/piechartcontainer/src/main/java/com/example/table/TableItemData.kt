package com.example.table

import androidx.compose.ui.graphics.Color
import com.example.piechartcontainer.ui.theme.color_0x99FFFFFF
import com.example.piechartcontainer.ui.theme.color_0xFF14CABF
import com.example.piechartcontainer.ui.theme.color_0xFF212121
import com.example.piechartcontainer.ui.theme.color_0xFF4E5969
import com.example.piechartcontainer.ui.theme.color_0xFF8C8A9A
import com.example.piechartcontainer.ui.theme.color_0xFF9195A4
import com.example.piechartcontainer.ui.theme.color_0xFFFDFDFD
import com.example.piechartcontainer.ui.theme.color_0xFFFFFFFF


data class TableItemData(
    val status: Int,
    val groupName: String,
    //是否选中
    val selected: Boolean = false,
    val openTime: Long? = null,
    //使用时长
    var usingTime: String = "",
    val seatNum: Int = 0,
    var guestNum: Int = 0,
    val orderId: String = "",
    val tableNum: String = "",
    val tableName: String = "",
    val realTable: Boolean,
    val tableId: String,
    val amount: String,
    val inCombine: Boolean = false,
    //连台下可用
    val areaId: String,
    val hasBooingInfo: Boolean = false,
//    val bookingInfo: ReservationItemEntity? = null,
    val haveItem: Boolean = false,
    val areaName: String,

    val reservationConflict: Boolean = false,

    var canCancelTableStatus: Boolean?=false,

    ) {
    var tableColor = color_0xFFFDFDFD
    var tableNameColor = color_0xFFFDFDFD
    var tableNumColor = color_0x99FFFFFF
    var subContentColor = color_0xFFFFFFFF
    var iconColor = color_0xFFFDFDFD
    var shadowColor: Color? = null

    init {
        refreshColor()

    }

    fun canMerge():Boolean{
        // todo 根据后端数据判断
        return true
    }

    fun canCancelTable(): Boolean {
        return canCancelTableStatus != null && canCancelTableStatus==true  && this.status == TableStatus.USED.value
    }

    fun getShowName(): String {
        return this.tableName
    }

    companion object {

        fun createItemFromReservation(tableItemInfo: TableItemInfo): TableItemData {
            val item = createItem(tableItemInfo)
            item.tableColor = color_0xFFFDFDFD
            item.tableNameColor = color_0xFF212121
            item.subContentColor = color_0xFF8C8A9A
            return item
        }

        fun createItem(tableItemInfo: TableItemInfo): TableItemData {
            return TableItemData(
                status = tableItemInfo.tableStatus,
                groupName = "${tableItemInfo.groupName}",
                selected = false,
                openTime = tableItemInfo.openTime,
                usingTime = if (tableItemInfo.tableStatus == TableStatus.USED.value) tableItemInfo.openTime?.let {
                    getTimeDate(
                        it
                    )
                } ?: "" else "",
                seatNum = tableItemInfo.guestCountMax,
                guestNum = tableItemInfo.numOfGuests,
                orderId = tableItemInfo.localOrderId ?: "",
                tableNum = tableItemInfo.tableCode,
                tableName = tableItemInfo.tableName,
                realTable = tableItemInfo.realTable,
                tableId = tableItemInfo.tableId,
                amount = "${tableItemInfo.total}",
                inCombine = tableItemInfo.groupName > 0,
                areaId = tableItemInfo.areaId,
                hasBooingInfo = tableItemInfo.hasBooingInfo ?: false,
//                bookingInfo = tableItemInfo.bookingInfo,
                areaName = tableItemInfo.areaName,
                canCancelTableStatus = tableItemInfo.canCancelTableStatus,
            )
        }

        fun getTimeDate(openTime: Long): String {
            return "3h4m"

        }
    }


    private fun refreshColor() {
        when (this.status) {
            TableStatus.EMPTY.value -> {
                tableColor = color_0xFFFDFDFD
                tableNameColor = color_0xFF212121
                tableNumColor = color_0xFF8C8A9A
                subContentColor = color_0xFF4E5969
                iconColor = color_0xFF8C8A9A
                if (this.hasBooingInfo) {
                    shadowColor = color_0xFFFFFFFF.copy(0.67f)
                }
            }

            TableStatus.USED.value -> {
                tableColor = color_0xFF14CABF
                iconColor = color_0xFFFDFDFD
                if (this.hasBooingInfo) {
                    shadowColor = color_0xFFFFFFFF.copy(0.67f)
                }
            }

            TableStatus.LOCKED.value -> {
                tableColor = color_0xFF9195A4
            }

            TableStatus.TO_BE_CLEANED.value -> {
                tableColor = color_0xFF9195A4
            }
        }
    }
}

enum class TableHostType(val value: String, val dsc: String) {
    HOST("1", "主台"),
    NOT_HOST("0", "从台"),
}

enum class TableStatus(val value: Int, val dsc: String) {
    EMPTY(0, "空台"),
    USED(10, "使用中"),
    TO_BE_CLEANED(20, "待清台"),
    LOCKED(30, "锁定"),
}

/**
 * tableId	string 桌台ID
 * tableCode	string 桌台编码
 * tableName	string 桌台名称
 * realTable	boolean 是否真实桌台
 * minimumAmount	number 最低消费
 * tableHost	string
 * uniqueId	string
 * groupName	integer areaId	string 区域ID
 * areaName	string 区域名称
 * guestCountMin	integer 最小人数
 * guestCountMax	integer 最大人数
 * tableStatus	integer TableStatusEnum.EMPTY.getValue() 桌台状态
 * openTime	object 备注: 开台时间
 *
 * localOrderId	string 账单ID
 * numOfGuests	integer 桌台人数
 * total	number public static final BigDecimal ZERO; 账单金额, 对应 orderCO 的total
 * isVipOrder	boolean false * 是否会员
 * cardNo	string  卡号
 * customerName	string 会员名称
 * hasBooingInfo	boolean 是否有预订信息
 * bookingInfo	object * 预订信息
 */
data class TableItemInfo(
    val tableId: String,
    val tableCode: String,
    val tableName: String,
    val realTable: Boolean = false,
    val minimumAmount: String = "",
    /**
     * @see TableHostType
     */
    val tableHost: String? = "",
    val groupName: Int,
    val uniqueId: String? = null,
    val areaId: String,
    val areaName: String,
    val guestCountMin: Int,
    val guestCountMax: Int,

    /**
     * @see [TableStatus]
     */
    val tableStatus: Int,
    val openTime: Long? = null,
    val localOrderId: String? = "",
    val numOfGuests: Int = 0,
    val tableSeat: Int = 0,
    val total: Float = 0f,
    val cardNo: String? = "",
    val customerName: String? = "",
    val vipOrder: Boolean = false,
    val hasBooingInfo: Boolean? = false,
//    val bookingInfo: ReservationItemEntity? = null,
    var canCancelTableStatus: Boolean? = null,

    )