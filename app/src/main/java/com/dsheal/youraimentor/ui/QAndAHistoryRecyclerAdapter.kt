package com.dsheal.youraimentor.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.dsheal.youraimentor.databinding.ItemQAndAHistoryBinding
import com.dsheal.youraimentor.domain.models.GptChatQuestionAndAnswerModel

class QAndAHistoryRecyclerAdapter :
    RecyclerView.Adapter<QAndAHistoryRecyclerAdapter.QAndAHistoryViewHolder>() {

    private val qAndAHistoryList = AsyncListDiffer(this, QAndAHistoryDiffCallback())

    class QAndAHistoryViewHolder(private val binding: ItemQAndAHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GptChatQuestionAndAnswerModel) {
            binding.tvQuestion.text = item.question
            binding.tvAnswer.text = item.answer
        }
    }

    fun updateList(newList: List<GptChatQuestionAndAnswerModel>) {
        qAndAHistoryList.submitList(newList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): QAndAHistoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemQAndAHistoryBinding.inflate(layoutInflater, parent, false)
        return QAndAHistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: QAndAHistoryViewHolder, position: Int) {
        holder.bind(qAndAHistoryList.currentList[position])
    }

    override fun getItemCount(): Int {
        return qAndAHistoryList.currentList.size
    }

    class QAndAHistoryDiffCallback: DiffUtil.ItemCallback<GptChatQuestionAndAnswerModel>() {

        override fun areItemsTheSame(
            oldItem: GptChatQuestionAndAnswerModel,
            newItem: GptChatQuestionAndAnswerModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: GptChatQuestionAndAnswerModel,
            newItem: GptChatQuestionAndAnswerModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}