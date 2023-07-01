package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import ru.netology.nmedia.R
import androidx.navigation.fragment.findNavController
import androidx.paging.map
import com.bumptech.glide.Glide
import ru.netology.nmedia.dao.FloatingValues
import ru.netology.nmedia.dataClasses.AttachmentType
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.util.Companion.Companion.longArg
import ru.netology.nmedia.util.Companion.Companion.textArg
import ru.netology.nmedia.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

/** ДАННЫЙ КЛАСС РАБОТАЕТ С ОТДЕЛЬНЫХ ПОСТОМ, ЕГО ОТРИСОВКОЙ И ВЫВОДОМ ИНФОРМАЦИИ */

@AndroidEntryPoint
class PostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(layoutInflater)
        val viewModel: PostViewModel by viewModels(::requireParentFragment)

        lifecycleScope.launchWhenStarted {
            viewModel.data.collectLatest { post ->
                with(binding.scrollContent) {
                    post.map() { post ->

                        //информация о посте
                        author.text = post.author
                        published.text = post.published.toString()
                        content.text = post.content
                        like.text = post.likes.toString()
                        like.isChecked = post.likeByMe
                        shareIt.text = post.share.toString()
                        shareIt.isChecked = post.shareByMe
                        view.text = post.viewIt.toString()
                        view.isChecked = post.viewItByMe

                        //работаем с иконкой
                        Glide.with(icon)
                            .load(FloatingValues.renameUrl(post.authorImage ?: "", "avatars"))
                            .placeholder(R.drawable.ic_img_not_support)
                            .error(R.drawable.ic_not_avatar)
                            .circleCrop()
                            .timeout(10_000)
                            .into(icon)

                        //приложения к посту (фото/видео)
                        if (post.attachment != null) {
                            attachmentContent.visibility = View.VISIBLE
                            Glide.with(imageAttachment)
                                .load(FloatingValues.renameUrl(post.attachment!!.url, "media"))
                                .placeholder(R.drawable.ic_img_not_support)
                                .timeout(10_000)
                                .into(imageAttachment)
                            playButtonVideoPost.isVisible =
                                (post.attachment!!.type == AttachmentType.VIDEO)
                        } else {
                            attachmentContent.visibility = View.GONE
                        }

                        //лайкнули пост
                        like?.setOnClickListener {
                            viewModel.likeById(post.id)
                        }

                        //постом поделились
                        shareIt?.setOnClickListener {
                            val intent = Intent().apply {
                                action = Intent.ACTION_SEND
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, post.content)
                            }

                            val shareIntent =
                                Intent.createChooser(intent, getString(R.string.chooser_share_post))
                            startActivity(shareIntent)
                        }

                        //доп меню поста (удалить, изменить)
                        moreActions?.setOnClickListener {
                            PopupMenu(
                                binding.root.context,
                                binding.scrollContent.moreActions
                            ).apply {
                                inflate(R.menu.options_post)
                                setOnMenuItemClickListener {
                                    when (it.itemId) {
                                        R.id.remove -> {
                                            findNavController().navigateUp()
                                            viewModel.removeById(post.id)
                                            true
                                        }
                                        R.id.edit -> {
                                            viewModel.edit(post)
                                            findNavController().navigate(R.id.action_postFragment_to_newPostFragment, //action_feedFragment_to_newPostFragment
                                                Bundle().apply {
                                                    textArg = post.content
                                                })
                                            true
                                        }
                                        else -> false
                                    }
                                }
                            }.show()
                        }

                        //включили видео
                        playButtonVideoPost.setOnClickListener {
                            val playIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.urlOfVideo))
                            if (playIntent.resolveActivity(requireContext().packageManager) != null) {
                                startActivity(playIntent)
                            }
                        }
                    }
                }
            }
        }
        return binding.root
    }
}