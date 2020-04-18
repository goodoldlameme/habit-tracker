package com.example.habittracker.models.netcommunication.typeadapters

import com.example.habittracker.models.HabitType
import com.example.habittracker.models.Priority
import com.example.habittracker.models.netcommunication.HabitDto
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type

class HabitDtoJsonDeserializer:
    JsonDeserializer<HabitDto> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): HabitDto? =
        json?.let { json ->
            HabitDto(
                json.asJsonObject.get("color").asInt,
                json.asJsonObject.get("count").asInt,
                json.asJsonObject.get("date").asLong,
                json.asJsonObject.get("description").asString,
                json.asJsonObject.get("frequency").asInt,
                Priority.values()
                    .first { it.ordinal == json.asJsonObject.get("priority").asInt },
                json.asJsonObject.get("title").asString,
                HabitType.values()
                    .first { it.ordinal == json.asJsonObject.get("type").asInt },
                json.asJsonObject.get("uid").asString
            )
        }
}