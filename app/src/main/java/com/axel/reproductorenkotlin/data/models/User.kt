package com.axel.reproductorenkotlin.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity data class User(
    @PrimaryKey var id: Long,
    var display_name: String? = null,
    var external_urls: ExternalUrls? = null,
    var followers: Followers? = null,
    var href: String? = null,
    var images: List<Image?>? = null,
    var type: String? = null,
    var uri: String? = null
) {
    @Entity data class ExternalUrls(
        var spotify: String? = null
    )

    @Entity data class Followers(
        var href: Any? = null,
        var total: Int? = null
    )

    @Entity data class Image(
        var height: Any? = null,
        var url: String? = null,
        var width: Any? = null
    )
}