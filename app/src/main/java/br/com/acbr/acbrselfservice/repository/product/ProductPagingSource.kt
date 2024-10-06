package br.com.acbr.acbrselfservice.repository.product

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import br.com.acbr.acbrselfservice.entity.Product
import br.com.acbr.acbrselfservice.util.ProcessStatus
import retrofit2.HttpException
import java.io.IOException

class ProductPagingSource(
    private val backend: ProductRepository,
    private val search: String? = null,
) : PagingSource<Int, Product>() {
    override fun getRefreshKey(state: PagingState<Int, Product>): Int? {
        // Tente encontrar a chave de página da página mais próxima de anchorPosition, de prevKey ou nextKey,
        // mas você precisa lidar com a nulidade aqui:
        //   * prevKey == null -> anchorPage é a primeira página.
        //   * nextKey == null -> anchorPage é a última página.
        //   * ambos prevKey e nextKey null -> anchorPage é a página inicial, então apenas retorne null.
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Product> {
        try {//params.placeholdersEnabled
            // Start refresh at page 1 if undefined.
            val nextPageNumber = params.key ?: 1
            val response = if(search == null){
                backend.getProductsByCategorySync(page = nextPageNumber, perPage = 15)
            } else {
                backend.getProductsSync(page = nextPageNumber, search = search, perPage = 15)
            }
            return if(response.status == ProcessStatus.Success && response.data != null){
                if(response.data.currentPage == response.data.lastPage) {
                    LoadResult.Page(
                        data = response.data.data,
                        prevKey = null, // Apenas paginação para frente.
                        nextKey = null
                    )
                } else if(response.data.data.isEmpty()){
                    LoadResult.Page(
                        data = response.data.data,
                        prevKey = null, // Apenas paginação para frente.
                        nextKey = null
                    )
                } else {
                    LoadResult.Page(
                        data = response.data.data,
                        prevKey = null, // Apenas paginação para frente.
                        nextKey = response.data.currentPage + 1
                    )
                }
            } else {
                LoadResult.Error(Throwable(response.status.name+" - "+response.message))
            }
        } catch (e: IOException) {
            Log.i("lista", "pediu carregamento IO - "+e)
            // IOException for network failures.
            return LoadResult.Error(e)
        } catch (e: HttpException) {
            Log.i("lista", "pediu carregamento http - "+e)
            // HttpException for any non-2xx HTTP status codes.
            return LoadResult.Error(e)
        } catch (e: Exception) {
            Log.i("lista", "pediu carregamento genérico - "+e)
            // Handle errors in this block and return LoadResult.Error if it is an
            // expected error (such as a network failure).
            return LoadResult.Error(e)
        }
    }
}
