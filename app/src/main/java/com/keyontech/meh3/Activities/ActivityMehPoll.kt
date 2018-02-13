package com.keyontech.meh3.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.jonesq.meh3.Models.ModelMeh
import com.example.jonesq.meh3.utils.*
import com.google.gson.GsonBuilder
import com.keyontech.meh3.R
import com.keyontech.meh3.adapters.AdapterRecyclerViewActivityPoll
import kotlinx.android.synthetic.main.activity_meh_poll.*
import java.text.SimpleDateFormat

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

//        println( "555aaa  modelMeh.poll = " + modelMeh.poll)
//        println( "555bbb  modelMeh.poll title = " + modelMeh.poll.title )
//        println( "555ccc  modelMeh.poll id = " + modelMeh.poll.id )
//        println( "555ddd  modelMeh.poll startDate = " + modelMeh.poll.startDate )
//        println( "555eee  modelMeh.poll answers[0].text = " + modelMeh.poll.answers[0].text )
//        println( "555fff  modelMeh.poll answers[0].voteCount = " + modelMeh.poll.answers[0].voteCount )
//        println( "555ggg  modelMeh.poll topic.url = " + modelMeh.poll.topic.url)

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        val date = dateFormat.parse(modelMeh.poll.startDate)
        val formatter = SimpleDateFormat("MM/dd/yyyy")
        val dateStr = formatter.format(date)
//        val pollTitle = modelMeh.poll.title + " " + dateStr
        val pollTitle = modelMeh.poll.title

        // set nav bar title
        setTitle(pollTitle)
//        textView_Activity_Meh_Poll_Header.text = pollTitle

        // set poll RecyclerView
//        recycler_view_meh_poll.setBackgroundColor(Color.BLUE)
        recycler_view_meh_poll.layoutManager = LinearLayoutManager(this)
        recycler_view_meh_poll.adapter = AdapterRecyclerViewActivityPoll(modelMeh.poll , getMaxVotes(modelMeh.poll) )
    }

}