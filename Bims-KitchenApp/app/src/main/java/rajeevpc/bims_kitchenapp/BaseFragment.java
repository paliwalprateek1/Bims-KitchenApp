package rajeevpc.bims_kitchenapp;

import android.app.Fragment;
import android.content.Context;

/**
 * Created by prateek on 7/11/16.
 */
public class BaseFragment extends Fragment {

    protected  ActionBarProvider mActionBarProvider;
    public void onAttach(Context context) {
        super.onAttach(context);
        mActionBarProvider = (ActionBarProvider) context;
    }

}
