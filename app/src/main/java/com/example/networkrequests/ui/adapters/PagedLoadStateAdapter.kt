package com.example.networkrequests.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.networkrequests.databinding.SeperatorItemBinding

class PagedLoadStateAdapter():LoadStateAdapter<PagedLoadStateAdapter.LoadStateViewHolder>() {
    inner class LoadStateViewHolder(val binding:SeperatorItemBinding) :ViewHolder(binding.root){
         fun bind(loadState:LoadState){
             //By default only loading and error are shown
             if (loadState is LoadState.Error){
                 binding.seperatorDescription.text = "Error Loading Data"
             }else{
                 binding.seperatorDescription.text = "Loading Data"
             }
         }
    }

    override fun onBindViewHolder(holder: LoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): LoadStateViewHolder {
        val binding = SeperatorItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  LoadStateViewHolder(binding)
    }
}