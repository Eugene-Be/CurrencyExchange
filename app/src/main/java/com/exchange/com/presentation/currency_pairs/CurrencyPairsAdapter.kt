package com.exchange.com.presentation.currency_pairs

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.exchange.com.R
import com.exchange.com.databinding.PairItemBinding
import com.exchange.com.domain.model.ExchangePair
import com.exchange.com.domain.model.FluctuationDirection

class CurrencyPairsAdapter(var currencyExchangePairList: List<ExchangePair> = emptyList()) :
    RecyclerView.Adapter<CurrencyPairsAdapter.PairItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PairItemHolder {
        val binding =
            PairItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PairItemHolder(binding)
    }

    override fun onBindViewHolder(holder: PairItemHolder, position: Int) {
        with(holder.binding) {
            val title =
                "${currencyExchangePairList[position].base}/" +
                        currencyExchangePairList[position].secondCurrency

            firstCurrencySymbol.text = title

            currencyExchangePairList[position].let {
                exchangeRate.text = it.exchangeRate
                val fluctuationText = "${it.fluctuation}%"
                fluctuation.text = fluctuationText
                when (it.direction) {
                    FluctuationDirection.UP -> arrowFluctuation.setImageResource(R.drawable.ic_baseline_arrow_drop_up_24)
                    FluctuationDirection.DOWN -> arrowFluctuation.setImageResource(R.drawable.ic_baseline_arrow_drop_down_24)
                    FluctuationDirection.NONE -> {}
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return currencyExchangePairList.size
    }

    class PairItemHolder(val binding: PairItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}