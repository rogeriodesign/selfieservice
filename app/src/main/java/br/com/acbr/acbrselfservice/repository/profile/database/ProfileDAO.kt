package br.com.acbr.acbrselfservice.repository.profile.database

import androidx.lifecycle.LiveData
import androidx.room.*
import br.com.acbr.acbrselfservice.entity.ConfigurationData

@Dao
interface ProfileDAO {
    @Query("SELECT * FROM ProfileEntity ORDER BY id ASC")
    fun getAll(): List<ProfileEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(oauth: ProfileEntity): Long?

    @Delete
    fun remove(oauth: ProfileEntity): Int?

    @Query("SELECT * FROM ProfileEntity WHERE id = :id")
    fun findForId(id: Long): ProfileEntity?


}