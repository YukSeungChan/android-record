package org.sunrin.activity;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;
import org.sunrin.R;
import org.sunrin.manager.RecordManager;

import java.io.File;


public class MainActivity extends Activity implements View.OnClickListener, View.OnTouchListener
{
    private final int MAX_RECORD_TIME = 5000;
    private int currentRecordTimeMs = 0;
    private boolean isPlayed = false;

    private RecordManager recordManager = null;
    private RecordAsyncTask recordAsyncTask = null;
    private MediaPlayer mediaPlayer = null;
    private Uri uri = null;

    private ImageView recordTextImageView, recordAllowImageView;
    private ImageButton recordImageButton, playImageButton;
    private SeekBar recordProgressBar;

    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize()
    {
        recordTextImageView = (ImageView)findViewById(R.id.iv_record_text);
        recordAllowImageView = (ImageView)findViewById(R.id.iv_arrow);
        recordImageButton = (ImageButton)findViewById(R.id.ib_record);
        playImageButton = (ImageButton)findViewById(R.id.ib_play);
        recordProgressBar = (SeekBar)findViewById(R.id.seekBar);
        recordProgressBar.setMax(MAX_RECORD_TIME);
        playImageButton.setOnClickListener(this);
        recordImageButton.setOnTouchListener(this);
    }


    private void playHandler()
    {
        if(isPlayed)
        {
            pause();
        }
        else
        {
            uri = Uri.fromFile(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "loup", "record.amr"));
            mediaPlayer = MediaPlayer.create(this, uri);
            if(mediaPlayer != null)
            {
                isPlayed = true;
                mediaPlayer.setOnCompletionListener(mediaPlayerOnCompletionListener);
                mediaPlayer.start();
                playImageButton.setImageResource(R.drawable.btn_pause);
            }
            else
            {
                Toast.makeText(this, "RecordFile doesn't exist.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void pause()
    {
        isPlayed = false;
        mediaPlayer.reset();
        playImageButton.setImageResource(R.drawable.btn_play);
    }

    private MediaPlayer.OnCompletionListener mediaPlayerOnCompletionListener = new MediaPlayer.OnCompletionListener()
    {
        @Override
        public void onCompletion(MediaPlayer mp)
        {
            pause();
        }
    };

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ib_play:
            {
                playHandler();
                break;
            }
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event)
    {
        if(event.getAction() == MotionEvent.ACTION_DOWN)
        {
            recordTextImageView.setVisibility(View.GONE);
            recordAllowImageView.setVisibility(View.GONE);
            recordProgressBar.setVisibility(View.VISIBLE);
            recordProgressBar.setProgress(0);
            currentRecordTimeMs = 0;
            recordAsyncTask = new RecordAsyncTask();
            recordAsyncTask.execute();

        }
        if(event.getAction() == MotionEvent.ACTION_UP)
        {
            recordTextImageView.setVisibility(View.VISIBLE);
            recordAllowImageView.setVisibility(View.VISIBLE);
            recordProgressBar.setVisibility(View.GONE);
            if(recordAsyncTask != null) recordAsyncTask.cancel(true);
            Toast.makeText(this, "Record Success!", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    public class RecordAsyncTask extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params)
        {
            recordManager = new RecordManager();
            recordManager.start();
            while(true)
            {
                if(!recordManager.isRecorded() || currentRecordTimeMs > MAX_RECORD_TIME)
                {
                    recordManager.stop();
                    return null;
                }
                try
                {
                    currentRecordTimeMs += 100;
                    publishProgress(currentRecordTimeMs);
                    Thread.sleep(100);
                }
                catch(InterruptedException e)
                {
                    recordManager.stop();
                    return null;
                }
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress)
        {
            recordProgressBar.setProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
            if(recordManager != null) recordManager.stop();
        }
    }
}