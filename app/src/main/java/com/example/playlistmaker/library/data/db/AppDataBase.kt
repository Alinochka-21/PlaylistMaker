package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.SQLiteConnection
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.library.data.db.dao.PlaylistDao
import com.example.playlistmaker.library.data.db.dao.PlaylistTracksDao
import com.example.playlistmaker.library.data.db.dao.TrackDao
import com.example.playlistmaker.library.data.db.entity.PlaylistEntity
import com.example.playlistmaker.library.data.db.entity.PlaylistTracksEntity
import com.example.playlistmaker.library.data.db.entity.TrackEntity

@Database(
    version = 5,
    entities = [TrackEntity::class, PlaylistEntity::class, PlaylistTracksEntity::class]
)
abstract class AppDataBase : RoomDatabase(){

    abstract fun getTrackDao(): TrackDao

    abstract fun getPlaylistDao(): PlaylistDao

    abstract fun getPlaylistTracksDao(): PlaylistTracksDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE track_table ADD COLUMN addedAt INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
        val MIGRATION_2_3 = object : Migration(2,3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS playlist_table (
                        playlistId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        playlistName TEXT NOT NULL,
                        playlistDescription TEXT,
                        playlistCover TEXT,
                        tracklist TEXT,
                        playlistSize INTEGER NOT NULL,
                        addedAt INTEGER NOT NULL
                    )
                """)
            }
        }

        val MIGRATION_3_4 = object : Migration(3,4){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("DELETE FROM playlist_table")
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5){
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS playlist_tracks_table (
                    trackId INTEGER PRIMARY KEY NOT NULL,
                    trackName TEXT NOT NULL,
                    artistName TEXT NOT NULL, 
                    trackTimeMillis INTEGER NOT NULL,
                    artworkUrl100 TEXT NOT NULL,
                    country TEXT NOT NULL,
                    primaryGenreName TEXT NOT NULL,
                    collectionName TEXT,
                    releaseDate TEXT,
                    previewUrl TEXT NOT NULL,
                    addedAt INTEGER NOT NULL )
                """)
            }
        }
    }
}