package com.exchange.com.presentation.currency_pairs

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.exchange.com.common.CURRENCY_SYMBOL_KEY
import com.exchange.com.common.Resource
import com.exchange.com.domain.model.CurrencyPairState
import com.exchange.com.domain.use_case.GetCurrencyListByNameUseCase
import com.exchange.com.domain.use_case.GetCurrencyPairsByNameUseCase
import com.exchange.com.domain.use_case.GetCurrencyPairsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class CurrencyPairViewModel @Inject constructor(
    val getCurrencyPairsUseCase: GetCurrencyPairsUseCase,
    val getCurrencyPairsByNameUseCase: GetCurrencyPairsByNameUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val _stateFlow = MutableStateFlow<CurrencyPairState?>(null)
    val stateFlow = _stateFlow.asStateFlow().filterNotNull()

    init {
        savedStateHandle.get<String>(CURRENCY_SYMBOL_KEY)?.let { getList(it) }
    }

    fun getList(currencySymbol: String) {
        getCurrencyPairsUseCase(currencySymbol).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _stateFlow.value =
                        CurrencyPairState(error = result.message ?: "some error")
                }
                is Resource.Loading -> {
                    _stateFlow.value = CurrencyPairState(true)
                }
                is Resource.Success -> {
                    _stateFlow.value =
                        CurrencyPairState(currencyPairList = result.data ?: emptyList())
                }
            }
        }.launchIn(viewModelScope)
    }

    fun findPairs(firstSymbol: String, secondSymbol:String){
        getCurrencyPairsByNameUseCase(firstSymbol,secondSymbol).onEach { result ->
            when (result) {
                is Resource.Error -> {
                    _stateFlow.value =
                        CurrencyPairState(error = result.message ?: "some error")
                }
                is Resource.Loading -> {
                    _stateFlow.value = CurrencyPairState(true)
                }
                is Resource.Success -> {
                    _stateFlow.value =
                        CurrencyPairState(currencyPairList = result.data ?: emptyList())
                }
            }
        }.launchIn(viewModelScope)
    }
}