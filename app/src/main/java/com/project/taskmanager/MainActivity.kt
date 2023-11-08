package com.project.taskmanager


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
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {
    //-------------SQLite--------------------------
    lateinit var db: SQLiteDB
    lateinit var tableDB: SQLiteDatabase
    lateinit var cursor: Cursor
    //-----------ListView & FAB--------------------
    lateinit var listView: ListView
    lateinit var fab: FloatingActionButton
    //--------------Navigation Drawer----------------------------
    lateinit var navView: NavigationView
    lateinit var drawerLayout: DrawerLayout
    lateinit var toggle: ActionBarDrawerToggle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        listView = findViewById(R.id.listView)

        fab = findViewById(R.id.floatingActionButton3)
        supportActionBar?.setTitle("Local Favourite Finder")
        supportActionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FFE501")))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navView = findViewById(R.id.navView)
        drawerLayout = findViewById(R.id.drawerLayout)

        toggle = ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.callUs -> {
                    Toast.makeText(this, "Call Us at: +91-7001067632", Toast.LENGTH_LONG).show()
                }
                R.id.mailUs -> {
                    Toast.makeText(this, "Mail Us at: \n com.example.localfavouritefinder.com", Toast.LENGTH_LONG).show()
                }
                R.id.aboutUs -> {
                    Toast.makeText(this, "Yours truly", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }


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
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.clock -> startActivity(Intent(this, Geocoder::class.java))
        }
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return true
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean { // For clock icon

        menuInflater.inflate(R.menu.menu, menu)
        return true
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
}