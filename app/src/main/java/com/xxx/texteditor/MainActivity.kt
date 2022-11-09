package com.xxx.texteditor

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class MainActivity : AppCompatActivity() {

    private lateinit var createFolderBtn: Button
    private lateinit var createFileBtn: Button
    private lateinit var backBtn: Button

    private lateinit var recyclerView: RecyclerView
    private val fileAdapter: FileAdapter = FileAdapter()

    private val rootFolder: File = FileCenter.getSDCardDownloadFolder()
    private var currentFolder: File? = rootFolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        createFolderBtn = findViewById(R.id.btn_create_folder)
        createFolderBtn.setOnClickListener {
            showInputDialog("创建文件夹") {
                FileCenter.createNewFolder(currentFolder, it)
                fileAdapter.setData(FileCenter.getSubFileList(currentFolder))
            }
        }
        createFileBtn = findViewById(R.id.btn_create_file)
        createFileBtn.setOnClickListener {
            showInputDialog("创建文件") {
                FileCenter.createNewFile(currentFolder, it)
                fileAdapter.setData(FileCenter.getSubFileList(currentFolder))
            }
        }
        backBtn = findViewById(R.id.btn_back)
        backBtn.setOnClickListener {
            if ((currentFolder?.absolutePath ?: "") == (rootFolder.absolutePath ?: "")) {
                showToast("已经是根目录了")
                return@setOnClickListener
            }
            currentFolder = currentFolder?.parentFile
        }

        recyclerView = findViewById(R.id.file_recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = fileAdapter

        fileAdapter.setData(FileCenter.getSubFileList(currentFolder))
    }

    private fun showToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
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
    }
}
