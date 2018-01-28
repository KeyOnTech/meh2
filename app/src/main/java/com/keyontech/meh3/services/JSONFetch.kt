package com.keyontech.meh3.services


//import android.graphics.Color
import android.content.Context
//import com.keyontech.meh3.Models.ModelMeh
//import com.keyontech.meh3.utils.JSONurl
import android.content.Intent
import android.net.Uri
//import com.example.jonesq.meh3.utils.printToErrorLog_10
import com.keyontech.meh3.R


/**
 * Created by kot on 1/16/18.
 */
class JSONFetch {



    fun buyThisItem(vContext: Context): Intent? {
        try {
            val url = vContext.getResources().getString(R.string.goto_URL_meh)

            val vIntent = Intent(Intent.ACTION_VIEW)
            vIntent.data = Uri.parse(url)
            return vIntent
        } catch (e: Exception) {
//            printToErrorLog_10(TAG, "ERROR " + e.message)
            e.printStackTrace()
            return null
        }
        // try
    } // buyThisItem






//    companion object Factory {
//        fun fetchJSON() {}
//    }



//    val abcGo = ""



/*
//    fun fetchJSONString(val url: String) : String {
    fun fetchJSONString() : String {
        println("fetchJSONString - url : " + JSONurl)
        val request = Request.Builder().url( JSONurl ).build()
        val client = OkHttpClient()

        // had to enqueue because you cannot execute in the main method needs a thread to do so
//        client.newCall( request ).execute()
        client.newCall( request ).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val responseBody = response?.body()?.string()
                println( "fetchJSONString - Response - " + responseBody )
                abcGo

//                parseJSON()
//                return responseBody

//                runOnUiThread{
//                    recyclerView_main.adapter = ActivityMainRecyclerViewAdapter(homeFeed)
//                }

            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("fetchJSON - failed to execute request - " + e.toString() )
            }
        }) // enqueue

        return "success"
    } //


//    fun parseJSON( val json: String) {
//        val gson = GsonBuilder().create()
//        val modelMeh = gson.fromJson( json , ModelMeh::class.java )
//    }






    fun fetchJSON1() {
        println("ACctiviyMain - onCreate - attempting to fetch JSON")
        println("JSON from - " + JSONurl)


        val request = Request.Builder().url( JSONurl ).build()

        val client = OkHttpClient()

        // had to enqueue because you cannot execute in the main method needs a thread to do so
//        client.newCall( request ).execute()
        client.newCall( request ).enqueue(object: Callback {
            override fun onResponse(call: Call?, response: Response?) {
                val responseBody = response?.body()?.string()
                println( "111  fetchJSON - onResponse - body - " + responseBody )

                val gson = GsonBuilder().create()

                val modelMeh = gson.fromJson( responseBody , ModelMeh::class.java )
                println( "222bbb  modelMeh.deal = " + modelMeh.deal )
                println( "222bbb  modelMeh.deal title = " + modelMeh.deal.title )

                println( "333aaa  modelMeh.poll = " + modelMeh.poll)
                println( "333bbb  modelMeh.poll title = " + modelMeh.poll.title )
                println( "333ccc  modelMeh.poll id = " + modelMeh.poll.id )
                println( "333ddd  modelMeh.poll startDate = " + modelMeh.poll.startDate )
                println( "333eee  modelMeh.poll answers[0].text = " + modelMeh.poll.answers[0].text )
                println( "333fff  modelMeh.poll answers[0].voteCount = " + modelMeh.poll.answers[0].voteCount )
                println( "333ggg  modelMeh.poll topic.url = " + modelMeh.poll.topic.url)



                println( "444aaa  modelMeh.vieo = " + modelMeh.video)
                println( "444bbb  modelMeh.video title = " + modelMeh.video.title )
                println( "444ccc  modelMeh.video topic.url = " + modelMeh.video.topic.url)

//                runOnUiThread{
//                    println('aaaaaaaaa')
////                    recyclerView_main.adapter = ActivityMainRecyclerViewAdapter(homeFeed)
//                }
            }

            override fun onFailure(call: Call?, e: IOException?) {
                println("fetchJSON - failed to execute request - error: " + e.toString() )
            }
        })
    } //
*/


} //