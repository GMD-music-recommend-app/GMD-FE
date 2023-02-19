package com.sesac.gmd.common.util

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

// Splash Logo Showing Time
const val SPLASH_LOGO_SHOWING_TIME = 1_500L

// Recycler View Item Decoration Offsets
const val RECYCLER_ITEM_SEARCH_MUSIC_OFFSET = 20


/**
 * Retrofit - OkHttp Client 내에서 사용되는 값
 */

// Connection & Read Timeout
const val REST_TIMEOUT = 10L
// Header Keys & Values
const val X_ACCESS_TOKEN = "X-ACCESS-TOKEN"
const val ACCEPT = "Accept"
const val GET_TO_JSON = "application/json"


/**
 * Google Map 내에서 사용되는 값
 */

// Google Map Zoom Level
const val DEFAULT_ZOOM_LEVEL = 16F
// Google Map Pin Center Point
const val DEFAULT_PIN_CENTER_POINTER = 0.5F
// Pin List 검색 반경
const val PIN_SEARCH_RADIUS = 5_000 // 반경 5km 내 생성된 핀 표시

// Default Location Latitude & Longitude When Network not working
const val SEOUL_CITY_LATITUDE = 37.5662952  // 서울 시청 위도
const val SEOUL_CITY_LONGITUDE = 126.97794509999994 // 서울 시청 경도


/**
 * Utils.parseXMLFromMania 내에서 사용되는 Keys
 */

// Utils.parseXMLFromMania.itemChildNode.nodeName
const val XML_TAG_MANIA_DB_ALBUM = "maniadb:album"
const val XML_TAG_ALBUM_INFO = "maniadb:trackartists"
const val XML_TAG_SONG_TITLE = "title" // <item> TAG 의 첫번째 자식 TAG

// Utils.parseXMLFromMania Keys
const val MUSIC_ITEM = "item"
const val MUSIC_ID = "id"
const val MUSIC_TITLE = "title"
const val ALBUM_IMAGE = "image"
const val MUSIC_NAME = "name"


/**
 * 프로젝트 내의 intent Key name 정의
 */

// 다음 페이지에서 보여 줄 fragment 정의
const val GO_TO_PAGE = "GO_TO_PAGE"
const val SET_OTHER_PLACE = "SET_OTHER_PLACE"
const val CREATE_MUSIC_HERE = "CREATE_MUSIC_HERE"

// 다음 화면으로 넘겨 줄 위/경도 값
const val LATITUDE = "latitude"
const val LONGITUDE = "longitude"