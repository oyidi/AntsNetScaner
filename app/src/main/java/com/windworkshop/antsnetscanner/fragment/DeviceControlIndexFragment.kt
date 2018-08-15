package com.windworkshop.antsnetscanner.fragment

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.windworkshop.antsnetscanner.NativeHttpRequestOkhttp
import com.windworkshop.antsnetscanner.R
import org.json.JSONObject

/**
 * Created by Johnson on 2018/8/15.
 */
class DeviceControlIndexFragment() : LazyLoadFragment() {
    var sn : String? = null
    var ip : String? = null
    var mac : String? = null
    var msg : String? = null
    var handler : Handler = Handler()
    companion object {
        var instance : DeviceControlIndexFragment? = null
    }
    public fun getInstance() : DeviceControlIndexFragment? {
        if (instance != null) {
            return instance
        } else {
            instance = DeviceControlIndexFragment()
            return instance
        }
    }

    fun setData(sn : String, ip : String, mac : String, msg : String) {
        this.sn = sn
        this.ip = ip
        this.mac = mac
        this.msg = msg
    }
    override fun initPrepare() {

    }

    override fun onInvisible() {

    }

    override fun initData() {

    }
    var statueText : TextView? = null
    var buttonClickListener : View.OnClickListener = View.OnClickListener {
        if(ip != null) {
            if(it.id == R.id.device_control_index_switch_screen) {

                var requester : NativeHttpRequestOkhttp = NativeHttpRequestOkhttp()
                requester.getRequest("http://$ip/setup?p=switch_screen", object : NativeHttpRequestOkhttp.IHttpRequest{
                    override fun onReadyRequest() {
                        println("onReadyRequest")
                    }
                    override fun onRequestRecive(isException : Boolean, result: String?) {
                        println("onRequestRecive $result")
                    }
                })

//                requester.postRequestWithData("http://" + ip + "/setup?p=switch_screen", "", object : NativeHttpRequestOkhttp.IHttpRequest{
//                    override fun onReadyRequest() {
//
//                    }
//                    override fun onRequestRecive(isException : Boolean, result: String?) {
//
//                    }
//                })
            }
        }
    }
    override fun initView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {
        var v : View = inflater!!.inflate(R.layout.device_control_index_fragment, null)
        statueText = v.findViewById(R.id.device_control_index_statue)
        var switchScreenButton : Button = v.findViewById(R.id.device_control_index_switch_screen)
        switchScreenButton.setOnClickListener(buttonClickListener)
        var hideScreenButton : Button = v.findViewById(R.id.device_control_index_hide_screen)
        hideScreenButton.setOnClickListener(buttonClickListener)
        var setTimeButton : Button = v.findViewById(R.id.device_control_index_set_time)
        setTimeButton.setOnClickListener(buttonClickListener)
        var showDetialButton : Button = v.findViewById(R.id.device_control_index_show_detail)
        showDetialButton.setOnClickListener(buttonClickListener)
        var rebootButton : Button = v.findViewById(R.id.device_control_index_reboot)
        rebootButton.setOnClickListener(buttonClickListener)
        return  v
    }
    var updateStatueRunnable : Runnable = Runnable {
        if(ip != null) {
            var requester : NativeHttpRequestOkhttp = NativeHttpRequestOkhttp()
            requester.getRequest("http://$ip/device?action=get_dht", object  : NativeHttpRequestOkhttp.IHttpRequest {
                override fun onRequestRecive(isException: Boolean, result: String?) {
                    if(isException == false) {
                        var json : JSONObject = JSONObject(result)
                        statueText?.text = "温度：" + json["temp"] + "\n湿度：" + json["hum"]
                    }

                }
                override fun onReadyRequest() {

                }
            })
        }

    }
}