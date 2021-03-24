package au.com.openpay.sdkandroid.internal

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Message
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import au.com.openpay.sdkandroid.OpenpayCheckoutResult

@SuppressLint("SetJavaScriptEnabled")
internal fun WebView.onCreate(
    onExternalRequest: (Uri) -> Unit,
    onRequestComplete: (OpenpayCheckoutResult) -> Unit,
    onRequestError: (WebView, String?) -> Unit,
    loadUrl: (WebView) -> Unit
): WebView = apply {
    settings.javaScriptEnabled = true
    settings.setSupportMultipleWindows(true)
    webChromeClient = OpenpayWebChromeClient(onExternalRequest)
    webViewClient = OpenpayWebViewClient(onRequestComplete, onRequestError)
    loadUrl(this)
}

internal class OpenpayWebChromeClient(
    private val onExternalRequest: (Uri) -> Unit
) : WebChromeClient() {

    override fun onCreateWindow(
        webView: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        val message = webView?.handler?.obtainMessage()
        webView?.requestFocusNodeHref(message)
        message?.data?.getString("url")?.let { onExternalRequest(Uri.parse(it)) }
        return false
    }
}

internal class OpenpayWebViewClient(
    private val onRequestComplete: (OpenpayCheckoutResult) -> Unit,
    private val onRequestError: (WebView, String?) -> Unit
) : WebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val result = request?.url?.let { OpenpayCheckoutResult.fromUrl(it) } ?: return false
        onRequestComplete(result)
        return true
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onReceivedError(
        webView: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        checkNotNull(webView) { "A WebView was expected but not received" }
        if (request?.isForMainFrame == true) onRequestError(webView, error?.description.toString())
    }

    override fun onReceivedError(
        webView: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        checkNotNull(webView) { "A WebView was expected but not received" }
        onRequestError(webView, description)
    }
}
