package com.example.networkrequests.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.networkrequests.data.ProductsRepository
import com.example.networkrequests.data.Result
import com.example.networkrequests.data.models.Product
import com.example.networkrequests.data.models.ProductApiResult
import com.example.networkrequests.data.remote.RetrofitInstance
import com.example.networkrequests.databinding.ProductLayoutBinding
import com.example.networkrequests.ui.adapters.ProductListAdapter
import com.example.networkrequests.ui.viewmodels.ProductViewModel
import com.example.networkrequests.ui.viewmodels.ProductViewModelFactory
import com.example.networkrequests.ui.viewmodels.ProductsListUiState
import kotlinx.coroutines.launch

class ProductsFragment: Fragment() {
    lateinit var  binding:ProductLayoutBinding
    val viewModel : ProductViewModel by viewModels<ProductViewModel> {
        ProductViewModelFactory(com.example.networkrequests.data.repository.ProductsRepositoryImpl(RetrofitInstance.productApiService))
    }
    var listadapter = ProductListAdapter()
   lateinit var listOfProducs:List<Product>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Then we  call the viewmodel function
        viewModel.getProducts()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ProductLayoutBinding.inflate(inflater,container,false)
        return binding.root



    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.productsList.apply {
            adapter = listadapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        //We monitor the flow
          viewLifecycleOwner.lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.currentProductsListUiState.collect{
                    when(it){
                       is ProductsListUiState.OnError -> {
                           Toast.makeText(requireContext(),it.message,Toast.LENGTH_SHORT).show()
                       }
                       is ProductsListUiState.isLoading ->{}
                       is ProductsListUiState.onSuccess<*> -> {
                           (it.data as List<Product>).also { listOfProducs = it }
                           listadapter.submitList(listOfProducs)
                       }


                    }
                }
            }

        }

    }
}
/*
* Now we have made a http request to dummy Json using OkHttp and Kotlin x Serialization
* Lets now learn Retrofit which is built on top of OkHttp
*  Here the ur http requests and so on into one single interface
* So all are our request are specified by fuctions in the interface using  annotations
*It is type safe
* In the interface methods we specify the http method ,the path we want to access staticaly and dynamically , insert headers for the request
* both dynamically and statically , queries , add body , form data,multipart
* By default this interface methods return a call(OkHttp Call) which we can execute synchrochronously or asynchronously
* If we are using Kotlin we can  put suspend in them to make them asynchronous
* That is prettym  much it then we set up the instance of Retrofit using its builder and then get the
* tell retrofit to create the instance of the service interface
* We can also specify a custom okhttp client for retrofit which may include all the logging , interceptors , timeouts etc just like we did with
* OkHttp Builder
* By default retrofit returns a call of type ResponseBody
*
* //NOTE if we put withContext(NonCancellable){}
* It is makes the coroutine not cancel until that piece of code is completed
* When working with blocking functions in a  suspend function we need to ensure to use check active to ensure cancellation is cooperative
*if u want to create a custom coroutine scope in a  component , add Supervisor Job to ensure the cancellation of one child scope doesn't affect the parent scope
* Avoid calling suspend functions  in the finnaly block as the corotine is cancelled all suspend functions are cancelled
* *  */
