package com.slaycard.entities.shared

import kotlinx.serialization.Serializable

@Serializable
data class Money(val value: Int) {
    fun isValid(): Boolean = value >= 0
}

operator fun Money.compareTo(other: Money): Int =
    this.value - other.value