package com.example.applemarket

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.applemarket.databinding.ActivityDetailBinding
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val myFormatter = DecimalFormat("###,###")

        val selected = intent.getParcelableExtra<Product>("data")
        var productIcon = selected?.productIcon
        val productName = selected?.productName
        val productDes = selected?.productDes
        val seller = selected?.seller
        val price = selected?.productPrice
        val productPrice = myFormatter.format(price) + "원"
        val address = selected?.address
        var productLike = selected?.productLike
        var productLikeIcon = selected?.productLikeIcon
        val productChatting = selected?.productChatting

        if (productIcon != null) {
            binding.detailImage.setImageResource(productIcon)
        }
        with(binding) {
            mannerTemp.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG)
            detailSeller.text = seller
            detailProductName.text = productName
            detailProductPrice.text = productPrice.toString()
            detailAddress.text = address
            detailProductDes.text = productDes
            heartImage.setImageResource(productLikeIcon!!)
            heartImage.setOnClickListener {
                if (productLikeIcon == R.drawable.heart) {
                    //Snackbar 메세지 표시
                    Snackbar.make(view,"관심 목록에 추가되었습니다.",Snackbar.LENGTH_SHORT).show();
                    // 이미지가 heart일 때, pressedheart로 변경하고 productLike + 1
                    heartImage.setImageResource(R.drawable.pressedheart)
                    productLikeIcon = R.drawable.pressedheart
                    productLike = productLike?.plus(1)
                } else {
                    // 이미지가 pressedheart일 때, heart로 변경하고 productLike - 1
                    heartImage.setImageResource(R.drawable.heart)
                    productLikeIcon = R.drawable.heart
                    productLike = productLike?.minus(1)
                }
                // 변경된 데이터를 Intent에 담아서 MainActivity로 전달
                val resultIntent = Intent().apply {
                    putExtra("updatedProduct", Product(productIcon ?: 0, productName ?: "", productDes ?: "",
                        seller ?: "", price ?: 0, address ?: "",
                        productLike ?: 0, productLikeIcon ?: 0, productChatting ?: 0))
                }
                setResult(Activity.RESULT_OK, resultIntent)
            }
            detailBack.setOnClickListener {
                finish()
            }
        }

    }
}