package com.windworkshop.antsnetscanner

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import org.json.JSONObject
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.net.InetSocketAddress

/**
 * Created by Johnson on 2018/8/10.
 */
class UDPService : Service() {
    var udpSocket : DatagramSocket? = null
    var controlInterface = object : IUDPServiceControl {
        override  fun startScaner() {
            var thread : Thread = Thread(Runnable { kotlin.run {
                binder.onStart()
                try {
                    if(udpSocket == null) {
                        udpSocket = DatagramSocket(null);
                        udpSocket?.setReuseAddress(true);
                        udpSocket?.bind(InetSocketAddress(8366));
                    }

                    udpSocket?.setSoTimeout(10000)
                    val host = InetAddress.getByName("255.255.255.255")
                    var  sendData : ByteArray = "hello".toByteArray()
                    //指定包要发送的目的地
                    val request = DatagramPacket(sendData, sendData.size, host, 8266)
                    //为接受的数据包创建空间
                    val response = DatagramPacket(ByteArray(1024), 1024)
                    udpSocket?.send(request)

                    var delayTime : Long = System.currentTimeMillis() + 10000

                    while (System.currentTimeMillis() < delayTime) {
                        udpSocket?.receive(response)
                        val result = String(response.getData(), 0, response.getLength(), Charsets.US_ASCII)
                        println(response.socketAddress)
                        val json : JSONObject = JSONObject(result)
                        println(result)
                        var ip : String = response.socketAddress.toString()
                        ip = ip.substring(1, ip.indexOf(":"))
                        binder.addDevice(json.getString("id"), json.getString("mac"), ip, json.getString("msg"))
                    }
                    //udpSocket?.close()
                } catch (e : Exception){
                    e.printStackTrace()
                } finally {

                }
                binder.onFinish()
            } });
            thread.start();
        }
    }
    var binder : UDPServiceBinder = UDPServiceBinder(controlInterface)
    override fun onBind(p0: Intent?): IBinder {
        return binder;
    }

    override fun onCreate() {
        super.onCreate()
        //device_list_text.setText("Getting")

    }
    class UDPServiceBinder(serviceListener : IUDPServiceControl) : Binder() {
        var deviceListener : DeviceItemChangeListener? = null
        var deviceList : ArrayList<DeviceItem> = ArrayList()
        var serviceListener : IUDPServiceControl = serviceListener
        fun addDevice(serialNumber : String, macAddress : String, ip : String, msg : String) {
           var  device : DeviceItem = DeviceItem(serialNumber, macAddress, ip, msg)
            deviceList.add(device)
            if(deviceListener != null) {
                deviceListener?.onAddDevice(device)
            }
        }
        fun onStart() {
            if(deviceListener != null) {
                //deviceListener?.onStartScaner()
            }
        }
        fun onFinish() {
            if(deviceListener != null) {
                println("scan finish")
                deviceListener?.onFinishScaner()
            }
        }
        fun getDevice(index : Int) : DeviceItem {
            return deviceList.get(index)
        }
        fun setListener(listener : DeviceItemChangeListener?) {
            deviceListener = listener
        }
        fun startScaner() {
            serviceListener.startScaner()
        }
    }
    interface IUDPServiceControl {
        fun startScaner()
    }
}