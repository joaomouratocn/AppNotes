package com.example.appnotes.util

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.widget.ImageView
import androidx.viewbinding.ViewBinding
import coil.load
import com.example.appnotes.R

fun Context.gotoActivity(clazz: Class<*>, intent:Intent.() -> Unit = {}){
    Intent(this, clazz).apply {
        intent()
        startActivity(this)
    }
}

fun Context.createDialog(
    viewBinding:ViewBinding? = null,
    title: String? = null,
    message:String? = null,
    negativeButtonText:String? = null,
    positiveButtonText:String? = null,
    actionPositive: (() -> Unit)? = null
):AlertDialog{
    return if (viewBinding != null){
        AlertDialog.Builder(this)
            .setView(viewBinding.root)
            .create()
    }else{
        AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(negativeButtonText, null)
            .setPositiveButton(positiveButtonText){_,_ ->
                actionPositive?.invoke()
            }
            .create()
    }
}

fun ImageView.loadImage(url:String?){
    load(url){
        crossfade(true)
        error(R.drawable.image_failure_load_image)
        fallback(R.drawable.place_holder)
    }
}