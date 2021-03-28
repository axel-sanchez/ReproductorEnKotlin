package com.axel.reproductorenkotlin.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity data class Search(
    @PrimaryKey var id: Long,
    var tracks: Tracks? = null
) {
    @Entity data class Tracks(
        var href: String? = null,
        var items: List<Item?>? = null,
        var limit: Int? = null,
        var next: String? = null,
        var offset: Int? = null,
        var previous: Any? = null,
        var total: Int? = null
    ) {
        @Entity data class Item(
            var album: Album? = null,
            var artists: List<Artist?>? = null,
            var available_markets: List<String?>? = null,
            var disc_number: Int? = null,
            var duration_ms: Int? = null,
            var explicit: Boolean? = null,
            var external_ids: ExternalIds? = null,
            var external_urls: ExternalUrls? = null,
            var href: String? = null,
            var id: String? = null,
            var is_local: Boolean? = null,
            var name: String? = null,
            var popularity: Int? = null,
            var preview_url: String? = null,
            var track_number: Int? = null,
            var type: String? = null,
            var uri: String? = null
        ) {
            @Entity data class Album(
                var album_type: String? = null,
                var artists: List<Artist?>? = null,
                var available_markets: List<String?>? = null,
                var external_urls: ExternalUrls? = null,
                var href: String? = null,
                var id: String? = null,
                var images: List<Image?>? = null,
                var name: String? = null,
                var release_date: String? = null,
                var release_date_precision: String? = null,
                var total_tracks: Int? = null,
                var type: String? = null,
                var uri: String? = null
            ) {
                @Entity data class Artist(
                    var external_urls: ExternalUrls? = null,
                    var href: String? = null,
                    var id: String? = null,
                    var name: String? = null,
                    var type: String? = null,
                    var uri: String? = null
                ) {
                    @Entity data class ExternalUrls(
                        var spotify: String? = null
                    )
                }

                @Entity data class ExternalUrls(
                    var spotify: String? = null
                )

                @Entity data class Image(
                    var height: Int? = null,
                    var url: String? = null,
                    var width: Int? = null
                )
            }

            @Entity data class Artist(
                var external_urls: ExternalUrls? = null,
                var href: String? = null,
                var id: String? = null,
                var name: String? = null,
                var type: String? = null,
                var uri: String? = null
            ) {
                @Entity data class ExternalUrls(
                    var spotify: String? = null
                )
            }

            @Entity data class ExternalIds(
                var isrc: String? = null
            )

            @Entity data class ExternalUrls(
                var spotify: String? = null
            )
        }
    }
}