package movee.presentation.core

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.graphics.drawable.Drawable
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.core.graphics.drawable.toBitmap
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.palette.graphics.Palette
import movee.presentation.theme.isDark

fun Context.asActivity(): Activity? {
    return when (this) {
        is Activity -> this
        is ContextWrapper -> this.baseContext.asActivity()
        else -> null
    }
}

fun Context.showToast(text: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, text, duration).show()
}

fun Context.getInsetsController(): WindowInsetsControllerCompat? {
    if (asActivity() == null) return null

    val activity = asActivity()!!

    return WindowCompat.getInsetsController(
        activity.window,
        activity.window.decorView
    )
}

fun Context.setStatusBarAppearanceByDrawable(drawable: Drawable?) {
    if (drawable != null) {
        val dominantDrawableRgb = Palette.from(drawable.toBitmap())
            .generate()
            .dominantSwatch
            ?.rgb

        if (dominantDrawableRgb != null) {
            getInsetsController()?.isAppearanceLightStatusBars = !Color(dominantDrawableRgb).isDark
        }
    }
}