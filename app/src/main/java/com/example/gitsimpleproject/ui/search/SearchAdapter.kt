package com.example.gitsimpleproject.ui.search

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gitsimpleproject.R
import com.example.gitsimpleproject.api.model.GithubRepo


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.RepositoryHolder>() {

    private var items: MutableList<GithubRepo> = ArrayList()

    private val placeholder = ColorDrawable(Color.GRAY)

    @Nullable
    private var listener: ItemClickListener? = null

    inner class RepositoryHolder(parent: ViewGroup) : RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_repository, parent, false)
    ) {
        var ivProfile: ImageView = itemView.findViewById(R.id.ivItemRepositoryProfile)
        var tvName: TextView = itemView.findViewById(R.id.tvItemRepositoryName)
        var tvLanguage: TextView = itemView.findViewById(R.id.tvItemRepositoryLanguage)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryHolder {
        return RepositoryHolder(parent);
    }

    override fun onBindViewHolder(holder: RepositoryHolder, position: Int) {
        val repo: GithubRepo = items.get(position)

        Glide.with(holder.itemView.context)
            .load(repo.owner!!.avatarUrl)
            .placeholder(placeholder)
            .into(holder.ivProfile)

        holder.tvName.text = repo.fullName
        holder.tvLanguage.text =
            if (TextUtils.isEmpty(repo.language)) holder.itemView.context.getText(R.string.no_language_specified) else repo.language

        holder.itemView.setOnClickListener {
            listener?.onItemClick(repo)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(@NonNull items: MutableList<GithubRepo>?) {
        this.items = items!!
    }

    fun clearItems() {
        items.clear()
    }
    fun setItemClickListener(listener: ItemClickListener?) {
        this.listener = listener
    }

    interface ItemClickListener {
        fun onItemClick(repository: GithubRepo?)
    }
}