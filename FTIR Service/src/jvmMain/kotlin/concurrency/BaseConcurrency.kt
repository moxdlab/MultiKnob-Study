package concurrency

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

abstract class BaseConcurrency<T>(
    dispatcher: CoroutineDispatcher = Dispatchers.Default,
    concurrentWorkers: Int,
    private val workerAction: suspend (T) -> Unit
) {

    private val channel = Channel<T>(capacity = Channel.UNLIMITED)
    private val scope = CoroutineScope(dispatcher)

    init {
        repeat(concurrentWorkers) {
            scope.launchProcessor(channel)
        }
    }

    fun addWorkElement(element: T) {
        channel.trySend(element)
    }

    private fun CoroutineScope.launchProcessor(channel: Channel<T>) = launch {
        for(element in channel) {
            workerAction(element)
        }
    }
}

