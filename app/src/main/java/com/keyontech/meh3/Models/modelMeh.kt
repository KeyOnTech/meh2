package com.example.jonesq.meh3.Models

/**
 * Created by kot on 1/16/18.
 */

class JSONUrL (
    val url: String
)

class ModelMeh(
    val deal: ModelMehDeal
    , val poll: ModelMehPoll
    , val video: ModelMehVideo
)

class ModelMehDeal(
    val features: String = ""
    , val id: String = ""
    , val items: ArrayList<ModelMehItem> = arrayListOf<ModelMehItem>()
//    , val items: List<ModelMehItem>
    , val photos: ArrayList<String> = arrayListOf<String>()
//    , val photos: List<String>
    , val title: String = ""
    , val specifications: String = ""
//        , val soldOutAt: String
//        , val story: ArrayList<ModelMehStory>
    , val story: ModelMehStory
    , val theme: ModelMehTheme
    , val url: String = ""
    , val launches: ArrayList<ModelMehLaunches> = arrayListOf<ModelMehLaunches>()
//    , val launches: List<ModelMehLaunches>
    , val topic: ModelMehTopic
)






class ModelMehPoll (
//    companion object Factory {
//        fun create(): ModelMehPoll = ModelMehPoll()
//    }
//
//    fun create() = ModelMehPoll
//    {
//        return@ModelMehPoll
//    }

    val id: String = ""
    , val startDate: String = ""
    , val title: String = ""
    , val topic: ModelMehTopic
    , val answers: ArrayList<ModelMehPollAnswer> = arrayListOf<ModelMehPollAnswer>()
//    , val answers: List<ModelMehPollAnswer>


//        companion object {
//            val id: String
//            ,
//            val startDate: String
//            ,
//            val title: String
//            ,
//            val topic: ModelMehTopic
//            ,
//            val answers: ArrayList<ModelMehPollAnswer>
//        }
)

class ModelMehPollAnswer(
    val id: String
    , val text: String
    , val voteCount: Int
)








class ModelMehVideo (
    val id: String
    , val startDate: String
    , val title: String
    , val url: String
    , val topic: ModelMehTopic
)






class ModelMehTopic (
    val commentCount: Int
    , val createdAt: String
    , val id: String
    , val replyCount: Int
    , val url: String
    , val voteCount: Int
)





class ModelMehItem (
    val id: String
    , val condition: String
    , val photo: String
    , val price: Double
)






class ModelMehStory (
    val title: String
    , val body: String
)



class ModelMehTheme (
    val accentColor: String
    , val backgroundColor: String
    , val backgroundImage: String
    , val foreground: String
)

class ModelMehLaunches (
    val soldOutAt: String
)
