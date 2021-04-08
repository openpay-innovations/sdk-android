package au.com.openpay.sdkandroid.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.use
import androidx.core.view.setPadding
import au.com.openpay.sdkandroid.Openpay
import au.com.openpay.sdkandroid.R
import au.com.openpay.sdkandroid.internal.coloredDrawable
import au.com.openpay.sdkandroid.internal.dp
import au.com.openpay.sdkandroid.view.OpenpayBranding.OPENPAY
import au.com.openpay.sdkandroid.view.OpenpayBranding.OPY

private const val MIN_WIDTH: Int = 80
private const val MIN_WIDTH_OPY: Int = 34

class OpenpayLogo @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

    var colorScheme: ColorScheme = ColorScheme.DEFAULT
        set(value) {
            field = value
            update()
        }

    init {
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        minimumWidth = when (Openpay.branding) {
            OPENPAY -> MIN_WIDTH.dp
            OPY -> MIN_WIDTH_OPY.dp
        }
        setPadding(0.dp)

        context.theme.obtainStyledAttributes(attrs, R.styleable.OpenpayLogo, 0, 0)
            .use { attributes ->
                colorScheme = ColorScheme.values()[
                    attributes.getInteger(
                        R.styleable.OpenpayLogo_logoColors,
                        ColorScheme.DEFAULT.ordinal
                    )
                ]
            }
    }

    private fun update() {
        setImageDrawable(
            context.coloredDrawable(
                drawableResId = when (Openpay.branding) {
                    OPENPAY -> R.drawable.openpay_logo_fg
                    OPY -> R.drawable.openpay_logo_fg_opy
                },
                colorResId = colorScheme.foregroundColorResId
            )
        )

        background = null

        invalidate()
        requestLayout()
    }

    enum class ColorScheme(@ColorRes val foregroundColorResId: Int) {

        GRANITE(foregroundColorResId = R.color.openpay_granite),
        WHITE(foregroundColorResId = android.R.color.white);

        companion object {

            @JvmField
            val DEFAULT = GRANITE
        }
    }
}
