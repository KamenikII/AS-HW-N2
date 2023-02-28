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
import ru.netology.nmedia.R
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import ru.netology.nmedia.dao.FloatingValues
import ru.netology.nmedia.dataClasses.AttachmentType
import ru.netology.nmedia.databinding.FragmentPostBinding
import ru.netology.nmedia.util.Companion.Companion.longArg
import ru.netology.nmedia.util.Companion.Companion.textArg
import ru.netology.nmedia.viewmodel.PostViewModel

class PostFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentPostBinding.inflate(layoutInflater)
        val viewModel: PostViewModel by viewModels(::requireParentFragment)

        with(binding.scrollContent) {
            viewModel.data.observe(viewLifecycleOwner) { model ->
                val post = model.posts.find { it.id == arguments?.longArg }
                if (post != null) {
                    author.text = post.author
                    published.text = post.published
                    content.text = post.content
                    like.text = post.likes.toString()
                    like.isChecked = post.likeByMe
                    shareIt.text = post.share.toString()
                    shareIt.isChecked = post.shareByMe
                    view.text = post.viewIt.toString()
                    view.isChecked = post.viewItByMe
                    Glide.with(icon)
                        .load(FloatingValues.renameUrl(post.authorImage ?: "", "avatars"))
                        .placeholder(R.drawable.ic_img_not_support)
                        .error(R.drawable.ic_not_avatar)
                        .circleCrop()
                        .timeout(10_000)
                        .into(icon)

                    if (post.attachment != null) {
                        attachmentContent.visibility = View.VISIBLE
                        Glide.with(imageAttachment)
                            .load(FloatingValues.renameUrl(post.attachment!!.url, "media"))
                            .placeholder(R.drawable.ic_img_not_support)
                            .timeout(10_000)
                            .into(imageAttachment)
                        playButtonVideoPost.isVisible = (post.attachment!!.type == AttachmentType.VIDEO)
                    } else {
                        attachmentContent.visibility = View.GONE
                    }

                    like?.setOnClickListener {
                        viewModel.likeById(post.id)
                    }

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

                    moreActions?.setOnClickListener {
                        PopupMenu(binding.root.context, binding.scrollContent.moreActions).apply {
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

                    playButtonVideoPost.setOnClickListener {
                        val playIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.urlOfVideo))
                        if (playIntent.resolveActivity(requireContext().packageManager) != null) {
                            startActivity(playIntent)
                        }
                    }
                }
            }
        }
        return binding.root
    }
}