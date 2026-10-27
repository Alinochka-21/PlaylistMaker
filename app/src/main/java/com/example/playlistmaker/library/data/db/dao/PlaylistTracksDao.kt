package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entity.PlaylistTracksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistTracksDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addTrack(entity: PlaylistTracksEntity)

    @Query("DELETE FROM playlist_tracks_table WHERE trackId = :id")
    suspend fun deleteTrack(id: Long)

    @Query("SELECT * FROM playlist_tracks_table WHERE trackId IN (:ids)")
    fun getTracks(ids: ArrayList<Long>) : Flow<List<PlaylistTracksEntity>>
}