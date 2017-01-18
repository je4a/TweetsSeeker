package com.tweetsseeker;

import twitter4j.Status;

/**
 * Created by yevgenderkach on 1/17/17.
 */

public class Tweet {
    public long   id;
    public String text;
    public String userName;
    public String userPic;

    public static Tweet fromStatus(Status status) {
        Tweet tweet = new Tweet();

        tweet.id = status.getId();
        tweet.text = status.getText();
        tweet.userName = status.getUser().getName();
        tweet.userPic = status.getUser().getProfileImageURL();

        return tweet;
    }
}
