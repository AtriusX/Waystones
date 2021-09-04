package xyz.atrius.waystones.data.advancement

import com.google.gson.annotations.SerializedName

@Suppress("unused")
enum class AdvancementType {

    @SerializedName("normal")
    NORMAL,

    @SerializedName("challenge")
    CHALLENGE,

    @SerializedName("goal")
    GOAL
}