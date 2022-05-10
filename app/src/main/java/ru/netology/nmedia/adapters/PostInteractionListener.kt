package ru.netology.nmedia.adapters

import ru.netology.nmedia.data.dto.Post

interface PostInteractionListener {

    fun onLikeCLicked(post: Post)

    fun onShareClicked(post: Post)

    fun onRemoveClicked(post: Post)

    fun onEditClicked(post:Post)


}