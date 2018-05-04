package com.example.anweshmishra.kotlinfootballpitchview

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.footballpitchview.FootballPitchView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FootballPitchView.create(this)
    }
}
