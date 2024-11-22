package com.app.Finny.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.app.Finny.R

class LeaderboardAdapter(
    private val context: Activity,
    private val name: List<String>,
    private val score: List<Int>
): ArrayAdapter<String>(context, R.layout.custom_history_list, name) {

    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_leaderboard_list, null, true)

        val dateText = rowView.findViewById<TextView>(R.id.name)
        val scoreText = rowView.findViewById<TextView>(R.id.score)

        dateText.text = name[position]
        scoreText.text = score[position].toString()

        return rowView
    }
}