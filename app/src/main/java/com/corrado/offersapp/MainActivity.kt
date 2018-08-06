package com.corrado.offersapp

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken

import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    var TAG = this.callingPackage

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        fab.setOnClickListener { view ->
            try {
                val inputStream: InputStream = assets.open("Offers.json")
                val inputString = inputStream.bufferedReader().use{ it.readText() }
                val gson = Gson()
                val jsonArray = JSONArray(inputString)
                for (i in 0..(jsonArray.length() - 1)) {
                    val item = jsonArray.getJSONObject(i)
                    val offerData = gson.fromJson(item.toString(), OfferData::class.java)
                    Log.d(TAG,offerData.toString())
                }
                Log.d(TAG,inputString)
            } catch (e:Exception){
                Log.d(TAG, e.message)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
