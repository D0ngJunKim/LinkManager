package com.linkmanager.lib.dao

import android.util.Log
import com.google.gson.reflect.TypeToken
import com.linkmanager.lib.LinkInterface
import com.linkmanager.lib.LinkManager
import com.linkmanager.lib.constants.ConvoyDataKey
import com.linkmanager.lib.constants.LinkManagerInterface
import com.linkmanager.lib.constants.WebActionType
import java.lang.reflect.Method

/**
 * Created by 180842 on 2023/11/07.
 */
class UrlParserRunner(val origin: UrlInfoItemDiData) {

    val name: String = origin.name.orEmpty()
    val webActionTypes: ArrayList<WebActionType> = arrayListOf()

    // Url에 포함되어 있는지 검사
    val checkUrls: ArrayList<String> = origin.checkUrls?.filterNotNullTo(arrayListOf())
            ?: arrayListOf()

    // Host 체크
    val checkUrlsHost: ArrayList<String> = origin.checkUrlsHost?.filterNotNullTo(arrayListOf())
            ?: arrayListOf()

    // Url이 모두 같은지 검사
    val sameUrls: ArrayList<String> = origin.sameUrls?.filterNotNullTo(arrayListOf())
            ?: arrayListOf()

    // 패턴 체크
    val patterns: ArrayList<String> = origin.patterns?.filterNotNullTo(arrayListOf())
            ?: arrayListOf()

    // 파라미터 키 검사
    val parameterKeys: ArrayList<String> = origin.parameterKeys?.filterNotNullTo(arrayListOf())
            ?: arrayListOf()

    // Custom Url / Url 함수 명
    private val funNames: ArrayList<String> = arrayListOf()

    // 함수 실행 후 결과 값.
    private var isSuccess: Boolean = true

    private var isRunning: Boolean = false

    init {
        origin.webActionTypes?.mapNotNullTo(webActionTypes) { actionType ->
            when (actionType) {
                WebActionType.PRE_ACTION.rawValue -> WebActionType.PRE_ACTION
                WebActionType.POST_ACTION.rawValue -> WebActionType.POST_ACTION
                else -> null
            }
        }

        for (funName in origin.funNames?.filterNotNull() ?: arrayListOf()) {
            val arrayFN = funName.split(".")
            if (arrayFN.size > 1) {
                funNames.add(arrayFN.last())
            }
        }
    }

    fun runFunction(url: String,
                    data: MutableMap<String, Any?>?): Boolean {
        if (isRunning) return true
        isRunning = true

        val toData: MutableMap<String, Any?>

        if (data == null) {
            toData = mutableMapOf<String, Any?>().apply {
                put(ConvoyDataKey.CALLER, arrayListOf(this@UrlParserRunner.name))
            }
        } else {
            val names = arrayListOf<String>()
            (data[ConvoyDataKey.CALLER] as? ArrayList<*>)?.filterIsInstanceTo(names, String::class.java)
            names.add(this@UrlParserRunner.name)
            data[ConvoyDataKey.CALLER] = names

            toData = data
        }

        val mapType = object : TypeToken<MutableMap<String, Any?>>() {}.rawType
        isSuccess = false
        for (funName in funNames) {
            Log.i("LinkManager", " --- ✈️️ UrlItemRunner RunFunction ✈️️ --- ")

            try {
                val method: Method = LinkInterface::class.java.getMethod(funName, String::class.java, mapType)
                        ?: continue

                if (!method.isAnnotationPresent(LinkManagerInterface::class.java)) continue
                if (method.returnType != Boolean::class.java) continue

                method.isAccessible = true
                isSuccess = (method.invoke(LinkInterface, url, toData) as? Boolean) ?: false

                Log.i(LinkManager.TAG, ">>> 호출 함수 : $funName")
                Log.i(LinkManager.TAG, ">>> 실행 함수 : ${method.name}")
                Log.i(LinkManager.TAG, ">>> 함수 설명 : ${method.getAnnotation(LinkManagerInterface::class.java)?.description.orEmpty()}")
                Log.i(LinkManager.TAG, ">>> ${if (isSuccess) "✅ SUCCESS ✅" else "⛔ FAIL ⛔️"}")
                Log.i(LinkManager.TAG, ">>> Data : ${
                    toData.toString()
                            .replace("{", "{\n")
                            .replace("=", " = ")
                            .replace(",", ",\n")
                            .replace("}", " }")
                }")
            } catch (e: Exception) {
                Log.i(LinkManager.TAG, ">>> ❗️ Exception ❗️ : \n${e.printStackTrace()}")
            }
        }

        isRunning = false

        return isSuccess
    }
}