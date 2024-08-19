package com.example.billapp.models

import android.os.Parcel
import android.os.Parcelable

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val image: String = "",
    val transactions: List<PersonalTransaction> = emptyList(), // 使用者的交易記錄列表
    val amount: Double = 0.0, // 新增的結餘屬性
    val groupsID: List<String> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        mutableListOf<PersonalTransaction>().apply {
            parcel.readList(this, PersonalTransaction::class.java.classLoader)
        },
        parcel.readDouble() // 讀取 amount
    )

    override fun describeContents(): Int = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(id)
        dest.writeString(name)
        dest.writeString(email)
        dest.writeString(image)
        dest.writeList(transactions)
        dest.writeDouble(amount) // 寫入 amount
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}
