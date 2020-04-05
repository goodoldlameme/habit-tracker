package com.example.habittracker.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import com.example.habittracker.database.HabitEntity
import java.util.*

class Habit(val name: String,
            val description: String,
            val priority: Int,
            val type: HabitType,
            val count: Int,
            val period: Int,
            val color: Int,
            val id: UUID = UUID.randomUUID(),
            val creationDate: Calendar = Calendar.getInstance())
    : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt(),
        HabitType.valueOf(parcel.readString().toString()),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(priority)
        parcel.writeString(type.name)
        parcel.writeInt(count)
        parcel.writeInt(period)
        parcel.writeInt(color)
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
                HabitType.valueOf(habitEntity.type),
                habitEntity.count,
                habitEntity.period,
                habitEntity.color,
                UUID.fromString(habitEntity.id),
                habitEntity.creationDate
            )
        }
    }
}

