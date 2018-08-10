package com.windworkshop.antsnetscanner

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 * Created by Johnson on 2018/8/9.
 */
class MainScanerTabAdapter(fm: FragmentManager?, context : Context, fragments : ArrayList<Fragment>) : FragmentPagerAdapter(fm) {
    var fragment = fragments
    var tabTitles : Array<String> = arrayOf("搜索列表")
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