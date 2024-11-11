package com.wx.architecture.data_source.net.http

import java.io.*
import java.net.*
import javax.net.ssl.HttpsURLConnection

object HttpUrlConnectionRequest {

    fun getResult(httpurl: String, header: MutableMap<String, String>?, inputEncoding: String = "UTF-8"): String {
        var result = ""
        var statusCode = 0
        try {
            val url = URL(httpurl)
            var conn: HttpURLConnection? = null
            if ("https" == url.protocol) {
                HttpURLConnectionUtils.trustAllHosts()
                val https = url.openConnection() as HttpsURLConnection
                HttpsURLConnection.setDefaultHostnameVerifier(HttpURLConnectionUtils.hnv)
                https.hostnameVerifier = HttpURLConnectionUtils.hnv
                HttpsURLConnection.setDefaultSSLSocketFactory(HttpURLConnectionUtils.mSSLSocketFactory)
                https.sslSocketFactory = HttpURLConnectionUtils.mSSLSocketFactory
                https.connectTimeout = 30000
                https.readTimeout = 30000
                conn = https
            } else {
                conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = 30000
                conn!!.readTimeout = 30000
            }
            conn!!.doInput = true
            conn.requestMethod = "GET"
            conn.useCaches = false
            conn.instanceFollowRedirects = true
            header?.forEach {
                conn.setRequestProperty(it.key, it.value)
            }
            conn.setRequestProperty("Connection", "Keep-Alive")
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.connect()
            statusCode = conn.responseCode
            var br: BufferedReader? = null
            var isr: InputStreamReader? = null
            if (statusCode == 200) {
                isr = InputStreamReader(conn.inputStream, inputEncoding)
                br = BufferedReader(isr)
                var tmp: String? = null
                while (br.readLine().also { tmp = it } != null) {
                    result += tmp
                }
                // saveCookie(conn);
            } else {
            }
            br!!.close()
            isr!!.close()
            conn.disconnect()
        } catch (e: Exception) { // 联网超时
            throw e
        }
        return result
    }

    fun postData(httpurl: String, header: MutableMap<String, String>?, body: String? = null, inputEncoding: String = "UTF-8"): String {
        var result = ""
        var statusCode = 0
        try {
            val url = URL(httpurl)
            var conn: HttpURLConnection? = null
            if ("https" == url.protocol) {
                HttpURLConnectionUtils.trustAllHosts()
                val https = url.openConnection() as HttpsURLConnection
                HttpsURLConnection.setDefaultHostnameVerifier(HttpURLConnectionUtils.hnv)
                https.hostnameVerifier = HttpURLConnectionUtils.hnv
                HttpsURLConnection.setDefaultSSLSocketFactory(HttpURLConnectionUtils.mSSLSocketFactory)
                https.sslSocketFactory = HttpURLConnectionUtils.mSSLSocketFactory
                https.connectTimeout = 30000
                https.readTimeout = 30000
                conn = https
            } else {
                conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = 30000
                conn!!.readTimeout = 30000
            }
            conn!!.doInput = true
            conn.doOutput = true
            conn.requestMethod = "POST"
            conn.useCaches = false
            conn.instanceFollowRedirects = true
            header?.forEach {
                conn.setRequestProperty(it.key, it.value)
            }
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.setRequestProperty("Charset", inputEncoding)
            conn.connect()
            var dos: DataOutputStream? = null
            if (body != null && body.length > 0) {
                dos = DataOutputStream(conn.outputStream)
                dos.write(body.toByteArray())
                dos.flush()
            }
            statusCode = conn.responseCode
            var br: BufferedReader? = null
            var isr: InputStreamReader? = null
            if (statusCode == 200) {
                isr = InputStreamReader(conn.inputStream)
                br = BufferedReader(isr)
                var tmp: String? = null
                while (br.readLine().also { tmp = it } != null) {
                    result += tmp
                }
                // saveCookie(conn);
            } else {
            }
            dos?.close()
            br!!.close()
            isr!!.close()
            conn.disconnect()
        } catch (e: Exception) { // 联网超时
            throw e
        }
        return result
    }
}