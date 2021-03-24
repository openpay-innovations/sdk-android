package com.example.openpay.domain

import java.math.BigDecimal

interface MerchantRepository {

    suspend fun createOrder(
        customerDetails: CustomerDetails,
        price: BigDecimal
    ): CheckoutDetails
}

data class CustomerDetails(
    val firstName: String,
    val familyName: String,
    val email: String,
    val phoneNumber: String?,
    val deliveryAddress: Address
)

data class Address(
    val line1: String,
    val suburb: String,
    val state: String,
    val postCode: String
)

data class CheckoutDetails(
    val handoverUrl: String,
    val transactionToken: String
)
