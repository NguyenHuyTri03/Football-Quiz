package com.app.Finny.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import com.app.Finny.Activities.Login
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
        var exitDialog: AlertDialog

        val idTxt = rowView.findViewById<TextView>(R.id.questionID)
        val questionTxt = rowView.findViewById<TextView>(R.id.questionText)
//        val editBtn = rowView.findViewById<Button>(R.id.editBtn)
//        val delBtn = rowView.findViewById<Button>(R.id.deleteBtn)

        idTxt.text = id[position]
        questionTxt.text = question[position]

//        // Create a an logout confirmation popup
//        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
//        builder
//            .setTitle("Logout?")
//            // Return the user to the home screen
//            .setPositiveButton("Yes") { _, _ ->
//                auth.signOut()
//                val intent = Intent(this, Login::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                startActivity(intent)
//                finish()
//            }
//            .setNegativeButton("No") { _, _ ->
//                exitDialog.cancel()
//            }
//        exitDialog = builder.create()


        return rowView
    }
}