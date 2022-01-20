package com.exchange.com.presentation.currency_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exchange.com.common.Resource
import com.exchange.com.domain.model.CurrencyListState
import com.exchange.com.domain.use_case.GetCurrencyListByNameUseCase
import com.exchange.com.domain.use_case.GetCurrencyListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CurrencyListViewModel @Inject constructor(
    val getCurrencyListUseCase: GetCurrencyListUseCase,
    val getCurrencyListByNameUseCase: GetCurrencyListByNameUseCase,
) : ViewModel() {

    private val _stateFlow = MutableStateFlow<CurrencyListState?>(null)
    val stateFlow = _stateFlow.asStateFlow().filterNotNull()

    init {
        getList()
    }

    private fun getList() {
        getCurrencyListUseCase().onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _stateFlow.value = CurrencyListState(error = result.message ?: "some error")
                }
                is Resource.Loading -> {
                    _stateFlow.value = CurrencyListState(true)
                }
                is Resource.Success -> {
                    _stateFlow.value = CurrencyListState(currencyList = result.data ?: emptyList())
                }
            }
        }.launchIn(viewModelScope)
    }

    fun getListByName(currencySymbol: String) {
        getCurrencyListByNameUseCase(currencySymbol).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _stateFlow.value = CurrencyListState(error = result.message ?: "some error")
                }
                is Resource.Loading -> {
                    _stateFlow.value = CurrencyListState(true)
                }
                is Resource.Success -> {
                    _stateFlow.value = CurrencyListState(currencyList = result.data ?: emptyList())
                }
            }
        }.launchIn(viewModelScope)
    }
}