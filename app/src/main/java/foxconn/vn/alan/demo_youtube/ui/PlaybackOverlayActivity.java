package foxconn.vn.alan.demo_youtube.ui;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.VideoView;

import foxconn.vn.alan.demo_youtube.Model.Movie;
import foxconn.vn.alan.demo_youtube.R;
import foxconn.vn.alan.demo_youtube.common.Utils;

public class PlaybackOverlayActivity extends Activity {
    private static final String TAG = PlaybackOverlayActivity.class.getSimpleName();

    private VideoView mVideoView;

    private LeanbackPlaybackState mPlaybackState = LeanbackPlaybackState.IDLE;

    private int mPosition = 0;
    private long mStartTimeMillis;
    private long mDuration = -1;

    /*
     * List of various states that we can be in
     */
    public enum LeanbackPlaybackState {
        PLAYING, PAUSED, IDLE
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback_overlay);

        loadview();
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "onDestroy");
        super.onDestroy();
        stopPlayback();
        mVideoView.suspend();
        mVideoView.setVideoURI(null);
    }

    private void stopPlayback() {
        if (mVideoView != null) {
            mVideoView.stopPlayback();
        }
    }

    private void loadview(){
        mVideoView = (VideoView) findViewById(R.id.videoView);
        mVideoView.setFocusable(false);
        mVideoView.setFocusableInTouchMode(false);

        Movie movie = (Movie) getIntent().getSerializableExtra("video");
        setVideoPath(movie.getVideoURL());
    }

    public void setVideoPath(String videoUrl) {
        setPosition(0);
        mVideoView.setVideoPath(videoUrl);
        mStartTimeMillis = 0;
        mDuration = Utils.getDuration(videoUrl);
    }

    private void setPosition(int position) {
        if (position > mDuration) {
            mPosition = (int) mDuration;
        } else if (position < 0) {
            mPosition = 0;
            mStartTimeMillis = System.currentTimeMillis();
        } else {
            mPosition = position;
        }
        mStartTimeMillis = System.currentTimeMillis();
        Log.d(TAG, "position set to " + mPosition);
    }

    public int getPosition() {
        return mPosition;
    }

    public void setPlaybackState(LeanbackPlaybackState playbackState) {
        this.mPlaybackState = playbackState;
    }

    public void playPause(boolean doPlay) {
        if (mPlaybackState == LeanbackPlaybackState.IDLE) {
            /* Callbacks for mVideoView */
            setupCallbacks();
        }

        if (doPlay && mPlaybackState != LeanbackPlaybackState.PLAYING) {
            mPlaybackState = LeanbackPlaybackState.PLAYING;
            if (mPosition > 0) {
                mVideoView.seekTo(mPosition);
            }
            mVideoView.start();
            mStartTimeMillis = System.currentTimeMillis();
        } else {
            mPlaybackState = LeanbackPlaybackState.PAUSED;
            int timeElapsedSinceStart = (int) (System.currentTimeMillis() - mStartTimeMillis);
            setPosition(mPosition + timeElapsedSinceStart);
            mVideoView.pause();
        }
    }

    private void setupCallbacks() {

        mVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {

            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                mVideoView.stopPlayback();
                mPlaybackState = LeanbackPlaybackState.IDLE;
                return false;
            }
        });

        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                if (mPlaybackState == LeanbackPlaybackState.PLAYING) {
                    mVideoView.start();
                }
            }
        });

        mVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mPlaybackState = LeanbackPlaybackState.IDLE;
            }
        });
    }

}
