package com.app.Finny.Adapters

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.app.Finny.R

class TutorialAdapter(
    private val context: Activity,
    private val imgList: List<Int>
): ArrayAdapter<Int>(context, R.layout.tutorial_img_list, imgList) {
    @SuppressLint("ViewHolder", "InflateParams")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val inflater = context.layoutInflater
        val rowView = inflater.inflate(R.layout.tutorial_img_list, null, true)

//        val img = rowView.findViewById<ImageView>(R.id.imgView)

//        img.setImageResource(imgList[position])

        return rowView
    }
}