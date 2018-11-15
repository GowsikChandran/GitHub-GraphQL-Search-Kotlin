package com.asana.github.graphqlkotlin.search

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.apollo.apolographql.api.SearchRepoQuery
import com.asana.github.graphqlkotlin.R
import com.asana.github.graphqlkotlin.util.GlideApp
import com.bumptech.glide.load.engine.DiskCacheStrategy
import java.util.ArrayList

/**
 * The SearchRepoAdapter is an adapter class for the search repository recycler view
 * in the Search Activity
 *
 * @author Gowsik K C
 * @version 1.0 ,11/2/2018
 * @since 1.0
 */

class SearchRepoAdapter(val mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG = SearchRepoAdapter::class.simpleName

    //constants for repository item and loading item
    companion object {
        private val VIEW_TYPE_REPO_ITEM = 0
        private val VIEW_TYPE_LOADING = 1
    }


    private val repositoryList = ArrayList<SearchRepoQuery.Edge?>()


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)

        //return appropriate view holder

        return when (viewType) {

            VIEW_TYPE_REPO_ITEM -> SearchViewHolder(layoutInflater.inflate(R.layout.rec_view_search_item, parent, false))

            else -> LoadingViewHolder(layoutInflater.inflate(R.layout.rec_view_loading_item, parent, false))

        }


    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {

            is SearchViewHolder -> {

                val repoItem: SearchRepoQuery.Edge? = repositoryList.get(position)

                GlideApp.with(mContext)
                        .load(repoItem?.node()?.fragments()?.repositoryFragment()?.owner()?.avatarUrl())
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .into(holder.avatarImgView)

                holder.nameTv.text = repoItem?.node()?.fragments()?.repositoryFragment()?.name()
                holder.descriptionTv.text = repoItem?.node()?.fragments()?.repositoryFragment()?.description()
                holder.forksTv.text = repoItem?.node()?.fragments()?.repositoryFragment()?.forkCount().toString()

            }


            is LoadingViewHolder -> {

                holder.progressBar.visibility = View.VISIBLE
            }

        }

    }

    override fun getItemCount() = repositoryList.size


    /**
     * Decides the view type of an item
     *
     * @param position int value position of item
     * @return int view type
     */
    override fun getItemViewType(position: Int): Int {
        return if (repositoryList[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_REPO_ITEM
    }

    /**
     * adds the given repository list to the existing List
     *
     * @param repositoryList list of git hub repository items
     */
    fun addRepos(repositoryList: List<SearchRepoQuery.Edge>) {

        this.repositoryList.addAll(itemCount, repositoryList)
        notifyDataSetChanged()
    }

    /**
     * Adds a null value to the end of the list
     * when the item is null the view type changer to
     * loading View to indicate the user that next set of
     * items is being loaded
     */
    fun showLoading() {
        repositoryList.add(null)
        notifyItemInserted(repositoryList.size - 1)
    }

    /**
     * Removes the last item - a null value in the end of the list
     * when the next set of data has been loaded
     */
    fun hideLoading() {
        repositoryList.removeAt(repositoryList.size - 1)
        notifyItemRemoved(repositoryList.size)
    }

    /**
     * clears the the repository list
     */
    fun clear() {
        val size = itemCount
        repositoryList.clear()
        notifyItemRangeRemoved(0, size)
    }

    /**
     * ViewHolder class to show a Repository item
     * in the Recycler view
     *
     * @author Gowsik K C
     * @version 1.0 ,11/2/2018
     * @since 1.0
     */
    class SearchViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val avatarImgView: ImageView = view.findViewById(R.id.avatar_image_view)
        val nameTv: TextView = view.findViewById(R.id.repo_name_tv)
        val descriptionTv: TextView = view.findViewById(R.id.repo_description_tv)
        val forksTv: TextView = view.findViewById(R.id.forks_tv)

    }

    /**
     * ViewHolder class to show a progress bar
     * in the Recycler view
     *
     * @author Gowsik K C
     * @version 1.0 ,11/2/2018
     * @since 1.0
     */
    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val progressBar: ProgressBar = view.findViewById(R.id.rv_repo_list_pb)

    }

}
