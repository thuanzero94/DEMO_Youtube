package foxconn.vn.alan.demo_youtube.ui.Presenter;

import android.content.Context;
import android.graphics.Color;
import android.support.v17.leanback.widget.Presenter;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.TextView;

import foxconn.vn.alan.demo_youtube.R;

public class GridItemPresenter extends Presenter{
    private static final String TAG = GridItemPresenter.class.getSimpleName();

    private static Context mContext;
    private static final int GRID_ITEM_WIDTH = 600;
    private static final int GRID_ITEM_HEIGHT = 400;

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent) {
        Log.d(TAG, "onCreateViewHolder");
        mContext=parent.getContext();

        TextView view = new TextView(parent.getContext());
        view.setLayoutParams(new ViewGroup.LayoutParams(GRID_ITEM_WIDTH, GRID_ITEM_HEIGHT));
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.setBackgroundColor(mContext.getResources().getColor(R.color.default_background));
        view.setTextColor(Color.WHITE);
        view.setGravity(Gravity.CENTER);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, Object item) {
        Log.d(TAG, "onBindViewHolder");
        ((TextView) viewHolder.view).setText((String) item);
    }

    @Override
    public void onUnbindViewHolder(ViewHolder viewHolder) {

    }
}
