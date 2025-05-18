import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.intrinsics.suspendCoroutineUninterceptedOrReturn
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.test.Test

@Test
fun main(): Unit = runBlocking {

    println()
    val coroutine = this
    val job = coroutineContext[Job]
    val dispatcher = coroutineContext[CoroutineDispatcher]
    println("Context $coroutine, $job, $dispatcher")

    val io = Dispatchers.IO
    val main = Dispatchers.Main
    val default = Dispatchers.Default
    val unconfined = Dispatchers.Unconfined
    println("Dispatches $io, $main, $default, $unconfined")

    val single = newSingleThreadContext("single")
    val pool = newFixedThreadPoolContext(8, "pool")
    val limit = pool.limitedParallelism(4)
    println("Thread $single, $pool, $limit")

    MainScope()
    CoroutineScope(coroutineContext)
    CoroutineScope(coroutineContext + Job(job))
    CoroutineScope(coroutineContext + SupervisorJob(job))

    coroutineScope { }
    supervisorScope { }


    println()
}

suspend fun s() = suspendCoroutineUninterceptedOrReturn { continuation ->
    continuation.resume("suspend intrinsics")
}

suspend fun s1() = suspendCoroutine { continuation ->
    continuation.resume("suspendCoroutine")
}

suspend fun s2() = suspendCancellableCoroutine { continuation ->
    continuation.resume("suspendCancellableCoroutine")
}