package com.linkmanager.lib

import android.util.Log
import com.linkmanager.lib.constants.LinkManagerException
import com.linkmanager.lib.constants.WebActionType
import com.linkmanager.lib.extension.isNormalUrl

/**
 * Created by 180842 on 2023/11/07.
 */
object LinkManager {
    const val TAG = "LinkManager"
    private val exceptionMethod = LinkInterface::class.java.methods.find { it.isAnnotationPresent(LinkManagerException::class.java) }

    @JvmOverloads
    fun openUrl(url: String?, data: MutableMap<String, Any?>? = null): Boolean {
        Log.i(TAG, " --- ⚡️ LinkManager OpenUrl ⚡️ --- ")
        Log.i(TAG, "Url : $url")

        if (url.isNullOrEmpty() || url == "#") {
            Log.i(TAG, "유효하지 않은 경로입니다.\nUrl이 비워져 있습니다.")
            return false
        }

        val targetUrl = url.trim()
        if (targetUrl != url) {
            Log.i(TAG, " --- ❓❗Url Modified ❓❗--- ")
            Log.i(TAG, "Url : $targetUrl")
        }

        var isSuccess = false
        val toData: MutableMap<String, Any?> = data ?: mutableMapOf()

        UrlParser.checkUrl(WebActionType.PRE_ACTION, targetUrl)?.run {
            isSuccess = runFunction(targetUrl, toData)
        }

        if (!isSuccess) {
            if (targetUrl.isNormalUrl()) {
                try {
                    exceptionMethod?.invoke(LinkInterface, targetUrl, toData)
                    Log.i(TAG, " --- ⛱️️ LinkManager ${exceptionMethod?.name} ⛱️️️ --- ")
                    Log.i(TAG, "함수 설명 : ${exceptionMethod?.getAnnotation(LinkManagerException::class.java)?.description.orEmpty()}")
                } catch (_: Exception) {

                }
            } else {
                Log.i(TAG, "유효하지 않은 경로입니다.\n\n($targetUrl)")
            }
        }

        return isSuccess
    }
}