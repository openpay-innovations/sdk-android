package au.com.openpay.sdkandroid.internal

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.webkit.WebView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import au.com.openpay.sdkandroid.OpenpayCheckoutResult
import au.com.openpay.sdkandroid.OpenpayCheckoutResult.Status.CANCELLED
import au.com.openpay.sdkandroid.OpenpayCheckoutResult.Status.REQUEST_ERROR
import au.com.openpay.sdkandroid.R
import au.com.openpay.sdkandroid.databinding.ActivityOpenpayCheckoutBinding

internal class OpenpayCheckoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        with(ActivityOpenpayCheckoutBinding.inflate(layoutInflater)) {
            setContentView(root)
            window.setLayout(MATCH_PARENT, MATCH_PARENT)
            openpayWebView.onCreate(
                loadUrl = ::loadCheckoutUrl,
                onExternalRequest = ::openExternalLink,
                onRequestComplete = ::finish,
                onRequestError = ::onRequestError
            )
        }
    }

    override fun onBackPressed() {
        finish(OpenpayCheckoutResult(status = CANCELLED))
    }

    private fun loadCheckoutUrl(webView: WebView) {
        val url = checkNotNull(intent.getStringExtra(OpenpayIntent.EXTRA_CHECKOUT_URL)) {
            "An intent containing a checkout URL was expected but not received"
        }
        webView.loadUrl(url)
    }

    private fun openExternalLink(url: Uri) {
        runCatching { startActivity(Intent(Intent.ACTION_VIEW, url)) }
    }

    private fun onRequestError(webView: WebView, errorDescription: CharSequence?) {
        // Clear web view
        webView.loadUrl("about:blank")

        AlertDialog.Builder(this)
            .setTitle(R.string.openpay_dialog_checkoutLoadError_title)
            .setMessage(
                getString(
                    R.string.openpay_dialog_checkoutLoadError_message,
                    errorDescription?.let { "\n\n$it" }.orEmpty()
                )
            )
            .setPositiveButton(R.string.openpay_dialog_checkoutLoadError_button_reload) { dialog, _ ->
                loadCheckoutUrl(webView)
                dialog.dismiss()
            }
            .setNegativeButton(android.R.string.cancel) { dialog, _ -> dialog.cancel() }
            .setOnCancelListener { finish(OpenpayCheckoutResult(status = REQUEST_ERROR)) }
            .show()
    }

    private fun finish(result: OpenpayCheckoutResult) {
        setResult(RESULT_OK, Intent().putExtra(OpenpayIntent.EXTRA_CHECKOUT_RESULT, result))
        finish()
    }
}
