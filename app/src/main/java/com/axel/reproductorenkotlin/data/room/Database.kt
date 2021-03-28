package com.axel.reproductorenkotlin.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.axel.reproductorenkotlin.data.models.*

/**
 * @author Axel Sanchez
 */
@Database(
    entities = [User::class, UserPlaylists::class, FeaturedPlaylist::class, PlaylistSongs::class, Search::class],
    version = 1
)
@TypeConverters(Converters::class)
abstract class Database: RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun songDao(): SongDao
}