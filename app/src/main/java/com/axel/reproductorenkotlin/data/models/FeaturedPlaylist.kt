package com.axel.reproductorenkotlin.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class ExternalUrls {

    @SerializedName("spotify")
    @Expose
    private var spotify: String = ""

    fun getSpotify(): String {
        return spotify
    }

    fun setSpotify(spotify: String) {
        this.spotify = spotify
    }

}

class ExternalUrls_ {

    @SerializedName("spotify")
    @Expose
    private var spotify: String = ""

    fun getSpotify(): String {
        return spotify
    }

    fun setSpotify(spotify: String) {
        this.spotify = spotify
    }

}

class FeaturedPlaylist {

    @SerializedName("message")
    @Expose
    private var message: String = ""
    @SerializedName("playlists")
    @Expose
    private lateinit var playlists: Playlists

    fun getMessage(): String {
        return message
    }

    fun setMessage(message: String) {
        this.message = message
    }

    fun getPlaylists(): Playlists {
        return playlists
    }

    fun setPlaylists(playlists: Playlists) {
        this.playlists = playlists
    }
}

class Image {

    @SerializedName("height")
    @Expose
    private lateinit var height: Object
    @SerializedName("url")
    @Expose
    private var url: String = ""
    @SerializedName("width")
    @Expose
    private lateinit var width: Object

    fun getHeight(): Object {
        return height
    }

    fun setHeight(height: Object) {
        this.height = height
    }

    fun getUrl(): String {
        return url
    }

    fun setUrl(url: String) {
        this.url = url
    }

    fun getWidth(): Object {
        return width
    }

    fun setWidth(width: Object) {
        this.width = width
    }
}

class ItemSong {

    @SerializedName("collaborative")
    @Expose
    private var collaborative: Boolean = false
    @SerializedName("description")
    @Expose
    private var description: String = ""
    @SerializedName("external_urls")
    @Expose
    private lateinit var externalUrls: ExternalUrls
    @SerializedName("href")
    @Expose
    private var href: String = ""
    @SerializedName("id")
    @Expose
    private var id: String = ""
    @SerializedName("images")
    @Expose
    private var images: List<Image>? = null
    @SerializedName("name")
    @Expose
    private var name: String = ""
    @SerializedName("owner")
    @Expose
    private lateinit var owner: Owner
    @SerializedName("primary_color")
    @Expose
    private lateinit var primaryColor: Object
    @SerializedName("public")
    @Expose
    private lateinit var _public: Object
    @SerializedName("snapshot_id")
    @Expose
    private var snapshotId = ""
    @SerializedName("tracks")
    @Expose
    private lateinit var tracks: Tracks
    @SerializedName("type")
    @Expose
    private var type = ""
    @SerializedName("uri")
    @Expose
    private var uri = ""

    fun getCollaborative(): Boolean {
        return collaborative
    }

    fun setCollaborative(collaborative: Boolean) {
        this.collaborative = collaborative
    }

    fun getDescription(): String {
        return description
    }

    fun setDescription(description: String) {
        this.description = description
    }

    fun getExternalUrls(): ExternalUrls {
        return externalUrls
    }

    fun setExternalUrls(externalUrls: ExternalUrls) {
        this.externalUrls = externalUrls
    }

    fun getHref(): String {
        return href
    }

    fun setHref(href: String) {
        this.href = href
    }

    fun getId(): String {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getImages(): List<Image> {
        return images!!
    }

    fun setImages(images: List<Image>) {
        this.images = images
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String) {
        this.name = name
    }

    fun getOwner(): Owner {
        return owner
    }

    fun setOwner(owner: Owner) {
        this.owner = owner
    }

    fun getPrimaryColor(): Object {
        return primaryColor
    }

    fun setPrimaryColor(primaryColor: Object) {
        this.primaryColor = primaryColor
    }

    fun getPublic(): Object {
        return _public
    }

    fun setPublic(_public: Object) {
        this._public = _public
    }

    fun getSnapshotId(): String {
        return snapshotId
    }

    fun setSnapshotId(snapshotId: String) {
        this.snapshotId = snapshotId
    }

    fun getTracks(): Tracks {
        return tracks
    }

    fun setTracks(tracks: Tracks) {
        this.tracks = tracks
    }

    fun getType(): String {
        return type
    }

    fun setType(type: String) {
        this.type = type
    }

    fun getUri(): String {
        return uri
    }

    fun setUri(uri: String) {
        this.uri = uri
    }

    inner class Tracks {

        @SerializedName("href")
        @Expose
        private var href = ""
        @SerializedName("total")
        @Expose
        private var total: Int = 0

        fun getHref(): String {
            return href
        }

        fun setHref(href: String) {
            this.href = href
        }

        fun getTotal(): Int {
            return total
        }

        fun setTotal(total: Int) {
            this.total = total
        }
    }

}

class Owner {

    @SerializedName("display_name")
    @Expose
    private var displayName = ""
    @SerializedName("external_urls")
    @Expose
    private lateinit var externalUrls: ExternalUrls_
    @SerializedName("href")
    @Expose
    private var href = ""
    @SerializedName("id")
    @Expose
    private var id = ""
    @SerializedName("type")
    @Expose
    private var type = ""
    @SerializedName("uri")
    @Expose
    private var uri = ""

    fun getDisplayName(): String {
        return displayName
    }

    fun setDisplayName(displayName: String) {
        this.displayName = displayName
    }

    fun getExternalUrls(): ExternalUrls_ {
        return externalUrls
    }

    fun setExternalUrls(externalUrls: ExternalUrls_) {
        this.externalUrls = externalUrls
    }

    fun getHref(): String {
        return href
    }

    fun setHref(href: String) {
        this.href = href
    }

    fun getId(): String {
        return id
    }

    fun setId(id: String) {
        this.id = id
    }

    fun getType(): String {
        return type
    }

    fun setType(type: String) {
        this.type = type
    }

    fun getUri(): String {
        return uri
    }

    fun setUri(uri: String) {
        this.uri = uri
    }
}

class Playlists {

    @SerializedName("href")
    @Expose
    private var href = ""
    @SerializedName("items")
    @Expose
    private var items: List<ItemSong>? = null
    @SerializedName("limit")
    @Expose
    private var limit: Int = 0
    @SerializedName("next")
    @Expose
    private lateinit var next: Object
    @SerializedName("offset")
    @Expose
    private var offset = 0
    @SerializedName("previous")
    @Expose
    private lateinit var previous: Object
    @SerializedName("total")
    @Expose
    private var total = 0

    fun getHref(): String {
        return href
    }

    fun setHref(href: String) {
        this.href = href
    }

    fun getItems(): List<ItemSong> {
        return items!!
    }

    fun setItems(items: List<ItemSong>) {
        this.items = items
    }

    fun getLimit(): Int {
        return limit
    }

    fun setLimit(limit: Int) {
        this.limit = limit
    }

    fun getNext(): Object {
        return next
    }

    fun setNext(next: Object) {
        this.next = next
    }

    fun getOffset(): Int {
        return offset
    }

    fun setOffset(offset: Int) {
        this.offset = offset
    }

    fun getPrevious(): Object {
        return previous
    }

    fun setPrevious(previous: Object) {
        this.previous = previous
    }

    fun getTotal(): Int {
        return total
    }

    fun setTotal(total: Int) {
        this.total = total
    }
}