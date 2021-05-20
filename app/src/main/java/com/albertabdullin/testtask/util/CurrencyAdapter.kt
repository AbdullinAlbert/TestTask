package com.albertabdullin.testtask.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.albertabdullin.testtask.R
import com.albertabdullin.testtask.database.CurrencyEntity
import com.albertabdullin.testtask.databinding.AdapterItemBinding

class CurrencyAdapter : ListAdapter<CurrencyEntity, CurrencyAdapter.CurrencyViewHolder>(CurrencyDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        return CurrencyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val item = getItem(position)
        val indicator = if (position == currentList.lastIndex) 0
        else {
            val helperItem = getItem(position + 1)
            item.value.compareTo(helperItem.value)
        }
        holder.bind(item, indicator)
    }

    class CurrencyViewHolder private constructor(private val binding: AdapterItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun from(parent: ViewGroup) : CurrencyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = AdapterItemBinding.inflate(layoutInflater, parent, false)
                return CurrencyViewHolder(binding)
            }
        }

        fun bind(item: CurrencyEntity, indicator: Int) {
            binding.dateTextView.text = item.date
            binding.currencyTextView.text = getStringViewOfCurrency(item.value)
            binding.indicatorImageView.setImageResource(when (indicator) {
                0 -> R.drawable.ic_baseline_arrow_downward_opacity_0_24
                1 -> R.drawable.ic_baseline_arrow_upward_24
                else -> R.drawable.ic_baseline_arrow_downward_24
            }
            )
        }

    }

}

class CurrencyDiffCallback: DiffUtil.ItemCallback<CurrencyEntity>() {

    override fun areItemsTheSame(oldItem: CurrencyEntity, newItem: CurrencyEntity): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CurrencyEntity, newItem: CurrencyEntity): Boolean {
        return oldItem == newItem
    }

}

