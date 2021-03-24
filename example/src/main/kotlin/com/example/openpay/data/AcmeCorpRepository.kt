package com.example.openpay.data

import com.example.openpay.domain.CheckoutDetails
import com.example.openpay.domain.CustomerDetails
import com.example.openpay.domain.MerchantRepository
import java.math.BigDecimal

class AcmeCorpRepository : MerchantRepository {

    override suspend fun createOrder(
        customerDetails: CustomerDetails,
        price: BigDecimal
    ): CheckoutDetails {

        // This is where the transaction details would be transmitted to the merchant's own
        // servers in order to create a plan/order and return the resulting handover URL and
        // transaction token of which the checkout URL is comprised.

        return CheckoutDetails(
            handoverUrl = "https://retailer.myopenpay.com.au/websales/",
            transactionToken = "azrIk0k5Lur81YKNyjzCb1WxFt85mI2CvzSmRapi3Xm4kritNeFTOpGi4UOg"
        )
    }
}
