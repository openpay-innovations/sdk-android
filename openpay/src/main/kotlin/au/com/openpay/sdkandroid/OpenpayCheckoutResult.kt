package au.com.openpay.sdkandroid

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class OpenpayCheckoutResult(
    val status: Status,
    val planId: String? = null,
    val orderId: String? = null
) : Parcelable {

    enum class Status {
        SUCCESS,
        LODGED,
        CANCELLED,
        FAILURE,
        REQUEST_ERROR
    }

    internal companion object {

        fun fromUrl(url: Uri): OpenpayCheckoutResult? {
            val status = url.getQueryParameter("status")?.let(Status::valueOf)
            val planId = url.getQueryParameter("planid")
            val orderId = url.getQueryParameter("orderid")

            return status?.let { OpenpayCheckoutResult(status, planId, orderId) }
        }
    }
}
