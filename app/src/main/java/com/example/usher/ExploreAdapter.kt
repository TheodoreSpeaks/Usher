package com.example.usher

import android.app.Activity
import android.app.ActivityOptions
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

import android.util.Pair as UtilPair


const val TITLE_INTENT = "title_message"
const val DESCRIPTION_INTENT = "desc_message"


class ExploreAdapter (private val activity: Activity):


    RecyclerView.Adapter<ExploreAdapter.RecyclerViewHolder>() {

    private val dataSet: List<Event> = generateEvents()

    data class Event (val title: String, val description: String, val image: Int)

    class RecyclerViewHolder(val view: MaterialCardView): RecyclerView.ViewHolder(view)

    private fun generateEvents(): List<Event> {
        val res = activity.resources
        val size = res.getStringArray(R.array.explore_show_titles).size
        val toReturn: MutableList<Event> = mutableListOf()

        val titles = res.getStringArray(R.array.explore_show_titles)
        val descriptions = res.getStringArray(R.array.explore_show_descriptions)
        val imageIds = res.getIntArray(R.array.explore_image_ids)

        for (i in 0 until size - 1) {
            val title = titles[i]
            val description = descriptions[i]
            val image = imageIds[i]
            toReturn.add(Event(title, description, image))
        }

        return toReturn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        val cardView = LayoutInflater.from(parent.context)
            .inflate(R.layout.explore_card, parent, false) as MaterialCardView

        return RecyclerViewHolder(cardView)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {

        val text: TextView = holder.view.findViewById(R.id.big_card_text_title)
        val image: ImageView = holder.view.findViewById(R.id.image_bigcard)
        val event = dataSet[position]
        text.text = event.title
        image.setImageResource(R.drawable.bizhiki)


        holder.view.setOnClickListener {
            val context = it.context
            val intent = Intent(context, InfoActivity::class.java)
            intent.putExtra(TITLE_INTENT, event.title)
            intent.putExtra(DESCRIPTION_INTENT, event.description)

            val options = ActivityOptions.makeSceneTransitionAnimation(
                    activity,
                    UtilPair.create(image as View, "image"),
                    UtilPair.create(text as View, "text"))

            context.startActivity(intent, options.toBundle())
        }
    }
}