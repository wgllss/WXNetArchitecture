package com.wx.architecture.data_source.net.http

import java.security.SecureRandom
import java.security.cert.CertificateException
import java.security.cert.X509Certificate
import javax.net.ssl.*

object HttpURLConnectionUtils {
    var mSSLSocketFactory: SSLSocketFactory? = null

    /**
     * 信任所有host
     */
    val hnv = HostnameVerifier { _, _ -> true }

    /**
     * 设置https
     *
     * @author :Atar
     * @createTime:2015-9-17下午4:57:39
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    fun trustAllHosts() {
        try {
            val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
                override fun getAcceptedIssuers(): Array<X509Certificate> {
                    return arrayOf()
                }

                @Throws(CertificateException::class)
                override fun checkClientTrusted(chain: Array<X509Certificate>, authType: String) {
                }

                @Throws(CertificateException::class)
                override fun checkServerTrusted(chain: Array<X509Certificate>, authType: String) {
                }
            })
            val sc = SSLContext.getInstance("TLS")
            sc.init(null, trustAllCerts, SecureRandom())
            if (mSSLSocketFactory == null) {
                mSSLSocketFactory = sc.socketFactory
            }
            HttpsURLConnection.setDefaultHostnameVerifier(hnv)
            HttpsURLConnection.setDefaultSSLSocketFactory(mSSLSocketFactory)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}