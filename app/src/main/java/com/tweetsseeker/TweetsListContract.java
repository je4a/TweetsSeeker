package com.tweetsseeker;

import java.util.List;

import rx.functions.Action1;

public interface TweetsListContract {
    interface View extends Action1<Throwable> {
        String getQuery();
        void showMessage(String message);
        void setTweets(List<Tweet> tweets);
    }

    interface Presenter {
        void refresh();
        void onStop();
    }
}
