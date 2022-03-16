package com.codepath.apps.restclienttemplate
import android.text.method.TextKeyListener.clear
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.codepath.apps.restclienttemplate.models.Tweet

class TweetsAdapter(val tweets: ArrayList<Tweet>): RecyclerView.Adapter<TweetsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)

        val view = inflater.inflate(R.layout.item_tweet, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val tweet: Tweet = tweets.get(position)
        holder.tvUsername.text = tweet.user?.name
        holder.tvTweetBody.text = tweet.body
        holder.tvCreatedAt.text = tweet.getFormattedTimestamp(tweet.createdAt)
//        Log.i("Caren", tweet.body)
        Glide.with(holder.itemView).load(tweet.user?.publicUrl).transform( RoundedCorners(30)).into(holder.ivProfileImage)

        if (tweet.entities != null) {
            if (tweet.entities!!.mediaURL != null) {
//                Log.i("Caren", tweet.entities!!.mediaURL)
                holder.ivTweetimage.setVisibility(View.VISIBLE);
                Glide.with(holder.itemView).load(tweet.entities!!.mediaURL).fitCenter().transform( RoundedCorners(20)).into(holder.ivTweetimage)
            }else {
                Glide.with(holder.itemView).clear(holder.ivTweetimage);
                holder.ivTweetimage.visibility = View.GONE
            }
        }
    }


        override fun getItemCount(): Int {
            return tweets.size
        }

        fun clear() {
            tweets.clear()
            notifyDataSetChanged()
        }

        // Add a list of items -- change to type used
        fun addAll(tweetList: List<Tweet>) {
            tweets.addAll(tweetList)
            notifyDataSetChanged()
        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            val ivProfileImage = itemView.findViewById<ImageView>(R.id.ivProfileImage)
            val tvUsername = itemView.findViewById<TextView>(R.id.tvUsername)
            val tvTweetBody = itemView.findViewById<TextView>(R.id.tvTweetBody)
            val ivTweetimage = itemView.findViewById<ImageView>(R.id.ivTweetImage)
            val tvCreatedAt = itemView.findViewById<TextView>(R.id.tvCreatedAt)
        }


}