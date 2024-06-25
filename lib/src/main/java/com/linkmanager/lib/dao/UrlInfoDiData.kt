package com.linkmanager.lib.dao

import androidx.annotation.Keep

/**
 * Created by 180842 on 2023/11/07.
 */
@Keep
data class UrlInfoDiData(val webUrlList: ArrayList<UrlInfoItemDiData?>?)

@Keep
data class UrlInfoItemDiData(val title: String? = null,
                             val description: String? = null,                 // 설명
                             val name: String? = null,                        // 이름
                             val webActionTypes: ArrayList<String?>? = null,
                             val samples: ArrayList<String?>? = null,
                             val checkUrls: ArrayList<String?>? = null,       // Url에 포함되어 있는지 검사
                             val checkUrlsHost: ArrayList<String?>? = null,   // Host 체크
                             val sameUrls: ArrayList<String?>? = null,        // url이 모두 같은지 검사
                             val patterns: ArrayList<String?>? = null,        // 패턴 체크
                             val parameterKeys: ArrayList<String?>? = null,   // 파라미터 키 검사
                             val funNames: ArrayList<String?>? = null)        // Custom Url / Url 함수 명
{
    fun isValid(): Boolean {
        return (!checkUrls.isNullOrEmpty() ||
                !checkUrlsHost.isNullOrEmpty() ||
                !sameUrls.isNullOrEmpty() ||
                !patterns.isNullOrEmpty() ||
                !parameterKeys.isNullOrEmpty()) && !funNames?.filterNotNull().isNullOrEmpty()
    }
}