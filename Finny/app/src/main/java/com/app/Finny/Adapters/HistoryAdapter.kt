package com.app.Finny.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.app.Finny.R

class HistoryAdapter(
    private val context: Activity,
    private val date: List<String>,
    private val difficulty: List<String>,
    private val score: List<Int>
): ArrayAdapter<String>(context, R.layout.custom_history_list, date) {
    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_history_list, null, true)

        val dateText = rowView.findViewById<TextView>(R.id.date)
        val scoreText = rowView.findViewById<TextView>(R.id.score)
        val difficultyText = rowView.findViewById<TextView>(R.id.difficulty)

        dateText.text = date[position]
        scoreText.text = score[position].toString()
        difficultyText.text = difficulty[position]

        return rowView
    }
}