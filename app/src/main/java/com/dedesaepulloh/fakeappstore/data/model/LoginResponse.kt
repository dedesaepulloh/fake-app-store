package com.dedesaepulloh.fakeappstore.data.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("token")
	val token: String
)
