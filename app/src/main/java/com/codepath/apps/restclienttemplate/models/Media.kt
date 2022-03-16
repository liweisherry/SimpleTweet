package com.codepath.apps.restclienttemplate.models
import android.util.Log
import android.provider.MediaStore.Video
import org.json.JSONObject

import org.json.JSONException

import org.json.JSONArray


class Media {


    companion object {
        val TAG = "MediaModel"
        private var mediaType: String? = null
        var mediaURL: String? = null
        var videoURL: String? = null
        private var video: Videos? = null
        @Throws(JSONException::class)
        fun fromJson(jsonObject: JSONObject): Media {
            val media = Media()
            if (jsonObject != null) {
                if (jsonObject.has("type")) {
                    mediaType = jsonObject.getString("type")
                    Log.i(TAG, "Media: " + jsonObject.getString("type"))
                }
            }
            if (jsonObject != null) {
                if (jsonObject.has("media_url_https")) {
                    mediaURL = jsonObject.getString("media_url_https")
                    Log.i(TAG, "Photo URL " + jsonObject.getString("media_url_https"))
                }
            }
            if (jsonObject != null) {
                if (jsonObject.has("video_info")) {
                    video =  Videos.fromJson(jsonObject.getJSONObject("video_info"));
                    Log.i(TAG, "has Video Info.")
                    videoURL = Videos.getVideoURL();
                    Log.i(TAG, "Media Model URL: $media.videoURL")
                }
            }
            return media
        }

        @Throws(JSONException::class)
        fun fromJsonArray(jsonArray: JSONArray): List<Media>? {
            val media: MutableList<Media> = ArrayList()
            for (i in 0 until jsonArray.length()) {
                media.add(fromJson(jsonArray.getJSONObject(i)))


            }
            return media
        }

        fun getMediaType(): String? {
            return mediaType
        }


        @JvmName("getMediaURL1")
        fun getMediaURL(): String? {
            return mediaURL
        }

        fun getVideo(): Videos? {
            return video
        }

        @JvmName("getVideoURL1")
        fun getVideoURL(): String? {
            return videoURL
        }


    }




}