package com.linkmanager.lib.util

import android.util.Log
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import com.google.gson.stream.MalformedJsonException

/**
 * Created by 180842 on 2024. 6. 25..
 */
class GsonErrorSkipTypeAdapter : TypeAdapterFactory {
    companion object {
        private const val TAG = "GsonErrorSkipTypeAdapter"
    }

    override fun <T : Any?> create(gson: Gson?, type: TypeToken<T>?): TypeAdapter<T?> {
        val delegate = gson?.getDelegateAdapter(this, type)

        return object : TypeAdapter<T?>() {
            override fun write(out: JsonWriter, value: T?) {
                delegate?.write(out, value)
            }

            override fun read(`in`: JsonReader): T? {
                return try {
                    delegate?.read(`in`)
                } catch (e: MalformedJsonException) {
                    malformedException(`in`)
                    null
                } catch (e: Exception) {
                    castingException(`in`, `in`.peek(), e.message)
                    `in`.skipValue()
                    null
                }
            }

            private fun castingException(reader: JsonReader, token: JsonToken, targetString: String? = "") {
                Log.e(TAG,
                        "\n"
                                + "\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C"
                                + "\nCasting Exception : "
                                + "\n" + reader.path + " 데이터가\n" + targetString + "\n" + token.toString() + "로 내려와 형식이 다릅니다."
                                + "\n"
                                + "\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C"
                )
            }

            private fun malformedException(reader: JsonReader) {
                Log.e(TAG,
                        "\n"
                                + "\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C"
                                + "\nMalformedJsonException : Json String 자체가 유효한 형태가 아닙니다. "
                                + "\n" + reader.path + " 데이터가 위치한 라인을 확인해 주세요."
                                + "\n"
                                + "\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C\uD83E\uDD2C"
                )
            }
        }
    }
}