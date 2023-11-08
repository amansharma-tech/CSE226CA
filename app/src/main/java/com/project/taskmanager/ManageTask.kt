package com.project.taskmanager

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.widget.AppCompatButton
import java.util.*

class ManageTask : AppCompatActivity() {
    lateinit var db: SQLiteDB
    lateinit var tableDB: SQLiteDatabase
    lateinit var cursor: Cursor
    lateinit var contentValue: ContentValues
    var note_id: Long = 0
    lateinit var txtTitle: EditText
    lateinit var txtDesc: EditText
    lateinit var saveButton: AppCompatButton
    lateinit var deleteButton: AppCompatButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manage_task)
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFE501")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setTitle("Local Favourite Finder")
        note_id = intent.getLongExtra("note_id", -1)
        txtTitle = findViewById(R.id.txtTitle)
        txtDesc = findViewById(R.id.txtDesc)
        db = SQLiteDB(this)
        tableDB = db.writableDatabase
        contentValue = ContentValues()

        if(note_id > 0) {
            tableDB = db.readableDatabase
            cursor = tableDB.query("notes", arrayOf("title", "description"), "_id=?", arrayOf(note_id.toString()), null, null, null)
            if(cursor.moveToFirst()) {
                txtTitle.setText(cursor.getString(0))
                txtDesc.setText(cursor.getString(1))
            }
        }

        saveButton = findViewById(R.id.saveButton)
        deleteButton = findViewById(R.id.deleteButton)

        saveButton.setOnClickListener { saveNote() }
        deleteButton.setOnClickListener { deleteNote() }

    }

    private fun deleteNote() {
        tableDB.delete("notes", "_id=?", arrayOf(note_id.toString()))
        Toast.makeText(this, "Deletion Successful", Toast.LENGTH_SHORT).show()
        startActivity(Intent(this, MainActivity::class.java))
    }

    private fun saveNote() {
        if(note_id.toInt() == -1) { // New Save
            contentValue.put("title", txtTitle.text.toString())
            contentValue.put("description", txtDesc.text.toString())
            tableDB.insert("notes", null, contentValue)
            startActivity(Intent(this, MainActivity::class.java))
            Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
        }
        if(note_id > 0) { // Update
            contentValue.put("title", txtTitle.text.toString())
            contentValue.put("description", txtDesc.text.toString())
            tableDB.update("notes", contentValue, "_id=?", arrayOf(note_id.toString()))
            startActivity(Intent(this, MainActivity::class.java))
            Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
            contentValue.clear()
        }
    }
}