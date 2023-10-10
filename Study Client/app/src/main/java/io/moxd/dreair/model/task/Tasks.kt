@file:Suppress("DataClassPrivateConstructor")

package io.moxd.dreair.model.task

import io.moxd.dreair.BuildConfig

data class Task private constructor(
    val id: Int,
    val initialCursorPos: Float,
    val goalPos: Float
) {

    companion object {
        private var currentId: Int = 0

        fun new(initialCursorPos: Float, goalPos: Float): Task {
            currentId = currentId.inc()
            return Task(currentId, initialCursorPos, goalPos)
        }
    }
}

private fun getTasksFromPool() = listOf(
    Task.new(0.3f, 0.6f),
    Task.new(0.6f, 0.3f),
    Task.new(0.2f, 0.8f),
    Task.new(0.4f, 0.5f),
    Task.new(0.8f, 0.2f),
    Task.new(0.7f, 0.4f),
    Task.new(0.4f, 0.7f),
    Task.new(0.5f, 0.4f),
    Task.new(0.5f, 0.1f),
    Task.new(0.1f, 0.5f),
)

val tasks: List<Task> by lazy {
    if (BuildConfig.BUILD_TYPE == "debug") return@lazy getTasksFromPool().take(2)
    val finalTasks = mutableListOf<Task>().also { it += getTasksFromPool() }
    val factor = 30 / finalTasks.size
    repeat(factor - 1) {
        finalTasks += getTasksFromPool()
    }
    finalTasks
}

fun getTaskById(id: Int): Task? = tasks.getOrNull(id - 1)