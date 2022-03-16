package com.codepath.apps.restclienttemplate.models

import android.content.ContentValues.TAG
import android.provider.MediaStore.Video
import android.util.Log
import org.json.JSONException

import org.json.JSONObject

class Entities {
    val TAG = "EntitiesModel"

    var media: List<Media>? = null
    var mediaURL: String? = null
    var mediaType: String? = null
    var videoURL: String? = null
    var video: Videos? = null


    companion object {
        @Throws(JSONException::class)
        fun fromJson(jsonObject: JSONObject): Entities {
            val entities = Entities()
            if (jsonObject.has("media")) {
                val media = Media()
                entities.media = Media.fromJsonArray(jsonObject.getJSONArray("media"))
                Log.i(TAG, entities.media.toString())
                entities.mediaURL =Media.getMediaURL()
                entities.mediaType = Media.getMediaType()
                Log.i(TAG, "entities.mediaURL: " + entities.mediaURL)
                Log.i(TAG, "entities.mediaType: " + entities.mediaType)
                if (entities.mediaType == "video") {
                    entities.video = Media.getVideo()
                    Log.i(TAG, "Type is Video")
                    entities.videoURL = Media.getVideoURL()
                }
            }else{
                Log.i(TAG,"No media")
            }
            return entities
        }
    }
}