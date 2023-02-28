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

class NewPostFragment : Fragment() {
    private val binding by lazy { FragmentNewPostBinding.inflate(layoutInflater) }
    private val viewModel: PostViewModel by viewModels(
        ownerProducer = ::requireParentFragment
    )

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

//            if (edit.text.isNullOrBlank()) {
//                edit.setText(textNewPost)
//            }
//
//            edit.requestFocus()
//
//
//            clickListeners()

            return root
        }
    }

//    override fun onStart() {
//        currentFragment = javaClass.simpleName
//        super.onStart()
//    }

//    private fun clickListeners() {
//        with(binding) {
//
//            fabComplete.setOnClickListener {
//                if (!edit.text.isNullOrBlank()) {
//                    val content = edit.text.toString()
//                    viewModel.changeContent(content)
//                    viewModel.save()
//                }
//                hideKeyboard(root)
//                fabComplete.isVisible = false
//                fabCancel.isVisible = false
//                edit.isVisible = false
//                savingProgressBar.isVisible = true
//            }
//            viewModel.postCreated.observe(viewLifecycleOwner) {
//                viewModel.loadPosts()
//                findNavController().navigateUp()
//            }
//
//            fabCancel.setOnClickListener {
//                if (viewModel.getEditedId() == 0L) {
//                    textNewPost = edit.text.toString()
//                } else {
//                    edit.text.clear()
//                    viewModel.save()
//                }
//                hideKeyboard(root)
//                findNavController().navigateUp()
//            }
//
//        }
//    }
}