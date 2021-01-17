package com.axel.reproductorenkotlin.data.models

data class User(
    var display_name: String? = null,
    var external_urls: ExternalUrls? = null,
    var followers: Followers? = null,
    var href: String? = null,
    var id: String? = null,
    var images: List<Image?>? = null,
    var type: String? = null,
    var uri: String? = null
) {
    data class ExternalUrls(
        var spotify: String? = null
    )

    data class Followers(
        var href: Any? = null,
        var total: Int? = null
    )

    data class Image(
        var height: Any? = null,
        var url: String? = null,
        var width: Any? = null
    )
}