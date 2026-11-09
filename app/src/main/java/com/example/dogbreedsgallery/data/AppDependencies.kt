package com.example.dogbreedsgallery.data

import android.content.Context
import com.example.dogbreedsgallery.data.local.AppDatabase
import com.example.dogbreedsgallery.data.remote.RetrofitClient

object AppDependencies {

    @Volatile
    private var _repository: BreedRepository? = null

    val repository: BreedRepository
        get() = _repository
            ?: error("AppDependencies.init(context) must be called first")

    fun init(context: Context) {
        if (_repository == null) {
            synchronized(this) {
                if (_repository == null) {
                    val db = AppDatabase.getInstance(context)
                    _repository = BreedRepository(db.breedDao(), RetrofitClient.api)
                }
            }
        }
    }
}