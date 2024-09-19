package com.example.networkrequests.ui.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.networkrequests.R
import com.example.networkrequests.data.sources.database.entities.pageddbmodels.PagedItem
import com.example.networkrequests.data.sources.database.entities.pageddbmodels.SimplifiedDummyItem
import com.example.networkrequests.data.sources.remote.networkmodels.Product
import com.example.networkrequests.databinding.PagedItemCardBinding
import com.example.networkrequests.databinding.ProductItemLayoutBinding
import com.example.networkrequests.databinding.SeperatorItemBinding
import com.example.networkrequests.domain.model.DummyModel
import com.example.networkrequests.ui.viewmodels.UserModel

val SimplifiedDummyItemDiffUtil = object : DiffUtil.ItemCallback<UserModel>(){
    override fun areItemsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
        val isSameSeperator =
            oldItem is UserModel.SeperatorModel && newItem is UserModel.SeperatorModel && oldItem.description == newItem.description
        val isSameProduct =
            oldItem  is UserModel.UIModel && newItem is UserModel.UIModel && oldItem.data.id == oldItem.data.id

      return  isSameProduct || isSameSeperator
    }


    override fun areContentsTheSame(oldItem: UserModel, newItem: UserModel): Boolean {
         return oldItem == newItem
    }

}
class BothCacheAndNetworkAdapter() :PagingDataAdapter<UserModel,RecyclerView.ViewHolder>(SimplifiedDummyItemDiffUtil){

    inner class ProductViewHolder( val binding : ProductItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bind (value: UserModel.UIModel?){
            //Here the values can be null here if it is placeholder
            if(value == null){
                return
            }
            Log.i("DummyItem",value.data.id.toString())
            binding.titleTv.text = value.data.title
            binding.priceTv.text = value.data.description.toString()
            //Load the image into the Image View using Glide
            Glide.with(binding.thumbnail).load(value.data.thumbnailUrl).placeholder(R.drawable.ic_launcher_background).into(binding.thumbnail)

        }}
    inner class SeperatorViewHolder(val binding: SeperatorItemBinding):RecyclerView.ViewHolder(binding.root){
       fun bind(value:UserModel.SeperatorModel?){
           binding.seperatorDescription.text = value?.description
       }
    }

    override fun getItemViewType(position: Int): Int {
        return when(peek(position)){
            is UserModel.UIModel ->{1}
            is UserModel.SeperatorModel->{2}
            null -> throw IllegalStateException("Unknown View")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when(holder){
            is ProductViewHolder -> holder.bind(item as UserModel.UIModel)
            is SeperatorViewHolder ->holder.bind(item as UserModel.SeperatorModel)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
       return when(viewType){
            1 -> ProductViewHolder(ProductItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        else ->{
            SeperatorViewHolder(SeperatorItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
        }
       }
    }
//In the recycler view adapter we create two view holders one for the Item and one for theSeperator


}