package com.windworkshop.antsnetscanner

import android.app.Service
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.Handler
import android.os.IBinder
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

/**
 * Created by Johnson on 2018/8/7.
 */
class ScanerFragment : LazyLoadFragment() {
    var scanerListview : RecyclerView? = null
    var scanerListviewAdapter : UDPDeviceListAdapter? = null
    var serviceInterface : UDPService.UDPServiceBinder? = null

    var hander : Handler = Handler()
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
        scanerListviewAdapter = UDPDeviceListAdapter(context)
        scanerListview?.setAdapter(scanerListviewAdapter)
        scanerListview?.layoutManager = LinearLayoutManager(context)
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
            val listener = object : DeviceItemChangeListener {
                override fun onAddDevice(device : DeviceItem) {
                    println("add device")
                    scanerListviewAdapter?.addDevice(device)
                    hander.post(updateListRunnable)
                }
            }
            serviceInterface?.setListener(listener)
            serviceInterface?.startScaner()
        }

    }
    var updateListRunnable : Runnable = Runnable {
        kotlin.run { scanerListviewAdapter?.notifyDataSetChanged() }
    }
    override fun onDestroy() {
        super.onDestroy()
        context.unbindService(connection)
    }
    class UDPDeviceListAdapter : RecyclerView.Adapter<UDPDeviceListAdapterViewHolder> {
        var deviceList : ArrayList<DeviceItem> = ArrayList()
        var context : Context? = null
        constructor(context : Context, list : ArrayList<DeviceItem>) {
            deviceList = list
            this.context = context
        }
        constructor(context: Context) {
            this.context = context
        }
        fun addDevice(item : DeviceItem) {
            deviceList.add(item)
            println("UDPDeviceListAdapter addDevice")
        }
        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): UDPDeviceListAdapterViewHolder {
            val v = LayoutInflater.from(context).inflate(R.layout.scaner_list_item_layout, parent, false)
            return UDPDeviceListAdapterViewHolder(v)
        }

        override fun getItemCount(): Int {
            return deviceList.size
        }

        override fun onBindViewHolder(holder: UDPDeviceListAdapterViewHolder?, position: Int) {
            holder?.itemView?.setTag(position)
            holder?.titleText?.setText("编号：" + deviceList.get(position).serialNumber);
            holder?.detailText?.setText("MAC:" + deviceList.get(position).macAddress + " ip:" + deviceList.get(position).ipAddress + " 自定义信息:" + deviceList.get(position).diymsg)
            println("onBindViewHolder " + position)
        }

    }
    class UDPDeviceListAdapterViewHolder : RecyclerView.ViewHolder {
        var titleText : TextView? = null
        var detailText : TextView? = null
        constructor(itemView: View?) : super(itemView) {
            titleText = itemView?.findViewById(R.id.scaner_list_item_title)
            detailText = itemView?.findViewById(R.id.scaner_list_item_detail)
        }
    }
}