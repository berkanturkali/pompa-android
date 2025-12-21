package com.pompa.android.util

import java.util.Locale


fun String.titleCase(locale: Locale = Locale.getDefault()): String =
    trim()
        .lowercase(locale)
        .split(Regex("\\s+"))
        .joinToString(" ") { word ->
            word.replaceFirstChar { ch ->
                if (ch.isLowerCase()) ch.titlecase(locale) else ch.toString()
            }
        }