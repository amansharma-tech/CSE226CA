package com.project.taskmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ListView
import android.widget.SimpleCursorAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    //-------------SQLite--------------------------
    lateinit var db: SQLiteDB
    lateinit var tableDB: SQLiteDatabase
    lateinit var cursor: Cursor
    //-----------ListView & FAB--------------------
    lateinit var listView: ListView
    lateinit var fab: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listView)

        fab = findViewById(R.id.floatingActionButton3)
        supportActionBar?.setTitle("My Tasks")
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#000080")))


        fab.setOnClickListener {
            var intent = Intent(this, ManageTask::class.java)
            intent.putExtra("note_id", -1)
            startActivity(intent)
        }
        listView.setOnItemClickListener { parent, view, position, id ->
            var intent = Intent(this, ManageTask::class.java)
            intent.putExtra("note_id", id)
            startActivity(intent)
        }

    }



    override fun onStart() {
        super.onStart()
        db = SQLiteDB(this)
        tableDB = db.writableDatabase
        cursor = tableDB.rawQuery("select * from notes order by _id DESC", null)
        var customListAdapter = SimpleCursorAdapter(
            this,
            R.layout.list_row,
            cursor,
            arrayOf("title", "description"),
            intArrayOf(R.id.idTitle),
            0
        )
        listView.adapter = customListAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        tableDB.close()
        cursor.close()
    }



    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.clock -> startActivity(Intent(this, AlarmNotify::class.java))
        }

        return true
    }
}