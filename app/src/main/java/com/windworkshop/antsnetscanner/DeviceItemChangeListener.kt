package com.windworkshop.antsnetscanner

/**
 * Created by Johnson on 2018/8/11.
 */
interface DeviceItemChangeListener {
    public fun onAddDevice(device : DeviceItem)
    public fun onStartScaner()
    public fun onFinishScaner()
}