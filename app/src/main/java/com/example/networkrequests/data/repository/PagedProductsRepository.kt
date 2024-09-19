package com.example.networkrequests.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.networkrequests.data.sources.remote.networkmodels.Product
import com.example.networkrequests.data.sources.remote.networkmodels.ProductApiResult
import com.example.networkrequests.data.pagingexample.ProductPagingSource
import kotlinx.coroutines.flow.Flow

const val LOAD_SIZE = 10
class PagedProductsRepository(val pagedProductSource : ProductPagingSource) {
    //Now that we have created the paging source we then need to create the data
    //We need to create the Pager which is incharge of pulling chunks of paged data  from the pagersource
    //We create in the data layer since it requires access to the paging
    //The Paging data and the paging source works in pairs  as the Paging Data fetches data from the paging source when needed
    //If the Paging source is invalidated then the Paging Data is also too
    //So for every refresh a new Paging Source and Data Pair will be created
    //So he Paging Data is like a snap shot of the Paging Source
    //So Pages are loaded into the Paging Data
    fun getPagedListOfProducts(): Flow<PagingData<Product>> {
        return Pager(PagingConfig(LOAD_SIZE, enablePlaceholders = false)){pagedProductSource}.flow

    }
}