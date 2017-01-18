package com.tweetsseeker;

import android.content.Context;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class TweetsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Tweet> tweets;
    private final LayoutInflater inflater;

    public TweetsAdapter(LayoutInflater inflater) {
        this.inflater = inflater;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new ViewHolder(inflater.inflate(R.layout.tweet_list_item, parent, false));
    }

    @Override public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ViewHolder) holder).bind(getItem(position));
    }

    public Tweet getItem(int position) {
        if (isEmpty() || position < 0 || position >= tweets.size()) return new Tweet();
        return tweets.get(position);
    }

    @Override public int getItemCount() {
        return isEmpty() ? 0 : tweets.size();
    }

    public void replaceTo(List<Tweet> tweets) {
        clear();
        addTweets(tweets);
    }

    public void addTweets(final List<Tweet> tweets) {
        if (isEmpty()) this.tweets = new ArrayList<>(tweets);

        this.tweets.addAll(tweets);
        notifyDataSetChanged();
    }

    public void clear() {
        if (isEmpty()) return;
        tweets.clear();
    }

    public boolean isEmpty() {
        return tweets == null || tweets.size() == 0;
    }

    private static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView userPic;
        private TextView  tweetText;

        private Resources resources;
        private final Context context;

        public ViewHolder(final View itemView) {
            super(itemView);
            context = itemView.getContext();
            userPic = (ImageView) itemView.findViewById(R.id.iv_user_pic);
            tweetText = (TextView) itemView.findViewById(R.id.tv_tweet_text);
            resources = context.getResources();
        }

        public void bind(Tweet tweet) {
            Picasso.with(context)
                    .load(tweet.userPic)
                    .into(userPic);

            tweetText.setText(resources.getString(R.string.tweet_text_format,
                    tweet.userName, tweet.text));
        }
    }
}
