package com.example.networkrequests.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.networkrequests.data.sources.database.entities.pageddbmodels.PagedItem
import com.example.networkrequests.databinding.PagedItemCardBinding
val PagedItemDiffUtil = object :DiffUtil.ItemCallback<PagedItem>(){
    override fun areItemsTheSame(oldItem: PagedItem, newItem: PagedItem): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: PagedItem, newItem: PagedItem): Boolean {
        return oldItem == newItem
    }

}
class PagedItemListAdapter():PagingDataAdapter<PagedItem,PagedItemListAdapter.PagedItemViewHolder> (
    PagedItemDiffUtil){
    inner class PagedItemViewHolder(val binding:PagedItemCardBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(item: PagedItem?){
            item?.let {
                binding.apply{
                    titleTv.text =  it.title
                    descriptionTv.text = it.description
                }
            }
        }
    }

    override fun onBindViewHolder(holder: PagedItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagedItemViewHolder {
        val binding = PagedItemCardBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PagedItemViewHolder(binding)
    }
}