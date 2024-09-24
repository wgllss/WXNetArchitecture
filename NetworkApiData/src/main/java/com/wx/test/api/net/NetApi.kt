package com.wx.test.api.net

import com.wx.annotations.Filter
import com.wx.annotations.PostBody
import com.wx.test.api.retrofit.RequestBodyWrapper
import com.wx.test.api.data.wanandroid.CommonResult
import com.wx.test.api.data.wanandroid.home.HomeData
import com.wx.test.api.data.wanandroid.home.WanAndroidHome
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface NetApi {

    //示例post 请求
    @FormUrlEncoded
    @POST("https://www.wanandroid.com/user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): String

    // 示例get 请求
    @GET("https://www.wanandroid.com/article/list/0/json")
    suspend fun getHomeList(): CommonResult<WanAndroidHome>

    // 示例get 请求2
    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList(@Path("path") page: Int): CommonResult<WanAndroidHome>

    @GET("https://www.wanandroid.com/article/list/{path}/json")
    fun getHomeListA(@Path("path") page: Int): Call<HomeData>

    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeListB(@Path("path") page: Int): HomeData

    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList(@Path("path") page: Int, @Path("path") a: Int): CommonResult<WanAndroidHome>

    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList(@Path("path") page: Int, @Path("path") f: Float): CommonResult<WanAndroidHome>

    // 示例get 请求2
    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList2222(@Path("path") page: Int): CommonResult<WanAndroidHome>

    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList3333(@Path("path") page: Int): CommonResult<WanAndroidHome>

    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList5555(@Path("path") page: Int, @Query("d") ss: String, @HeaderMap map: Map<String, String>): CommonResult<WanAndroidHome>

    @GET("https://www.wanandroid.com/article/list/{path}/json")
    suspend fun getHomeList6666(
        @Path("path") page: Int,
        @Query("d") float: Float,
        @Query("d") long: Long,
        @Query("d") double: Double,
        @Query("d") byte: Byte,
        @Query("d") short: Short,
        @Query("d") char: Char,
        @Query("d") boolean: Boolean,
        @Query("d") string: String,
        @Body body: RequestBodyWrapper
    ): CommonResult<WanAndroidHome>

    /************************* 以下只 示例写法，接口调不通，因为找不到那么多 公开接口  全是 Retrofit的用法 来测试 *****************************************************/

    @PostBody("{\"ID\":\"Long\",\"name\":\"String\"}")
    @POST("https://www.wanandroid.com/user/register")
    suspend fun testPostBody222(@Body body: RequestBody): String

//    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")  //todo 固定 header
    @POST("https://xxxxxxx")
    suspend fun post1(@Body body: RequestBody): String

    //    @FormUrlEncoded
    @Headers("Content-Type: application/x-www-form-urlencoded")
    @POST("https://xxxxxxx22222")
    suspend fun post12(@Body body: RequestBody, @HeaderMap map: Map<String, String>): String //todo  HeaderMap 多个请求头部自己填写

    @POST("https://xxxxxxx22222")
    suspend fun post1222(@Body body: RequestBody, @HeaderMap map: Map<String, Any>): String //todo  HeaderMap 多个请求头部自己填写


    @Filter
    @POST("https://xxxxxxx22222")
    suspend fun post333(@Body body: RequestBody, @HeaderMap map: Map<String, Any>): String //todo  HeaderMap 多个请求头部自己填写
}