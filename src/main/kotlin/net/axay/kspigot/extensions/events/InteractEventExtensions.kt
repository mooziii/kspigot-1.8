package net.axay.kspigot.extensions.events

import org.bukkit.event.block.Action

/**
 * @return True, if the action was a left mouse button click.
 */
val Action.isLeftClick get() = this == Action.LEFT_CLICK_BLOCK || this == Action.LEFT_CLICK_AIR

/**
 * @return True, if the action was a right mouse button click.
 */
val Action.isRightClick get() = this == Action.RIGHT_CLICK_BLOCK || this == Action.RIGHT_CLICK_AIR
