package com.xxx.texteditor

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.xxx.texteditor.FileAdapter.VH
import java.io.File

/**
 * @Author: duke
 * @DateTime: 2022-11-09 14:27:19
 * @Description: 功能描述：
 */
class FileAdapter : RecyclerView.Adapter<VH>() {

    private val dataList = ArrayList<File>()

    private var onClickListener: ((File, Boolean) -> Unit)? = null
    private var onLongClickListener: ((File, Boolean) -> Unit)? = null

    fun setOnClickListener(l: ((File, Boolean) -> Unit)?) {
        onClickListener = l
    }

    fun setOnLongClickListener(l: ((File, Boolean) -> Unit)?) {
        onLongClickListener = l
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun setData(data: ArrayList<File>?, hasData:(Boolean)->Unit) {
//        dataList.clear()
//        if (data != null && data.isNotEmpty()) {
//            dataList.addAll(data)
//        }
//        notifyDataSetChanged()
//    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(array: Array<File>?, hasData: (Boolean) -> Unit) {
        // 先清空数据，再添加
        dataList.clear()
        if (array != null && array.isNotEmpty()) {
            dataList.addAll(array)
        }
        // 最终无论有没有数据，都需要是刷新页面
        hasData(dataList.size > 0)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val file = dataList[position]
        holder.text.text = file.absolutePath
        if (file.isDirectory) {
            holder.icon.setImageResource(R.drawable.icon_folder)
        } else {
            holder.icon.setImageResource(R.drawable.icon_file)
        }

        holder.itemView.setOnClickListener {
            onClickListener?.let { invoke -> invoke(file, file.isDirectory) }
        }
        holder.itemView.setOnLongClickListener {
            onLongClickListener?.let { invoke -> invoke(file, file.isDirectory) }
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView
        val text: TextView

        init {
            icon = itemView.findViewById(R.id.item_icon)
            text = itemView.findViewById(R.id.item_text)
        }
    }
}
