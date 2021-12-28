package com.example.spotifyintegration

import android.content.Context
import android.util.Log
import com.spotify.android.appremote.api.ConnectionParams
import com.spotify.android.appremote.api.Connector
import com.spotify.android.appremote.api.SpotifyAppRemote
import com.spotify.protocol.types.Track

object SpotifyService {
    private val clientId = "8c352ad77f27435fa6e7a613c38f4127"
    private val redirectUri = "com.example.spotifyintegration://callback"

    private var spotifyAppRemote: SpotifyAppRemote? = null
    private var connectionParams: ConnectionParams = ConnectionParams.Builder(clientId)
        .setRedirectUri(redirectUri)
        .showAuthView(true)
        .build()

    fun connect(context: Context, handler: (connected: Boolean) -> Unit) {
        if (spotifyAppRemote?.isConnected == true) {
            handler(true)
            return
        }
        val connectionListener = object : Connector.ConnectionListener {
            override fun onConnected(spotifyAppRemote: SpotifyAppRemote) {
                this@SpotifyService.spotifyAppRemote = spotifyAppRemote
                handler(true)
            }
            override fun onFailure(throwable: Throwable) {
                Log.e("MainActivity", throwable.message, throwable)

                handler(false)
            }
        }
        SpotifyAppRemote.connect(context, connectionParams, connectionListener)
    }

    fun resume() {
        spotifyAppRemote?.playerApi?.resume()
    }

    fun pause() {
        spotifyAppRemote?.playerApi?.pause()
    }

    fun skip(){
        spotifyAppRemote?.playerApi?.skipNext()
    }

    fun skipPrevious(){
        spotifyAppRemote?.playerApi?.skipPrevious()
    }

    fun subscribeToPlayerState(handler: (track: Track, isPaused: Boolean) -> Unit){
        spotifyAppRemote?.let {
            // Subscribe to PlayerState
            it.playerApi.subscribeToPlayerState().setEventCallback {
                val track: Track = it.track
                Log.d("MainActivity", track.name + " by " + track.artist.name)
                handler(track, it.isPaused)
            }
        }

        //playPlaylist()
    }

    fun playPlaylist(){
        spotifyAppRemote?.let {
            // Play a playlist
            val playlistURI = "spotify:playlist:2JMsJkDddVnwgL45P5BXwp"
            it.playerApi.play(playlistURI)
        }
    }

    fun playAlbum(uri: String){
        Log.d("MainActivity", "play album")

        spotifyAppRemote?.let {
            // Play a playlist
            //val playlistURI = "spotify:playlist:" +uri
            val playlistURI = "spotify:album:$uri"

            it.playerApi.play(playlistURI)
        }
    }



    fun stop(){
        spotifyAppRemote?.let {
            SpotifyAppRemote.disconnect(it)
        }
    }
}