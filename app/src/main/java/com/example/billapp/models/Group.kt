package com.example.billapp.models

import android.os.Parcel
import android.os.Parcelable

data class Group(
    val id: String = "",
    val name: String = "",
    val image: String = "",
    val createdBy: String = "",
    val assignedTo: ArrayList<String> = ArrayList(),  // List of user id
    val transactions: List<GroupTransaction> = emptyList(),
    val deptRelations: List<DeptRelation> = emptyList()
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.createStringArrayList()!!,
        parcel.createTypedArrayList(GroupTransaction.CREATOR)!!,
        parcel.createTypedArrayList(DeptRelation.CREATOR)!!
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(image)
        parcel.writeString(createdBy)
        parcel.writeStringList(assignedTo)
        parcel.writeTypedList(transactions)
        parcel.writeTypedList(deptRelations)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Group> {
        override fun createFromParcel(parcel: Parcel): Group {
            return Group(parcel)
        }

        override fun newArray(size: Int): Array<Group?> {
            return arrayOfNulls(size)
        }
    }
}
