package edu.josakapp.proyectoJosakapp.data.util

import android.content.Context
import kotlin.math.max

private const val PREFS_NAME = "habito_timer_prefs"
private const val PREFIX_LAST_COMPLETED = "habito_last_completed_"

fun habitLastCompletedKey(habitoId: Int): String = "$PREFIX_LAST_COMPLETED$habitoId"

fun saveHabitLastCompletedAt(
    context: Context,
    habitoId: Int,
    timestamp: Long = System.currentTimeMillis()
) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .putLong(habitLastCompletedKey(habitoId), timestamp)
        .apply()
}

fun clearHabitLastCompletedAt(context: Context, habitoId: Int) {
    context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .edit()
        .remove(habitLastCompletedKey(habitoId))
        .apply()
}

fun getHabitLastCompletedAt(context: Context, habitoId: Int): Long? {
    val value = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        .getLong(habitLastCompletedKey(habitoId), 0L)
    return value.takeIf { it > 0L }
}

fun frequencyDurationMillis(frecuencia: String): Long {
    return when (frecuencia.trim().lowercase()) {
        "cada hora" -> 60L * 60L * 1000L
        "todos los días" -> 24L * 60L * 60L * 1000L
        "todas las semanas" -> 7L * 24L * 60L * 60L * 1000L
        "todos los meses" -> 30L * 24L * 60L * 60L * 1000L
        else -> 24L * 60L * 60L * 1000L
    }
}

fun formatHabitRemainingTime(
    lastCompletedAt: Long?,
    createdAt: Long,
    frecuencia: String,
    now: Long = System.currentTimeMillis()
): String {
    val anchor = lastCompletedAt ?: createdAt
    val dueAt = anchor + frequencyDurationMillis(frecuencia)
    val remaining = max(0L, dueAt - now)

    if (remaining <= 0L) return "Ahora"

    val dayMillis = 24L * 60L * 60L * 1000L
    val hourMillis = 60L * 60L * 1000L
    val minuteMillis = 60L * 1000L

    return when {
        remaining >= dayMillis -> {
            val days = remaining / dayMillis
            "$days ${if (days == 1L) "día" else "días"}"
        }
        remaining >= hourMillis -> {
            val hours = remaining / hourMillis
            "$hours ${if (hours == 1L) "hora" else "horas"}"
        }
        remaining >= minuteMillis -> {
            val minutes = remaining / minuteMillis
            "$minutes ${if (minutes == 1L) "minuto" else "minutos"}"
        }
        else -> "Ahora"
    }
}

