@file:JvmName("Openpay")

package au.com.openpay.sdkandroid

import android.net.Uri
import android.util.Patterns
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultLauncher
import androidx.fragment.app.Fragment
import au.com.openpay.sdkandroid.internal.CheckoutResultContract
import au.com.openpay.sdkandroid.internal.OpenpayCheckoutActivity

/**
 * Create an [ActivityResultLauncher] for an [OpenpayCheckoutActivity].
 *
 * @param onCheckoutResult A callback that expects an [OpenpayCheckoutResult].
 * @return A [CheckoutWithOpenpay] instance that will launch an [OpenpayCheckoutActivity]
 * for an [OpenpayCheckoutResult].
 */
fun ComponentActivity.checkoutWithOpenpay(
    onCheckoutResult: (OpenpayCheckoutResult) -> Unit
): CheckoutWithOpenpay = CheckoutWithOpenpay(
    checkoutLauncher = registerForActivityResult(CheckoutResultContract()) { onCheckoutResult(it) }
)

/**
 * Create an [ActivityResultLauncher] for an [OpenpayCheckoutActivity].
 *
 * @param onCheckoutResult A callback that expects an [OpenpayCheckoutResult].
 * @return A [CheckoutWithOpenpay] instance that will launch an [OpenpayCheckoutActivity]
 * for an [OpenpayCheckoutResult].
 */
fun Fragment.checkoutWithOpenpay(
    onCheckoutResult: (OpenpayCheckoutResult) -> Unit
): CheckoutWithOpenpay = CheckoutWithOpenpay(
    checkoutLauncher = registerForActivityResult(CheckoutResultContract()) { onCheckoutResult(it) }
)

data class CheckoutWithOpenpay(private val checkoutLauncher: ActivityResultLauncher<String>) {

    /**
     * Launch an instance of [OpenpayCheckoutActivity].
     *
     * @param handoverUrl The Openpay handover URL.
     * @param transactionToken A valid Openpay transaction token.
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
