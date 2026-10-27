package com.example.playlistmaker.library.data.db.dao

import android.net.Uri
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table ORDER BY addedAt DESC")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Query("DELETE FROM playlist_table WHERE  playlistId = :id")
    suspend fun deletePlaylist(id:Long)

    @Query("SELECT tracklist FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getTracksId(playlistId: Long): String

    @Query("UPDATE playlist_table SET tracklist = :newTrackList WHERE playlistId = :id")
    suspend fun changeTrackList(id: Long, newTrackList: String)
    @Query("SELECT * FROM playlist_table WHERE playlistId = :id")
    suspend fun getPlaylistInfo(id: Long): PlaylistEntity

    @Query("SELECT tracklist FROM playlist_table")
    suspend fun getAllTracksId(): List<String>

    @Query("UPDATE playlist_table SET playlistName = :newName, playlistCover = :newCover, playlistDescription = :newDescription WHERE playlistId = :id")
    suspend fun changePlaylist(id: Long, newName: String, newCover: String?, newDescription: String?)
}
