package com.app.Finny.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.app.Finny.R

class UserAdapter(
    private val context: Activity,
    private val emailList: List<String>,
    private val nameList: List<String>
): ArrayAdapter<String>(context, R.layout.custom_user_list, emailList) {
    @SuppressLint("ViewHolder", "InflateParams", "SetTextI18n")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.custom_user_list, null, true)

        val email = rowView.findViewById<TextView>(R.id.emailField)
        val scoreText = rowView.findViewById<TextView>(R.id.nameField)

        email.text = emailList[position]
        scoreText.text = nameList[position]

        return rowView
    }
}