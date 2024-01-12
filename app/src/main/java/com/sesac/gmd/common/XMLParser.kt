package com.sesac.gmd.common

import com.sesac.gmd.data.model.ManiaDBAlbum
import com.sesac.gmd.data.model.Music
import com.sesac.gmd.data.model.MusicList
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

fun String.parseXMLFromMania(): MusicList {
    val xmlDOMBuilder = DocumentBuilderFactory.newInstance()
    val musicList = MusicList()

    try {
        val inputSource = InputSource()
        inputSource.characterStream = StringReader(this)
        val rootDoc = xmlDOMBuilder.newDocumentBuilder().parse(inputSource)

        val itemTags = rootDoc.getElementsByTagName(MUSIC_ITEM)

        for (idx in 0 until itemTags.length) {
            val music = Music()

            music.musicIdx =
                itemTags.item(idx).attributes.getNamedItem(MUSIC_ID).nodeValue.trim().toInt()
            val itemSubTagLength = itemTags.item(idx).childNodes.length

            for (subIdx in 0 until itemSubTagLength) {
                val itemChildNode = itemTags.item(idx).childNodes.item(subIdx)
                if (itemChildNode != null) {
                    when (itemChildNode.nodeName) {
                        XML_TAG_MANIA_DB_ALBUM -> {
                            val album = ManiaDBAlbum()
                            val albumNodes = itemChildNode.childNodes
                            for (childIdx in 0 until albumNodes.length) {
                                when (albumNodes.item(childIdx).nodeName) {
                                    MUSIC_TITLE -> {
                                        album.albumTitle =
                                            albumNodes.item(childIdx).textContent.trim()
                                                .replace("&#39;", "'")
                                                .replace("&amp;", "&")
                                                .replace("&nbsp;", " ")
                                                .replace("&quot;", "\"")
                                    }

                                    ALBUM_IMAGE -> {
                                        album.albumImage =
                                            albumNodes.item(childIdx).textContent.trim()
                                    }
                                }
                            }
                            music.album = album
                        }

                        XML_TAG_ALBUM_INFO -> {
                            val artists = itemChildNode.childNodes
                            for (artIdx in 0 until artists.length) {
                                val artistChildNode = artists.item(artIdx).childNodes
                                for (artSubIdx in 0 until artistChildNode.length) {
                                    val artistName = artistChildNode.item(artSubIdx).nodeName
                                    if (artistName == MUSIC_NAME) {
                                        music.artist.add(
                                            artistChildNode.item(artSubIdx).textContent.trim()
                                                .replace("&#39;", "'")
                                                .replace("&amp;", "&")
                                                .replace("&nbsp;", " ")
                                                .replace("&quot;", "\"")
                                        )
                                    }
                                }
                            }
                        }

                        XML_TAG_MUSIC_TITLE -> {
                            music.musicTitle = itemChildNode.textContent.trim()
                                .replace("&#39;", "'")
                                .replace("&amp;", "&")
                                .replace("&nbsp;", " ")
                                .replace("&quot;", "\"")
                        }
                    }
                }
            }
            musicList.songs.add(music)
        }
    } catch (e: Exception) {
        Logger.traceException(e)
    }
    return musicList
}

// region XML TAG names
private const val XML_TAG_MANIA_DB_ALBUM = "maniadb:album"
private const val XML_TAG_ALBUM_INFO = "maniadb:trackartists"
private const val XML_TAG_MUSIC_TITLE = "title"

private const val MUSIC_ITEM = "item"
private const val MUSIC_ID = "id"
private const val MUSIC_TITLE = "title"
private const val ALBUM_IMAGE = "image"
private const val MUSIC_NAME = "name"
// endregion