package com.linkmanager.lib.extension

/**
 * Created by 180842 on 2024. 6. 25..
 */

internal fun String?.removeProtocol(): String {
    return if (this.isNullOrEmpty()) {
        return this.orEmpty()
    } else {
        this.replace("http://", "")
                .replace("https://", "")
                .replace("qa-", "")
                .replace("qaonly-", "")
                .replace("stg-", "")
    }
}

internal fun String?.removeLastSlash(): String {
    return this?.takeIf { it.lastOrNull() == '/' }?.run {
        this.dropLast(1)
    } ?: this.orEmpty()
}

internal fun String?.isNormalUrl(): Boolean {
    this?.run {
        if (startsWith("http") || startsWith("https") || startsWith("about:blank")) {
            return true
        }
    }
    return false
}