package com.github.livingwithhippos.unchained.utilities

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.drawable.LayerDrawable
import android.net.Uri
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.github.livingwithhippos.unchained.R

@BindingAdapter("imageURL")
fun ImageView.loadImage(imageURL: String?) {
    if (imageURL != null)
        Glide.with(this.context)
            .load(imageURL)
            .into(this)
}

@BindingAdapter("adapter")
fun AutoCompleteTextView.setAdapter(contents: List<String>) {
    // a simple layout is set for the dropdown items
    val adapter = ArrayAdapter<String>(this.context, R.layout.dropdown_plain_item, contents)
    this.setAdapter(adapter)
}

@BindingAdapter("progressColor")
fun ProgressBar.setProgressColor(color: Int) {
    val progressBarLayers = progressDrawable as LayerDrawable
    val progressDrawable = progressBarLayers.findDrawableByLayerId(android.R.id.progress).mutate()
    progressDrawable.setTint(color)
}

@BindingAdapter("progressDrawable")
fun ProgressBar.setProgressDrawable(drawableId: Int) {
    val drawable = ContextCompat.getDrawable(context, drawableId)
    drawable?.let{
        val progressBarLayers = progressDrawable as LayerDrawable
        progressBarLayers.setDrawableByLayerId(android.R.id.progress, it)
    }
}

fun Fragment.showToast(stringResource: Int, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(requireContext(), getString(stringResource), length).show()
}

// note: should this be added to Context instead of Fragment?
fun Fragment.copyToClipboard(label: String, text: String) {
    val clipboard = requireContext().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val clip: ClipData = ClipData.newPlainText(label, text)
    // Set the clipboard's primary clip.
    clipboard.setPrimaryClip(clip)
}

fun Fragment.openExternalWebPage(url: String, showErrorToast: Boolean = true): Boolean {
    // this pattern accepts everything that is something.tld since there were too many new tlds and Google gave up updating their regex
    if (Patterns.WEB_URL.matcher(url).matches()) {
        val webIntent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(webIntent)
        return true
    } else
        if (showErrorToast)
            showToast(R.string.invalid_url)

    return false
}