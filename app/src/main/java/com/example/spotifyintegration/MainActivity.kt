package com.example.spotifyintegration

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;

import com.spotify.protocol.client.Subscription;
import com.spotify.protocol.types.PlayerState;
import com.spotify.protocol.types.Track;

class MainActivity : AppCompatActivity() {
    private lateinit var button_connect: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        button_connect = findViewById(R.id.button_connect)
        button_connect.setOnClickListener {
            SpotifyService.connect(this) {
                connected(it)
//                val intent = Intent(this, PlayerActivity::class.java)
//                startActivity(intent)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        // We will start writing our code here.
    }

    private fun connected(connected: Boolean) {
        Log.d("Connected ", connected.toString());

    }

    override fun onStop() {
        super.onStop()
        // Aaand we will finish off here.
    }
}