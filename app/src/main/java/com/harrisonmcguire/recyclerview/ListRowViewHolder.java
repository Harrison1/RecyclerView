package com.harrisonmcguire.recyclerview;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by Harrison on 5/23/2015.
 */
public class ListRowViewHolder extends RecyclerView.ViewHolder {
    protected NetworkImageView thumbnail;
    protected TextView title;
    protected TextView url;
    protected RelativeLayout recLayout;
    protected TextView author;
    protected TextView subreddit;

    public ListRowViewHolder(View view) {
        super(view);
        this.thumbnail = (NetworkImageView) view.findViewById(R.id.thumbnail);
        this.title = (TextView) view.findViewById(R.id.title);
        this.url = (TextView) view.findViewById(R.id.url);
        this.recLayout = (RelativeLayout) view.findViewById(R.id.recLayout);
        this.subreddit = (TextView) view.findViewById(R.id.subreddit);
        this.author = (TextView) view.findViewById(R.id.author);
        view.setClickable(true);
    }

}