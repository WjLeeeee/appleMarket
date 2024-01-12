package com.example.applemarket

import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.applemarket.databinding.ItemRecyclerviewBinding
import java.text.DecimalFormat

class MyAdapter(val mItems: MutableList<Product>) : RecyclerView.Adapter<MyAdapter.Holder>() {
    private lateinit var resultLauncher: ActivityResultLauncher<Intent>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecyclerviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.itemView.setOnClickListener {  //클릭이벤트추가부분
            val intent = Intent(holder.itemView.context, DetailActivity::class.java).apply {
                putExtra(Constants.ITEM_DATA, mItems[position])
            }
            resultLauncher.launch(intent)
        }
        //상품 롱클릭시 삭제
        holder.itemView.setOnLongClickListener {
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setTitle("상품 삭제")
            builder.setMessage("상품을 정말로 삭제하시겠습니까?")
            builder.setIcon(R.drawable.chat)
            builder.setCancelable(false)

            val listener = object : DialogInterface.OnClickListener {
                override fun onClick(p0: DialogInterface?, p1: Int) {
                    val adapterPosition = holder.adapterPosition
                    when (p1) {
                        DialogInterface.BUTTON_POSITIVE -> {
                            // 확인 버튼을 눌렀을 때
                            mItems.removeAt(adapterPosition)
                            notifyItemRemoved(adapterPosition)
                        }

                        DialogInterface.BUTTON_NEGATIVE -> {
                            // 취소 버튼을 눌렀을 때
                            p0?.dismiss()
                        }
                    }
                }
            }

            builder.setPositiveButton("확인", listener)
            builder.setNegativeButton("취소", listener)

            builder.show()
            true
        }
        val myFormatter = DecimalFormat("###,###")

        holder.iconImageView.clipToOutline = true
        holder.iconImageView.setImageResource(mItems[position].productIcon)
        holder.name.text = mItems[position].productName
        holder.address.text = mItems[position].address
        holder.price.text = myFormatter.format(mItems[position].productPrice).toString() + "원"
        holder.chat.text = mItems[position].productChatting.toString()
        holder.heart.text = mItems[position].productLike.toString()
        holder.heartImg.setImageResource(mItems[position].productLikeIcon)
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
        val heartImg = binding.heartItemImg
    }

    fun setResult(resultLauncher: ActivityResultLauncher<Intent>) {
        this.resultLauncher = resultLauncher
    }
}