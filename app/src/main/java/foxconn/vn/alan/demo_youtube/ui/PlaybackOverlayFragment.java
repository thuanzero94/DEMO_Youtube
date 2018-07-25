package foxconn.vn.alan.demo_youtube.ui;

import android.app.Activity;
import android.content.Context;
import android.media.session.PlaybackState;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v17.leanback.widget.Action;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ClassPresenterSelector;
import android.support.v17.leanback.widget.ControlButtonPresenterSelector;
import android.support.v17.leanback.widget.HeaderItem;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnActionClickedListener;
import android.support.v17.leanback.widget.PlaybackControlsRow;
import android.support.v17.leanback.widget.PlaybackControlsRowPresenter;
import android.util.Log;

import java.util.ArrayList;

import foxconn.vn.alan.demo_youtube.Model.Movie;
import foxconn.vn.alan.demo_youtube.R;
import foxconn.vn.alan.demo_youtube.common.Utils;
import foxconn.vn.alan.demo_youtube.ui.Presenter.CardPresenter;
import foxconn.vn.alan.demo_youtube.ui.Presenter.DetailsDescriptionPresenter;


public class PlaybackOverlayFragment extends android.support.v17.leanback.app.PlaybackOverlayFragment {

    private static final String TAG = PlaybackOverlayFragment.class.getSimpleName();

    private static final int SIMULATED_BUFFERED_TIME = 10000;
    private static final int DEFAULT_UPDATE_PERIOD = 1000;
    private static final int UPDATE_PERIOD = 16;

    private Movie mSelectedMovie;
    private PlaybackControlsRow mPlaybackControlsRow;
    private ArrayObjectAdapter mPrimaryActionsAdapter;
    private ArrayObjectAdapter mSecondaryActionsAdapter;

    private PlaybackControlsRow.PlayPauseAction mPlayPauseAction;
    private PlaybackControlsRow.RepeatAction mRepeatAction;
    private PlaybackControlsRow.ThumbsUpAction mThumbsUpAction;
    private PlaybackControlsRow.ThumbsDownAction mThumbsDownAction;
    private PlaybackControlsRow.ShuffleAction mShuffleAction;
    private PlaybackControlsRow.SkipNextAction mSkipNextAction;
    private PlaybackControlsRow.SkipPreviousAction mSkipPreviousAction;
    private PlaybackControlsRow.FastForwardAction mFastForwardAction;
    private PlaybackControlsRow.RewindAction mRewindAction;
    private PlaybackControlsRow.HighQualityAction mHighQualityAction;
    private PlaybackControlsRow.ClosedCaptioningAction mClosedCaptioningAction;
    private PlaybackControlsRow.MoreActions mMoreActions;

    private ArrayList<Movie> mItems = new ArrayList<Movie>();
    private int mCurrentPlaybackState;
    private Runnable mRunnable;
    private Handler mHandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "onCreate");
        super.onCreate(savedInstanceState);

        mSelectedMovie = (Movie)getActivity().getIntent().getSerializableExtra("video");
        mItems.add(mSelectedMovie);
        mHandler = new Handler(Looper.getMainLooper());

        setBackgroundType(PlaybackOverlayFragment.BG_LIGHT);
        setFadingEnabled(true);

        setUpRows();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        togglePlayback(true);
    }

    private ArrayObjectAdapter mRowsAdapter;

    private void setUpRows() {
        ClassPresenterSelector ps = new ClassPresenterSelector();

        PlaybackControlsRowPresenter playbackControlsRowPresenter;
        playbackControlsRowPresenter = new PlaybackControlsRowPresenter(new DetailsDescriptionPresenter());
        playbackControlsRowPresenter.setBackgroundColor(getResources().getColor(R.color.youtube_icon));
        ps.addClassPresenter(PlaybackControlsRow.class, playbackControlsRowPresenter);
        ps.addClassPresenter(ListRow.class, new ListRowPresenter());
        mRowsAdapter = new ArrayObjectAdapter(ps);

        /*
         * Add PlaybackControlsRow to mRowsAdapter, which makes video control UI.
         * PlaybackControlsRow is supposed to be first Row of mRowsAdapter.
         */
        addPlaybackControlsRow();
        /* add ListRow to second row of mRowsAdapter */
        addOtherRows();

        /* onClick */
        playbackControlsRowPresenter.setOnActionClickedListener(new OnActionClickedListener() {
            public void onActionClicked(Action action) {
                if (action.getId() == mPlayPauseAction.getId()) {
                    /* PlayPause action */
                    togglePlayback(mPlayPauseAction.getIndex() == PlaybackControlsRow.PlayPauseAction.PLAY);
                } /*else if (action.getId() == mSkipNextAction.getId()) {
                    // SkipNext action
                    next(mCurrentPlaybackState == PlaybackState.STATE_PLAYING);
                } else if (action.getId() == mSkipPreviousAction.getId()) {
                    // SkipPrevious action
                    prev(mCurrentPlaybackState == PlaybackState.STATE_PLAYING);
                } else if (action.getId() == mFastForwardAction.getId()) {
                    // FastForward action
                    fastForward();
                } else if (action.getId() == mRewindAction.getId()) {
                    // Rewind action
                    rewind();
                }*/
                if (action instanceof PlaybackControlsRow.MultiAction) {
                    /* Following action is subclass of MultiAction
                     * - PlayPauseAction
                     * - FastForwardAction
                     * - RewindAction
                     * - ThumbsAction
                     * - RepeatAction
                     * - ShuffleAction
                     * - HighQualityAction
                     * - ClosedCaptioningAction
                     */
                    notifyChanged(action);
                }
            }
        });

        setAdapter(mRowsAdapter);
    }

    private void togglePlayback(boolean playPause) {
        /* Video control part */
        ((PlaybackOverlayActivity) getActivity()).playPause(playPause);
        /* UI control part */
        playbackStateChanged();
        //updatePlaybackRow();
    }

    public void playbackStateChanged() {

        if (mCurrentPlaybackState != PlaybackState.STATE_PLAYING) {
            mCurrentPlaybackState = PlaybackState.STATE_PLAYING;
            startProgressAutomation();
            setFadingEnabled(true);
            mPlayPauseAction.setIndex(PlaybackControlsRow.PlayPauseAction.PAUSE);
            mPlayPauseAction.setIcon(mPlayPauseAction.getDrawable(PlaybackControlsRow.PlayPauseAction.PAUSE));
            notifyChanged(mPlayPauseAction);
        } else if (mCurrentPlaybackState != PlaybackState.STATE_PAUSED) {
            mCurrentPlaybackState = PlaybackState.STATE_PAUSED;
            stopProgressAutomation();
            //setFadingEnabled(false); // if set to false, PlaybackcontrolsRow will always be on the screen
            mPlayPauseAction.setIndex(PlaybackControlsRow.PlayPauseAction.PLAY);
            mPlayPauseAction.setIcon(mPlayPauseAction.getDrawable(PlaybackControlsRow.PlayPauseAction.PLAY));
            notifyChanged(mPlayPauseAction);
        }

        int currentTime = ((PlaybackOverlayActivity) getActivity()).getPosition();
        mPlaybackControlsRow.setCurrentTime(currentTime);
        mPlaybackControlsRow.setBufferedProgress(currentTime + SIMULATED_BUFFERED_TIME);

    }

    private int getUpdatePeriod() {
        if (getView() == null || mPlaybackControlsRow.getTotalTime() <= 0) {
            return DEFAULT_UPDATE_PERIOD;
        }
        return Math.max(UPDATE_PERIOD, mPlaybackControlsRow.getTotalTime() / getView().getWidth());
    }

    private void startProgressAutomation() {
        if (mRunnable == null) {
            mRunnable = new Runnable() {
                @Override
                public void run() {
                        int updatePeriod = getUpdatePeriod();
                    int currentTime = mPlaybackControlsRow.getCurrentTime() + updatePeriod;
                    int totalTime = mPlaybackControlsRow.getTotalTime();
                    mPlaybackControlsRow.setCurrentTime(currentTime);
                    mPlaybackControlsRow.setBufferedProgress(currentTime + SIMULATED_BUFFERED_TIME);

                    if (totalTime > 0 && totalTime <= currentTime) {
                        stopProgressAutomation();
                        //next(true);
                    } else {
                        mHandler.postDelayed(this, updatePeriod);
                    }
                }
            };
            mHandler.postDelayed(mRunnable, getUpdatePeriod());
        }
    }

    private void stopProgressAutomation() {
        if (mHandler != null && mRunnable != null) {
            mHandler.removeCallbacks(mRunnable);
            mRunnable = null;
        }
    }

    private void notifyChanged(Action action) {
        ArrayObjectAdapter adapter = mPrimaryActionsAdapter;
        if (adapter.indexOf(action) >= 0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action), 1);
            return;
        }
        adapter = mSecondaryActionsAdapter;
        if (adapter.indexOf(action) >= 0) {
            adapter.notifyArrayItemRangeChanged(adapter.indexOf(action), 1);
            return;
        }
    }

    private void updatePlaybackRow() {
        Log.d(TAG, "updatePlaybackRow");
        if (mPlaybackControlsRow.getItem() != null) {
            Movie item = (Movie) mPlaybackControlsRow.getItem();
            item.setTitle(mSelectedMovie.getTitle());
            //item.setStudio(mItems.get(mCurrentItem).getStudio());

            mRowsAdapter.notifyArrayItemRangeChanged(0, 1);
            /* total time is necessary to show video playing time progress bar */
            //int duration = (int) Utils.getDuration(mSelectedMovie.getVideoURL());
            //Log.i(TAG, "videoUrl: " + mSelectedMovie.getVideoURL());
            //Log.i(TAG, "duration = " + duration);
            //mPlaybackControlsRow.setTotalTime(duration);
            mPlaybackControlsRow.setCurrentTime(0);
            mPlaybackControlsRow.setBufferedProgress(0);
        }
        /*if (SHOW_IMAGE) {
            mPlaybackControlsRowTarget = new PicassoPlaybackControlsRowTarget(mPlaybackControlsRow);
            updateVideoImage(mItems.get(mCurrentItem).getCardImageURI());
        }*/
    }

    private void addPlaybackControlsRow() {
        mPlaybackControlsRow = new PlaybackControlsRow(mSelectedMovie);
        mRowsAdapter.add(mPlaybackControlsRow);

        ControlButtonPresenterSelector presenterSelector = new ControlButtonPresenterSelector();
        mPrimaryActionsAdapter = new ArrayObjectAdapter(presenterSelector);
        mSecondaryActionsAdapter = new ArrayObjectAdapter(presenterSelector);
        mPlaybackControlsRow.setPrimaryActionsAdapter(mPrimaryActionsAdapter);
        mPlaybackControlsRow.setSecondaryActionsAdapter(mSecondaryActionsAdapter);

        Activity activity = getActivity();
        mPlayPauseAction = new PlaybackControlsRow.PlayPauseAction(activity);
        mRepeatAction = new PlaybackControlsRow.RepeatAction(activity);
        mThumbsUpAction = new PlaybackControlsRow.ThumbsUpAction(activity);
        mThumbsDownAction = new PlaybackControlsRow.ThumbsDownAction(activity);
        mShuffleAction = new PlaybackControlsRow.ShuffleAction(activity);
        mSkipNextAction = new PlaybackControlsRow.SkipNextAction(activity);
        mSkipPreviousAction = new PlaybackControlsRow.SkipPreviousAction(activity);
        mFastForwardAction = new PlaybackControlsRow.FastForwardAction(activity);
        mRewindAction = new PlaybackControlsRow.RewindAction(activity);
        mHighQualityAction = new PlaybackControlsRow.HighQualityAction(activity);
        mClosedCaptioningAction = new PlaybackControlsRow.ClosedCaptioningAction(activity);
        mMoreActions = new PlaybackControlsRow.MoreActions(activity);

        /* PrimaryAction setting */
        mPrimaryActionsAdapter.add(mSkipPreviousAction);
        mPrimaryActionsAdapter.add(mRewindAction);
        mPrimaryActionsAdapter.add(mPlayPauseAction);
        mPrimaryActionsAdapter.add(mFastForwardAction);
        mPrimaryActionsAdapter.add(mSkipNextAction);

        /* SecondaryAction setting */
        mSecondaryActionsAdapter.add(mThumbsUpAction);
        mSecondaryActionsAdapter.add(mThumbsDownAction);
        mSecondaryActionsAdapter.add(mRepeatAction);
        mSecondaryActionsAdapter.add(mShuffleAction);
        mSecondaryActionsAdapter.add(mHighQualityAction);
        mSecondaryActionsAdapter.add(mClosedCaptioningAction);
        mSecondaryActionsAdapter.add(mMoreActions);

    }

    private void addOtherRows() {
        ArrayObjectAdapter listRowAdapter = new ArrayObjectAdapter(new CardPresenter());
        Movie movie = new Movie();
        movie.setTitle("FAPtv Cơm Nguội: Tập 163 - Chàng Trai Mù");
        //movie.setStudio("studio");
        //movie.setDescription("description");
        movie.setCardImageUrl("https://i.ytimg.com/vi/ImpUTKkIz88/maxresdefault.jpg");
        listRowAdapter.add(movie);
        listRowAdapter.add(movie);

        HeaderItem header = new HeaderItem(0, "Relate Videos");
        mRowsAdapter.add(new ListRow(header, listRowAdapter));
    }

}
