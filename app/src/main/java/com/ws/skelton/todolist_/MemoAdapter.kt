package com.ws.skelton.todolist_

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ws.skelton.todolist_.databinding.ItemMemoBinding

class MyAdapter(val context : Context,
                var list: List<MemoEntity>,
                var onFunListener: OnFunListener): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = ItemMemoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return  MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        //list = 1,2,3,
        val memo = list[position]

        holder.memo.text = memo.memo
        holder.del.setOnClickListener(object : View.OnClickListener{
            override fun onClick(p0: View?) {
                onFunListener.onDeleteListener(memo)
            }
        })

//        holder.update.setOnClickListener(object : View.OnClickListener{
//            override fun onClick(p0: View?) {
//                onFunListener.onUpdateListener(memo)
//            }
//        })



//        setOnLongClickListener(object : View.OnLongClickListener {
//            override fun onLongClick(p0: View?): Boolean {
//                onDeleteListener.onDeleteListener(memo)
//                return true
//            }
//        })

    }

    override fun getItemCount(): Int {
        return list.size
    }





    inner class MyViewHolder(val binding: ItemMemoBinding) : RecyclerView.ViewHolder(binding.root){

        val memo = binding.textviewMemo
        val del = binding.deletBtn
//        val update = binding.updateBtn
    }
}