package com.codepath.apps.restclienttemplate.models

import org.json.JSONArray
import org.json.JSONObject
import TimeFormatter
import android.content.ContentValues
import android.os.Parcelable
import android.util.Log
import kotlinx.parcelize.Parcelize

@Parcelize
class Tweet(var body: String ="", var createdAt: String ="", var user: User? = null, var max_id: Long = 0) : Parcelable {


//
//    var body: String = ""
//    var createdAt: String = ""
//    var user: User? = null
//    var max_id: Long = 0
   var entities: Entities? = null

    companion object {
        fun fromJson(jsonObject: JSONObject): Tweet {
            val tweet = Tweet()
            tweet.body = jsonObject.getString("text")
            tweet.createdAt = jsonObject.getString("created_at")
            tweet.user = User.fromJson(jsonObject.getJSONObject("user"))
            tweet.max_id = jsonObject.getLong("id")
            Log.i(ContentValues.TAG, "$jsonObject")
            if (jsonObject.has("entities")) {
//                Log.i(ContentValues.TAG, "has Video Info.")
                tweet.entities = Entities.fromJson(jsonObject.getJSONObject("entities"));
            }else{
                Log.i(ContentValues.TAG, "None")
            }

            return tweet
        }

        fun fromJsonArrary(jsonArray: JSONArray): List<Tweet> {
            val tweets = ArrayList<Tweet>()
            for (i in 0 until jsonArray.length()) {
                tweets.add(fromJson(jsonArray.getJSONObject((i))))
            }
            return tweets
        }

    }

    fun getFormattedTimestamp(createdAt: String): String {
        val formatter = TimeFormatter()
        var diff = formatter.getTimeDifference(createdAt)
        return diff
    }

}