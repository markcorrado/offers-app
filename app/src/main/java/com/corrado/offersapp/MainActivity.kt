package com.corrado.offersapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
    MainActivity just contains a NavHostFragment to swap out the Fragments
    Refer to Navigation Architecture Component documentation
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}
