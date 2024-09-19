package com.example.networkrequests.data.pagingexample

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.example.networkrequests.data.sources.database.db.SimpleDummyDatabase
import com.example.networkrequests.data.sources.database.entities.pageddbmodels.SimplifiedDummyItem
import com.example.networkrequests.data.sources.mappers.toSimpleDummyItem
import com.example.networkrequests.data.sources.remote.services.DummyJsonApi
import com.example.networkrequests.data.sources.remote.services.SortOrder
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class PagedExampleRemoteMediator(
    //Here we can pass a query (optional), the db and the retrofit service
    val db : SimpleDummyDatabase,
    val remoteService:DummyJsonApi,
    val sortBy:String,
    val order:SortOrder
) : RemoteMediator<Int, SimplifiedDummyItem>() {

    override suspend fun initialize(): InitializeAction {
        //In this method  we can decide what happens when we start the mediator loading for the first time whether we want to refresh the data or continue loading

        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SimplifiedDummyItem>
    ): MediatorResult {
        //This is called when there is some form of loading is required
        //Whether we want to refresh the data or we want to append or prepend data
        //The paging state just passes data about the last loaded page
        return  try {
            //We need to get the current key we want to load  for
            //We check the load type to see which operation was carried out
           val loadKey =   when(loadType){
                LoadType.APPEND-> {
                    //Since we can append we can calculate the key
                    //We first get the last item
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null){
                       STARTING_SKIP
                    }else{
                        lastItem.id
                    }
                }
                LoadType.PREPEND-> return MediatorResult.Success(true)// We cannot go back in the remote service or get data backwards we cannot prepend so we will return that pagination has ended telling it that no data can be loaded again
                LoadType.REFRESH-> STARTING_SKIP //Since we are refreshing we should start from the beginnig so we can return null or 1
            }
            //We will now load the data from the api service
            val remoteData = remoteService.getProductsByPaginationSortedAndOrdered(state.config.pageSize,loadKey,sortBy,order.order)
            //Now that we have gotten the data we will save it into the database
            db.withTransaction {
               //If we are refreshing we will clear thee table b4 adding a new set of data as we dont want to load the same data twice
                if(loadType == LoadType.REFRESH){
                    db.simpleDao().deleteAll()
                }
                db.simpleDao().insertAll(remoteData.products.map { it.toSimpleDummyItem() })
            }
            //When we are done with the transaction we want to return success but also check whether there is still more data that can be loaded

            return MediatorResult.Success(remoteData.products.isEmpty()) //If it is not empty there are more products




        }catch (e:IOException){
           return MediatorResult.Error(e)
            //This will show that there was an error loading data from the remote service and a retry can happen
        }catch (e:HttpException){
            return MediatorResult.Error(e)

        }
    }
}

//Since we have learnt how to paginte in Room and also using Retrofit
//We will learn Remote Mediator
//When building apps we want a situation where our app can function fully offline for better user experience
//So When using paging  we can do by getting data from a paging source from the database
//But if we want to get new data there is a way which is the Remote Mediator which allows us to page using both network and database
//The way it works
//The Paging source of the database loads data in pages for the user as per request of new data while scrolling through the Recycler View
//When the database paging source has been exhausted and there is  no new data to be loaded
//The Pager calls the remote mediator load method as a signal telling it there is no new data

//This load  allows us to load data from the network and store into the database
//In the load function of the Remote Mediator there we pass the the Lod Type and Paging State
//This pass the type of load operation that wants to be carried out like a refresh , prepend or append
//It also pass the last item accessed in its paging state
//After loading we return MediatorResult which can result in a error or success
//In the success we can return whether ther is no new data to load from the network source also
//This will make so that there is no new data loaded
//After everything the pager invalidates the Paging Source and creates a new one and starts loading from the database
//In this example we will still use the Dummy Json Api but we will store less data
//We will store just the id , title ,description , and picture url