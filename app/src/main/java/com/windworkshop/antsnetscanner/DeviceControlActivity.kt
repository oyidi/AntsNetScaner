package com.windworkshop.antsnetscanner

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v7.app.AppCompatActivity
import com.windworkshop.antsnetscanner.fragment.DeviceControlIndexFragment
import com.windworkshop.antsnetscanner.fragment.ScanerFragment
import kotlinx.android.synthetic.main.device_control_layout.*

/**
 * Created by Johnson on 2018/8/14.
 */
class DeviceControlActivity : AppCompatActivity() {
    var fragments : ArrayList<Fragment> = ArrayList()
    var sn : String? = null
    var ip : String? = null
    var mac : String? = null
    var msg : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.device_control_layout)
        val intent : Intent = getIntent()
        control_toolbar.setTitleTextColor(Color.WHITE)
        setSupportActionBar(control_toolbar)
        var indexFragment : DeviceControlIndexFragment? = DeviceControlIndexFragment().getInstance()
        sn = intent.getStringExtra("sn")
        ip = intent.getStringExtra("ip")
        mac = intent.getStringExtra("mac")
        msg = intent.getStringExtra("info")
        indexFragment?.setData(sn, ip, mac, msg)
        fragments.add(indexFragment as Fragment)
        control_pager.adapter = TabAdapter(supportFragmentManager, fragments)
        control_tablayout.setupWithViewPager(control_pager);
        control_tablayout.setTabMode(TabLayout.MODE_FIXED);


    }
    class TabAdapter(fm: FragmentManager?, fragments : ArrayList<Fragment>) : FragmentPagerAdapter(fm) {
        var fragment = fragments
        var tabTitles : Array<String> = arrayOf("基本控制")
        override fun getItem(position: Int): Fragment {
            return fragment.get(position)
        }

        override fun getCount(): Int {
            return tabTitles.size
        }

        override fun getPageTitle(position: Int): CharSequence {
            return tabTitles[position]
        }
    }
}