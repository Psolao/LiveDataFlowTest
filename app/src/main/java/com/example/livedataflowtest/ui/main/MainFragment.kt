package com.example.livedataflowtest.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.livedataflowtest.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var messageText:TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        val view =  inflater.inflate(R.layout.main_fragment, container, false)
        view.findViewById<Button>(R.id.livedata_button).setOnClickListener{viewModel.postLiveData()}
        view.findViewById<Button>(R.id.stateflow_button).setOnClickListener{viewModel.postStateFlow()}
        view.findViewById<Button>(R.id.sharedflow_button).setOnClickListener{viewModel.postSharedFlow()}
        messageText = view.findViewById<TextView>(R.id.message)
        viewModel.liveData.observe(viewLifecycleOwner){
            addData("LiveData", it)
        }
        lifecycleScope.launch {
             repeatOnLifecycle(Lifecycle.State.STARTED) {
                 viewModel.sharedFlow.collect { data ->
                     withContext(Dispatchers.Main){
                         addData("SharedFlow", data)
                     }
                 }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.stateFlow.collect { data ->
                    withContext(Dispatchers.Main){
                        addData("StateFlow", data)
                    }
                }
            }
        }

        return view
    }

    fun addData(type:String, data:Int){
        messageText.text="${messageText.text}\n ${type} ${data}"
    }

}