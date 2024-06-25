package com.linkmanager.lib.constants

/**
 * Created by 180842 on 2023/11/07.
 */

enum class WebActionType(val rawValue: String) {
    PRE_ACTION("preAction"),
    POST_ACTION("postAction")
}

annotation class ConvoyDataKey {
    companion object {
        const val RUNNER_DATA = "runnerData"
        const val CALLER = "caller"
        const val WEB_VIEW = "webView"
        const val WEB_VIEW_FRAGMENT = "webViewFragment"
        const val IS_REDIRECT = "isRedirect"
    }
}