package com.axel.reproductorenkotlin.helpers

import com.axel.reproductorenkotlin.R
import com.axel.reproductorenkotlin.data.models.Song

/**
 * @author Axel Sanchez
 */
object SongHelper {
    val songsList: List<Song> = listOf(
        Song(R.raw.edsheerancastleonthehill, R.drawable.castleonthehill, "Castle on the hill"),
        Song(R.raw.edsheerandive, R.drawable.dive, "Dive"),
        Song(R.raw.edsheerangalwaygirl, R.drawable.galwaygirl, "Galway girl"),
        Song(R.raw.edsheerangivemelove, R.drawable.givemelove, "Give me love"),
        Song(R.raw.edsheeranhowwouldyoufeel, R.drawable.howwouldyoufeel, "How would you feel"),
        Song(R.raw.edsheeranlegohouse, R.drawable.legohouse, "Lego house"),
        Song(R.raw.edsheeranone, R.drawable.one, "One"),
        Song(R.raw.edsheeranperfect, R.drawable.perfect, "Perfect"),
        Song(R.raw.edsheerantheateam, R.drawable.theateam, "The a team"),
        Song(R.raw.edsheeranthinkingoutloud, R.drawable.thinkingoutloud, "Thinking out loud"),
        Song(R.raw.happieredsheeranlyric, R.drawable.happier, "Happier"),
        Song(R.raw.photographedsheeran, R.drawable.photograph, "Photograph")
    )
}