package com.mutkuensert.movee.data.util


fun getImageUrl(path: String?, size: ApiImageSize = ApiImageSize.SIZE_ORIGINAL): String? {
    return path?.let { IMAGE_BASE_URL + size.sizePath + it }
}

enum class ApiImageSize(val sizePath: String) {
    POSTER_SIZE_W500("w500"),
    SIZE_ORIGINAL("original")
}

const val IMAGE_BASE_URL = "https://image.tmdb.org/t/p/"