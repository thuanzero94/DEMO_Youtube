package foxconn.vn.alan.demo_youtube.ui;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import foxconn.vn.alan.demo_youtube.R;


/**
 * Created by alan on 10/02/2018.
 */

public class ErrorActivity extends Activity {
    private static final String TAG = ErrorActivity.class.getSimpleName();

    private ErrorFragment mErrorFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCraete");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        testError();
    }

    private void testError(){
        mErrorFragment = new ErrorFragment();
        getFragmentManager().beginTransaction().add(R.id.home_browse_fragment, mErrorFragment).commit();
    }
}
