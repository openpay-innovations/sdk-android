package au.com.openpay.sdkandroid.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.content.res.use
import au.com.openpay.sdkandroid.Openpay
import au.com.openpay.sdkandroid.R
import au.com.openpay.sdkandroid.internal.coloredDrawable
import au.com.openpay.sdkandroid.internal.dp
import au.com.openpay.sdkandroid.internal.rippleDrawable
import au.com.openpay.sdkandroid.view.OpenpayBranding.OPENPAY
import au.com.openpay.sdkandroid.view.OpenpayBranding.OPY

private const val MIN_HEIGHT: Int = 48
private const val MIN_WIDTH: Int = 218
private const val MAX_WIDTH: Int = 380

private const val PADDING_START: Int = 20
private const val PADDING_TOP: Int = 10
private const val PADDING_END: Int = 20
private const val PADDING_BOTTOM: Int = 10

private const val CORNER_RADIUS: Float = 4f

class OpenpayButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageButton(context, attrs) {

    var colorScheme: ColorScheme = ColorScheme.DEFAULT
        set(value) {
            field = value
            update()
        }

    var cornerRadius: Float = CORNER_RADIUS.dp
        set(value) {
            field = value.dp
            update()
        }

    init {
        contentDescription =
            context.getString(R.string.openpay_button_payWithOpenpay_contentDescription)
        minimumHeight = MIN_HEIGHT.dp
        minimumWidth = MIN_WIDTH.dp
        maxWidth = MAX_WIDTH.dp
        setPaddingRelative(PADDING_START.dp, PADDING_TOP.dp, PADDING_END.dp, PADDING_BOTTOM.dp)

        context.theme.obtainStyledAttributes(attrs, R.styleable.OpenpayButton, 0, 0)
            .use { attributes ->
                colorScheme = ColorScheme.values()[
                    attributes.getInteger(
                        R.styleable.OpenpayButton_buttonColors,
                        ColorScheme.DEFAULT.ordinal
                    )
                ]

                cornerRadius =
                    attributes.getDimension(R.styleable.OpenpayButton_cornerRadius, CORNER_RADIUS)
            }
    }

    private fun update() {
        setImageDrawable(
            context.coloredDrawable(
                drawableResId = when (Openpay.branding) {
                    OPENPAY -> R.drawable.openpay_button_fg
                    OPY -> R.drawable.openpay_button_fg_opy
                },
                colorResId = colorScheme.foregroundColorResId
            )
        )

        background = context.rippleDrawable(
            rippleColorResId = colorScheme.rippleColorResId,
            backgroundColorResId = colorScheme.backgroundColorResId,
            cornerRadius = cornerRadius
        )

        invalidate()
        requestLayout()
    }

    enum class ColorScheme(
        @ColorRes val foregroundColorResId: Int,
        @ColorRes val backgroundColorResId: Int,
        @ColorRes val rippleColorResId: Int
    ) {

        GRANITE_ON_AMBER(
            foregroundColorResId = R.color.openpay_granite,
            backgroundColorResId = R.color.openpay_amber,
            rippleColorResId = R.color.openpay_ripple_light
        ),
        AMBER_ON_GRANITE(
            foregroundColorResId = R.color.openpay_amber,
            backgroundColorResId = R.color.openpay_granite,
            rippleColorResId = R.color.openpay_ripple_dark
        ),
        GRANITE_ON_WHITE(
            foregroundColorResId = R.color.openpay_granite,
            backgroundColorResId = android.R.color.white,
            rippleColorResId = R.color.openpay_ripple_light
        ),
        WHITE_ON_GRANITE(
            foregroundColorResId = android.R.color.white,
            backgroundColorResId = R.color.openpay_granite,
            rippleColorResId = R.color.openpay_ripple_dark
        );

        companion object {

            @JvmField
            val DEFAULT = GRANITE_ON_AMBER
        }
    }
}
