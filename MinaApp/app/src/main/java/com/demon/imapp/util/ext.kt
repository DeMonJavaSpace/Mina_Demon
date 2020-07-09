package com.demon.imapp.util

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.google.gson.Gson

/**
 * @author DeMon
 * Created on 2020/5/14.
 * E-mail 757454343@qq.com
 * Desc:
 */
val gson = Gson()

fun Context?.toast(str: String) {
    Toast.makeText(this, str, Toast.LENGTH_SHORT).show()
}

fun Any?.toJson(): String = gson.toJson(this)


inline val <T : Any> T.TAG: String
    get() = ((this as Object).getClass() as Class<T>).simpleName

fun View?.hideSoft(context: Context) {
    val inputMethodManager = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
}



