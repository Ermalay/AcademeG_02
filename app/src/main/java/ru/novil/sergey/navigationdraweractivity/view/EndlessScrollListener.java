package ru.novil.sergey.navigationdraweractivity.view;

import android.content.Context;
import android.widget.AbsListView;


public class EndlessScrollListener implements AbsListView.OnScrollListener {

    private int visibleThreshold = 5;
    private int currentPage = 0;
    private int previousTotal = 0;
    private boolean loading = true;
//    FirstFragment load = new FirstFragment();
    Context context;

    public EndlessScrollListener() {
    }
    public EndlessScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }


    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView absListView, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        if (loading) {
            if (totalItemCount > previousTotal) {
                loading = false;
                previousTotal = totalItemCount;
                currentPage++;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold)) {
            // I load the next page of gigs using a background task,
            // but you can call any function here.
//            new FirstFragment.ParseTask().execute(currentPage + 1);
//            new LoadGigsTask().execute(currentPage + 1);
//            new FragmentActivity().ParseTask(this).execute();
//            Toast.makeText(context, "!!! lv.getLastVisiblePosition() !!!", Toast.LENGTH_SHORT).show();
            loading = true;
        }
    }

}
