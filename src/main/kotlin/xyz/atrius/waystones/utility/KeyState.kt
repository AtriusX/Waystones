package xyz.atrius.waystones.utility

import org.bukkit.Location

sealed class WarpKeyState

object Severed : WarpKeyState()

class Connected(val warp: Location) : WarpKeyState()