package com.example.playlistmaker.library.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table ORDER BY addedAt DESC")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    @Delete()
    suspend fun deletePlaylist(playlistEntity: PlaylistEntity)

    @Query("SELECT tracklist FROM playlist_table WHERE playlistId = :playlistId")
    suspend fun getTracksId(playlistId: Long): String

    @Query("UPDATE playlist_table SET tracklist = :newTrackList WHERE playlistId = :id")
    suspend fun addTrack(id: Long, newTrackList: String)
}
