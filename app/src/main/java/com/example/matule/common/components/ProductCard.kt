package com.example.matule.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.matule.R
import com.example.matule.domain.BASE_URL
import com.example.matule.ui.theme.accent
import com.example.matule.ui.theme.block
import com.example.matule.ui.theme.subtextDark

@Composable
fun ProductCard(
    name: String,
    price: Float,
    image: String,
    isLiked: Boolean,
    inCart: Boolean,
    toCart: () -> Unit,
    toFavourite: () -> Unit,
    onClick: () -> Unit
) {
    val favIcon =
        if (isLiked) painterResource(R.drawable.heart_fav) else painterResource(R.drawable.heart)
    val cartIcon =
        if (inCart) painterResource(R.drawable.cart_truck) else painterResource(R.drawable.plus)
    val interactionSource = remember { MutableInteractionSource() }

    Card(
        modifier = Modifier.fillMaxWidth(0.5f)
            .clickable(
            interactionSource = interactionSource,
            indication = null
        ) {onClick()},
        colors = CardColors(
            block,
            contentColor = Color.Unspecified,
            disabledContainerColor = block,
            disabledContentColor = Color.Unspecified
        ),
        shape = RoundedCornerShape(20.dp),
    ) {
        Column {
            Box {
                AsyncImage(
                    model = "$BASE_URL/$image",
                    contentDescription = "Фото товара",
                )
                IconButton(
                    modifier = Modifier.size(34.dp).padding(start = 6.dp, top = 6.dp),
                    onClick = { toFavourite() }
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        painter = favIcon,
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(
                text = name,
                color = subtextDark,
                fontSize = 26.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )
            Spacer(Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .padding(start = 10.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "₽$price",
                    fontSize = 20.sp
                )
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(shape = RoundedCornerShape(topStart = 20.dp))
                        .background(accent),
                ) {
                    IconButton(
                        onClick = { toCart() }
                    ) {
                        Icon(
                            painter = cartIcon,
                            contentDescription = "",
                            tint = block
                        )
                    }
                }
            }
        }
    }
}