package com.example.playlistmaker.library.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.library.data.db.dao.TrackDao
import com.example.playlistmaker.library.data.db.entity.TrackEntity

@Database(
    version = 2,
    entities = [TrackEntity::class]
)
abstract class AppDataBase : RoomDatabase(){

    abstract fun getTrackDao(): TrackDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    "ALTER TABLE track_table ADD COLUMN addedAt INTEGER NOT NULL DEFAULT 0"
                )
            }
        }
    }
}