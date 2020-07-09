package com.demon.imapp.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.demon.imapp.R


/**
 * @author DeMon
 * Created on 2020/5/14.
 * E-mail 757454343@qq.com
 * Desc:
 */
class SPUtil {
    private lateinit var sp: SharedPreferences
    private lateinit var context: Context


    companion object {
        fun getInstance() = Helper.instance
    }

    private object Helper {
        val instance = SPUtil()
    }


    fun init(context: Context) {
        this.context = context;
        sp = context.getSharedPreferences(context.getString(R.string.app_name), MODE_PRIVATE)
    }


    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    fun put(key: String?, `object`: Any) {
        val editor = sp.edit()
        editor?.run {
            when (`object`) {
                is String -> {
                    editor.putString(key, `object`)
                }
                is Int -> {
                    editor.putInt(key, `object`)
                }
                is Boolean -> {
                    editor.putBoolean(key, `object`)
                }
                is Float -> {
                    editor.putFloat(key, `object`)
                }
                is Long -> {
                    editor.putLong(key, `object`)
                }
                else -> {
                    editor.putString(key, `object`.toString())
                }
            }
            commit()
        }
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
     fun get(key: String?, defaultObject: Any?): Any? {
        when (defaultObject) {
            is String -> {
                return sp.getString(key, defaultObject as String?)
            }
            is Int -> {
                return sp.getInt(key, (defaultObject as Int?)!!)
            }
            is Boolean -> {
                return sp.getBoolean(key, (defaultObject as Boolean?)!!)
            }
            is Float -> {
                return sp.getFloat(key, (defaultObject as Float?)!!)
            }
            is Long -> {
                return sp.getLong(key, (defaultObject as Long?)!!)
            }
            else -> return null
        }
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    fun remove(key: String?) {
        val editor = sp.edit()
        editor?.remove(key)
        editor?.apply()
    }

    /**
     * 清除所有数据
     */
    fun clear() {
        val editor = sp.edit()
        editor?.clear()
        editor?.apply()
    }

    /**
     * 查询某个key是否已经存在
     *
     * @param key
     * @return
     */
    operator fun contains(key: String?): Boolean {
        return sp.contains(key)
    }

    /**
     * 返回所有的键值对
     *
     * @return
     */
    fun getAll(): Map<String?, *>? {
        return sp.all
    }

}