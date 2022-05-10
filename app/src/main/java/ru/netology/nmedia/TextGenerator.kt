package ru.netology.nmedia

import java.util.*

object TextGenerator {
    fun generateRandomText(len: Int): String {
        val chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!@#$%&"
        val rnd = Random()
        val sb = StringBuilder(len)
        for (i in 0 until len) sb.append(chars[rnd.nextInt(chars.length)])
        return sb.toString()
    }
}