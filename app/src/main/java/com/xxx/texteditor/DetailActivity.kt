package com.xxx.texteditor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class DetailActivity : AppCompatActivity() {

    companion object {
        private const val PARAM_FILE_PATH = "param_file_path"
        fun startActivity(context: Context, filePath: String) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(PARAM_FILE_PATH, filePath)
            context.startActivity(intent)
        }
    }

    private lateinit var btnSaveExit: Button
    private lateinit var btnExit: Button
    private lateinit var etContent: EditText

    private var filePath: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        filePath = intent?.getStringExtra(PARAM_FILE_PATH)
        filePath = Util.trimAfter(filePath)
        if (filePath == null || Util.isNullOrEmpty(filePath)) {
            finish()
            return
        }

        btnSaveExit = findViewById(R.id.btn_save_exit)
        btnSaveExit.setOnClickListener {
            if (filePath != null) {
                FileHub.writeFile(File(filePath), etContent.text.toString())
                Util.showToast(this, "保存成功")
            }
            btnSaveExit.postDelayed({
                finish()
            }, 50)
        }
        btnExit = findViewById(R.id.btn_exit)
        btnExit.setOnClickListener {
            finish()
        }
        etContent = findViewById(R.id.et_content)

        if (filePath != null) {
            etContent.setText(FileHub.readFile(File(filePath)))
        }
    }
}