package xyz.atrius.waystones.handler

interface Handler {

    val error: String?

    fun handle(): Boolean
}