package com.wx.test.api.retrofit

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class HeaderInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()
        val authorised = request
            .newBuilder()
            .addHeader("Connection", "keep-alive") //
            .addHeader("Accept-Language", "zh-CN,zh;q=0.9") //
            .addHeader(
                "Authorization",
                "Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJtZXJjaGFudElkIjo1OTIsInVzZXJfbmFtZSI6IjE4OTgyOTkwNzAyIiwib3BlbmlkIjpudWxsLCJzY29wZSI6WyJhbGwiXSwiaWQiOjY5MSwiY2xpZW50X3NvdXJjZSI6bnVsbCwiZXhwIjoxNzI2MjIzNzYyLCJhdXRob3JpdGllcyI6WyJyYmFjXzEzX-aXoOS6uuWUruWNluWVhuWutiJdLCJqdGkiOiJmYmVhMGRlZi01MjNmLTRkYTctYjY3Yy1lMjljZjJkYmU1YzUiLCJjbGllbnRfaWQiOiJjdXN0b21lci1hcHAiLCJyZWFsbmFtZSI6bnVsbH0.f4QtuwWdj096Dzp2o4aVyEnDof3-7gR7NX-LX-j8KbBmXwdYtKG32AvRKxY5t5ubwhTqe0IHkobZZqTcub4bnd8DdmSUJa0Q8UQP1EgMWuUubSuojHifOctNzZFlc74v28NUbzlhslmQNzHL5f7zxjP7zVdMfKvGD9eSjeHlG8w"
            ) //
            .addHeader("Accept", "application/json, text/javascript, */*") //
//            .addHeader("Referer", " https://wwz.lanzouj.com/ihT5x20ulecf") //https
//            .addHeader("Upgrade-insecure-Requests", "1") //
//            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36") //
//        if (request.header("User-Agent") == null) {
        authorised.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.45 Safari/537.36") //
//        }
        return chain.proceed(authorised.build())
//        return chain.proceed(authorised.build())
    }
}