package com.example.myproject.ui.products

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myproject.data.model.ProductGroup
import com.example.myproject.databinding.ItemGroupBinding

class GroupsAdapter(
    private val onGroupClick: (ProductGroup) -> Unit
) : ListAdapter<ProductGroup, GroupsAdapter.GroupViewHolder>(GroupDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupViewHolder {
        val binding = ItemGroupBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return GroupViewHolder(binding)
    }

    override fun onBindViewHolder(holder: GroupViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class GroupViewHolder(
        private val binding: ItemGroupBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onGroupClick(getItem(position))
                }
            }
        }

        fun bind(group: ProductGroup) {
            binding.apply {
                textGroupName.text = group.name
                groupImage.setBackgroundResource(group.imageResId)
            }
        }
    }

    private class GroupDiffCallback : DiffUtil.ItemCallback<ProductGroup>() {
        override fun areItemsTheSame(oldItem: ProductGroup, newItem: ProductGroup): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProductGroup, newItem: ProductGroup): Boolean {
            return oldItem == newItem
        }
    }
} 