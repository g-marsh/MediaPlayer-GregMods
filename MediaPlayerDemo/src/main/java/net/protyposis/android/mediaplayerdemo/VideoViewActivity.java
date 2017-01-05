/* * Copyright 2014 Mario Guggenberger <mg@protyposis.net> * * Licensed under the Apache License, Version 2.0 (the "License"); * you may not use this file except in compliance with the License. * You may obtain a copy of the License at * *     http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, software * distributed under the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. * See the License for the specific language governing permissions and * limitations under the License. */package net.protyposis.android.mediaplayerdemo;import android.app.Activity;import android.net.Uri;import android.os.Bundle;import android.util.Log;import android.view.Menu;import android.view.MenuItem;import android.view.MotionEvent;import android.view.View;import android.widget.ImageButton;import android.widget.MediaController;import android.widget.ProgressBar;import android.widget.TextView;import android.widget.Toast;import net.protyposis.android.mediaplayer.MediaPlayer;import net.protyposis.android.mediaplayer.MediaSource;import net.protyposis.android.mediaplayer.VideoView;import java.util.concurrent.TimeUnit;import static android.R.drawable.ic_media_pause;import static android.R.drawable.ic_media_play;public class VideoViewActivity extends Activity {    private static final String TAG = VideoViewActivity.class.getSimpleName();    private VideoView mVideoView;    // ProgressBar is the circle in the middle of screen during seeks - in xml set visibility to see    private ProgressBar mProgress;//    private MediaController.MediaPlayerControl mMediaPlayerControl;//    private MediaController mMediaController;    private Uri mVideoUri;    private int mVideoPosition;    private int mVideoStart;    private float mVideoPlaybackSpeed;    private boolean mVideoPlaying;    private MediaSource mMediaSource;    private ImageButton btnPlayPause;    private TextView textView;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_videoview);        Utils.setActionBarSubtitleEllipsizeMiddle(this);        mVideoView = (VideoView) findViewById(R.id.vv);        mProgress = (ProgressBar) findViewById(R.id.progress);        btnPlayPause = (ImageButton) findViewById(R.id.imageButton);        textView = (TextView) findViewById(R.id.textView);//        mMediaPlayerControl = mVideoView; //new MediaPlayerDummyControl();//        mMediaController = new MediaController(this);//        mMediaController.setAnchorView(findViewById(R.id.container));//        mMediaController.setMediaPlayer(mMediaPlayerControl);//        mMediaController.setEnabled(false);        mProgress.setVisibility(View.VISIBLE);        // Init video playback state (will eventually be overwritten by saved instance state)        mVideoUri = getIntent().getData();        //mVideoPosition = 0; ********** debug        mVideoPlaybackSpeed = 1;        mVideoPlaying = false;        // Play/Pause        btnPlayPause.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                if (mVideoView.isPlaying()){                    mVideoView.pause();                    btnPlayPause.setImageResource(ic_media_play);                    mVideoPosition = mVideoView.getCurrentPosition();                    textView.setText(getVideoPositionString(mVideoPosition,mVideoView.getDuration()));                }                else {                    mVideoView.start();                    btnPlayPause.setImageResource(ic_media_pause);                }            }        });        // Frame Back        findViewById(R.id.imageButton3).setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                mVideoView.pause();                if (mVideoPosition >= 34) {                    mVideoPosition -=34;                    mVideoView.seekTo(mVideoPosition);                    textView.setText(getVideoPositionString(mVideoPosition,mVideoView.getDuration()));                }                else {                    Toast.makeText(VideoViewActivity.this,                            "At start of video",                            Toast.LENGTH_LONG).show();                }            }        });        // Step Back        findViewById(R.id.imageButton5).setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                mVideoView.pause();                if (mVideoPosition >= 667) {                    mVideoPosition -=667;                    mVideoView.seekTo(mVideoPosition);                    textView.setText(getVideoPositionString(mVideoPosition,mVideoView.getDuration()));                }                else {                    Toast.makeText(VideoViewActivity.this,                            "At start of video",                            Toast.LENGTH_LONG).show();                }            }        });        // Frame Forward        findViewById(R.id.imageButton2).setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                mVideoView.pause();                if (mVideoPosition <= (mVideoView.getDuration()-34)) {                    mVideoPosition +=34;                    mVideoView.seekTo(mVideoPosition);                    textView.setText(getVideoPositionString(mVideoPosition,mVideoView.getDuration()));                }                else {                    Toast.makeText(VideoViewActivity.this,                            "At end of video",                            Toast.LENGTH_LONG).show();                }            }        });        // Step Forward        findViewById(R.id.imageButton4).setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                mVideoView.pause();                if (mVideoPosition <= (mVideoView.getDuration()-667-150)) {                    mVideoView.start();                    while(mVideoView.getCurrentPosition() < mVideoPosition+(667-15)){                        // wait while video plays to approximate position then pause and seek to exact                    }                    mVideoView.pause();                    mVideoPosition +=667;                    mVideoView.seekTo(mVideoPosition);                    textView.setText(getVideoPositionString(mVideoPosition,mVideoView.getDuration()));                }                else {                    Toast.makeText(VideoViewActivity.this,                            "Near end of video",                            Toast.LENGTH_LONG).show();                }            }        });        // Back to 0 or start        findViewById(R.id.imageButton6).setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                mVideoView.pause();                mVideoPosition = mVideoStart;                mVideoView.seekTo(mVideoPosition);                textView.setText(getVideoPositionString(mVideoPosition,mVideoView.getDuration()));            }        });        // Loop set point        findViewById(R.id.imageButton6).setOnLongClickListener(new View.OnLongClickListener() {            @Override            public boolean onLongClick(View v) {                mVideoView.pause();                mVideoStart = mVideoView.getCurrentPosition();                Toast.makeText(VideoViewActivity.this,                        "Loop point set",                        Toast.LENGTH_SHORT).show();                return false;            }        });    }    public String getMilliToString(int millis)    /**     * Convert a millisecond duration to a string stopwatch format     *     * @param millis A duration to convert to a string form     * @return A string of the form ss.SS, was M:ss.SS but running out of screen width in portrait     */    {//        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);        long hundreds = (Math.round(millis/10.0))%100;//        return(minutes+":"+String.format("%02d",seconds)+"."+String.format("%02d", hundreds));        return(String.format("%02d",seconds)+"."+String.format("%02d", hundreds));    }    public String getVideoPositionString(int currentTime, int endTime)    /**     * Convert a millisecond duration to a string videoplayer time format     *     * @param currentTime a duration to convert to a string form     * @param endTime a duration to convert to a string form and concatenate     * @return A string of the form ss.SS \n ss.SS     */    {//        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);        return(getMilliToString(currentTime)+ "\n"+ getMilliToString(endTime-currentTime));    }    @Override    protected void onRestoreInstanceState(Bundle savedInstanceState) {        super.onRestoreInstanceState(savedInstanceState);        mVideoUri = savedInstanceState.getParcelable("uri");        mVideoPosition = savedInstanceState.getInt("position");        mVideoStart = savedInstanceState.getInt("start");        mVideoPlaybackSpeed = savedInstanceState.getFloat("playbackSpeed");        mVideoPlaying = savedInstanceState.getBoolean("playing");        // Update on screen rotate        textView.setText(getMilliToString(mVideoPosition)+                "\n");    }    @Override    protected void onResume() {        super.onResume();        if(!mVideoView.isPlaying()) {            initPlayer();        }    }    private void initPlayer() {        getActionBar().setSubtitle(mVideoUri+"");        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {            @Override            public void onPrepared(MediaPlayer vp) {                mProgress.setVisibility(View.GONE);//                mMediaController.setEnabled(true);            }        });        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {            @Override            public boolean onError(MediaPlayer mp, int what, int extra) {                Toast.makeText(VideoViewActivity.this,                        "Cannot play the video, see logcat for the detailed exception",                        Toast.LENGTH_LONG).show();                mProgress.setVisibility(View.GONE);//                mMediaController.setEnabled(false);                return true;            }        });        mVideoView.setOnInfoListener(new MediaPlayer.OnInfoListener() {            @Override            public boolean onInfo(MediaPlayer mp, int what, int extra) {                String whatName = "";                switch (what) {                    case MediaPlayer.MEDIA_INFO_BUFFERING_END:                        whatName = "MEDIA_INFO_BUFFERING_END";                        break;                    case MediaPlayer.MEDIA_INFO_BUFFERING_START:                        whatName = "MEDIA_INFO_BUFFERING_START";                        break;                    case MediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:                        whatName = "MEDIA_INFO_VIDEO_RENDERING_START";                        break;                    case MediaPlayer.MEDIA_INFO_VIDEO_TRACK_LAGGING:                        whatName = "MEDIA_INFO_VIDEO_TRACK_LAGGING";                        break;                }                Log.d(TAG, "onInfo " + whatName);                return false;            }        });        mVideoView.setOnSeekListener(new MediaPlayer.OnSeekListener() {            @Override            public void onSeek(MediaPlayer mp) {                Log.d(TAG, "onSeek");                mProgress.setVisibility(View.VISIBLE);            }        });        mVideoView.setOnSeekCompleteListener(new MediaPlayer.OnSeekCompleteListener() {            @Override            public void onSeekComplete(MediaPlayer mp) {                Log.d(TAG, "onSeekComplete");                mProgress.setVisibility(View.GONE);            }        });        mVideoView.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {            @Override            public void onBufferingUpdate(MediaPlayer mp, int percent) {                Log.d(TAG, "onBufferingUpdate " + percent + "%");            }        });        Utils.MediaSourceAsyncCallbackHandler mMediaSourceAsyncCallbackHandler =                new Utils.MediaSourceAsyncCallbackHandler() {            @Override            public void onMediaSourceLoaded(MediaSource mediaSource) {                mMediaSource = mediaSource;                mVideoView.setVideoSource(mediaSource);                mVideoView.seekTo(mVideoPosition);                mVideoView.setPlaybackSpeed(mVideoPlaybackSpeed);                if (mVideoPlaying) {                    mVideoView.start();                }            }            @Override            public void onException(Exception e) {                Log.e(TAG, "error loading video", e);            }        };        if(mMediaSource == null) {            // Convert uri to media source asynchronously to avoid UI blocking            // It could take a while, e.g. if it's a DASH source and needs to be preprocessed            Utils.uriToMediaSourceAsync(this, mVideoUri, mMediaSourceAsyncCallbackHandler);        } else {            // Media source is already here, just use it            mMediaSourceAsyncCallbackHandler.onMediaSourceLoaded(mMediaSource);        }    }    @Override    public boolean onCreateOptionsMenu(Menu menu) {        // Inflate the menu; this adds items to the action bar if it is present.        getMenuInflater().inflate(R.menu.videoview, menu);        return true;    }    @Override    public boolean onOptionsItemSelected(MenuItem item) {        // Handle action bar item clicks here. The action bar will        // automatically handle clicks on the Home/Up button, so long        // as you specify a parent activity in AndroidManifest.xml.        int id = item.getItemId();        if (id == R.id.action_slowspeed) {            mVideoView.setPlaybackSpeed(0.2f);            return true;        } else if(id == R.id.action_halfspeed) {            mVideoView.setPlaybackSpeed(0.5f);            return true;        } else if(id == R.id.action_doublespeed) {            mVideoView.setPlaybackSpeed(2.0f);            return true;        } else if(id == R.id.action_quadspeed) {            mVideoView.setPlaybackSpeed(4.0f);            return true;        } else if(id == R.id.action_normalspeed) {            mVideoView.setPlaybackSpeed(1.0f);            return true;        } else if(id == R.id.action_seekcurrentposition) {            mVideoView.pause();            mVideoView.seekTo(mVideoView.getCurrentPosition());            return true;        } else if(id == R.id.action_seekcurrentpositionplus1ms) {            mVideoView.pause();            mVideoView.seekTo(mVideoView.getCurrentPosition()+1);            return true;        } else if(id == R.id.action_seektoend) {            mVideoView.pause();            mVideoView.seekTo(mVideoView.getDuration());            return true;        } else if(id == R.id.action_getcurrentposition) {            Toast.makeText(this, "current position: " + mVideoView.getCurrentPosition(), Toast.LENGTH_SHORT).show();            return true;        } else if(id == R.id.action_reload_source) {            initPlayer();        }        return super.onOptionsItemSelected(item);    }//    @Override//    public boolean onTouchEvent(MotionEvent event) {//        if (event.getAction() == MotionEvent.ACTION_UP) {//            if (mMediaController.isShowing()) {//                mMediaController.hide();//            } else {//                mMediaController.show();//            }//        }//        return super.onTouchEvent(event);//    }////    @Override//    protected void onStop() {//        mMediaController.hide();//        super.onStop();//    }    @Override    protected void onSaveInstanceState(Bundle outState) {        super.onSaveInstanceState(outState);        if (mVideoView != null) {            mVideoPosition = mVideoView.getCurrentPosition();            mVideoPlaybackSpeed = mVideoView.getPlaybackSpeed();            mVideoPlaying = mVideoView.isPlaying();            // the uri is stored in the base activity            outState.putParcelable("uri", mVideoUri);            outState.putInt("position", mVideoPosition);            outState.putInt("start", mVideoStart);            outState.putFloat("playbackSpeed", mVideoView.getPlaybackSpeed());            outState.putBoolean("playing", mVideoPlaying);        }    }}