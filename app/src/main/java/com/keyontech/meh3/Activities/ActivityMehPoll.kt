package com.keyontech.meh3.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.jonesq.meh3.Models.ModelMeh
import com.example.jonesq.meh3.utils.*
import com.google.gson.GsonBuilder
import com.keyontech.meh3.R
import kotlinx.android.synthetic.main.activity_meh_poll.*

/**
 * Created by kot on 1/30/18.
 */

class ActivityMehPoll: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView( R.layout.activity_meh_poll)

        val jsonResponse = intent.getStringExtra(KEY_MEH_RESPONSE_STRING)

        val gson = GsonBuilder().create()
        val modelMeh = gson.fromJson( jsonResponse , ModelMeh::class.java )

//        println( "333aaa  modelMeh.poll = " + modelMeh.poll)
//        println( "333bbb  modelMeh.poll title = " + modelMeh.poll.title )
//        println( "333ccc  modelMeh.poll id = " + modelMeh.poll.id )
//        println( "333ddd  modelMeh.poll startDate = " + modelMeh.poll.startDate )
//        println( "333eee  modelMeh.poll answers[0].text = " + modelMeh.poll.answers[0].text )
//        println( "333fff  modelMeh.poll answers[0].voteCount = " + modelMeh.poll.answers[0].voteCount )
//        println( "333ggg  modelMeh.poll topic.url = " + modelMeh.poll.topic.url)

        textView_Activity_Meh_Poll.text = modelMeh.poll.title
    }
}