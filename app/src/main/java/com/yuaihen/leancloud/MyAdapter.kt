package com.yuaihen.leancloud

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.leancloud.AVObject
import kotlinx.android.synthetic.main.view_holder.view.*

/**
 * Created by Yuaihen.
 * on 2021/2/26
 */
class MyAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var _dataList = listOf<AVObject>()
    fun updateDataList(dataList: List<AVObject>) {
        _dataList = dataList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.view_holder, parent, false)
        ) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val ob = _dataList[position]
        holder.itemView.contentText.text = ob.get("word")?.toString()
        holder.itemView.timeText.text = ob.get("createdAt")?.toString()

    }

    override fun getItemCount() = _dataList.size
}