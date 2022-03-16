package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.codepath.apps.restclienttemplate.models.Media.Companion.TAG
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {
    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose =findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        client = TwitterApplication.getRestClient(this)
        btnTweet.setOnClickListener {
            val tweetcontent = etCompose.text.toString()
            // 1. make sure tweet isn't empty
            if (tweetcontent.isEmpty()) {
                Toast.makeText(this, "You are sending empty tweet!", Toast.LENGTH_SHORT).show()
                // look into displaying snakerbar message
            } else {
                // 2. Make sure the tweet is under character count
                if (tweetcontent.length > 140) {
                    Toast.makeText(
                        this,
                        "Tweet is too long. Limited to 140 characters",
                        Toast.LENGTH_SHORT
                    ).show()

                }else{
//                Toast.makeText(this, tweetcontent, Toast.LENGTH_SHORT).show()
                // Make an api call to publish tweet
                client.publistTweet(tweetcontent, object: JsonHttpResponseHandler(){
                    override fun onFailure(
                        statusCode: Int,
                        headers: Headers?,
                        response: String?,
                        throwable: Throwable?
                    ) {
                        Log.i(TAG, "Failed on posing a tweet!")
                    }

                    override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                        Log.i(TAG, "Successfully posted a tweet!")
                        // Send back to the timeline activity\
                        val tweet = Tweet.fromJson(json.jsonObject)
                        val intent = Intent()
                        intent.putExtra("tweet", tweet)
                        setResult(RESULT_OK, intent)
                        finish()
                    }

                })
            }}
        }
    }
}