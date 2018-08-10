package com.windworkshop.antsnetscanner

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import java.net.DatagramSocket
import java.lang.reflect.Array.getLength
import android.system.Os.socket
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*
import java.net.DatagramPacket
import java.net.InetAddress


class MainActivity : AppCompatActivity() {
    var fragments : ArrayList<Fragment> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(main_toolbar)
        fragments.add(ScanerFragment().getInstan() as Fragment)
        var fragmentManager : MainScanerTabAdapter = MainScanerTabAdapter(supportFragmentManager, this, fragments)
        viewPager.setAdapter(fragmentManager)
        main_tabs.setupWithViewPager(viewPager);
        main_tabs.setTabMode(TabLayout.MODE_FIXED);

        //device_list_text.setText("Getting")
//        var thread : Thread = Thread(Runnable { kotlin.run {
//            try {
//                var udpSocket : DatagramSocket = DatagramSocket(8366);
//                udpSocket.setSoTimeout(10000)
//                val host = InetAddress.getByName("255.255.255.255")
//                var  sendData : ByteArray = "hello".toByteArray()
//                //指定包要发送的目的地
//                val request = DatagramPacket(sendData, sendData.size, host, 8266)
//                //为接受的数据包创建空间
//                val response = DatagramPacket(ByteArray(1024), 1024)
//                udpSocket.send(request)
//                udpSocket.receive(response)
//
//                val result = String(response.getData(), 0, response.getLength(), Charsets.US_ASCII)
//
//                println(result)
//                //device_list_text.setText(device_list_text.text.toString() + "\n" + result)
//            } catch (e : Exception){
//                e.printStackTrace()
//                //device_list_text.text = "timeout"
//            } finally {
//
//            }
//        } });
//        thread.start();
    }
}
