package se.sample.android.refactoring.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pokemon(
    val id: Int,
    val name: String
) : Parcelable
