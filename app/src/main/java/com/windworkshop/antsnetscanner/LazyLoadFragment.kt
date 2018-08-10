package com.windworkshop.antsnetscanner

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View


/**
 * Created by Johnson on 2018/8/8.
 */
abstract class LazyLoadFragment : Fragment() {
    protected var mRootView: View? = null
    protected var mContext: Context? = null
    var isVisiable: Boolean = false
    private var isPrepared: Boolean = false
    private var isFirst = true
    private var pageName: String? = null
    protected fun setPageName(name: String) {
        pageName = name
    }
    //--------------------system method callback------------------------//

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isPrepared = true
        initPrepare()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (userVisibleHint) {
            isVisiable = true
            lazyLoad()
        } else {
            isVisiable = false
            onInvisible()
        }
    }

    override fun onResume() {
        super.onResume()
        //StatService.onPageStart(context, if (pageName == null) this.javaClass.name else pageName)
        if (userVisibleHint) {
            userVisibleHint = true
        }
    }

    override fun onCreate( savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = activity
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mRootView == null) {
            mRootView = initView(inflater, container, savedInstanceState)
        }

        return mRootView
    }

//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle): View? {
//        if (mRootView == null) {
//            mRootView = initView(inflater, container, savedInstanceState)
//        }
//
//        return mRootView
//    }

    //--------------------------------method---------------------------//

    /**
     * 懒加载
     */
    protected fun lazyLoad() {
        if (!isPrepared || !isVisible || !isFirst) {
            return
        }
        initData()
        isFirst = false
    }

    //--------------------------abstract method------------------------//

    /**
     * 在onActivityCreated中调用的方法，可以用来进行初始化操作。
     */
    protected abstract fun initPrepare()

    /**
     * fragment被设置为不可见时调用
     */
    protected abstract fun onInvisible()

    /**
     * 这里获取数据，刷新界面
     */
    protected abstract fun initData()

    /**
     * 初始化布局，请不要把耗时操作放在这个方法里，这个方法用来提供一个
     * 基本的布局而非一个完整的布局，以免ViewPager预加载消耗大量的资源。
     */
    protected abstract fun initView(inflater: LayoutInflater?,
                                    container: ViewGroup?,
                                    savedInstanceState: Bundle?): View

    override fun onPause() {
        super.onPause()
        //StatService.onPause(getContext());
        //StatService.onPageEnd(context, if (pageName == null) this.javaClass.name else pageName)
    }
}