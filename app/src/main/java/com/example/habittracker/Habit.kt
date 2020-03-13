package com.example.habittracker

import android.os.Parcel
import android.os.Parcelable

class Habit(val name: String,
            val description: String,
            val priority: Int,
            val type: HabitType,
            val count: Int,
            val period: Int,
            val color: Int)
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
    }
}


