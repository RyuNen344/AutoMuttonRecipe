package io.github.ryunen344.mutton.coroutines

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import io.github.ryunen344.mutton.coroutine.withReentrantLock
import kotlinx.atomicfu.AtomicInt
import kotlinx.atomicfu.atomic
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class ReentrantMutexTest {

    private val counter: AtomicInt = atomic(0)
    private lateinit var mutex: Mutex

    @BeforeTest
    fun setup() {
        mutex = Mutex()
        counter.value = 0
    }

    @AfterTest
    fun cleanup() {
        if (mutex.isLocked) mutex.unlock()
    }

    @Test
    fun testWithReentrantLock_whenNotLocked_thenExecuteImmediately() = runTest {
        mutex.withReentrantLock {
            counter.compareAndSet(0, 1)
        }
        assertThat(counter.value).isEqualTo(1)
    }

    @Test
    fun testWithReentrantLock_whenLockedSameContext_thenExecuteImmediately() = runTest {
        mutex.withReentrantLock {
            counter.compareAndSet(0, 1)
            mutex.withReentrantLock {
                counter.compareAndSet(1, 2)
            }
        }
        assertThat(counter.value).isEqualTo(2)
    }

    @Test
    fun testWithReentrantLock_whenLockedByAnotherScope_thenWaitForUnlock() = runTest {
        val scope1 = CoroutineScope(CoroutineName("scope1") + Job() + Dispatchers.Default)
        val scope2 = CoroutineScope(CoroutineName("scope2") + Job() + Dispatchers.Default)

        val job1 = scope1.async(start = CoroutineStart.LAZY) {
            mutex.withReentrantLock {
                delay(3000)
                assertThat(counter.compareAndSet(0, 1)).isTrue()
            }
        }

        val job2 = scope2.async(start = CoroutineStart.LAZY) {
            delay(300)
            mutex.withReentrantLock {
                assertThat(counter.incrementAndGet()).isEqualTo(2)
            }
        }

        awaitAll(job1, job2)
    }
}
