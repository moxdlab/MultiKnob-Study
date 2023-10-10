package io.moxd.dreair.model.task

object TaskHelper {

    private const val FIRST_TASK_ID = 1

    var currentId = 0
        private set

    /**
     * returns next task. If you reach task 20, this function will return task 1
     */
    fun getNext(): Task {
        currentId = if (isEndReached()) FIRST_TASK_ID else currentId + 1
        return getTaskById(currentId)!!
    }

    /**
     * returns whether you reached the last task (task 20).
     */
    fun isEndReached() = getTaskById(currentId + 1) == null

    fun reset() {
        currentId = 0
    }

    fun restoreLastTask(taskId: Int) {
        currentId = taskId
    }
}