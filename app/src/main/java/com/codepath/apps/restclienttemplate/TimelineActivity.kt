package com.codepath.apps.restclienttemplate

import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONException

import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler.JSON

import org.json.JSONArray




class TimelineActivity : AppCompatActivity() {
    lateinit var client: TwitterClient
    lateinit var rvTweets: RecyclerView
    lateinit var adapter: TweetsAdapter
    lateinit var swipeContainer: SwipeRefreshLayout
    val tweets = ArrayList<Tweet>()
    private lateinit var scrollListener: EndlessRecyclerViewScrollListener
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timeline)

        client = TwitterApplication.getRestClient(this)
        swipeContainer = findViewById(R.id.swipeContainer)
        swipeContainer.setOnRefreshListener {
            Log.i("caren","Refreshing")
            populateHomeTimeline()
        }
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light);

        rvTweets = findViewById(R.id.rvTweets)
        adapter = TweetsAdapter(tweets)
        val linearLayoutManager = LinearLayoutManager(this)

        rvTweets.layoutManager = linearLayoutManager
        rvTweets.adapter = adapter
        populateHomeTimeline()

        scrollListener =  object:EndlessRecyclerViewScrollListener(linearLayoutManager){
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to the bottom of the list
                loadMoreData()
            }
        }
        rvTweets.addOnScrollListener(scrollListener as EndlessRecyclerViewScrollListener);
    }
    fun loadMoreData() {
        // 1. Send an API request to retrieve appropriate paginated data
        // 2. Deserialize and construct new model objects from the API response
        // 3. Append the new data objects to the existing set of items inside the array of items
        // 4. Notify the adapter of the new items made with `notifyItemRangeInserted()`
        client.getNextPageOfTweets(object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess getNextPageOfTweets: $json")
                //  --> Deserialize and construct new model objects from the API response
                val jsonArray = json.jsonArray
                try {
                    val tweets = Tweet.fromJsonArrary(jsonArray)
                    //  --> Append the new data objects to the existing set of items inside the array of items
                    //  --> Notify the adapter of the new items made with `notifyItemRangeInserted()`
                    adapter.addAll(tweets)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure getNextPageOfTweets: ", throwable)
            }
        }, tweets[tweets.size - 1].max_id)
        Log.i(TAG, "loadMoreData: " + tweets[tweets.size - 1].max_id)
    }

    fun populateHomeTimeline() {
        client.getHomeTimeline(object : JsonHttpResponseHandler() {


            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.i("Caren", "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i("Caren", "onSuccess!")
                val jsonArray = json.jsonArray
                // Clear out currently tweets
                adapter.clear()
                val newtweets = Tweet.fromJsonArrary(jsonArray)

                tweets.addAll(newtweets)

                adapter.notifyDataSetChanged()
                swipeContainer.isRefreshing= false
            }

        })
    }
}