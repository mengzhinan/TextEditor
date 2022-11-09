package com.xxx.texteditor

import android.os.Environment
import java.io.*

/**
 * @Author: duke
 * @DateTime: 2022-11-09 10:58:13
 * @Description: 功能描述：
 *
 */
class FileAdapter {

    fun getSDCardDownloadFileList(): Array<File>? {
        val folder = Environment.DIRECTORY_DOWNLOADS
        val downloadFolder = Environment.getExternalStoragePublicDirectory(folder)
        return getSubFileList(downloadFolder)
    }

    fun getSubFileList(baseFolder: File?): Array<File>? {
        baseFolder ?: return null
        try {
            baseFolder.listFiles()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun createNewFolder(baseFolder: File?, childFolderName: String?): Boolean {
        baseFolder ?: return false
        val folderName = Util.trimAfter(childFolderName)
        if (childFolderName == null || Util.isNullOrEmpty(folderName)) {
            return false
        }
        val newFile = File(baseFolder, childFolderName)
        if (newFile.exists()) {
            return false
        }
        try {
            return newFile.mkdirs()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    fun createNewFile(baseFolder: File?, fileName: String?): Boolean {
        baseFolder ?: return false
        val fName = addFileSuffix(fileName)
        if (fName == null || Util.isNullOrEmpty(fName)) {
            return false
        }
        val newFile = File(baseFolder, fName)
        if (newFile.exists()) {
            return false
        }
        try {
            newFile.setExecutable(true)
            newFile.setReadable(true)
            newFile.setWritable(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            return newFile.createNewFile()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return false
    }

    private fun addFileSuffix(fileName: String?): String? {
        val fName = Util.trimAfter(fileName) ?: return null
        val splitList = fName.split("\\.")
        if (splitList.size - 1 < 0) {
            return "$fileName.txt"
        }
        return fileName
    }

    fun readFile(file: File?): String {
        if (file == null || !file.exists()) {
            return ""
        }
        if (!file.canRead()) {
            return ""
        }
        val sb = StringBuffer()
        var fis: FileInputStream? = null
        var isr: InputStreamReader? = null
        var br: BufferedReader? = null

        try {
            fis = FileInputStream(file)
            isr = InputStreamReader(fis)
            br = BufferedReader(isr)
            var line: String? = null
            do {
                line = br.readLine()
                // null 为空时，表示指针指向了文件末尾，没有内容了
                if (line != null) {
                    sb.append(line)
                } else {
                    break
                }
            } while (true)

            return sb.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeIO(br)
            closeIO(isr)
            closeIO(fis)
        }
        return ""
    }

    fun writeFile(file: File?, content: String?): Boolean {
        if (file == null || !file.exists()) {
            return false
        }
        if (!file.canWrite()) {
            return false
        }
        var fos: FileOutputStream? = null
        var osw: OutputStreamWriter? = null
        var bw: BufferedWriter? = null

        try {
            // 不追加内容，而是全部覆盖
            fos = FileOutputStream(file, false)
            osw = OutputStreamWriter(fos)
            bw = BufferedWriter(osw)
            bw.write(content)
            bw.flush()

            return true
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            closeIO(bw)
            closeIO(osw)
            closeIO(fos)
        }
        return false
    }

    private fun closeIO(c: Closeable?) {
        try {
            c?.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}