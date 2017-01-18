package com.tweetsseeker;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.jakewharton.rxbinding.support.v4.widget.RxSwipeRefreshLayout;

import java.util.List;

import rx.functions.Action1;

public class TweetsListActivity extends AppCompatActivity implements TweetsListContract.View {
    private static final String TAG = TweetsListActivity.class.getSimpleName();

    private SwipeRefreshLayout refreshLayout;
    private RecyclerView       tweetsList;
    private EditText           searchEdit;

    private TweetsListContract.Presenter presenter;
    private TweetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweets_list);
        presenter = new TweetsListPresenter(this);
        setupViews();
    }

    @Override protected void onStop() {
        super.onStop();
        presenter.onStop();
    }

    private void setupViews() {
        refreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_refresh);
        RxSwipeRefreshLayout.refreshes(refreshLayout).subscribe(refreshAction(), this);

        adapter = new TweetsAdapter(getLayoutInflater());
        tweetsList = (RecyclerView) findViewById(R.id.rv_tweets_list);
        tweetsList.setLayoutManager(new LinearLayoutManager(this));
        tweetsList.setHasFixedSize(true);
        tweetsList.setAdapter(adapter);

        searchEdit = (EditText) findViewById(R.id.ed_search);
        RxEditText.searchAction(searchEdit).subscribe(refreshAction());
    }

    @Override public String getQuery() {
        return searchEdit.getText().toString();
    }

    @Override public void call(final Throwable throwable) {
        String message = "Unknown error";
        if (throwable != null) {
            message = throwable.toString();
        }

        showMessage(message);
        Log.e(TAG, "", throwable);
    }

    @Override public void showMessage(final String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override public void setTweets(final List<Tweet> tweets) {
        adapter.replaceTo(tweets);
        stopProgress();
    }

    private Action1<Void> refreshAction() {
        return voidObj -> {
            startProgress();
            hideKeyboard();
            presenter.refresh();
        };
    }

    void startProgress() {
        startOrStopProgress(true);
    }

    void stopProgress() {
        startOrStopProgress(false);
    }

    void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusView = getCurrentFocus();

        if (imm != null && focusView != null && focusView.getWindowToken() != null) {
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void startOrStopProgress(boolean start) {
        refreshLayout.setRefreshing(start);
    }
}
