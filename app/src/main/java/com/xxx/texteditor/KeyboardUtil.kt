package com.xxx.texteditor

import android.content.Context
import android.renderscript.ScriptGroup.Input
import android.view.View
import android.view.inputmethod.InputMethodManager

/**
 * @Author: duke
 * @DateTime: 2022-11-09 17:12:14
 * @Description: 功能描述：
 *
 */
object KeyboardUtil {

    fun showKeyboard(view: View?) {
        view ?: return
        view.postDelayed({
            val context = view.context ?: return@postDelayed
            val inputMethodManager =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            inputMethodManager?.showSoftInput(view, InputMethodManager.SHOW_FORCED)
        },200)
    }


}