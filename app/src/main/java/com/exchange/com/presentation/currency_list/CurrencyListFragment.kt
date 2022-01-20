package com.exchange.com.presentation.currency_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.exchange.com.R
import com.exchange.com.common.CURRENCY_SYMBOL_KEY
import com.exchange.com.databinding.FragmentCurrencyListBinding
import com.exchange.com.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class CurrencyListFragment : Fragment(), CurrencyItemClickListener {
    private var _binding: FragmentCurrencyListBinding? = null
    private val binding get() = _binding!!
    var find: String? = null

    private var adapterCurrency: CurrencyListAdapter? = null

    private val viewModel: CurrencyListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrencyListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapterCurrency = CurrencyListAdapter(currencyItemClickListener = this)

        binding.recyclerCurrency.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = adapterCurrency
        }

        lifecycleScope.launchWhenStarted {
            viewModel.stateFlow.collectLatest { listState ->
                binding.progress.visibility = if (listState.isLoading) View.VISIBLE else View.GONE

                adapterCurrency?.currencyList = listState.currencyList
                adapterCurrency?.notifyDataSetChanged()
            }
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
                find = query
                viewModel.getListByName(it)
                return true
            }
            find = null
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            newText?.let {
                viewModel.getListByName(newText)
                return true
            }
            return false
        }
    }

    override fun currencyClicked(pos: String) {
        findNavController().navigate(
            R.id.currencyPairsFragment,
            Bundle().apply { putString(CURRENCY_SYMBOL_KEY, pos) })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = CurrencyListFragment()
    }
}