package com.example.musicplayer
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var runnable: Runnable
    private var handler: Handler = Handler()
    private var pause: Boolean = false
    private lateinit  var seekBar: SeekBar
    private lateinit var textViewPass:TextView
    private lateinit var textViewDue:TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //Start the media player
        val playBtn = findViewById<FloatingActionButton>(R.id.fabPlay)
        val pauseBtn = findViewById<FloatingActionButton>(R.id.fabPause)
        val stopBtn = findViewById<FloatingActionButton>(R.id.fabStop)
        seekBar = findViewById(R.id.seekBar)
        textViewPass = findViewById<TextView>(R.id.tv_pass)
        textViewDue = findViewById<TextView>(R.id.tv_due)

        //Play Music
        playBtn.setOnClickListener(){
            if(pause){
                mediaPlayer.seekTo(mediaPlayer.currentPosition)
                mediaPlayer.start()
                pause=false
                Toast.makeText(this,"Media Playing..",Toast.LENGTH_SHORT).show()
            }
            else{
                mediaPlayer=MediaPlayer.create(applicationContext,R.raw.rock_me)
                mediaPlayer.start()
                Toast.makeText(this,"Media is playing",R.raw.rock_me).show()
            }
            initialiseSeekBar()

            playBtn.isEnabled=false
            pauseBtn.isEnabled=true
            stopBtn.isEnabled=true

            mediaPlayer.setOnCompletionListener(){
                playBtn.isEnabled=true
                pauseBtn.isEnabled=false
                stopBtn.isEnabled=false

                Toast.makeText(this,"End",Toast.LENGTH_SHORT).show()
            }
        }
        //Pause the Music

        pauseBtn.setOnClickListener(){
            if (mediaPlayer.isPlaying){

                mediaPlayer.pause()

                pause=true

                playBtn.isEnabled=true
                pauseBtn.isEnabled=false
                stopBtn.isEnabled=true

                Toast.makeText(this,"Music Paused",Toast.LENGTH_LONG).show()
            }
        }

        //Stop Music
        stopBtn.setOnClickListener(){
            if (mediaPlayer.isPlaying||pause.equals(false)){
                pause=false
                seekBar.setProgress(0)
                mediaPlayer.stop()
                mediaPlayer.reset()
                mediaPlayer.release()

                handler.removeCallbacks(runnable)

                playBtn.isEnabled=true
                pauseBtn.isEnabled=false
                stopBtn.isEnabled=false

                textViewPass.text = ""
                textViewDue.text=""

                Toast.makeText(this,"Media Stop",Toast.LENGTH_SHORT).show()
            }
        }

        //Seek Bar Listener
        seekBar.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar,i:Int,b:Boolean){
                if (b){
                    mediaPlayer.seekTo(i*1000)
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar){

            }
            override fun onStopTrackingTouch(seekBar: SeekBar){

            }

        })

    }

    private fun initialiseSeekBar() {
        seekBar.max = mediaPlayer.currentSeconds
        runnable= Runnable {
            seekBar.progress=mediaPlayer.currentSeconds

            textViewPass.text="${mediaPlayer.currentSeconds} sec"
            val difference = mediaPlayer.seconds-mediaPlayer.currentSeconds
            textViewDue.text="$difference sec"

            handler.postDelayed(runnable,1000)
        }
        handler.postDelayed(runnable,1000)
    }


    //Creating an extension method to get the duration

    val MediaPlayer.seconds:Int get() {
        return this.duration/1000
    }

    val MediaPlayer.currentSeconds:Int get() {
        return this.currentPosition/1000
    }

}

