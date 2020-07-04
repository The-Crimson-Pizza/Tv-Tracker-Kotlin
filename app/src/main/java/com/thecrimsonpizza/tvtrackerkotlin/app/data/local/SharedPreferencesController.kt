package com.thecrimsonpizza.tvtrackerkotlin.app.data.local

import android.content.SharedPreferences
import com.thecrimsonpizza.tvtrackerkotlin.core.utils.GlobalConstants.FAV_TEMP_DATA
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

class SharedPreferencesController {
    fun setPrefIntArray(sp: SharedPreferences, value: IntArray) {
        val prefEditor = sp.edit()
        val s: String
        s = try {
            val jsonArr = JSONArray()
            for (i in value) jsonArr.put(i)
            val json = JSONObject()
            json.put(FAV_TEMP_DATA, jsonArr)
            json.toString()
        } catch (excp: JSONException) {
            ""
        }
        prefEditor.putString(FAV_TEMP_DATA, s)
        prefEditor.apply()
    }
}