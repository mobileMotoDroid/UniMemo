package com.mobilemotodroid.unimemo

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var editTextMemo: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonLoad: Button
    private lateinit var buttonDelete: Button // 削除ボタン

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextMemo = findViewById(R.id.editTextMemo)
        buttonSave = findViewById(R.id.buttonSave)
        buttonLoad = findViewById(R.id.buttonLoad)
        buttonDelete = findViewById(R.id.buttonDelete)

        // メモを保存する
        buttonSave.setOnClickListener {
            val memo = editTextMemo.text.toString()
            if (memo.isNotEmpty()) {
                saveMemo(memo)
            } else {
                Toast.makeText(this, "Please enter some text", Toast.LENGTH_SHORT).show()
            }
        }

        // メモを読み込む
        buttonLoad.setOnClickListener {
            val memo = loadMemo()
            if (memo != null) {
                editTextMemo.setText(memo)
                Toast.makeText(this, "Memo loaded!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "No saved memo found", Toast.LENGTH_SHORT).show()
            }
        }

        // メモを削除する
        buttonDelete.setOnClickListener {
            val deleted = deleteMemo()
            if (deleted) {
                Toast.makeText(this, "Memo deleted!", Toast.LENGTH_SHORT).show()
                editTextMemo.text.clear() // 削除後に入力フィールドをクリア
            } else {
                Toast.makeText(this, "No memo to delete", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // メモを保存する
    private fun saveMemo(memo: String) {
        try {
            val file = File(filesDir, "memo.txt")
            FileOutputStream(file).use { fos ->
                fos.write(memo.toByteArray())
            }
            Toast.makeText(this, "Memo saved!", Toast.LENGTH_SHORT).show()
            editTextMemo.text.clear() // 保存後に入力フィールドをクリア
        } catch (e: IOException) {
            Toast.makeText(this, "Failed to save memo", Toast.LENGTH_SHORT).show()
        }
    }

    // メモを読み込む
    private fun loadMemo(): String? {
        return try {
            val file = File(filesDir, "memo.txt")
            if (file.exists()) {
                FileInputStream(file).use { fis ->
                    fis.readBytes().toString(Charsets.UTF_8)
                }
            } else {
                null // ファイルが存在しない場合
            }
        } catch (e: IOException) {
            null // 読み込み失敗時はnullを返す
        }
    }

    // メモを削除する
    private fun deleteMemo(): Boolean {
        val file = File(filesDir, "memo.txt")
        return if (file.exists()) {
            file.delete()
        } else {
            false
        }
    }
}