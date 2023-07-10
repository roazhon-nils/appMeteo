package com.example.sae4

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class TaskAdapter(context: Context, items: List<String>) : ArrayAdapter<String>(
    context, 0, items
) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return super.getView(position, convertView, parent)
    }
}