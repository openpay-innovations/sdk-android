package au.com.openpay.sdkandroid.internal

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import au.com.openpay.sdkandroid.OpenpayCheckoutResult

internal class CheckoutResultContract : ActivityResultContract<String, OpenpayCheckoutResult>() {

    override fun createIntent(context: Context, checkoutUrl: String): Intent =
        Intent(context, OpenpayCheckoutActivity::class.java)
            .putExtra(OpenpayIntent.EXTRA_CHECKOUT_URL, checkoutUrl)

    override fun parseResult(resultCode: Int, result: Intent?): OpenpayCheckoutResult {
        return checkNotNull(result?.getParcelableExtra(OpenpayIntent.EXTRA_CHECKOUT_RESULT)) {
            "An intent containing a CheckoutResult was expected but not received"
        }
    }
}
