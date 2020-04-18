package com.example.habittracker.models.netcommunication.typeadapters

import com.example.habittracker.models.netcommunication.HabitId
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class HabitIdJsonSerializer:
    JsonSerializer<HabitId> {
    override fun serialize(
        src: HabitId,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement = JsonObject()
        .apply {
        addProperty("uid", src.uid)
    }
}