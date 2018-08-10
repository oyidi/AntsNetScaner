package com.windworkshop.antsnetscanner

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

/**
 * Created by Johnson on 2018/8/10.
 */
class UDPService : Service() {

    override fun onBind(p0: Intent?): IBinder {
        return UDPServiceBinder();
    }

    override fun onCreate() {
        super.onCreate()
        //device_list_text.setText("Getting")
        var thread : Thread = Thread(Runnable { kotlin.run {
            try {
                var udpSocket : DatagramSocket = DatagramSocket(8366);
                udpSocket.setSoTimeout(10000)
                val host = InetAddress.getByName("255.255.255.255")
                var  sendData : ByteArray = "hello".toByteArray()
                //指定包要发送的目的地
                val request = DatagramPacket(sendData, sendData.size, host, 8266)
                //为接受的数据包创建空间
                val response = DatagramPacket(ByteArray(1024), 1024)
                udpSocket.send(request)
                udpSocket.receive(response)

                val result = String(response.getData(), 0, response.getLength(), Charsets.US_ASCII)

                println(result)
            } catch (e : Exception){
                e.printStackTrace()
            } finally {

            }
        } });
        thread.start();
    }
    class UDPServiceBinder : Binder() {
        var deviceList : ArrayList<DeviceItem> = ArrayList()
        fun addDevice(serialNumber : String, macAddress : String, ip : String) {
            deviceList.add(DeviceItem(serialNumber, macAddress, ip))
        }
        fun getDevice(index : Int) : DeviceItem {
            return deviceList.get(index)
        }
    }

}