package com.sywang

import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ads.AdsLoader
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.android.exoplayer2.util.Util.getUserAgent

class ViewActivity: AppCompatActivity() {

    var playerView: PlayerView? = null
    var player: SimpleExoPlayer? = null

    private var playWhenReady = true
    private var currentWindow = 0
    private var playbackPosition = 0L

    var uri = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val data = intent.getSerializableExtra("sy") as String

        playerView = findViewById(R.id.sy_player)
        uri = data

        initializePlayer(uri)
    }


    fun initializePlayer(filename:String?){
        if(player == null){
            player = ExoPlayerFactory.newSimpleInstance(this.getApplicationContext());
            playerView!!
                .setPlayer(player);
        }
        //var video_url:String = "{url}"
        var mediaSource: MediaSource = buildMediaSource(Uri.parse(filename))
        //준비
        player!!.prepare(mediaSource, true, false)
        //스타트, 스탑
        player!!.playWhenReady.and(playWhenReady)
    }

    fun buildMediaSource(uri: Uri) : MediaSource{
        var userAgent:String = Util.getUserAgent(this, "project_name")
        if(uri.getLastPathSegment()!!.contains("mp3") || uri.getLastPathSegment()!!.contains("mp4")){
            return ExtractorMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent)).createMediaSource(uri)
        }else if(uri.getLastPathSegment()!!.contains("m3u8")){
            return HlsMediaSource.Factory(DefaultHttpDataSourceFactory(userAgent)).createMediaSource(uri)
        }else{
            return ExtractorMediaSource.Factory(DefaultDataSourceFactory(this, userAgent)).createMediaSource(uri)
        }
    }
    fun releasePlayer(){
        if(player != null){
            playbackPosition = player!!.currentPosition
            currentWindow = player!!.currentWindowIndex
            playWhenReady = player!!.playWhenReady
            playerView!!.setPlayer(null)
            player!!.release()
            player = null
        }
    }
    override fun onStop() {
        super.onStop()
        releasePlayer()
    }


    fun logd(logtext: String) {
        Log.d("승윤", "wang $logtext")
    }
}