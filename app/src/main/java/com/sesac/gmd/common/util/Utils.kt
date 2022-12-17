/*
* Created by gabriel
* date : 22/11/21
* */

package com.sesac.gmd.common.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.sesac.gmd.application.GMDApplication
import com.sesac.gmd.data.model.Song
import com.sesac.gmd.data.model.ManiaDBAlbum
import com.sesac.gmd.data.model.SongList
import org.xml.sax.InputSource
import java.io.StringReader
import javax.xml.parsers.DocumentBuilderFactory

class Utils {
    companion object {
        private const val TAG = "Utils"

        // Toast 출력 함수
        fun toastMessage(message: String) {
            Toast.makeText(GMDApplication.getAppInstance(), message, Toast.LENGTH_SHORT).show()
        }

        // event 발생 시 키보드 내려가는 함수
        fun hideKeyBoard(activity: Activity) {
            val inputManager: InputMethodManager = activity.getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputManager.hideSoftInputFromWindow(activity.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
        }

        // AlertDialog
        fun setAlertDialog(context: Context, title: String? = null, message: String,
                           posFunc: () -> Unit, negFunc: () -> Unit) {
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("예") { _, _ ->
                    posFunc()
                }
                .setNegativeButton("아니오") { _, _ ->
                    negFunc()
                }
                .create()
                .show()
        }

        // XML -> DTO parsing 함수
        fun parseXMLFromMania(xmlFromManiaDB: String): SongList {
            val xmlDOMBuilder = DocumentBuilderFactory.newInstance()
            val songList = SongList()
            try {
                val inputSource = InputSource()
                inputSource.characterStream = StringReader(xmlFromManiaDB)
                val rootDoc = xmlDOMBuilder.newDocumentBuilder().parse(inputSource)
                val itemTags = rootDoc.getElementsByTagName("item")
                for (idx in 0 until itemTags.length) {
                    val song = Song()
                    song.songIdx = itemTags.item(idx).attributes.getNamedItem("id").nodeValue.trim().toInt()
                    val itemSubTagLength = itemTags.item(idx).childNodes.length
                    for (subIdx in 0 until itemSubTagLength) {
                        val itemChildNode = itemTags.item(idx).childNodes.item(subIdx)
                        if (itemChildNode != null) {
                            when (itemChildNode.nodeName) {
                                XML_TAG_MANIA_DB_ALBUM -> {
                                    val album = ManiaDBAlbum()
                                    val albumNodes = itemChildNode.childNodes
                                    for (childIdx in 0 until albumNodes.length) {
                                        val childNodeName = albumNodes.item(childIdx).nodeName
                                        if (childNodeName == "title") {
                                            album.albumTitle =
                                                albumNodes.item(childIdx).textContent.trim()
                                        } else if (childNodeName == "image") {
                                            album.albumImage =
                                                albumNodes.item(childIdx).textContent.trim()
                                        }
                                    }
                                    song.album = album
                                }
                                XML_TAG_ALBUM_INFO -> {
                                    val artists = itemChildNode.childNodes
                                    for (artIdx in 0 until artists.length) {
                                        val artists = artists.item(artIdx).childNodes
                                        for (artSubIdx in 0 until artists.length) {
                                            val artistName = artists.item(artSubIdx).nodeName
                                            if (artistName == "name") {
                                                song.artist.add(artists.item(artSubIdx).textContent.trim())
                                            }
                                        }
                                    }
                                }
                                XML_TAG_SONG_TITLE -> {
                                    song.songTitle = itemChildNode.textContent.trim()
                                }
                            }
                        }
                    }
                    songList.songs.add(song)
                }
            } catch (e: Exception) {
                Log.e(DEFAULT_TAG+TAG+"XML Parser", e.toString())
            }
            return songList
        }
    }
}