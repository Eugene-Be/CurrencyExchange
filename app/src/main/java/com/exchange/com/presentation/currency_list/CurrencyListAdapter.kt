package com.exchange.com.presentation.currency_list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exchange.com.databinding.CurrencyItemBinding
import com.exchange.com.domain.model.CurrencyRate

class CurrencyListAdapter(
    var currencyList: List<CurrencyRate> = emptyList(),
    val currencyItemClickListener: CurrencyItemClickListener
) : RecyclerView.Adapter<CurrencyListAdapter.CurrencyItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyItemHolder {
        val binding =
            CurrencyItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CurrencyItemHolder(binding)
    }

    override fun onBindViewHolder(holder: CurrencyItemHolder, position: Int) {
        holder.binding.root.setOnClickListener(holder)
        holder.binding.currencySymbol.text = currencyList[position].symbol
    }

    override fun getItemCount(): Int {
        return currencyList.size
    }

    inner class CurrencyItemHolder(
        val binding: CurrencyItemBinding
    ) : RecyclerView.ViewHolder(binding.root), View.OnClickListener {

        override fun onClick(v: View?) {
            currencyItemClickListener.currencyClicked(currencyList[layoutPosition].symbol)
        }
    }
}

interface CurrencyItemClickListener {
    fun currencyClicked(pos: String)
}
