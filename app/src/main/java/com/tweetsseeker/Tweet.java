package com.tweetsseeker;

import twitter4j.Status;

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
