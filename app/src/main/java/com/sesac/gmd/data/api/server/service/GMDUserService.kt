///*
//* Created by gabriel
//* date : 22/12/09
//* */
//
//package com.sesac.gmd.data.api.server.service
//
//import retrofit2.http.GET
//import retrofit2.http.PATCH
//import retrofit2.http.POST
//
//private const val userIdx = 1
//private const val pinIdx = 2
//
//interface GMDUserService {
//
//    // 유저 정보 반환
//    @GET("/users/info/${userIdx}")
//    fun getUserInfo()
//
//    // 임시 회원 가입
//    @POST("/users/sign-up")
//    fun postRegister()
//
//    // 닉네임 변경
//    @PATCH("/users/nickname/${userIdx}")
//    fun setNickName()
//
//    // 관심지역 변경
//    @PATCH("/users/location/${userIdx}")
//    fun setLocation()
//}