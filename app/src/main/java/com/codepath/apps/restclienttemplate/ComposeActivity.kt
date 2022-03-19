package com.codepath.apps.restclienttemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.codepath.apps.restclienttemplate.models.Media.Companion.TAG
import com.codepath.apps.restclienttemplate.models.Tweet
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers

class ComposeActivity : AppCompatActivity() {
    lateinit var etCompose: EditText
    lateinit var btnTweet: Button
    lateinit var client: TwitterClient
    lateinit var tvCounts: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_compose)

        etCompose = findViewById(R.id.etTweetCompose)
        btnTweet = findViewById(R.id.btnTweet)
        client = TwitterApplication.getRestClient(this)
        tvCounts = findViewById(R.id.tvCounts)

        etCompose.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                // Fires right as the text is being changed (even supplies the range of text)

                val tweetcontent = 280 - etCompose.text.toString().length
                tvCounts.setText("$tweetcontent/280")
            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                // Fires right before text is changing
                tvCounts.setText("0/280")
            }

            override fun afterTextChanged(s: Editable) {
                // Fires right after the text has changed
                val tweetcontent = 280 - etCompose.text.toString().length
                tvCounts.setText("$tweetcontent/280")
                if (tweetcontent < 0 ) {
                    btnTweet?.isEnabled = false
                    btnTweet?.setTextColor(ContextCompat.getColor(btnTweet.context, R.color.white))
                    btnTweet?.setBackgroundColor(ContextCompat.getColor(btnTweet.context, R.color.cardview_dark_background))}
            }
        })

        btnTweet.setOnClickListener {
            val tweetcontent = etCompose.text.toString()
            // 1. make sure tweet isn't empty
            if (tweetcontent.isEmpty()) {
                Toast.makeText(this, "You are sending empty tweet!", Toast.LENGTH_SHORT).show()
                // look into displaying snakerbar message
            } else {
                // 2. Make sure the tweet is under character count
                if (tweetcontent.length > 280) {
                    Toast.makeText(
                        this,
                        "Tweet is too long. Limited to 280 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnTweet?.isEnabled = false
//                    btnTweet?.setTextColor(ContextCompat.getColor(tweetcontent.context, R.color.white))

                } else {
//                Toast.makeText(this, tweetcontent, Toast.LENGTH_SHORT).show()
                    // Make an api call to publish tweet
                    client.publistTweet(tweetcontent, object : JsonHttpResponseHandler() {
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
                }
            }
        }
    }
}