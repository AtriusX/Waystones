package xyz.atrius.waystones.data.json.advancement

import com.google.gson.annotations.SerializedName

enum class AdvancementType {

    @SerializedName("task")
    TASK,

    @SerializedName("challenge")
    CHALLENGE,

    @SerializedName("goal")
    GOAL
}