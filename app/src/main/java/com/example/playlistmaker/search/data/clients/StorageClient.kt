package com.example.playlistmaker.search.data.clients

interface StorageClient<T> {
    fun saveData(data: T)
    fun getData(): T?
    fun clearData()
}