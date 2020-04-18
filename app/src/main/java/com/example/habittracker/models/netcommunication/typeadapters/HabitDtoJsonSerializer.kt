package com.example.habittracker.models.netcommunication.typeadapters

import com.example.habittracker.models.netcommunication.HabitDto
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.google.gson.JsonSerializer
import java.lang.reflect.Type

class HabitDtoJsonSerializer:
    JsonSerializer<HabitDto> {
    override fun serialize(
        src: HabitDto,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement = JsonObject()
        .apply {
        addProperty("color", src.color)
        addProperty("count", src.count)
        addProperty("date", src.date)
        addProperty("description", src.description)
        addProperty("frequency", src.frequency)
        addProperty("priority", src.priority.ordinal)
        addProperty("title", src.title)
        addProperty("type", src.type.ordinal)
        if (src.uid != null)
            addProperty("uid", src.uid)
    }
}