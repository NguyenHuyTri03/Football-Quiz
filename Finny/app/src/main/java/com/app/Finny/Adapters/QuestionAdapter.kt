package com.app.Finny.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.app.Finny.R

class QuestionAdapter(
    private val context: Activity,
    private val id: List<String>,
    private val question: List<String>
): ArrayAdapter<String>(context, R.layout.custom_question_list, id) {
    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_question_list, null, true)

        val idTxt = rowView.findViewById<TextView>(R.id.questionID)
        val questionTxt = rowView.findViewById<TextView>(R.id.questionText)

        idTxt.text = id[position]
        questionTxt.text = question[position]

        return rowView
    }
}