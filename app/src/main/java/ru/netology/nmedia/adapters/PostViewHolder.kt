package ru.netology.nmedia.adapters

import android.view.View
import android.widget.PopupMenu
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.netology.nmedia.R
import ru.netology.nmedia.dao.FloatingValues.renameUrl
import ru.netology.nmedia.dataClasses.AttachmentType
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.databinding.FragmentCardPostBinding

/** РЕНДЕРИНГ ПОСТОВ, ИХ ВНЕШНИЙ ВИД, "СЛУШАЕМ" НАЖАТИЯ */

class PostViewHolder(
    private val binding: FragmentCardPostBinding,
    private val onPostListener: OnPostListener //Описано в PostAdapter
) : RecyclerView.ViewHolder(binding.root) {

    fun renderingPostStructure(post: Post) {
        with(binding) {
            author.text = post.author //Автор поста
            published.text = post.published //Дата публикации
            content.text = post.content //текст поста
            like.text = post.likes.toString() //кол-во лайков
            like.isChecked = post.likeByMe //понравилось ли пользователю
            shareIt.text = post.share.toString() //кол-во репостов
            shareIt.isChecked = post.shareByMe //поделился ли пользователь
            view.text = post.viewIt.toString() //кол-во просмотров
            view.isChecked = post.viewItByMe //видел ли пользователь
            Glide.with(icon)
                .load(renameUrl(post.authorImage ?: "","avatars"))
                .placeholder(R.drawable.ic_img_not_support)
                .error(R.drawable.ic_not_avatar)
                .circleCrop()
                .timeout(10_000)
                .into(icon)
            if (post.attachment != null) {
                attachmentContent.isVisible = true
                Glide.with(imageAttachment)
                    .load(renameUrl(post.attachment.url,"media"))
                    .placeholder(R.drawable.ic_img_not_support)
                    .timeout(10_000)
                    .into(imageAttachment)
                descriptionAttachment.text = post.attachment.description
                playButtonVideoPost.isVisible = (post.attachment.type == AttachmentType.VIDEO)
            } else {
                attachmentContent.visibility = View.GONE
            }


            //like listener
            like.setOnClickListener{
                onPostListener.onLike(post)
            }
            like.isChecked = post.likeByMe
            like.text = post.likes.toString()

            //share listener
            shareIt.setOnClickListener{
                onPostListener.onShare(post)
            }
            shareIt.isChecked = post.shareByMe
            shareIt.text = post.share.toString()

            //view listener
            view.text = post.viewIt.toString()
            onPostListener.view(post)

            //menu listener
            moreActions.setOnClickListener {
                PopupMenu(it.context, it).apply {
                    inflate(R.menu.options_post) //при нажатие открывается менюшка res/menu/optional_post.xml
                    setOnMenuItemClickListener { item ->
                        when (item.itemId) {
                            R.id.remove -> {
                                onPostListener.onRemove(post)
                                true
                            }
                            R.id.edit -> {
                                onPostListener.onEdit(post)
                                true
                            }

                            else -> false
                        }
                    }
                }.show()
            }
        }
    }
}
