package com.xxx.texteditor

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    companion object {
        private const val PARAM_FILE_PATH = "param_file_path"
        fun startActivity(context: Context, filePath: String) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(PARAM_FILE_PATH, filePath)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }
}