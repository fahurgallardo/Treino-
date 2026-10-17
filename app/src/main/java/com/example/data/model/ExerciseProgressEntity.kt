package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "exercise_progress")
data class ExerciseProgressEntity(
    @PrimaryKey
    val exerciseId: String,
    val weightsJson: String = "{}",
    val completedSetsJson: String = "{}",
    val customNote: String = "",
    val updatedAt: Long = System.currentTimeMillis()
) {
    fun getWeightsMap(): Map<Int, String> {
        if (weightsJson.isBlank() || weightsJson == "{}") return emptyMap()
        return try {
            val content = weightsJson.trim().removeSurrounding("{", "}").trim()
            if (content.isEmpty()) return emptyMap()
            content.split(",")
                .mapNotNull { pair ->
                    val parts = pair.split(":")
                    if (parts.size == 2) {
                        val key = parts[0].trim().replace("\"", "").toIntOrNull()
                        val value = parts[1].trim().replace("\"", "")
                        if (key != null) key to value else null
                    } else null
                }.toMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    fun getCompletedSetsMap(): Map<Int, Boolean> {
        if (completedSetsJson.isBlank() || completedSetsJson == "{}") return emptyMap()
        return try {
            val content = completedSetsJson.trim().removeSurrounding("{", "}").trim()
            if (content.isEmpty()) return emptyMap()
            content.split(",")
                .mapNotNull { pair ->
                    val parts = pair.split(":")
                    if (parts.size == 2) {
                        val key = parts[0].trim().replace("\"", "").toIntOrNull()
                        val value = parts[1].trim().replace("\"", "").toBoolean()
                        if (key != null) key to value else null
                    } else null
                }.toMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }

    companion object {
        fun serializeWeights(map: Map<Int, String>): String {
            if (map.isEmpty()) return "{}"
            return map.entries.joinToString(prefix = "{", postfix = "}") { (k, v) ->
                "\"$k\":\"${v.replace("\"", "\\\"")}\""
            }
        }

        fun serializeCompletedSets(map: Map<Int, Boolean>): String {
            if (map.isEmpty()) return "{}"
            return map.entries.joinToString(prefix = "{", postfix = "}") { (k, v) ->
                "\"$k\":$v"
            }
        }
    }
}
