package com.example.networkrequests.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.example.networkrequests.R
import com.example.networkrequests.data.sources.remote.networkmodels.Product
import com.example.networkrequests.databinding.ProductItemLayoutBinding
//This is similar to a List Adapter but it uses PagingData thevalues can be null
object PagedProductDiffUtils: DiffUtil.ItemCallback<Product>() {
    override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
       return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
        return  oldItem == newItem
    }

}
class PagedProductListAdapter(): PagingDataAdapter<Product,PagedProductListAdapter.ProductViewHolder>(PagedProductDiffUtils){
    inner class ProductViewHolder( val binding :ProductItemLayoutBinding):ViewHolder(binding.root){
      fun bind (value: Product?){
      //Here the values can be null here if it is placeholder
      if(value == null){
          return
      }
          binding.titleTv.text = value.title
          binding.priceTv.text = value.price.toString()
          //Load the image into the Image View using Glide
          Glide.with(binding.thumbnail).load(value.thumbnail).placeholder(R.drawable.ic_launcher_background).into(binding.thumbnail)

    }}

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
         val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
       val binding = ProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ProductViewHolder(binding)
    }
}