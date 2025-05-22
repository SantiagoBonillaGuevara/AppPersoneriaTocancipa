
package com.personeriatocancipa.app.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.personeriatocancipa.app.databinding.ItemMessageBotBinding
import com.personeriatocancipa.app.databinding.ItemMessageUserBinding
import com.personeriatocancipa.app.ui.chat.viewmodel.ChatBotViewModel


class ChatBotAdapter : ListAdapter<ChatBotViewModel.Message, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    companion object {
        private const val TYPE_USER = 0
        private const val TYPE_BOT  = 1

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ChatBotViewModel.Message>() {
            override fun areItemsTheSame(old: ChatBotViewModel.Message, new: ChatBotViewModel.Message) =
                old === new

            override fun areContentsTheSame(old: ChatBotViewModel.Message, new: ChatBotViewModel.Message) =
                old.text == new.text && old.fromUser == new.fromUser
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).fromUser) TYPE_USER else TYPE_BOT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_USER) {
            val binding = ItemMessageUserBinding.inflate(inflater, parent, false)
            UserVH(binding)
        } else {
            val binding = ItemMessageBotBinding.inflate(inflater, parent, false)
            BotVH(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = getItem(position)
        when (holder) {
            is UserVH -> holder.bind(msg)
            is BotVH  -> holder.bind(msg)
        }
    }

    class UserVH(private val b: ItemMessageUserBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun bind(m: ChatBotViewModel.Message) {
            b.tvMessage.text = m.text
        }
    }
    class BotVH(private val b: ItemMessageBotBinding) :
        RecyclerView.ViewHolder(b.root) {
        fun bind(m: ChatBotViewModel.Message) {
            b.tvMessage.text = m.text
        }
    }
}
