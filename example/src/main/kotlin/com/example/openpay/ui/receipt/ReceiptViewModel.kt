package com.example.openpay.ui.receipt

import androidx.annotation.StringRes
import androidx.lifecycle.ViewModel
import com.example.openpay.R
import com.example.openpay.ui.toCurrencyString
import com.example.openpay.ui.viewModelFactory
import java.math.BigDecimal

class ReceiptViewModel(
    orderID: String,
    orderTotal: BigDecimal
) : ViewModel() {

    val orderDetails: OrderDetails = OrderDetails(
        messageResID = R.string.receipt_message,
        orderID = orderID,
        orderTotal = orderTotal.toCurrencyString()
    )

    data class OrderDetails(
        @StringRes val messageResID: Int,
        val orderID: String,
        val orderTotal: String
    )

    companion object {

        fun factory(orderID: String, orderTotal: BigDecimal) = viewModelFactory {
            ReceiptViewModel(orderID, orderTotal)
        }
    }
}
