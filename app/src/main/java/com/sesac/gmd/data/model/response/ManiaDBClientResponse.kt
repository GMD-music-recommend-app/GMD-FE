package com.sesac.gmd.data.model.response

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

// https://github.com/HanYeop/ManiaDB-ApiTest/blob/main/app/src/main/java/com/hanyeop/maniadb/model/ManiaDBClientResponse.kt

@Xml(name = "rss")
data class ManiaDBClientResponse(
    @Element(name = "channel")
    var channel: Channel? = null
)

@Xml(name = "channel")
data class Channel(
    // 결과 없을 경우 고려 (required = false)
    @Element(name = "item")
    var itemList: List<Item>? = null
)

@Xml(name = "item")
data class Item (
    @PropertyElement(name = "title")
    var title: String = "",

    @Element(name = "item")
    var album: Album? = null,

    @Element
    var artist: Artist? = null
)

@Xml(name = "album")
data class Album(
    /*@PropertyElement
    var albumTitle: String = "",*/

    @PropertyElement(name = "image")
    var image: String = ""
)

@Xml(name = "artist")
data class Artist(
    @PropertyElement(name = "name")
    var name: String = ""
)