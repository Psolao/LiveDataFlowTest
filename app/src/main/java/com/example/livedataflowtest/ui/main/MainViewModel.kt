package com.example.livedataflowtest.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
   private var counter = 0

   private val _liveData = MutableLiveData<Int>()
   val liveData: LiveData<Int> get()=_liveData

   private val _sharedFlow = MutableSharedFlow<Int>()
   val sharedFlow = _sharedFlow.asSharedFlow()

   private val _stateFlow = MutableStateFlow<Int>(counter)
   val stateFlow = _stateFlow.asSharedFlow()

   fun postLiveData(){
      _liveData.value = ++counter
   }

   fun postSharedFlow(){
      viewModelScope.launch { _sharedFlow.emit(++counter) }
   }

   fun postStateFlow(){
      _stateFlow.value =++counter
   }

}