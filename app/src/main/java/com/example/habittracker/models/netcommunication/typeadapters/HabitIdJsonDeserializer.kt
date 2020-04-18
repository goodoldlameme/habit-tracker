package com.example.habittracker.models.netcommunication.typeadapters

import com.example.habittracker.models.netcommunication.HabitId
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class HabitIdJsonDeserializer: JsonDeserializer<HabitId> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): HabitId? =
        json?.let{ json ->
            HabitId(
                json.asJsonObject.get("uid").asString
            )
        }
}