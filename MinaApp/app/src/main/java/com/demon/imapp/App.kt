package com.demon.imapp

import android.app.Application
import com.demon.imapp.util.SPUtil

/**
 * @author DeMon
 * Created on 2020/5/14.
 * E-mail 757454343@qq.com
 * Desc:
 */
class App : Application() {


    override fun onCreate() {
        super.onCreate()
        SPUtil.getInstance().init(applicationContext)
    }
}