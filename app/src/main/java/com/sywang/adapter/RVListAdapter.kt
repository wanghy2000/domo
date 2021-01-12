package com.sywang.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sywang.R
import com.sywang.model.MediaFileData

class RVListAdapter (callback: Callback): RecyclerView.Adapter<RVListAdapter.ViewHolder>(){

    var nCallback = callback
    private val fileList: MutableList<MediaFileData> = mutableListOf()

    inner class ViewHolder internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var title: TextView = itemView.findViewById(R.id.sy_name)
        var button: Button = itemView.findViewById(R.id.sy_button)
    }

    interface Callback {
        fun onClick(data: MediaFileData)
    }

    fun setFileList(newList: List<MediaFileData>) {
        synchronized(fileList) {
            fileList.clear()
            fileList.addAll(newList)
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val view = inflater.inflate(R.layout.rvitem_list, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = fileList[position]

        holder.title.text = data.displayName
        holder.button.setOnClickListener {
            nCallback.onClick(data)
        }
    }

    override fun getItemCount(): Int {
        return fileList.size
    }
}