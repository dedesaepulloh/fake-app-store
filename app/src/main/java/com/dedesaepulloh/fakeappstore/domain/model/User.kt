package com.dedesaepulloh.fakeappstore.domain.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    @SerializedName("email")
    val email: String? = "",
    @SerializedName("password")
    val password: String? = "",
    @SerializedName("name")
    val name: String? = "",
    @SerializedName("address")
    val address: String? = ""
) : Parcelable
