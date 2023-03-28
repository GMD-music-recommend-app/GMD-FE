package com.sesac.gmd.common.util

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.sesac.gmd.R
import com.sesac.gmd.application.GMDApplication
import com.sesac.gmd.data.model.Song
import com.sesac.gmd.data.model.ManiaDBAlbum
import com.sesac.gmd.data.model.SongList
import org.xml.sax.InputSource
import java.io.StringReader
import java.net.SocketTimeoutException
import java.util.concurrent.TimeoutException
import javax.xml.parsers.DocumentBuilderFactory

class Utils {
    companion object {
        private val TAG = Utils::class.simpleName

        // Toast 출력 함수
        fun toastMessage(message: String) {
            Toast.makeText(GMDApplication.getAppInstance(), message, Toast.LENGTH_SHORT).show()
        }

        // Exception 에 따라 ToastMessage 출력
        fun displayToastExceptions(e: Exception) {
            when (e) {
                is TimeoutException -> toastMessage(GMDApplication.getAppInstance().resources.getString(R.string.error_unstable_network_connection))
                is SocketTimeoutException -> toastMessage(GMDApplication.getAppInstance().resources.getString(R.string.error_unstable_network_connection))
                else -> toastMessage(GMDApplication.getAppInstance().resources.getString(R.string.unexpected_error))
            }
            Log.e(DEFAULT_TAG+TAG+"toastExceptions()", "error : ${e.message}")
        }

        // event 발생 시 키보드 내려가는 함수
        fun hideKeyBoard(activity: Activity) {
            val inputManager: InputMethodManager = activity.getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

        // XML -> DTO parsing 함수
        fun parseXMLFromMania(xmlFromManiaDB: String): SongList {
            val xmlDOMBuilder = DocumentBuilderFactory.newInstance()
            val songList = SongList()
            try {
                val inputSource = InputSource()
                inputSource.characterStream = StringReader(xmlFromManiaDB)
                val rootDoc = xmlDOMBuilder.newDocumentBuilder().parse(inputSource)
                val itemTags = rootDoc.getElementsByTagName(MUSIC_ITEM)
                for (idx in 0 until itemTags.length) {
                    val song = Song()
                    song.songIdx = itemTags.item(idx).attributes.getNamedItem(MUSIC_ID).nodeValue.trim().toInt()
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
                                                        .replace("&amp;","&")
                                                        .replace("&nbsp;"," ")
                                                        .replace("&quot;", "\"")
                                            }
                                            ALBUM_IMAGE -> {
                                                album.albumImage =
                                                    albumNodes.item(childIdx).textContent.trim()
                                            }
                                        }
                                    }
                                    song.album = album
                                }
                                XML_TAG_ALBUM_INFO -> {
                                    val artists = itemChildNode.childNodes
                                    for (artIdx in 0 until artists.length) {
                                        val artistChildNode = artists.item(artIdx).childNodes
                                        for (artSubIdx in 0 until artistChildNode.length) {
                                            val artistName = artistChildNode.item(artSubIdx).nodeName
                                            if (artistName == MUSIC_NAME) {
                                                song.artist.add(
                                                    artistChildNode.item(artSubIdx).textContent.trim()
                                                        .replace("&#39;", "'")
                                                        .replace("&amp;","&")
                                                        .replace("&nbsp;"," ")
                                                        .replace("&quot;", "\"")
                                                )
                                            }
                                        }
                                    }
                                }
                                XML_TAG_SONG_TITLE -> {
                                    song.songTitle = itemChildNode.textContent.trim()
                                        .replace("&#39;", "'")
                                        .replace("&amp;","&")
                                        .replace("&nbsp;"," ")
                                        .replace("&quot;", "\"")
                                }
                            }
                        }
                    }
                    songList.songs.add(song)
                }
            } catch (e: Exception) {
                Log.e(DEFAULT_TAG+TAG+"XML Parser", "Erorr parsing XML : $e")
            }
            return songList
        }
    }
}