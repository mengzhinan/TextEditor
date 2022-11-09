package com.xxx.texteditor

import java.io.File

/**
 * @Author: duke
 * @DateTime: 2022-11-09 16:45:48
 * @Description: 功能描述：
 *
 */
interface OnItemClickCallback {

    fun onClick(file:File, isFolder:Boolean)

}