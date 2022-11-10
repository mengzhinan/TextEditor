package com.xxx.texteditor

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var tvNoData: TextView
    private lateinit var tvTopCurrentPath: TextView
    private lateinit var createFolderBtn: Button
    private lateinit var createFileBtn: Button
    private lateinit var backBtn: Button

    private lateinit var recyclerView: RecyclerView
    private val fileAdapter: FileAdapter = FileAdapter()

    private var currentFolder: File? = FileHub.getSDCardDownloadFolder()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tvNoData = findViewById(R.id.tv_no_data)
        tvTopCurrentPath = findViewById(R.id.tv_current_path)
        tvTopCurrentPath.text = currentFolder?.absolutePath ?: ""

        createFolderBtn = findViewById(R.id.btn_create_folder)
        createFolderBtn.setOnClickListener {
            showInputDialog("创建文件夹") {
                FileHub.createNewFolder(currentFolder, it)
                fileAdapter.setData(FileHub.getSubFileList(currentFolder)) { hasDataBoolean ->
                    hasData(hasDataBoolean)
                }
            }
        }
        createFileBtn = findViewById(R.id.btn_create_file)
        createFileBtn.setOnClickListener {
            showInputDialog("创建文件") {
                FileHub.createNewFile(currentFolder, it)
                fileAdapter.setData(FileHub.getSubFileList(currentFolder)) { hasDataBoolean ->
                    hasData(hasDataBoolean)
                }
            }
        }
        backBtn = findViewById(R.id.btn_back)
        backBtn.setOnClickListener {
            val rootStr = FileHub.getSDCardDownloadFolder().absolutePath ?: ""
            if ((currentFolder?.absolutePath ?: "") == rootStr) {
                // 尝试重新赋值，获取最新的 list
                currentFolder = FileHub.getSDCardDownloadFolder()
                tvTopCurrentPath.text = currentFolder?.absolutePath ?: ""
                Util.showToast(this, "已经是根目录了")
                fileAdapter.setData(FileHub.getSubFileList(currentFolder)) { hasDataBoolean ->
                    hasData(hasDataBoolean)
                }
                return@setOnClickListener
            }
            currentFolder = currentFolder?.parentFile
            tvTopCurrentPath.text = currentFolder?.absolutePath ?: ""
            fileAdapter.setData(FileHub.getSubFileList(currentFolder)) { hasDataBoolean ->
                hasData(hasDataBoolean)
            }
        }

        recyclerView = findViewById(R.id.file_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = fileAdapter
        fileAdapter.setData(FileHub.getSubFileList(currentFolder)) { hasDataBoolean ->
            hasData(hasDataBoolean)
        }

        fileAdapter.setOnClickListener { file, isFolder ->
            if (isFolder) {
                currentFolder = file
                tvTopCurrentPath.text = currentFolder?.absolutePath ?: ""
                fileAdapter.setData(FileHub.getSubFileList(currentFolder)) { hasDataBoolean ->
                    hasData(hasDataBoolean)
                }
            } else {
                DetailActivity.startActivity(this, file.absolutePath)
            }
        }
        fileAdapter.setOnLongClickListener { file, isFolder ->
            val message = if (isFolder) {
                "确定要删除文件夹吗？\n${file.absolutePath}"
            } else {
                "确定要删除文件吗？\n${file.absolutePath}"
            }
            showConfirmDialog("警告", message) {
                val isSuccess = FileHub.deleteFiles(file)
                val msg = if (isSuccess) "删除成功!" else "删除失败!"
                Util.showToast(this, msg)
                fileAdapter.setData(FileHub.getSubFileList(currentFolder)) { hasDataBoolean ->
                    hasData(hasDataBoolean)
                }
            }
        }
    }

    private fun hasData(hasListData: Boolean) {
        tvNoData.visibility = if (hasListData) View.GONE else View.VISIBLE
    }

    private fun showInputDialog(title: String, onConfirm: (String) -> Unit) {
        val view = LayoutInflater.from(this).inflate(R.layout.layout_dialog_input, null)
        val etInput = view.findViewById<EditText>(R.id.et_input)
        val alertDialog = AlertDialog.Builder(this).setTitle(title).setView(view).setNegativeButton(
            "取消"
        ) { dialog, which -> dialog?.dismiss() }.setPositiveButton(
            "确定"
        ) { dialog, which -> onConfirm(etInput.text.toString()) }
        alertDialog.show()
        KeyboardUtil.showKeyboard(view)
    }

    private fun showConfirmDialog(title: String, message: String, onConfirm: () -> Unit) {
        val alertDialog =
            AlertDialog.Builder(this).setTitle(title).setMessage(message).setNegativeButton(
                "取消"
            ) { dialog, which -> dialog?.dismiss() }.setPositiveButton(
                "确定"
            ) { dialog, which -> onConfirm() }
        alertDialog.show()
    }
}
