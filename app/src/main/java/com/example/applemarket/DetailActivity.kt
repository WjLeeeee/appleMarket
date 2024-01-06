package com.example.applemarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.applemarket.databinding.ActivityDetailBinding
import java.text.DecimalFormat

class DetailActivity : AppCompatActivity() {
    private val binding by lazy { ActivityDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val myFormatter = DecimalFormat("###,###")

        val selected = intent.getParcelableExtra<Product>("data")
        val productIcon = selected?.productIcon
        val productName = selected?.productName
        val productDes = selected?.productDes
        val seller = selected?.seller
        val productPrice = myFormatter.format(selected?.productPrice) + "원"
        val address = selected?.address

        if (productIcon != null) {
            binding.detailImage.setImageResource(productIcon)
        }
        with(binding) {
            detailSeller.text = seller
            detailProductName.text = productName
            detailProductPrice.text = productPrice.toString()
            detailAddress.text = address
            detailProductDes.text = productDes
            detailBack.setOnClickListener {
                finish()
            }
        }


    }
}