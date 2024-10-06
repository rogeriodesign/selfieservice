package br.com.acbr.acbrselfservice.repository

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.acbr.acbrselfservice.repository.converters.DateConverter
import br.com.acbr.acbrselfservice.repository.cart.database.ProductDAO
import br.com.acbr.acbrselfservice.repository.cart.database.ProductEntity
import br.com.acbr.acbrselfservice.repository.cart.database.ProductOptionDAO
import br.com.acbr.acbrselfservice.repository.cart.database.ProductOptionEntity
import br.com.acbr.acbrselfservice.repository.profile.database.ProfileDAO
import br.com.acbr.acbrselfservice.repository.profile.database.ProfileEntity

const val NAME_DATA_BASE = "boxdeliveryconcierge.db"

@Database(
    entities = [ProductEntity::class, ProductOptionEntity::class, ProfileEntity::class],
    version = 1,
    exportSchema = false
)

@TypeConverters(DateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract val productDAO: ProductDAO
    abstract val productOptionDAO: ProductOptionDAO
    abstract val profileDAO: ProfileDAO

    companion object {
        private lateinit var db: AppDatabase

        fun getInstance(context: Context): AppDatabase {

            if (::db.isInitialized) return db

            synchronized(AppDatabase::class) {
                db = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    NAME_DATA_BASE
                )
                    .allowMainThreadQueries()
                    //.addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                    .build()
            }

            return db
        }
    }
}