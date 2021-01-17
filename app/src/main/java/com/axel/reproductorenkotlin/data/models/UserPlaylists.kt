package com.axel.reproductorenkotlin.data.models

data class UserPlaylists(
    var href: String? = null,
    var items: List<Item?>? = null,
    var limit: Int? = null,
    var next: Any? = null,
    var offset: Int? = null,
    var previous: Any? = null,
    var total: Int? = null
) {
    data class Item(
        var collaborative: Boolean? = null,
        var description: String? = null,
        var external_urls: ExternalUrls? = null,
        var href: String? = null,
        var id: String? = null,
        var images: List<Image?>? = null,
        var name: String? = null,
        var owner: Owner? = null,
        var primary_color: Any? = null,
        var `public`: Boolean? = null,
        var snapshot_id: String? = null,
        var tracks: Tracks? = null,
        var type: String? = null,
        var uri: String? = null
    ) {
        data class ExternalUrls(
            var spotify: String? = null
        )

        data class Image(
            var height: Any? = null,
            var url: String? = null,
            var width: Any? = null
        )

        data class Owner(
            var display_name: String? = null,
            var external_urls: ExternalUrls? = null,
            var href: String? = null,
            var id: String? = null,
            var type: String? = null,
            var uri: String? = null
        ) {
            data class ExternalUrls(
                var spotify: String? = null
            )
        }

        data class Tracks(
            var href: String? = null,
            var total: Int? = null
        )
    }
}