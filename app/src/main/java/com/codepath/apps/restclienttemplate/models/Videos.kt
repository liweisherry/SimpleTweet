package com.codepath.apps.restclienttemplate.models

import android.util.Log
import org.json.JSONArray

import org.json.JSONObject


class Videos {

    companion object {
        val TAG = "Video Model: "
        var videoURL: String? =""
        var video: Videos? = null
        fun fromJson(jsonObject: JSONObject): Videos {
            val video = Videos()
            val videoList: JSONArray = jsonObject.getJSONArray("variants")
            videoURL = videoList.getJSONObject(0).getString("url")
            Log.i(TAG, "Video URL is " + videoURL)
            return video
        }

        @JvmName("getVideoURL1")
        fun getVideoURL(): String? {

            return videoURL
        }


        @JvmName("getVideo1")
        fun getVideo(): Videos? {
            return video
        }
    }
}