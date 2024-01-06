package com.example.applemarket

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ItemRecyclerviewBinding
import java.text.DecimalFormat

class MyAdapter(val mItems: List<Product>) : RecyclerView.Adapter<MyAdapter.Holder>() {

    interface ItemClick {
        fun onClick(view : View, position : Int)
    }

    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {  //클릭이벤트추가부분
            val intent = Intent(holder.itemView.context, DetailActivity::class.java).apply {
                putExtra("data", mItems[position])
            }
            holder.itemView.context.startActivity(intent)
        }
        val myFormatter = DecimalFormat("###,###")

        holder.iconImageView.clipToOutline = true
        holder.iconImageView.setImageResource(mItems[position].productIcon)
        holder.name.text = mItems[position].productName
        holder.address.text = mItems[position].address
        holder.price.text = myFormatter.format(mItems[position].productPrice).toString() + "원"
        holder.chat.text = mItems[position].productChatting.toString()
        holder.heart.text = mItems[position].productLike.toString()
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemCount(): Int {
        return mItems.size
    }

    inner class Holder(val binding: ItemRecyclerviewBinding) : RecyclerView.ViewHolder(binding.root) {
        val iconImageView = binding.iconItem
        val name = binding.nameItem
        val address = binding.addressItem
        val price = binding.priceItem
        val chat = binding.chatItem
        val heart = binding.heartItem
    }
}