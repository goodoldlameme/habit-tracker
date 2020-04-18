package com.example.habittracker.models

import android.os.Parcel
import android.os.Parcelable
import com.example.habittracker.models.database.HabitEntity
import java.util.*

class Habit(val name: String,
            val description: String,
            val priority: Priority,
            val type: HabitType,
            val count: Int,
            val period: Int,
            val color: Int,
            val id: UUID,
            val creationDate: Calendar)
    : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        Priority.valueOf(parcel.readString().toString()),
        HabitType.valueOf(parcel.readString().toString()),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        UUID.fromString(parcel.readString()),
        parseCalendar(parcel.readLong())
    ) {
    }


    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(priority.name)
        parcel.writeString(type.name)
        parcel.writeInt(count)
        parcel.writeInt(period)
        parcel.writeInt(color)
        parcel.writeString(id.toString())
        parcel.writeLong(creationDate.timeInMillis)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Habit> {
        override fun createFromParcel(parcel: Parcel): Habit {
            return Habit(parcel)
        }

        override fun newArray(size: Int): Array<Habit?> {
            return arrayOfNulls<Habit?>(size)
        }

        fun fromHabitEntity(habitEntity: HabitEntity): Habit{
            return Habit(
                habitEntity.name,
                habitEntity.description,
                habitEntity.priority,
                habitEntity.type,
                habitEntity.count,
                habitEntity.period,
                habitEntity.color,
                UUID.fromString(habitEntity.id),
                habitEntity.creationDate
            )
        }

        private fun parseCalendar(timeInMillis: Long): Calendar{
            val res = Calendar.getInstance()
            res.timeInMillis = timeInMillis
            return res
        }
    }

    fun toHabitEntity(): HabitEntity {
        return HabitEntity(
            name,
            description,
            priority,
            type,
            count,
            period,
            color,
            id.toString(),
            creationDate
        )
    }

}


