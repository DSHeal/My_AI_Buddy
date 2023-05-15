package com.dsheal.youraimentor.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.dsheal.youraimentor.databinding.FragmentGptChatBinding
import com.dsheal.youraimentor.viewmodel.GptChatViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GptChatFragment : Fragment() {

    private var mBinding: FragmentGptChatBinding? = null
    private val binding get() = mBinding!!

    private val gptChatViewModel: GptChatViewModel by viewModels<GptChatViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentGptChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    //вся инициализация должна быть после создания View, т.е. в этом методе, а не в onCreate!
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recycler = binding.rvQuestionsAndAnswers
        recycler.apply {
            adapter = QAndAHistoryRecyclerAdapter()
            layoutManager = LinearLayoutManager(requireContext())
        }
        gptChatViewModel.questionsAndAnswers.observe(viewLifecycleOwner) { qAndAList ->
            (recycler.adapter as QAndAHistoryRecyclerAdapter).updateList(qAndAList)
            recycler.smoothScrollToPosition(if (qAndAList.size > 1) qAndAList.size - 1 else qAndAList.size)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            gptChatViewModel.answer.collect { response ->
                binding.answerText.text = response?.message
                gptChatViewModel.listenAllPreviousQuestionsAndAnswersFromDb()
            }
        }
        binding.sendButton.setOnClickListener {
            val input = binding.questionInput.text.toString()
            if (input.isNotBlank()) {
                gptChatViewModel.getAnswerFromApiAndSave(input)
                binding.questionInput.setText("")
            }
            binding.questionInput.onEditorAction(EditorInfo.IME_ACTION_DONE)
        }
    }
}