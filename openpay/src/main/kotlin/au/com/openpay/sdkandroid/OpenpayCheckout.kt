@file:JvmName("OpenpayCheckout")

package au.com.openpay.sdkandroid

import android.net.Uri
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import au.com.openpay.sdkandroid.internal.CheckoutResultContract
import au.com.openpay.sdkandroid.internal.OpenpayCheckoutActivity

/**
 * Returns a [launcher][CheckoutWithOpenpay] that will present an [OpenpayCheckoutActivity] and
 * pass the [result][OpenpayCheckoutResult] to the given [callback][onCheckoutResult].
 */
@JvmName("commence")
fun ComponentActivity.checkoutWithOpenpay(
    onCheckoutResult: (OpenpayCheckoutResult) -> Unit
): CheckoutWithOpenpay = CheckoutWithOpenpay(
    checkoutLauncher = registerForActivityResult(CheckoutResultContract()) { onCheckoutResult(it) }
)

/**
 * Returns a [launcher][CheckoutWithOpenpay] that will present an [OpenpayCheckoutActivity] and
 * pass the [result][OpenpayCheckoutResult] to the given [callback][onCheckoutResult].
 */
@JvmName("commence")
fun Fragment.checkoutWithOpenpay(
    onCheckoutResult: (OpenpayCheckoutResult) -> Unit
): CheckoutWithOpenpay = CheckoutWithOpenpay(
    checkoutLauncher = registerForActivityResult(CheckoutResultContract()) { onCheckoutResult(it) }
)

data class CheckoutWithOpenpay(private val checkoutLauncher: ActivityResultLauncher<String>) {

    /**
     * Launches an [OpenpayCheckoutActivity] with a URL comprising the given
     * [handover URL][handoverUrl] and [transaction token][transactionToken].
     */
    operator fun invoke(
        handoverUrl: String,
        transactionToken: String,
    ) {
        require(Patterns.WEB_URL.matcher(handoverUrl).matches()) {
            "A valid handover URL is required"
        }

        require(transactionToken.isNotBlank()) {
            "A valid transaction token is required"
        }

        checkoutLauncher.launch(
            Uri.parse(handoverUrl)
                .buildUpon()
                .encodedQuery("TransactionToken=$transactionToken")
                .build()
                .toString()
        )
    }
}
