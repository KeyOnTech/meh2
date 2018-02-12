package com.keyontech.meh3.Activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import com.example.jonesq.meh3.Models.ModelMeh
import com.example.jonesq.meh3.utils.*
import com.google.gson.GsonBuilder
import com.keyontech.meh3.R
import com.keyontech.meh3.adaptersViewPager.AdapterRecyclerViewActivityPoll
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


        println( "555aaa  modelMeh.poll = " + modelMeh.poll)
        println( "555bbb  modelMeh.poll title = " + modelMeh.poll.title )
        println( "555ccc  modelMeh.poll id = " + modelMeh.poll.id )
        println( "555ddd  modelMeh.poll startDate = " + modelMeh.poll.startDate )
        println( "555eee  modelMeh.poll answers[0].text = " + modelMeh.poll.answers[0].text )
        println( "555fff  modelMeh.poll answers[0].voteCount = " + modelMeh.poll.answers[0].voteCount )
        println( "555ggg  modelMeh.poll topic.url = " + modelMeh.poll.topic.url)



        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        val date = dateFormat.parse(modelMeh.poll.startDate)//You will get date object relative to server/client timezone wherever it is parsed
        val formatter = SimpleDateFormat("MM/dd/yyyy") //If you need time just put specific format for time like 'HH:mm:ss'
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





//    // CourseLesson is a generic it can be anything it is the same name as above on line 61 so things are easy to follow
//    private class Adapter_RecyclerViewHolder_ActivityMehPoll(val pollAnswers: ArrayList<ModelMehPollAnswer>): RecyclerView.Adapter<RecyclerViewHolder_ActivityMehPoll>() {
//        override fun getItemCount(): Int {
////            return 5
//            return pollAnswers.size
//        }
//
//
//        override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): RecyclerViewHolder_ActivityMehPoll{
//            val layoutInflator = LayoutInflater.from(parent?.context)
//            val customView = layoutInflator.inflate(R.layout.recyclerview_row_poll_activity, parent, false)
//
//
////            val yellowView = View ( parent?.context )
////            yellowView.setBackgroundColor( Color.YELLOW )
////            yellowView.minimumHeight=50
//            return RecyclerViewHolder_ActivityMehPoll( customView )
//        }
//
//        override fun onBindViewHolder(holder: RecyclerViewHolder_ActivityMehPoll?, position: Int) {
//            val courseLesson = pollAnswers.get(position)
//
//            holder?.customView?.textView_Poll_Answer?.text = courseLesson.text + "  " + courseLesson.voteCount
//
//            holder?.courseLesson = courseLesson
//        }
//    } //




//    // add the vars you need to pass in to make this work  and you can initialize as whatever this case null
//    // used vaR instead of vaL because when you use vaL you cant modify it when you use vaR you can
////    private class RecyclerViewHolder_ActivityMehPoll ( val customView: View , var courseLesson: CourseLesson? = null ): RecyclerView.ViewHolder(customView) {
////    class RecyclerViewHolder_ActivityMehPoll (val customView: View, var courseLesson: CourseLesson? = null ): RecyclerView.ViewHolder(customView) {
//    class RecyclerViewHolder_ActivityMehPoll (val customView: View, var courseLesson: CourseLesson? = null ): RecyclerView.ViewHolder(customView) {
////        companion object {
////            val KEY_COURSELESSONLINK = "KEY_COURSELESSONLINK"
////        }
//
////        init {
////            customView.setOnClickListener {
////                //                println ( "attempt to load web view here")
////
////                val intent = Intent(customView.context, CourseLessonActivity::class.java)
////                intent.putExtra( KEY_COURSELESSONLINK , courseLesson?.link )
////                customView.context.startActivity( intent )
////            }
////        }
//    } //




















}