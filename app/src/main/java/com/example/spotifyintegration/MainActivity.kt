package com.example.spotifyintegration

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout

import com.spotify.protocol.types.Track;

class MainActivity : AppCompatActivity() {
    private lateinit var button_random: Button
    private lateinit var button_play: ImageButton
    private lateinit var button_next: ImageButton
    private lateinit var button_previous: ImageButton
    private lateinit var tv_playState: TextView
    private lateinit var tv_song: TextView
    private lateinit var tv_artist: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        tv_playState = findViewById(R.id.tv_playState)
        tv_song = findViewById(R.id.tv_song_name)
        tv_artist = findViewById(R.id.tv_artist_name)
        setOnClickListeners()

    }

    private fun setOnClickListeners(){
        button_play = findViewById(R.id.button_play)
        handleStopped()


        button_next = findViewById(R.id.button_next)
        button_next.setOnClickListener{
            SpotifyService.skip()

        }

        button_previous = findViewById(R.id.button_previous)
        button_previous.setOnClickListener{
            SpotifyService.skipPrevious()

        }
        findViewById<Button>(R.id.button_reconnect).setOnClickListener{connect()}
        findViewById<Button>(R.id.button_random).setOnClickListener{playRandom()}

        findViewById<Button>(R.id.button_donda).setOnClickListener{playAlbum(Album.DONDA)}
        findViewById<Button>(R.id.button_jik).setOnClickListener{playAlbum(Album.JIK)}
        findViewById<Button>(R.id.button_ksg).setOnClickListener{playAlbum(Album.KSG)}
        findViewById<Button>(R.id.button_ye).setOnClickListener{playAlbum(Album.YE)}
        findViewById<Button>(R.id.button_tlop).setOnClickListener{playAlbum(Album.TLOP)}
        findViewById<Button>(R.id.button_yeezus).setOnClickListener{playAlbum(Album.YEEZUS)}
        findViewById<Button>(R.id.button_throne).setOnClickListener{playAlbum(Album.THRONE)}
        findViewById<Button>(R.id.button_mbdtf).setOnClickListener{playAlbum(Album.MBDTF)}
        findViewById<Button>(R.id.button_808).setOnClickListener{playAlbum(Album.EIGHT_O_EIGHT)}
        findViewById<Button>(R.id.button_graduation).setOnClickListener{playAlbum(Album.GRADUATION)}
        findViewById<Button>(R.id.button_late_registration).setOnClickListener{playAlbum(Album.LATE_REGISTRATION)}
        findViewById<Button>(R.id.button_college_dropout).setOnClickListener{playAlbum(Album.COLLEGE_DROPOUT)}
    }

    override fun onStart() {
        super.onStart()
        // We will start writing our code here.
        connect()
    }

    private fun connect() {
        SpotifyService.connect(this) {
            connected(it)
        }
    }

    private fun connected(connected: Boolean) {
        Log.d("MainActivity ", "connected");
        if(connected){
            findViewById<ConstraintLayout>(R.id.mainLayout).visibility = View.VISIBLE
            findViewById<ConstraintLayout>(R.id.errorLayout).visibility = View.GONE

            SpotifyService.setNoShuffle()
            subscribePlayerState()
        }else{
            //show reconnect screen
            findViewById<ConstraintLayout>(R.id.mainLayout).visibility = View.GONE
            findViewById<ConstraintLayout>(R.id.errorLayout).visibility = View.VISIBLE
        }
    }

    private fun subscribePlayerState(){
        SpotifyService.subscribeToPlayerState { track: Track, isPaused: Boolean ->
            tv_song.text = track.name
            tv_artist.text = track.artist.name
            handleState(isPaused)
        }
    }


    private fun handleState(isPaused: Boolean): Unit{
        if(isPaused){
            handlePaused();
        }else{
            handlePlaying();
        }
    }


    private fun handleStopped(){
        tv_playState.text = "Stopped"
        button_play.background = getDrawable(R.drawable.play)
        button_play.setOnClickListener{
            SpotifyService.playPlaylist();
        }
    }

    private fun handlePaused(){
        tv_playState.text = "Paused"
        button_play.background = getDrawable(R.drawable.play)
        button_play.setOnClickListener{
            SpotifyService.resume()
        }

    }

    private fun handlePlaying(){
        tv_playState.text = "Now Playing"
        button_play.background = getDrawable(R.drawable.pause)
        button_play.setOnClickListener{
            SpotifyService.pause();
        }

    }



    private fun playAlbum(album: Album){
        var uri = "";
        when(album){
            Album.DONDA -> uri = "2Wiyo7LzdeBCsVZiRA6vVZ"
            Album.JIK -> uri = "0FgZKfoU2Br5sHOfvZKTI9"
            Album.KSG -> uri = "6pwuKxMUkNg673KETsXPUV"
            Album.YE -> uri = "2Ek1q2haOnxVqhvVKqMvJe"
            Album.TLOP -> uri = "7gsWAHLeT0w7es6FofOXk1"
            Album.YEEZUS -> uri = "7D2NdGvBHIavgLhmcwhluK"
            Album.THRONE -> uri = "2P2Xwvh2xWXIZ1OWY9S9o5"
            Album.MBDTF -> uri = "20r762YmB5HeofjMCiPMLv"
            Album.EIGHT_O_EIGHT -> uri = "3WFTGIO6E3Xh4paEOBY9OU"
            Album.GRADUATION -> uri = "5fPglEDz9YEwRgbLRvhCZy"
            Album.LATE_REGISTRATION -> uri = "5ll74bqtkcXlKE7wwkMq4g"
            Album.COLLEGE_DROPOUT -> uri = "4Uv86qWpGTxf7fU7lG5X6F"
        }

        if(!uri.equals("")){
            //play
            SpotifyService.playAlbum(uri)
        }
    }

    private fun playRandom() {
        val randonList = Album.values().toList().shuffled()

        val randomAlbum = randonList.first()
        playAlbum(randomAlbum)
    }

    override fun onStop() {
        super.onStop()
        SpotifyService.stop()
    }
}