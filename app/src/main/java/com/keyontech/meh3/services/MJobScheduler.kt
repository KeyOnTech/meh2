package com.keyontech.meh3.services

/**
 * Created by kot on 2/26/18.
 */

import android.app.job.JobParameters
import android.app.job.JobService
import android.content.Context
import android.widget.Toast
import com.example.jonesq.meh3.utils.JS_PERSISTABLE_BUNDLE_DEAL_URL
import com.keyontech.meh3.Models.modelJobExecuterAsyncTaskParams

/**
 * Created by kot on 2/23/18.
 *
 * https://www.youtube.com/watch?v=x0F6iw8PvVw
 */
/*
class MJobScheduler { // : JobService() {
//    lateinit var mJobExecuter : MJobExecuter
//
//    override fun onStartJob(jobParameters: JobParameters?): Boolean {
//
//        mJobExecuter = new MJobExecuter() {
//            override fun onPostExecute(result: String?) {
//
//                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
//            }
//
////            fun onPostExecute(result: String?) {
////                Toast.makeText(applicationContext, result, Toast.LENGTH_SHORT).show()
////            }
//        }
//
//
//
//
//        return true
//    }
//
//    override fun onStopJob(jobParameters: JobParameters?): Boolean {
//
//        return false
//    }
}
*/

class MJobScheduler : JobService() {
//class MJobScheduler(var mehDealUrl: String) : JobService() {
    lateinit var mJobExecuter: MJobExecuter

    /*** public vars */
//    private var cContext: Context
//    var mehDealUrl: String = ""

//    constructor(cContext: Context, mehDealUrl: String) : super() {
//    constructor(mehDealUrl: String) {
////        this.cContext = cContext
//        this.mehDealUrl = mehDealUrl
//    }

//    companion object {
//        var mehDealUrl: String = ""
//    }

    override fun onStartJob(jobParameters: JobParameters): Boolean
    {
        println("MJobScheduler  - onStartJob  ")

//        val persistableBundle = jobParameters.extras
//        val vmehDealUrl = persistableBundle.getString(JS_PERSISTABLE_BUNDLE_DEAL_URL)

//        println("MJobScheduler  - onStartJob  ----  vmehDealUrl  = " + vmehDealUrl)

//        var params = modelJobExecuterAsyncTaskParams(applicationContext, vmehDealUrl)
        var params = modelJobExecuterAsyncTaskParams()
        params.cContext = applicationContext
//        params.dealUrl = vmehDealUrl

//        mJobExecuter = object : MJobExecuter(applicationContext, vmehDealUrl) {
//        mJobExecuter = object : MJobExecuter(params) {
        mJobExecuter = object : MJobExecuter() {
            override fun onPostExecute(s: String) {
                println("MJobScheduler  - onStartJob  -  onPostExecute")
//                Toast.makeText(applicationContext, s, Toast.LENGTH_SHORT).show()
                jobFinished(jobParameters, false)
            }
        }

        mJobExecuter.execute(params)
        return true
    }

    override fun onStopJob(jobParameters: JobParameters): Boolean {
        println("MJobScheduler  - onStopJob")
        mJobExecuter.cancel(true)

        return false
    }
}
