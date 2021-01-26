package se.appshack.android.refactoring.util

import android.os.Build
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide

@BindingAdapter("showLoading")
fun View.showLoading(resource: Resource<*>?) {
    visibility = if (resource is Resource.Loading) View.VISIBLE else View.GONE
}

@BindingAdapter("showError")
fun View.showError(resource: Resource<*>?) {
    visibility = if (resource is Resource.Failure) View.VISIBLE else View.GONE
}

@BindingAdapter("showData")
fun View.showData(resource: Resource<*>?) {
    visibility = if (resource is Resource.Success) View.VISIBLE else View.GONE
}

@BindingAdapter("imageUrl")
fun ImageView.bindImage(url: String?) {
    Glide.with(context).load(url).into(this)
}

@Suppress("DEPRECATION")
@BindingAdapter("showText")
fun TextView.bindText(url: String) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(url, Html.FROM_HTML_MODE_COMPACT)
    } else {
        Html.fromHtml(url)
    }
}