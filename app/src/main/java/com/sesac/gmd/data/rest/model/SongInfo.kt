/*
* Created by gabriel
* date : 22/12/01
* */

package com.sesac.gmd.data.rest.model

import android.os.Bundle
import androidx.core.os.bundleOf
import java.io.Serializable

// TODO: 추후 수정 필요
data class SongInfo(
    val songID : Int,
    val userID : Int,
    val title : String,
    val artist : String,
    val reason : String,
    val hashtag : String?,
    val latitude : Float,
    val longitude : Float,
    val status : Char,
    val createTime : String,
    val updateTime : String
) : Serializable {
    fun toBundle(): Bundle {
        return bundleOf(
            "SongID" to songID,
            "UserID" to userID,
            "Title" to title,
            "Artist" to artist,
            "Reason" to reason,
            "Hashtag" to hashtag,
            "Latitude" to latitude,
            "Longitude" to longitude,
            "Status" to status,
            "CreateTime" to createTime
        )
    }
}