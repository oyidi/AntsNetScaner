package com.windworkshop.antsnetscanner

import android.os.AsyncTask
import com.squareup.okhttp.*
import java.io.IOException
import java.util.concurrent.TimeUnit
import com.squareup.okhttp.RequestBody
import com.windworkshop.antsnetscanner.NativeHttpRequestOkhttp.IHttpRequest
import com.squareup.okhttp.OkHttpClient







/**
 * Created by Johnson on 2018/8/15.
 */
class NativeHttpRequestOkhttp {

    companion object {
        var client: OkHttpClient? = createNormalClient()
        fun createNormalClient(): OkHttpClient {
            var okhttp : OkHttpClient = OkHttpClient()
            okhttp.setWriteTimeout(10, TimeUnit.SECONDS)
            okhttp.setReadTimeout(10, TimeUnit.SECONDS)
            okhttp.setConnectTimeout(10, TimeUnit.SECONDS)
            return okhttp
        }
        fun createNormalRequest(url: String): Request {
            return Request.Builder().url(url).build()
        }
    }



    fun postRequestWithData(url: String?, data: String, requestReciver: IHttpRequest) {
        val postThread = PostThread(url, requestReciver, data)
        postThread.execute()
    }

    fun postRequest(url: String?, requester: IHttpRequest, keydata : ArrayList<HttpKeyData>) {
        val postThread = PostThread(url, requester, keydata)
        postThread.execute()
    }

    fun getRequest(url: String, requester: IHttpRequest) {
        try {
            val request = NativeHttpRequestOkhttp.createNormalRequest(url)
            val call = client?.newCall(request)
            call?.enqueue(object : Callback {
                override fun onFailure(request: Request, e: IOException) {
                    requester?.onRequestRecive(true, e.toString())
                }
                override fun onResponse(response: Response) {
                    requester?.onRequestRecive(false, response.body().string())
                }
            })

        } catch (e: Exception) {
            e.printStackTrace()
        }
        //var getThread = GetThread(url, requester)
        //getThread.execute()
    }
    class GetThread : AsyncTask<String, Int, String> {
        var requestReciver: IHttpRequest? = null
        var url : String = ""
        var result : String = ""
        var error : Boolean = false
        constructor(url: String, requestReciver: IHttpRequest){
            this.url = url
            this.requestReciver = requestReciver

        }

        override fun doInBackground(vararg p0: String?): String {
            try {
                val request = NativeHttpRequestOkhttp.createNormalRequest(url)
                val call = client?.newCall(request)
                call?.enqueue(object : Callback {
                    override fun onFailure(request: Request, e: IOException) {
                        requestReciver?.onRequestRecive(true, e.toString())
                        //error = true
                        //result = e.toString()
                    }
                    override fun onResponse(response: Response) {
                        requestReciver?.onRequestRecive(false, response.body().string())
                        //result = response.body().string()
                    }
                })

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return ""
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
           // requestReciver?.onRequestRecive(error, result)
        }

    }
    class PostThread : AsyncTask<String, Int, String> {
        var commandList = ArrayList<HttpKeyData>()
        var requestReciver: IHttpRequest? = null
        private var postUrl: String? = null
        private var sendData: String? = null
        private var result = ""
        var error : Boolean = false

        constructor(url: String?, requestReciver: IHttpRequest, keydata : ArrayList<HttpKeyData>){
            this.requestReciver = requestReciver
            // 载入参数池
            for (i in keydata.indices) {
                commandList.add(keydata[i])
            }
            this.postUrl = url
        }

        constructor(url: String?, requestReciver: IHttpRequest, data: String) {
            this.requestReciver = requestReciver
            this.sendData = data
            this.postUrl = url
        }

        override fun onPreExecute() {
            super.onPreExecute()
            requestReciver?.onReadyRequest()
        }
        override fun doInBackground(vararg p0: String?): String {
            try {
                val body: RequestBody
                // 压入参数
                if (sendData != null) {
                    body = RequestBody.create(MediaType.parse("application/json"), sendData)
                    //val request = Request.Builder().url(postUrl).post(body).build()
                } else {
                    var postData = ""
                    for (i in 0 until commandList.size) {
                        postData = postData + commandList[i].key + "=" + commandList[i].value + "&"
                    }
                    postData.subSequence(0, postData.length - 1)
                    body = RequestBody.create(MediaType.parse("application/x-www-form-urlencoded"), postData)
                }
                val request = Request.Builder().url(postUrl).post(body).build()
                val response = createNormalClient().newCall(request).execute()
                if (response.isSuccessful()) {
                    result = response.body().string()
                } else {
                    error = true
                    result = "ERROR"
                }
            } catch (e: IOException) {
                e.printStackTrace()
                error = true
                result = "ERROR"
            }

            return result
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)
            requestReciver?.onRequestRecive(error, result)
        }
    }

    class HttpKeyData(var key: String, var value: String) {
        companion object {
            fun create(key: String, value: String): HttpKeyData {
                return HttpKeyData(key, value)
            }
        }
    }

    public interface IHttpRequest {
        fun onReadyRequest()
        fun onRequestRecive(isException : Boolean, result: String?)
    }
}