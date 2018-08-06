package com.corrado.offersapp

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONArray
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    var TAG = this.callingPackage

    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        viewManager = GridLayoutManager(this, 2)
        recyclerView = findViewById<RecyclerView>(R.id.recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
        }

        fab.setOnClickListener { view ->
            try {
                val inputStream: InputStream = assets.open("Offers.json")
                val inputString = inputStream.bufferedReader().use{ it.readText() }
                val gson = Gson()
                val jsonArray = JSONArray(inputString)
                val offers = ArrayList<OfferData>()

                for (i in 0..(jsonArray.length() - 1)) {
                    val item = jsonArray.getJSONObject(i)
                    val offerData = gson.fromJson(item.toString(), OfferData::class.java)
                    Log.d(TAG,offerData.toString())
                    offers.add(offerData)
                }

                viewAdapter = OffersAdapter(offers)
                recyclerView.adapter = viewAdapter
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
