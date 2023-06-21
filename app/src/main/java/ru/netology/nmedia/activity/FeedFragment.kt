package ru.netology.nmedia.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.PictureViewFragment.Companion.urlArg
import ru.netology.nmedia.adapters.OnPostListener
import ru.netology.nmedia.adapters.PostsAdapter
import ru.netology.nmedia.dataClasses.Post
import ru.netology.nmedia.databinding.FragmentFeedBinding
import ru.netology.nmedia.util.Companion.Companion.longArg
import ru.netology.nmedia.util.Companion.Companion.textArg
import ru.netology.nmedia.viewmodel.AuthViewModel
import ru.netology.nmedia.viewmodel.PostViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.auth.AppAuth
import javax.inject.Inject

/** ДАННЫЙ КЛАСС ОТВЕЧАЕТ ЗА ЛЕНТУ НОВОСТЕЙ, РАБОТУ С ПОСТАМИ И ОТРИСОВКУ, А ТАК ЖЕ НАВИГАЦИЮ */

@AndroidEntryPoint
class FeedFragment : Fragment() {

    @Inject //Внедрение зависимости
    lateinit var appAuth: AppAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //рисуем фрагмент fragment_feed
        val binding = FragmentFeedBinding.inflate(layoutInflater)

        var newPostCount = 0

        //viewmodel
        val viewModel: PostViewModel by viewModels(
            ownerProducer = ::requireParentFragment,
        )
        val authViewModel: AuthViewModel by viewModels()

        val adapter = PostsAdapter(
            object : OnPostListener {
                //нажали лайк, переходим во viewmodel
                override fun onLike(post: Post) { //все override ведут в ../viewmodelPostViewModel
                    viewModel.likeById(post.id)
                }

                //поделились записью, переходим во viewmodel + БД
                override fun onShare(post: Post) {
                    val intent = Intent().apply {
                        action = Intent.ACTION_SEND
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, post.content)
                    }

                    val shareIntent =
                        Intent.createChooser(intent, getString(R.string.chooser_share_post))
                    startActivity(shareIntent)

                    viewModel.shareById(post)
                }

                //удалили пост, переходим во viewmodel
                override fun onRemove(post: Post) {
                    viewModel.removeById(post.id)
                }

                //изменили пост, переходим во viewmodel
                override fun onEdit(post: Post) {
                    viewModel.edit(post)
                    findNavController().navigate(
                        R.id.action_feedFragment_to_newPostFragment,
                        Bundle().apply {
                            textArg = post.content
                        })
                }

                //запустили видео
                override fun onPlayVideo(post: Post) {
                    val playIntent = Intent(Intent.ACTION_VIEW, Uri.parse(post.urlOfVideo))
                    if (playIntent.resolveActivity(requireContext().packageManager) != null) {
                        startActivity(playIntent)
                    }
                }

                //превью поста
                override fun onPreviewPost(post: Post) {
                    findNavController().navigate(
                        R.id.action_feedFragment_to_postFragment,
                        Bundle().apply {
                            longArg = post.id
                    })
                }

                //запись просмотрена
                override fun view(post: Post) {
                    viewModel.viewById(post)
                }

                //обновление
                override fun onReload() {
                    viewModel.loadPosts()
                }

                //нажали на картинку
                override fun onPicture(url: String) {
                    findNavController().navigate(R.id.action_feedFragment_to_pictureViewFragment,
                        Bundle().apply { urlArg = url })
                }

            }
        )

        binding.list.adapter = adapter

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.progress.isVisible = state.loading
            binding.swipe.isRefreshing = state.refreshing
            if (state.error){
                Snackbar.make(binding.root, R.string.error_loading, Snackbar.LENGTH_LONG)
                    .setAction(R.string.retry_loading){viewModel.loadPosts()}
                    .show()
            }
        }
        viewModel.newerCount.observe(viewLifecycleOwner) { state ->
            println("----------Новый пост: " + state)
            if (state != 0) {
                newPostCount += 1
                binding.newPostButton.isVisible = true
                binding.newPostButton.text = "$newPostCount" + getString(R.string.new_post)
            }

        }

        binding.swipe.setOnRefreshListener { viewModel.refreshPosts()}

        viewModel.data.observe(viewLifecycleOwner) { data ->
            adapter.submitList(data.posts)
            binding.emptyText.isVisible = data.empty
        }

        binding.retryButton.setOnClickListener {
            viewModel.loadPosts()
        }

        //нажали кнопку создания нового поста
        binding.fab.setOnClickListener {
            findNavController().navigate(R.id.action_feedFragment_to_newPostFragment)
        }


        //login | logout
        var menuProvider: MenuProvider? = null
        authViewModel.data.observe(viewLifecycleOwner) { authState ->
            menuProvider?.let { requireActivity().removeMenuProvider(it)}
            requireActivity().addMenuProvider(object : MenuProvider {
                //раздуваем менюшку
                override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                    menuInflater.inflate(R.menu.main_menu, menu)

                    menu.setGroupVisible(R.id.authorized, authViewModel.authenticated)
                    menu.setGroupVisible(R.id.unauthorized, !authViewModel.authenticated)
                }

                //реакции на кнопки
                override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                    return when (menuItem.itemId) {
                        R.id.signin -> {
                            findNavController().navigate(
                                R.id.action_feedFragment_to_authFragment,
                                Bundle().apply {
                                    textArg = getString(R.string.sign_in)
                                }
                            )
                            appAuth.setAuth(5, "x-token")
                            true
                        }
                        R.id.signup -> {
                            findNavController().navigate(
                                R.id.action_feedFragment_to_authFragment,
                                Bundle().apply {
                                    textArg = getString(R.string.sign_up)
                                }
                            )
                            appAuth.setAuth(5, "x-token")
                            true
                        }
                        R.id.signout -> {
                            appAuth.removeAuth()
                            true
                        }
                        else -> false
                    }
                }
            }.apply { menuProvider = this }, viewLifecycleOwner)
        }

        return binding.root
    }
}