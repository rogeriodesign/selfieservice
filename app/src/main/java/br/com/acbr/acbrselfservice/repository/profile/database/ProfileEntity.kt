package br.com.acbr.acbrselfservice.repository.profile.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class ProfileEntity (
    @PrimaryKey(autoGenerate = false)
    val id: Long = 0,
    val name: String,
    val upId: String? = null,
    val email: String? = null,
    val phoneNumber: String? = null,
    val loginId: String? = null,
    val updatedAt: Date? = null,
    val createdAt: Date? = null
)