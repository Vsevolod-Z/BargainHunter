package com.example.bargainhunter

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonWriter

/*
class AppJsonAdapter : TypeAdapter<App>() {
    private val gson = Gson()

    override fun write(out: JsonWriter?, value: App?) {
        if (value == null) {
            out?.nullValue()
            return
        }
        out?.beginObject()
        out?.name("LocalID")
        out?.value(value.localId)
        out?.name("SteamAppData")
        gson.toJson(value.steamAppData, SteamAppData::class.java, out)
        out?.name("SteamBuyAppData")
        gson.toJson(value.steamBuyAppData, SteamBuyAppData::class.java, out)
        out?.name("SteamPayAppData")
        gson.toJson(value.steamPayAppData, SteamPayAppData::class.java, out)
        out?.name("GOGAppData")
        gson.toJson(value.gogAppData, GOGAppData::class.java, out)
        out?.endObject()
    }

    override fun read(`in`: JsonReader?): App {
        var localId = 0
        var steamAppData: SteamAppData? = null
        var steamBuyAppData: SteamBuyAppData? = null
        var steamPayAppData: SteamPayAppData? = null
        var gogAppData: GOGAppData? = null
        `in`?.beginObject()
        while (`in`?.hasNext() == true) {
            when (`in`.nextName()) {
                "LocalID" -> localId = `in`.nextInt()
                "SteamAppData" -> steamAppData = gson.fromJson(`in`, SteamAppData::class.java)
                "SteamBuyAppData" -> steamBuyAppData = gson.fromJson(`in`, SteamBuyAppData::class.java)
                "SteamPayAppData" -> steamPayAppData = gson.fromJson(`in`, SteamPayAppData::class.java)
                "GOGAppData" -> gogAppData = gson.fromJson(`in`, GOGAppData::class.java)
            }
        }
        `in`?.endObject()
        return App(localId, steamAppData ?: SteamAppData(), steamBuyAppData ?: SteamBuyAppData(),
            steamPayAppData ?: SteamPayAppData(), gogAppData ?: GOGAppData()
    }
}*/
