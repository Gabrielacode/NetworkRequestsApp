package com.example.networkrequests.data.pagingexample

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.networkrequests.data.sources.remote.Result
import com.example.networkrequests.data.sources.remote.networkmodels.Product
import com.example.networkrequests.data.sources.remote.networkmodels.ProductApiResult
import com.example.networkrequests.data.repository.ProductsRepository
  const val STARTING_SKIP = 0
class ProductPagingSource( val productsRepository: ProductsRepository) :PagingSource<Int, Product>(){
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {

        //This is the key needed to load data for the next paging source if this is invalidated either due to refresh or modification of data
        //We should try to approximate the next key to be loaded
         return state.anchorPosition?.let {
             state.closestPageToPosition(it)?.prevKey?.plus(state.config.initialLoadSize/2)?: state.closestPageToPosition(it)?.nextKey?.minus(state.config.initialLoadSize/2)
         }
        //We then tru to center it since the anchor position is either at the top or the bottom of the recycler view



    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
         val keytoLoadData = params.key?: STARTING_SKIP //This can be null if we want to refresh the data so that is where we write in the get refresh key or no initial key was loaded
          val loadSize = params.loadSize //This is the amount of items we want to load that will be passed to the pager //This will be the limit
          //We then load the data from the products repo
         val resultOfApi = productsRepository.getProductsPaged(loadSize,keytoLoadData)

         //Now we will either get a result or a failure since we have handled it in the network side we dont need to worry about it
        //We also need to check how the api works when we go above the total amont of items if it returns null or emptly list

        return  when(resultOfApi){
             is Result.Success<*> -> {
                 val successResult = resultOfApi.result as ProductApiResult
                 val nextKey = if(successResult.products.isEmpty()) null else resultOfApi.result.skip + loadSize

                 LoadResult.Page(
                     data = (successResult).products,
                     prevKey = if (resultOfApi.result.skip <= STARTING_SKIP) null else resultOfApi.result.skip - loadSize,
                     nextKey = nextKey
                 )
             }
            is Result.Failure-> LoadResult. Error(resultOfApi.e)
         }


    }

}
//This is where paging data comes from
//Here we use our api to fetch the data by pages
//Since we are using the dummy json api we will use it with its paging capacity