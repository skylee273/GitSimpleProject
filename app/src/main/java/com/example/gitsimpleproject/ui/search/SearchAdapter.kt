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
import kotlinx.android.synthetic.main.item_repository.view.*


class SearchAdapter : RecyclerView.Adapter<SearchAdapter.RepositoryHolder>() {

    // 초깃값을 빈 ArrayList 할당
    private var items: MutableList<GithubRepo> = mutableListOf()

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepositoryHolder =
        RepositoryHolder(parent);


    override fun onBindViewHolder(holder: RepositoryHolder, position: Int) {
        // let() 함수를 사용하여 값이 사용되는 범위를 한정한다.
        items[position].let { repo ->
            with(holder.itemView) {
                Glide.with(context)
                    .load(repo.owner.avatarUrl)
                    .placeholder(placeholder)
                    .into(ivItemRepositoryProfile)
                tvItemRepositoryName.text = repo.fullName
                tvItemRepositoryLanguage.text =
                    if (TextUtils.isEmpty(repo.language)) holder.itemView.context.getText(R.string.no_language_specified) else repo.language
                listener?.onItemClick(repo)
            }

        }
    }

    // 항상 크기만 반환되기 때문에 단일 표현식 가능
    override fun getItemCount(): Int = items.size

    fun setItems(items: List<GithubRepo>) {
        this.items = items.toMutableList()
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