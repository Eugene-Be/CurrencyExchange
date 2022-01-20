package com.exchange.com.presentation.currency_pairs

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.exchange.com.R
import com.exchange.com.common.CURRENCY_SYMBOL_KEY
import com.exchange.com.databinding.FragmentCurrencyPairsBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CurrencyPairsFragment : Fragment() {
    private var _binding: FragmentCurrencyPairsBinding? = null
    private val binding get() = _binding!!

    private var pairsAdapter: CurrencyPairsAdapter? = null
    private var currencySymbol: String? = null
    private val viewModel: CurrencyPairViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            currencySymbol = it.getString(CURRENCY_SYMBOL_KEY)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyPairsBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupRecycler()

        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest {
                binding.progress.visibility = if (it.isLoading) View.VISIBLE else View.GONE

                pairsAdapter?.currencyExchangePairList = it.currencyPairList
                pairsAdapter?.notifyDataSetChanged()
            }
        }
    }

    private fun setupRecycler() {
        pairsAdapter = CurrencyPairsAdapter()

        binding.recyclerPairs.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = pairsAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        (menu.findItem(R.id.find_action).actionView as SearchView).setOnQueryTextListener(
            getOnQueryTextListener()
        )
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun getOnQueryTextListener() = object :
        SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            query?.let {
                viewModel.findPairs(currencySymbol!!,it)
                return true
            }
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.length?.let {
                currencySymbol?.let { it1 -> viewModel.findPairs(it1,newText) }
                return true
            }
            return false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String) =
            CurrencyPairsFragment().apply {
                arguments = Bundle().apply {
                    putString(CURRENCY_SYMBOL_KEY, param1)
                }
            }
    }
}