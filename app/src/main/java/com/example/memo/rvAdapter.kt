package com.example.memo

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memo.databinding.RvItemBinding

class rvAdapter(val context : Context,val data : ArrayList<rvModel>,val listener : OnItemClickListener)
    :RecyclerView.Adapter<rvAdapter.myViewHolder>()
{
    interface OnItemClickListener{
        fun onDelete(dataList : ArrayList<rvModel>,position: Int)
        fun onUpdate(dataList : ArrayList<rvModel>,position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): rvAdapter.myViewHolder {
        val binding = RvItemBinding.inflate(LayoutInflater.from(context),parent,false)
        return myViewHolder(binding)
    }

    override fun onBindViewHolder(holder: rvAdapter.myViewHolder, position: Int) {
        holder.binding.tvTitleR.text = data[position].title
        holder.binding.tvDescriptionR.text = data[position].content
    }

    override fun getItemCount(): Int {
        return data.count()
    }
    inner class myViewHolder(val binding: RvItemBinding):RecyclerView.ViewHolder(binding.root),
        View.OnClickListener{
        init {
            binding.btnDelete.setOnClickListener(this)
            binding.btnUpdate.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            if(v!=null){
                val pos = adapterPosition
                if(v.id==R.id.btnDelete){
                    listener.onDelete(data,pos)
                }
                else if(v.id==R.id.btnUpdate){
                    listener.onUpdate(data,pos)
                }
            }
        }

    }
}