package com.example.networkrequests.ui.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.networkrequests.R
import com.example.networkrequests.data.sources.remote.networkmodels.Product
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
        fun bind( product: Product){
            binding.priceTv.text = " \$ ${product.price.toString()}"
            binding.titleTv.text = product.title
            //Lets us quicly learn how to use the Glide Library
          //  Glide.with(binding.thumbnail).load(product.thumbnail).into(binding.thumbnail)
            //We know how to simply load an image in Glide
            //We can also cancel loads -> Images that has been loaded  by calling Glide. with (Context).cancel on the image,
            //Although Glide automatically recycles the resources and clears them when the context ,activity or fragment is destroyed
            //Glide  offer  options that can be applied to requests
            //They can directly be appllied to an request
          //  Glide.with(binding.thumbnail).load(product.thumbnail).placeholder(R.drawable.ic_launcher_background).into(binding.thumbnail)
            //We can also create a general one and apply it to all the image views
            val requestOptions = RequestOptions().placeholder(R.drawable.ic_launcher_background).fitCenter()
            Glide.with(binding.thumbnail).load(product.thumbnail).apply(requestOptions).into(binding.thumbnail)
            //This behaviour is also the same for image views in a recycler view
            //We can load this url images into non view targest like drawables  by creating custom targets
            //We can learn them at the tutorial page


            binding.root.setOnClickListener {
               binding.root.findNavController().navigate(R.id.action_productsFragment_to_productItemFragment,
                   Bundle().apply {
                       putInt("Id",product.id)
                   })
            }

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