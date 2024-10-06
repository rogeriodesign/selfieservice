package br.com.acbr.acbrselfservice.repository.cart.database

import androidx.room.*

@Dao
interface ProductDAO {
    @Query("SELECT * FROM ProductEntity ")
    fun getAll(): List<ProductEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(product: ProductEntity): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(products: List<ProductEntity>)

    @Delete
    fun remove(oauth: ProductEntity): Int?

    @Query("SELECT * FROM ProductEntity WHERE uuid = :uuid")
    fun findForId(uuid: String): ProductEntity?

    @Query("DELETE FROM ProductEntity WHERE id = :id")
    fun delete(id: Long): Int?
}