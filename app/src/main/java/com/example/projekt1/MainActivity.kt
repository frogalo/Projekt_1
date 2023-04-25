package com.example.projekt1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val listFragment = ListFragment()
        supportFragmentManager.beginTransaction()
            .add(R.id.container, listFragment, listFragment.javaClass.name)
            .commit()
    }
}