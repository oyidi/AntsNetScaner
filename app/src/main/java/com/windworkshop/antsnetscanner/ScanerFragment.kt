package com.windworkshop.antsnetscanner

import android.app.Fragment
import android.app.Service
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView

/**
 * Created by Johnson on 2018/8/7.
 */
class ScanerFragment : LazyLoadFragment() {
    var scanerListview : RecyclerView? = null
    var serviceInterface : UDPService.UDPServiceBinder? = null
    companion object {
        var instance : ScanerFragment? = null
    }
    public fun getInstan() : ScanerFragment? {
        if (instance != null) {
            return instance
        } else {
            instance = ScanerFragment()
            return instance
        }
    }
    override fun onInvisible() {

    }

    override fun initData() {
        context.bindService(Intent(context, UDPService::class.java),connection, Service.BIND_AUTO_CREATE)
        //context.startService(Intent(context, UDPService::class.java))
    }

    override fun initView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View {

        var v : View = inflater!!.inflate(R.layout.scaner_list_layout, null)
        scanerListview = v.findViewById(R.id.scaner_list)

        setPageName("搜索列表")
        return v
    }

    override fun initPrepare() {

    }
    var connection = object : ServiceConnection {
        override fun onServiceDisconnected(p0: ComponentName?) {
            println("服务解除绑定")
        }

        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            println("服务已绑定")
            serviceInterface = p1 as UDPService.UDPServiceBinder
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        context.unbindService(connection)
    }
}