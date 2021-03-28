package com.axel.reproductorenkotlin.data.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity data class PlaylistSongs(
    @PrimaryKey var id: Long,
    var href: String? = null,
    var items: List<Item?>? = null,
    var limit: Int? = null,
    var next: String? = null,
    var offset: Int? = null,
    var previous: Any? = null,
    var total: Int? = null
) {
    @Entity data class Item(
        var added_at: String? = null,
        var added_by: AddedBy? = null,
        var is_local: Boolean? = null,
        var primary_color: Any? = null,
        var track: Track? = null,
        var video_thumbnail: VideoThumbnail? = null
    ) {
        @Entity data class AddedBy(
            var external_urls: ExternalUrls? = null,
            var href: String? = null,
            var id: String? = null,
            var type: String? = null,
            var uri: String? = null
        ) {
            @Entity data class ExternalUrls(
                var spotify: String? = null
            )
        }

        @Entity data class Track(
            var album: Album? = null,
            var artists: List<Artist?>? = null,
            var disc_number: Int? = null,
            var duration_ms: Int? = null,
            var episode: Boolean? = null,
            var explicit: Boolean? = null,
            var external_ids: ExternalIds? = null,
            var external_urls: ExternalUrls? = null,
            var href: String? = null,
            var id: String? = null,
            var is_local: Boolean? = null,
            var is_playable: Boolean? = null,
            var linked_from: LinkedFrom? = null,
            var name: String? = null,
            var popularity: Int? = null,
            var preview_url: String? = null,
            var track: Boolean? = null,
            var track_number: Int? = null,
            var type: String? = null,
            var uri: String? = null
        ) {
            @Entity data class Album(
                var album_type: String? = null,
                var artists: List<Artist?>? = null,
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

            @Entity data class LinkedFrom(
                var external_urls: ExternalUrls? = null,
                var href: String? = null,
                var id: String? = null,
                var type: String? = null,
                var uri: String? = null
            ) {
                @Entity data class ExternalUrls(
                    var spotify: String? = null
                )
            }
        }

        @Entity data class VideoThumbnail(
            var url: Any? = null
        )
    }
}