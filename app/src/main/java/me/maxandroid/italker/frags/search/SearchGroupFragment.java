package me.maxandroid.italker.frags.search;


import me.maxandroid.common.app.Fragment;
import me.maxandroid.italker.R;
import me.maxandroid.italker.activities.SearchActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchGroupFragment extends Fragment implements SearchActivity.SearchFragment {


    public SearchGroupFragment() {
        // Required empty public constructor
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search_group;
    }

    @Override
    public void search(String content) {

    }
}
