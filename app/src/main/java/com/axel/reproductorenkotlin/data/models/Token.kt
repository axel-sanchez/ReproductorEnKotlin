package com.axel.reproductorenkotlin.data.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Token {

    @SerializedName("access_token")
    @Expose
    private lateinit var accessToken: String
    @SerializedName("token_type")
    @Expose
    private lateinit var tokenType: String
    @SerializedName("expires_in")
    @Expose
    private lateinit var expiresIn: Integer
    @SerializedName("scope")
    @Expose
    private lateinit var scope: String

    fun getAccessToken(): String {
        return accessToken
    }

    fun setAccessToken(accessToken: String) {
        this.accessToken = accessToken
    }

    fun getTokenType(): String{
        return tokenType
    }

    fun setTokenType(tokenType: String) {
        this.tokenType = tokenType
    }

    fun getExpiresIn(): Integer {
        return expiresIn
    }

    fun setExpiresIn(expiresIn: Integer) {
        this.expiresIn = expiresIn
    }

    fun getScope(): String{
        return scope
    }

    fun setScope(scope: String) {
        this.scope = scope
    }
}