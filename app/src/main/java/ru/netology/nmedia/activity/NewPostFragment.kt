package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ru.netology.nmedia.dao.FloatingValues.textNewPost
import ru.netology.nmedia.util.AndroidUtils
import ru.netology.nmedia.viewmodel.PostViewModel
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.util.Companion.Companion.textArg

/** ДАННЫЙ КЛАСС ОТВЕЧАЕТ ЗА СОЗДАНИЕ НОВОГО ПОСТА */

class NewPostFragment : Fragment() {
    private val binding by lazy { FragmentNewPostBinding.inflate(layoutInflater) }
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

    //редачим / создаём пост
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        with(binding) {

            arguments?.textArg?.let(binding.edit::setText)

            ok.setOnClickListener {
                viewModel.changeContent(binding.edit.text.toString())
                viewModel.save()
                AndroidUtils.hideKeyboard(requireView())
                findNavController().navigateUp()
            }
            return root
        }
    }
}