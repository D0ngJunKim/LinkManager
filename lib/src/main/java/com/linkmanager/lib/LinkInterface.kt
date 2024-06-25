package com.linkmanager.lib

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.linkmanager.lib.constants.LinkManagerException
import com.linkmanager.lib.constants.LinkManagerInterface

/**
 * Created by 180842 on 2024. 6. 25..
 */
object LinkInterface {

    @LinkManagerException("앱 내 매칭되는 네이티브 케이스가 없는 경우, 웹뷰로 랜딩.")
    fun loadWebView(url: String, data: MutableMap<String, Any?>) {
        val context = data.get("context") as? Context ?: return
        Toast.makeText(context, "매칭되는 케이스가 존재하지 않아 웹으로 랜딩됩니다.", Toast.LENGTH_SHORT).show()
    }

    @LinkManagerInterface("구글 웹 랜딩")
    fun urlFuncGoogleWeb(url: String, data: MutableMap<String, Any?>): Boolean {
        val context = data.get("context") as? Context ?: return false
        Toast.makeText(context, "매칭 성공 : urlFuncGoogleWeb", Toast.LENGTH_SHORT).show()
        return true
    }

    @LinkManagerInterface("BBC 웹 랜딩")
    fun urlFuncBBCWeb(url: String, data: MutableMap<String, Any?>): Boolean {
        val context = data.get("context") as? Context ?: return false
        Toast.makeText(context, "매칭 성공 : urlFuncBBCWeb", Toast.LENGTH_SHORT).show()
        return true
    }

    @LinkManagerInterface("네이버 쇼핑 탭 랜딩")
    fun urlFuncNaverShopping(url: String, data: MutableMap<String, Any?>): Boolean {
        val context = data.get("context") as? Context ?: return false
        Toast.makeText(context, "매칭 성공 : urlFuncNaverShopping", Toast.LENGTH_SHORT).show()
        return true
    }

    @LinkManagerInterface("네이버 쇼핑 카테고리 랜딩")
    fun urlFuncNaverShoppingTab(url: String, data: MutableMap<String, Any?>): Boolean {
        val context = data.get("context") as? Context ?: return false
        Toast.makeText(context, "매칭 성공 : urlFuncNaverShoppingTab", Toast.LENGTH_SHORT).show()
        return true
    }

    @LinkManagerInterface("카카오 웹툰 랜딩")
    fun urlFuncKakaoWebtoon(url: String, data: MutableMap<String, Any?>): Boolean {
        val context = data.get("context") as? Context ?: return false
        Toast.makeText(context, "매칭 성공 : urlFuncKakaoWebtoon", Toast.LENGTH_SHORT).show()
        return true
    }
}