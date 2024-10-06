package br.com.acbr.acbrselfservice.repository.cart.database

import androidx.room.*

@Dao
interface ProductOptionDAO {
    @Query("SELECT * FROM ProductOptionEntity WHERE productId = :productId ")
    fun getAll(productId: Long): List<ProductOptionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(product: ProductOptionEntity): Long?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(products: List<ProductOptionEntity>)

    @Delete
    fun remove(oauth: ProductOptionEntity): Int?

    @Query("SELECT * FROM ProductOptionEntity WHERE uuid = :uuid")
    fun findForId(uuid: String): ProductOptionEntity?

    @Query("DELETE FROM ProductOptionEntity WHERE productId = :productId")
    fun delete(productId: Long): Int?
}