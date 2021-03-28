package com.axel.reproductorenkotlin.data.room

import androidx.room.TypeConverter
import com.axel.reproductorenkotlin.data.models.*
import com.axel.reproductorenkotlin.data.models.User.*
import com.google.gson.Gson

/**
 * @author Axel Sanchez
 */
class Converters{
    private val gson: Gson = Gson()

    //Objects
    @TypeConverter
    fun fromUser(user: User?): String? {
        user?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toUser(string: String?): User? {
        string?.let {
            return gson.fromJson(it, User::class.java)
        } ?: return null
    }

    @TypeConverter
    fun fromPlaylistSongs(playlistSongs: PlaylistSongs?): String? {
        playlistSongs?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toPlaylistSongs(string: String?): PlaylistSongs? {
        string?.let {
            return gson.fromJson(it, PlaylistSongs::class.java)
        } ?: return null
    }

    @TypeConverter
    fun fromSearch(search: Search?): String? {
        search?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toSearch(string: String?): Search? {
        string?.let {
            return gson.fromJson(it, Search::class.java)
        } ?: return null
    }

    @TypeConverter
    fun fromUserPlaylists(userPlaylists: UserPlaylists?): String? {
        userPlaylists?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toUserPlaylists(string: String?): UserPlaylists? {
        string?.let {
            return gson.fromJson(it, UserPlaylists::class.java)
        } ?: return null
    }

    @TypeConverter
    fun fromFeaturedPlaylistSong(featuredPlaylistSong: FeaturedPlaylistSong?): String? {
        featuredPlaylistSong?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toFeaturedPlaylistSong(string: String?): FeaturedPlaylistSong? {
        string?.let {
            return gson.fromJson(it, FeaturedPlaylistSong::class.java)
        } ?: return null
    }

    //List
    @TypeConverter
    fun fromEShopList(list: List<FeaturedPlaylistSong?>?): String? {
        var response = ""
        list?.let {
            for (i in list.indices) {
                response += if (i == 0) fromFeaturedPlaylistSong(list[i])
                else ";${fromFeaturedPlaylistSong(list[i])}"
            }
        } ?: return null
        return response
    }

    @TypeConverter
    fun toEShopList(concat: String?): List<FeaturedPlaylistSong?>? {
        val newList = concat?.split(";")
        newList?.let {
            return it.map { str -> toFeaturedPlaylistSong(str) }
        } ?: return null
    }

    @TypeConverter
    fun fromFollowers(followers: Followers?): String? {
        followers?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toFollowers(string: String?): Followers? {
        string?.let {
            return gson.fromJson(it, Followers::class.java)
        } ?: return null
    }

    @TypeConverter
    fun fromImage(image: User.Image?): String? {
        image?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toImage(string: String?): User.Image? {
        string?.let {
            return gson.fromJson(it, User.Image::class.java)
        } ?: return null
    }

    @TypeConverter
    fun fromItem(image: UserPlaylists.Item?): String? {
        image?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toItem(string: String?): UserPlaylists.Item? {
        string?.let {
            return gson.fromJson(it, UserPlaylists.Item::class.java)
        } ?: return null
    }

    @TypeConverter
    fun fromObject(mObject: Any?): String? {
        mObject?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toObject(string: String?): Any? {
        string?.let {
            return gson.fromJson(it, Object::class.java)
        } ?: return null
    }

    @TypeConverter
    fun fromPlaylists(playlists: Playlists?): String? {
        playlists?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toPlaylists(string: String?): Playlists? {
        string?.let {
            return gson.fromJson(it, Playlists::class.java)
        } ?: return null
    }

    @TypeConverter
    fun fromFeaturedPlaylist(featuredPlaylist: FeaturedPlaylist?): String? {
        featuredPlaylist?.let {
            return gson.toJson(it)
        } ?: return null
    }

    @TypeConverter
    fun toFeaturedPlaylist(string: String?): FeaturedPlaylist? {
        string?.let {
            return gson.fromJson(it, FeaturedPlaylist::class.java)
        } ?: return null
    }
}