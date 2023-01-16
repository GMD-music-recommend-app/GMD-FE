/**
* Created by 조진수
* date : 22/12/09
*/
package com.sesac.gmd.common.util

// TEMP, Account : 10   TODO : 추후 삭제 예정
const val TEMP_USER_IDX = 10
const val TEMP_JWT = "eyJ0eXBlIjoiand0IiwiYWxnIjoiSFMyNTYifQ.eyJ1c2VySWR4IjoxMCwiaWF0IjoxNjcwODM1MzY3LCJleHAiOjE2NzIzMDY1OTZ9.UuG_dhxswqK7S64NB9ahfHFk5-Ks0XDaPKCGehXetG4"

// Default TAG
const val DEFAULT_TAG = "TAG_"

// MainActivity TabLayout Contents
const val TAB_CHART = 0
const val TAB_HOME = 1
const val TAB_SETTING = 2

// BASE_URL
const val MANIADB_BASE_URL = "http://www.maniadb.com/api/search/"
const val GMD_BASE_URL = "http://yourplaylistgmd.shop/"
const val YOUTUBE_BASE_URL = "https://www.youtube.com/results?search_query="

// Utils.parseXMLFromMania.itemChildNode.nodeName
const val XML_TAG_MANIA_DB_ALBUM = "maniadb:album"
const val XML_TAG_ALBUM_INFO = "maniadb:trackartists"
const val XML_TAG_SONG_TITLE = "title" // <item> TAG 의 첫번째 자식 TAG

// Location Latitude & Longitude
const val SEOUL_CITY_LATITUDE = 37.5662952  // 서울 시청 위도
const val SEOUL_CITY_LONGITUDE = 126.97794509999994 // 서울 시청 경도