package com.tweetsseeker;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;
import twitter4j.Query;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

public class TweetsListPresenter implements TweetsListContract.Presenter {
    private final AccessToken accessToken = new AccessToken(BuildConfig.ACCESS_TOKEN, BuildConfig.ACCESS_TOKEN_SECRET);

    private TweetsListContract.View view;
    private CompositeSubscription compositeSubscription;
    private Twitter twitter = TwitterFactory.getSingleton();
    private Query searchQuery;

    public TweetsListPresenter(final TweetsListContract.View view) {
        this.view = view;
        compositeSubscription = new CompositeSubscription();
        searchQuery = new Query();
        searchQuery.setResultType(Query.RECENT);
        twitter.setOAuthConsumer(BuildConfig.CONSUMER_KEY, BuildConfig.CONSUMER_SECRET);
    }

    @Override public void refresh() {
        String query = view.getQuery();

        if (TextUtils.isEmpty(query)) {
            view.showMessage("Empty query!");
            return;
        }

        addSubscription(
            Observable.<List<Tweet>>create(subscriber -> {
                twitter.setOAuthAccessToken(accessToken);
                searchQuery.setQuery(view.getQuery());
                try {
                    List<Status> statuses = twitter.search(searchQuery).getTweets();
                    List<Tweet> tweets = new ArrayList<>(statuses.size());

                    for (int i = 0; i < statuses.size(); i++) {
                        tweets.add(Tweet.fromStatus(statuses.get(i)));
                    }

                    subscriber.onNext(tweets);
                } catch (TwitterException e) {
                    subscriber.onError(e);
                }
            })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::setTweets, view)
        );
    }

    @Override public void onStop() {
        compositeSubscription.clear();
    }

    void addSubscription(Subscription subscription) {
        compositeSubscription.add(subscription);
    }
}
