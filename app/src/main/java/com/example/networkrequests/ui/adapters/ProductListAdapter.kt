package com.example.networkrequests.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.networkrequests.data.models.Product
import com.example.networkrequests.databinding.ProductItemLayoutBinding

object ProductDiffUtil: DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
       return  oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
       return  oldItem == newItem
    }

}
class ProductListAdapter:ListAdapter<Product,ProductListAdapter.ProductViewHolder> (ProductDiffUtil){
    inner class ProductViewHolder( val binding:ProductItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind( product:Product){
            binding.priceTv.text = " \$ ${product.price.toString()}"
            binding.titleTv.text = product.title
            //Lets us quicly learn how to use the Glide Library
            Glide.with(binding.thumbnail).load(product.thumbnail).into(binding.thumbnail)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder((binding))
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))

    }
}