package foxconn.vn.alan.demo_youtube.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v17.leanback.app.BackgroundManager;
import android.support.v17.leanback.app.BrowseFragment;
import android.support.v17.leanback.widget.ArrayObjectAdapter;
import android.support.v17.leanback.widget.ListRow;
import android.support.v17.leanback.widget.ListRowPresenter;
import android.support.v17.leanback.widget.OnItemViewClickedListener;
import android.support.v17.leanback.widget.Presenter;
import android.support.v17.leanback.widget.PresenterSelector;
import android.support.v17.leanback.widget.Row;
import android.support.v17.leanback.widget.RowPresenter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.widget.Toast;

import java.util.HashMap;

import at.huber.youtubeExtractor.Format;
import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;
import foxconn.vn.alan.demo_youtube.Model.IconHeaderItem;
import foxconn.vn.alan.demo_youtube.Model.Movie;
import foxconn.vn.alan.demo_youtube.R;
import foxconn.vn.alan.demo_youtube.ui.Presenter.CardPresenter;
import foxconn.vn.alan.demo_youtube.ui.Presenter.GridItemPresenter;
import foxconn.vn.alan.demo_youtube.ui.Presenter.IconHeaderItemPresenter;

public class HomeFragment extends BrowseFragment{
    private static final String TAG = HomeFragment.class.getSimpleName();

    private ArrayObjectAdapter mRowsAdapter;
    private SpinnerFragment mSpinnerFragment = new SpinnerFragment();

    private BackgroundManager mBackgroundManager;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        Log.i(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);

        mBackgroundManager = BackgroundManager.getInstance(getActivity());
        mBackgroundManager.attach(getActivity().getWindow());
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(new DisplayMetrics());

        colorBackground();
        setupUIElements();
        loadRows();
        setupEventListeners();
    }

    private void setupEventListeners() {
        //setOnItemViewSelectedListener(new ItemViewSelectedListener());
        setOnItemViewClickedListener(new ItemViewClickedListener());
    }

    private final class ItemViewClickedListener implements OnItemViewClickedListener {
        @Override
        public void onItemClicked(Presenter.ViewHolder itemViewHolder, Object item,
                                  RowPresenter.ViewHolder rowViewHolder, Row row) {
            if (!(item instanceof Movie)) return;
            final Movie movie = (Movie) item;
            if (!(getFragmentManager().findFragmentById(R.id.home_browse_fragment) instanceof SpinnerFragment))
                getFragmentManager().beginTransaction().add(R.id.home_browse_fragment, mSpinnerFragment).commit();
            String youtubeLink = movie.getVideoURL();
            new YouTubeExtractor(getActivity()) {
                @Override
                public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                    if (ytFiles != null) {
                        try {
                            HashMap<Integer, String> videoUrls = new HashMap<Integer, String>();
                            int maxTag = 0, maxHeight = 0;
                            for (int i = 0, itag; i < ytFiles.size(); i++) {
                                itag = ytFiles.keyAt(i);
                                if (itag > 100) continue;
                                // ytFile represents one file with its url and meta data
                                YtFile ytFile = ytFiles.get(itag);
                                Format ytFormat = ytFile.getFormat();
                                int resolution = ytFormat.getHeight();
                                if (resolution > -1)
                                    videoUrls.put(resolution, ytFile.getUrl());
                                if (ytFormat.getHeight() > maxHeight) {
                                    maxHeight = ytFormat.getHeight();
                                    maxTag = itag;

                                }

                                Log.d(TAG, "is hls:" + ytFormat.isHlsContent() + "---is DASH:" + ytFormat.isDashContainer() + "----tag: " + itag + "-----height: " + resolution + "---url:" + ytFile.getUrl());
                            }
                            //   int itag = 22;
                            String downloadUrl = ytFiles.get(maxTag).getUrl();
                            Log.d(TAG, "height:" + maxHeight + "p" + "---link:" + downloadUrl);
                            movie.setVideoURL(downloadUrl);
                            movie.setVideoUrls(videoUrls);
                            Intent intent = new Intent(getActivity(), PlaybackOverlayActivity.class);
                            intent.putExtra("video", movie);
                            getFragmentManager().beginTransaction().remove(mSpinnerFragment).commit();
                            startActivity(intent);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Intent intent = new Intent(getActivity(), ErrorActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            }.extract(youtubeLink, true, true);
            /*//Toast.makeText(getActivity(), "Clicked", Toast.LENGTH_SHORT).show();
            // each time the item is clicked, code inside here will be executed.
            if (item instanceof Movie) {
                final Movie movie = (Movie) item;
                Log.d(TAG, "Movie Item: " + movie.toString());

                Intent intent = new Intent(getActivity(), PlaybackOverlayActivity.class);
                intent.putExtra("video", movie);

                startActivity(intent);
            } else if (item instanceof String){
                Log.d(TAG, "String Item: " + item.toString());
                if(item == "ErrorFragment"){
                    Intent intent = new Intent(getActivity(), ErrorActivity.class);
                    startActivity(intent);
                }
            }*/
        }
    }

    private void setupUIElements(){
        setBadgeDrawable(getResources().getDrawable(R.drawable.youtube_logo));
        //setTitle("TV Tube");
        setHeadersState(HEADERS_ENABLED);
        setHeadersTransitionOnBackEnabled(true);

        // set fastLane (or headers) background color
        setBrandColor(getResources().getColor(R.color.fastlane_background));
        // set search icon color
        setSearchAffordanceColor(getResources().getColor(R.color.search_opaque));

        setHeaderPresenterSelector(new PresenterSelector() {
            @Override
            public Presenter getPresenter(Object o) {
                return new IconHeaderItemPresenter();
            }
        });

    }


    private void loadRows(){
        mRowsAdapter = new ArrayObjectAdapter(new ListRowPresenter());

        /* GridItemPresenter */
        IconHeaderItem gridItemPresenterHeader = new IconHeaderItem(0, "Home", R.drawable.home_icon);

        GridItemPresenter mGridPresenter = new GridItemPresenter();
        ArrayObjectAdapter gridRowAdapter = new ArrayObjectAdapter(mGridPresenter);
        gridRowAdapter.add("ErrorFragment");
        gridRowAdapter.add("ITEM 2");
        gridRowAdapter.add("ITEM 3");
        mRowsAdapter.add(new ListRow(gridItemPresenterHeader, gridRowAdapter));

        /* CardPresenter */
        IconHeaderItem cardPresenterHeader = new IconHeaderItem(1, "Trending", R.drawable.trend_icon);
        CardPresenter cardPresenter = new CardPresenter();
        ArrayObjectAdapter cardRowAdapter = new ArrayObjectAdapter(cardPresenter);

        for(int i=0; i<10; i++) {
            Movie movie = new Movie();

            //movie.setCardImageUrl("https://i.ytimg.com/vi/Ks-_Mh1QhMc/hqdefault.jpg");
            movie.setCardImageUrl("https://i.ytimg.com/vi/UyEGJjQfO0o/maxresdefault.jpg");
            movie.setTitle("FAPtv Cơm Nguội: Tập 174 - Anh Chồng Nhu Nhược");
            //movie.setVideoURL("http://commondatastorage.googleapis.com/android-tv/Sample%20videos/Zeitgeist/Zeitgeist%202010_%20Year%20in%20Review.mp4");
            movie.setVideoURL("https://www.youtube.com/watch?v=UyEGJjQfO0o");
            cardRowAdapter.add(movie);
        }
        mRowsAdapter.add(new ListRow(cardPresenterHeader, cardRowAdapter));

        /* set */
        setAdapter(mRowsAdapter);
    }

    public void colorBackground(){
        mBackgroundManager.setColor(getResources().getColor(R.color.window_background));
    }
}
