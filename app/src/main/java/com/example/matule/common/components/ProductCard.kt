package com.example.matule.common.components

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Card
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable

@Composable
fun ProductCard(
    id: Long,
    name: String,
    price: Float,
    image: String,
    isLiked: Boolean,
    inCart: Boolean,
    toCart: () -> Unit,
    toFavourite: () -> Unit
) {
    Card() {
        Row {
            IconButton(onClick = {}) {

            }
        }
    }
}