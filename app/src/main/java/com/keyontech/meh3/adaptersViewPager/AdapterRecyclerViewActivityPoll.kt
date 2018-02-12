package com.keyontech.meh3.adaptersViewPager

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.jonesq.meh3.Models.ModelMehPoll
import com.keyontech.meh3.R
import kotlinx.android.synthetic.main.recyclerview_row_poll_activity.view.*


/**
 * Created by kot on 2/11/18.
 */

//class AdapterRecyclerViewActivityPoll: RecyclerView.Adapter<CustomerViewHolderClass>() {
//class AdapterRecyclerViewActivityPoll(val pollReturn: ModelMehPoll): RecyclerView.Adapter<CustomerViewHolderClass>() {
class AdapterRecyclerViewActivityPoll(val pollReturn: ModelMehPoll, val pollProgressMax: Int): RecyclerView.Adapter<CustomerViewHolderClass>() {
    override fun getItemCount(): Int {
        return pollReturn.answers.count()
    }

    // users custom view holder class below
    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CustomerViewHolderClass {
        /*** this method talks to the recyclerview_content layout file */
        val layoutInflator = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflator.inflate(R.layout.recyclerview_row_poll_activity,parent,false)

        /*** return cannot use cellForRow requires return below */
        return CustomerViewHolderClass(cellForRow)
    }
    override fun onBindViewHolder(holder: CustomerViewHolderClass?, position: Int) {
        holder?.customViewHolder_View?.progressBar_Poll_Answer?.max = pollProgressMax
        holder?.customViewHolder_View?.progressBar_Poll_Answer?.setProgress(pollReturn.answers[position].voteCount, true)
        holder?.customViewHolder_View?.textView_Poll_Answer?.text = pollReturn.answers[position].text
    }

}

/*** the customViewHolder_View at the end is the same view defined as a parameter class CustomerViewHolderClass(val customViewHolder_View: View) */
class CustomerViewHolderClass(val customViewHolder_View: View): RecyclerView.ViewHolder(customViewHolder_View) {

}