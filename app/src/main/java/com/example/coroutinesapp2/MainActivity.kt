package com.example.coroutinesapp2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.coroutinesapp2.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception
import java.net.URL

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.myButton.setOnClickListener {
            requestAPI()
        }

    }

    private fun requestAPI() {
        CoroutineScope(IO).launch {
            val data = async{ fetchData() }.await()
            if (data.isNotEmpty()) {
                populateRV(data)
            } else {
                Toast.makeText(this@MainActivity, "Noy found data", Toast.LENGTH_LONG)
            }
        }
    }

    private fun fetchData(): String {
        var response = ""
        try {
            response = URL("https://api.adviceslip.com/advice").readText()
        } catch (ex: Exception) {
            Toast.makeText(this, "Error: ${ex.message}", Toast.LENGTH_LONG).show()
        }
        return response
    }

    private suspend fun populateRV(result: String) {
        val jsonObjects = JSONObject(result)
        val slip = jsonObjects.getJSONObject("slip")
        val advice = slip.getString("advice")

        runOnUiThread {
            binding.textStatement.text= advice
        }
    }


}