package com.xxx.texteditor

import android.content.Context
import android.widget.Toast

/**
 * @Author: duke
 * @DateTime: 2022-11-09 11:07:14
 * @Description: 功能描述：
 *
 */
object Util {

    fun trimAfter(s: String?): String? {
        return s?.trim()
    }

    fun isNullOrEmpty(s: String?): Boolean {
        val n = trimAfter(s)
        return s == null || "" == s
    }

    fun showToast(context: Context, msg: String) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
    }

}
