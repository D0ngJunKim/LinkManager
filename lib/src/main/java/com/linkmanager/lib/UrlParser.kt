package com.linkmanager.lib

import android.app.Application
import android.net.Uri
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.linkmanager.lib.constants.WebActionType
import com.linkmanager.lib.dao.UrlInfoDiData
import com.linkmanager.lib.dao.UrlInfoItemDiData
import com.linkmanager.lib.dao.UrlParserRunner
import com.linkmanager.lib.extension.loadJsonString
import com.linkmanager.lib.extension.removeLastSlash
import com.linkmanager.lib.extension.removeProtocol
import java.util.EnumMap


object UrlParser {
    private const val WEB_URL_LIST_JSON = "webUrlList.json"

    private var dataUrlMap: MutableMap<WebActionType, ArrayList<UrlParserRunner>> = EnumMap(WebActionType::class.java)


    fun init(application: Application) {
        dataUrlMap = loadJsonData(application)
    }

    fun getWebUrlList(): List<UrlInfoItemDiData> {
        val dataList = arrayListOf<UrlInfoItemDiData>()
        dataUrlMap.forEach { (_, value) ->
            dataList.addAll(value.map { it.origin })
        }
        return Gson().fromJson(Gson().toJson(dataList),
                object : TypeToken<List<UrlInfoItemDiData>>() {}.type)
    }

    private fun loadJsonData(application: Application): EnumMap<WebActionType, ArrayList<UrlParserRunner>> {
        val result: EnumMap<WebActionType, ArrayList<UrlParserRunner>> = EnumMap(WebActionType::class.java)

        for (actionType in WebActionType.entries) {
            result[actionType] = arrayListOf()
        }

        try {
            loadJsonString<UrlInfoDiData>(application.assets
                    .open(WEB_URL_LIST_JSON)
                    .bufferedReader()
                    .use { it.readText() }).webUrlList?.forEach { item ->
                if (item != null && item.isValid()) {
                    val runner = UrlParserRunner(item)
                    if (runner.webActionTypes.isNotEmpty()) {
                        for (actionType in runner.webActionTypes) {
                            result[actionType]?.add(runner)
                        }
                    } else {
                        result[WebActionType.PRE_ACTION]?.add(runner)
                    }
                }
            }
        } catch (_: Exception) {

        }

        return result
    }


    fun checkUrl(webActionType: WebActionType, url: String?): UrlParserRunner? {
        val urlList = dataUrlMap[webActionType] ?: return null
        return checkUrlList(url, urlList)
    }

    private fun checkUrlList(originUrl: String?, list: ArrayList<UrlParserRunner>): UrlParserRunner? {
        if (originUrl.isNullOrEmpty()) return null
        val uri = Uri.parse(originUrl)

        var isChecked = false

        for (item in list) {
            // 1. Url 체크
            if (item.checkUrls.isNotEmpty()) {
                isChecked = false

                val url = originUrl.removeProtocol()
                for (checkUrl in item.checkUrls) {
                    val modifiedUrl = checkUrl.removeProtocol().removeLastSlash()

                    if (modifiedUrl.isNotEmpty() && url.contains(modifiedUrl)) {
                        isChecked = true
                        break
                    }
                }

                if (!isChecked) continue
            }

            // 2. Host 체크
            if (item.checkUrlsHost.isNotEmpty()) {
                isChecked = false

                val host = uri.host
                if (!host.isNullOrEmpty()) {
                    for (checkUrlHost in item.checkUrlsHost) {
                        if (host.contains(checkUrlHost)) {
                            isChecked = true
                            break
                        }
                    }
                }

                if (!isChecked) continue
            }

            // 3. 정규식 체크
            if (item.patterns.isNotEmpty()) {
                isChecked = false

                for (pattern in item.patterns) {
                    if (pattern.toRegex().containsMatchIn(originUrl)) {
                        isChecked = true
                        break
                    }
                }

                if (!isChecked) continue
            }

            // 4. Url 일치 체크
            if (item.sameUrls.isNotEmpty()) {
                isChecked = false

                val url = originUrl.removeProtocol().removeLastSlash()
                for (sameUrl in item.sameUrls) {
                    if (url == sameUrl.removeProtocol().removeLastSlash()) {
                        isChecked = true
                        break
                    }
                }

                if (!isChecked) continue
            }

            // 5. 파라미터 체크
            if (item.parameterKeys.isNotEmpty()) {
                isChecked = false

                val keys = if (uri.isHierarchical) {
                    uri.queryParameterNames
                } else {
                    setOf()
                }

                for (key in item.parameterKeys) {
                    if (key.isNotEmpty()) {
                        if (keys.contains(key)) {
                            isChecked = true
                            break
                        }
                    }
                }

                if (!isChecked) continue
            }

            if (isChecked) {
                return item
            }
        }
        return null
    }
}