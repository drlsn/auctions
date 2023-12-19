package com.slaycard.basic

abstract class Entity<TId>(
    val id: TId,
    internal var version: Int = 0)