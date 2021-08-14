package se.sample.android.refactoring.util

import java.util.*

fun Random.nextInt(range: IntRange): Int = range.first + nextInt(range.last)