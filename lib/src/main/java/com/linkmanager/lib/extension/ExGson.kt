package com.linkmanager.lib.extension

import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.linkmanager.lib.util.GsonErrorSkipTypeAdapter

/**
 * Created by 180842 on 2024. 6. 25..
 */

internal inline fun <reified T> loadJsonString(json: String): T =
        GsonBuilder().setLenient().registerTypeAdapterFactory(GsonErrorSkipTypeAdapter()).create().fromJson(
                json,
                object : TypeToken<T>() {}.type)