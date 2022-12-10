/*
* Created by gabriel
* date : 22/11/21
* */

package com.sesac.gmd.common.util

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.sesac.gmd.application.GMDApplication
import com.sesac.gmd.data.model.Item
import com.sesac.gmd.data.model.ManiaDBAlbum
import com.sesac.gmd.data.model.SongInfo
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

        // XML -> DTO parsing 함수
        fun parseXMLFromMania(xmlFromManiaDB: String): SongInfo {
            // TODO: SongID 파싱 필요
            val xmlDOMBuilder = DocumentBuilderFactory.newInstance()
            val songInfo = SongInfo()
            try {
                val inputSource = InputSource()
                inputSource.characterStream = StringReader(xmlFromManiaDB)
                val rootDoc = xmlDOMBuilder.newDocumentBuilder().parse(inputSource)
                val itemTags = rootDoc.getElementsByTagName("item")
                for (idx in 0 until itemTags.length) {
                    val item = Item()
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
                                            album.albumImageURL =
                                                albumNodes.item(childIdx).textContent.trim()
                                        }
                                    }
                                    item.album = album
                                }
                                XML_TAG_ALBUM_INFO -> {
                                    val artists = itemChildNode.childNodes
                                    for (artIdx in 0 until artists.length) {
                                        val artists = artists.item(artIdx).childNodes
                                        for (artSubIdx in 0 until artists.length) {
                                            val artistName = artists.item(artSubIdx).nodeName
                                            if (artistName == "name") {
                                                item.artist.add(artists.item(artSubIdx).textContent.trim())
                                            }
                                        }
                                    }
                                }
                                XML_TAG_SONG_TITLE -> {
                                    item.songTitle = itemChildNode.textContent.trim()
                                }
                            }
                        }
                    }
                    songInfo.items.add(item)
                }
            } catch (e: Exception) {
                Log.e("CreateSongViewModel", e.toString())
            }
            return songInfo
        }
    }
}