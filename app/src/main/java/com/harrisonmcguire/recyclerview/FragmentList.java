package com.harrisonmcguire.recyclerview;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by harrisonmcguire on 6/5/15.
 */
public class FragmentList extends Fragment {

    private static final String TAG = "RecyclerViewExample";
    private List<ListItems> listItemsList = new ArrayList<ListItems>();

    private RecyclerView mRecyclerView;
    private MyRecyclerAdapter adapter;

    private int counter = 0;
    private String count;
    private String jsonSubreddit;
    private String after_id;
    private static final String gaming = "gaming";
    private static final String aww = "aww";
    private static final String pics = "pics";
    private static final String subredditUrl = "http://www.reddit.com/r/";
    private static final String jsonEnd = "/.json";
    private static final String qCount = "?count=";
    private static final String after = "&after=";

    private ProgressDialog progressDialog;

    public FragmentList() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        //Initialize recycler view
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);

        // add the line under each row
        // if you are creating a card views, it would be best to delete this decoration
        mRecyclerView.addItemDecoration(
                new HorizontalDividerItemDecoration.Builder(getActivity())
                        .color(Color.BLACK)
                        .build());

        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        updateList(MainActivity.fragSubreddit);

        mRecyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int current_page) {
                Log.d("SCROLL PAST UPDATE", "You hit me");

                //maintain scroll position
                int lastFirstVisiblePosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
                ((LinearLayoutManager) mRecyclerView.getLayoutManager()).scrollToPosition(lastFirstVisiblePosition);

                loadMore(jsonSubreddit);
            }
        });


        Button mButton = (Button) rootView.findViewById(R.id.gaming_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragSubreddit = gaming;
                reloadFragment();
            }
        });

        Button nButton = (Button) rootView.findViewById(R.id.pics_button);
        nButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.fragSubreddit = pics;
                reloadFragment();

            }
        });

        return rootView;
    }

    public void updateList(String subreddit) {

        // Set the counter to 0. This counter will be used to create new json urls
        // In the loadMore function we will increase this integer by 25
        counter = 0;

        // Create the reddit json url for parsing
        subreddit = subredditUrl + subreddit + jsonEnd;

        //declare the adapter and attach it to the recyclerview
        adapter = new MyRecyclerAdapter(getActivity(), listItemsList);
        mRecyclerView.setAdapter(adapter);

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // Clear the adapter because new data is being added from a new subreddit
        adapter.clearAdapter();

       showPD();

        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, subreddit, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                Log.d(TAG, response.toString());
                hidePD();

                // Parse json data.
                // Declare the json objects that we need and then for loop through the children array.
                // Do the json parse in a try catch block to catch the exceptions
                try {
                    JSONObject data = response.getJSONObject("data");
                    after_id = data.getString("after");
                    JSONArray children = data.getJSONArray("children");

                    for (int i = 0; i < children.length(); i++) {

                        JSONObject post = children.getJSONObject(i).getJSONObject("data");
                        ListItems item = new ListItems();
                        item.setTitle(post.getString("title"));
                        item.setThumbnail(post.getString("thumbnail"));
                        item.setUrl(post.getString("url"));
                        item.setSubreddit(post.getString("subreddit"));
                        item.setAuthor(post.getString("author"));

                        jsonSubreddit = post.getString("subreddit");

                        listItemsList.add(item);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Update list by notifying the adapter of changes
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePD();
            }
        });

        queue.add(jsObjRequest);

    }

    public void loadMore(String subreddit) {

        // Add 25 each time the function is called
        // Then convert it to a string to add to other strings to create the new reddit json url.
        counter = counter + 25;
        count = String.valueOf(counter);


        subreddit = jsonSubreddit;

        // Create the reddit json url for parsing
        subreddit = subredditUrl + subreddit + jsonEnd + qCount + count + after + after_id;

        // Declare the adapter and attach it to the recyclerview
        adapter = new MyRecyclerAdapter(getActivity(), listItemsList);
        mRecyclerView.setAdapter(adapter);

        showPD();


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(getActivity());

        // Request a string response from the provided URL.
        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.GET, subreddit, null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                // Log to console the whole json string for debugging
                Log.d(TAG, response.toString());
                hidePD();

                // Parse json data.
                // Declare the json objects that we need and then for loop through the children array.
                // Do the json parse in a try catch block to catch the exceptions
                try {
                    JSONObject data = response.getJSONObject("data");
                    after_id = data.getString("after");
                    JSONArray children = data.getJSONArray("children");

                    for (int i = 0; i < children.length(); i++) {

                        JSONObject post = children.getJSONObject(i).getJSONObject("data");
                        ListItems item = new ListItems();
                        item.setTitle(post.getString("title"));
                        item.setThumbnail(post.getString("thumbnail"));
                        item.setUrl(post.getString("url"));
                        item.setSubreddit(post.getString("subreddit"));
                        item.setAuthor(post.getString("author"));
                        listItemsList.add(item);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Update list by notifying the adapter of changes
                adapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error" + error.getMessage());
                hidePD();

            }
        });

        queue.add(jsObjRequest);
    }

    // Reload the fragment list holding the recyclerviews
    private void reloadFragment() {
        Fragment newFragment = new FragmentList();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, newFragment);
        transaction.commit();
    }

    private void showPD() {
        if(progressDialog == null) {
            progressDialog  = new ProgressDialog(getActivity());
            progressDialog .setMessage("Loading...");
            progressDialog .setCancelable(false);
            progressDialog .setCanceledOnTouchOutside(false);
            progressDialog .show();
        }
    }

    // function to hide the loading dialog box
    private void hidePD() {
        if (progressDialog  != null) {
            progressDialog .dismiss();
            progressDialog  = null;
        }
    }

    // Stop app from running
    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePD();
    }

}


