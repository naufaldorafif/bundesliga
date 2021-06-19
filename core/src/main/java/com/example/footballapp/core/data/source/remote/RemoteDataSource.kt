package com.example.footballapp.core.data.source.remote

import android.util.Log
import com.example.footballapp.core.data.source.remote.network.ApiResponse
import com.example.footballapp.core.data.source.remote.network.ApiService
import com.example.footballapp.core.data.source.remote.response.TeamResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.lang.Exception

class RemoteDataSource(private val apiService: ApiService) {
    companion object {
        @Volatile
        private var instance: RemoteDataSource? = null

        fun getInstance(service: ApiService): RemoteDataSource =
            instance ?: synchronized(this) {
                instance ?: RemoteDataSource(service)
            }
    }

    suspend fun getAllTeam(): Flow<ApiResponse<List<TeamResponse>>> {

        return flow {
            try {
                val response = apiService.getList()
                val dataArray = response.teams
                if(dataArray.isNotEmpty()){
                    emit(ApiResponse.Success(response.teams))
                    Log.e("Teams", response.teams.toString())
                } else {
                    emit(ApiResponse.Empty)
                }
            } catch (e: Exception) {
                emit(ApiResponse.Error(e.toString()))
                Log.e("RemoteDataSource", e.toString())
            }
        }.flowOn(Dispatchers.IO)
    }
}