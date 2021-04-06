package au.com.openpay.sdkandroid.view

import android.content.Context
import android.util.AttributeSet
import androidx.annotation.ColorRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.res.use
import androidx.core.os.LocaleListCompat
import au.com.openpay.sdkandroid.R
import au.com.openpay.sdkandroid.internal.BackgroundDrawable
import au.com.openpay.sdkandroid.internal.color
import au.com.openpay.sdkandroid.internal.coloredDrawable
import au.com.openpay.sdkandroid.internal.dp
import java.util.Locale

private const val MIN_WIDTH: Int = 75
private const val MIN_WIDTH_OPY: Int = 40

private const val PADDING_START: Int = 6
private const val PADDING_TOP: Int = 6
private const val PADDING_END: Int = 6
private const val PADDING_BOTTOM: Int = 4

private const val CORNER_RADIUS: Float = 3f

class OpenpayBadge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : AppCompatImageView(context, attrs) {

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
        importantForAccessibility = IMPORTANT_FOR_ACCESSIBILITY_NO
        minimumWidth = when (LocaleListCompat.getDefault()[0]) {
            Locale.US -> MIN_WIDTH_OPY.dp
            else -> MIN_WIDTH.dp
        }
        setPaddingRelative(PADDING_START.dp, PADDING_TOP.dp, PADDING_END.dp, PADDING_BOTTOM.dp)

        context.theme.obtainStyledAttributes(attrs, R.styleable.OpenpayBadge, 0, 0)
            .use { attributes ->
                colorScheme = ColorScheme.values()[
                    attributes.getInteger(
                        R.styleable.OpenpayBadge_badgeColors,
                        ColorScheme.DEFAULT.ordinal
                    )
                ]

                cornerRadius =
                    attributes.getDimension(R.styleable.OpenpayBadge_cornerRadius, CORNER_RADIUS)
            }
    }

    private fun update() {
        setImageDrawable(
            context.coloredDrawable(
                drawableResId = when (LocaleListCompat.getDefault()[0]) {
                    Locale.US -> R.drawable.openpay_logo_fg_opy
                    else -> R.drawable.openpay_logo_fg
                },
                colorResId = colorScheme.foregroundColorResId
            )
        )

        background = BackgroundDrawable(
            backgroundColor = context.color(colorScheme.backgroundColorResId),
            cornerRadius = cornerRadius
        )

        invalidate()
        requestLayout()
    }

    enum class ColorScheme(
        @ColorRes val foregroundColorResId: Int,
        @ColorRes val backgroundColorResId: Int
    ) {

        GRANITE_ON_AMBER(
            foregroundColorResId = R.color.openpay_granite,
            backgroundColorResId = R.color.openpay_amber
        ),
        AMBER_ON_GRANITE(
            foregroundColorResId = R.color.openpay_amber,
            backgroundColorResId = R.color.openpay_granite
        );

        companion object {

            @JvmField
            val DEFAULT = GRANITE_ON_AMBER
        }
    }
}
