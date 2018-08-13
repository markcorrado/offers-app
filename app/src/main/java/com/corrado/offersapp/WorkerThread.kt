package com.corrado.offersapp

import android.os.Handler
import android.os.HandlerThread

/**
 * Convenience WorkerThread to handle doing work in background
 */
class WorkerThread(threadName: String) : HandlerThread(threadName) {

    private lateinit var workerHandler: Handler

    override fun onLooperPrepared() {
        super.onLooperPrepared()
        workerHandler = Handler(looper)
    }

    fun postTask(task: Runnable) {
        workerHandler.post(task)
    }
}