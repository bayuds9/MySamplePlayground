package id.flowerencee.mysampleplayground.ui

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.MemoryCategory
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestListener
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

fun View.isVisible() : Boolean {
    return this.visibility == View.VISIBLE
}

fun View.setVisible(state: Boolean) {
    when(state) {
        true -> this.toShow()
        else -> this.toHide()
    }
}

fun View.toShow() {
    if (!this.isVisible()) this.visibility = View.VISIBLE
}

fun View.toHide() {
    if (this.visibility != View.GONE) this.visibility = View.GONE
}

fun loadImage(context: Context,
              url: Any,
              imageView: ImageView,
              placeHolder: Int,
              listener: RequestListener<Drawable>? = null) {

    fun setMemoryCategory(context: Context) {
        Glide.get(context).setMemoryCategory(MemoryCategory.NORMAL)
    }

    setMemoryCategory(context)
    Glide.with(context)
        .load(url)
        .centerCrop()
        .dontAnimate()
        .addListener(listener)
        .placeholder(placeHolder)
        .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
        .into(imageView)
}

fun Double.thousandSeparator(): String {
    val myFormatter = DecimalFormat("###,###,###.##", DecimalFormatSymbols(Locale.GERMANY))
    return myFormatter.format(this)
}

fun String.getIntValue(): Int {
    return try {
        this.toInt()
    } catch (e: Exception) {
        e.printStackTrace()
        0
    }
}