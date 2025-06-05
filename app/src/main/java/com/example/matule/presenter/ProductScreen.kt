package com.example.matule.presenter

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.matule.R
import com.example.matule.common.components.PagerWithSemiOvalSlider
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.view.CartViewModel
import com.example.matule.domain.view.FavouriteViewModel
import com.example.matule.domain.view.ProductViewModel
import com.example.matule.ui.theme.accent
import com.example.matule.ui.theme.background
import com.example.matule.ui.theme.block
import com.example.matule.ui.theme.subTextLight
import kotlinx.coroutines.launch

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun ProductScreen(
    navController: NavController,
    id: Long,
    productViewModel: ProductViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(),
    favouriteViewModel: FavouriteViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val product by productViewModel.productInfo.collectAsState()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val scroll = rememberScrollState()
    val interactionSource = remember { MutableInteractionSource() }

    var expanded by remember { mutableStateOf(false) }
    var fullscreenImage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        productViewModel.getProductInfo(preferencesManager, id)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .padding(horizontal = 6.dp)
            .padding(top = 40.dp)
            .verticalScroll(scroll),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        navController.popBackStack()
                    },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = block
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back_arrow),
                        contentDescription = "назад"
                    )
                }
                Text(
                    text = "Sneaker Shop",
                    fontSize = 28.sp
                )
                IconButton(
                    onClick = {},
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = block
                    )
                ) {
                    Icon(
                        painter = painterResource(R.drawable.cart),
                        contentDescription = "Корзина",
                    )
                }
            }
            product?.result?.let { p ->
                val pagerState = rememberPagerState(pageCount = { p.image.size })
                val progress = pagerState.currentPage + pagerState.currentPageOffsetFraction
                progress / (p.image.size - 1).coerceAtLeast(1)
                val limit = 100
                val displayText = if (expanded || p.description.length <= limit) {
                    p.description
                } else {
                    p.description.take(limit)
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp)
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(0.5f),
                        text = p.name,
                        fontSize = 40.sp
                    )
                    Text(
                        text = "₽${p.price}",
                        fontSize = 24.sp
                    )
                    PagerWithSemiOvalSlider(
                        pagerState = pagerState,
                        pageCount = p.image.size,
                        images = p.image,
                        onClickImage = {

                        }
                    )
                    Spacer(Modifier.height(80.dp))
                    Text(
                        text = displayText,
                        fontSize = 22.sp
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        if (p.description.length > limit) {
                            Text(
                                modifier = Modifier.clickable(
                                    interactionSource = interactionSource,
                                    indication = null
                                ) {
                                    expanded = !expanded
                                },
                                text = if (expanded) "Скрыть" else "Подробнее",
                                overflow = TextOverflow.Ellipsis,
                                fontSize = 18.sp,
                                color = accent
                            )
                        }
                    }
                }
            }
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 36.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            product?.result?.let { p ->
                var isLiked by remember { mutableStateOf(p.is_liked)}
                var inCart by remember { mutableStateOf(p.in_cart)}
                var favIcon =
                    if (isLiked) painterResource(R.drawable.heart_fav) else painterResource(R.drawable.heart)
                val cartText = if (inCart) "Перейти в корзину" else "Добавить в корзину"

                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(shape = RoundedCornerShape(50))
                        .background(subTextLight),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        modifier = Modifier.size(24.dp)
                            .clickable(
                                interactionSource = interactionSource,
                                indication = null
                            ) {
                                scope.launch {
                                    if (!isLiked){
                                        val err = favouriteViewModel.addToFavourite(preferencesManager, p.id)
                                        if (err.error == null){
                                            isLiked = true
                                        }
                                    } else {
                                        val err = favouriteViewModel.deleteFromFavourite(preferencesManager, p.id)
                                        if (err.error == null){
                                            isLiked = false
                                        }
                                    }
                                }
                            },
                        painter = favIcon,
                        contentDescription = "",
                        tint = Color.Unspecified
                    )
                }
                Button(
                    onClick = {
                        scope.launch {
                            if (!inCart){
                                val err = cartViewModel.addToCart(preferencesManager, p.id)
                                if (err.error == null){
                                    inCart = true
                                }
                            } else {
                                navController.navigate("CartScreen")
                            }
                        }
                    },
                    colors = ButtonColors(
                        containerColor = accent,
                        disabledContainerColor = accent,
                        contentColor = Color.Unspecified,
                        disabledContentColor = Color.Unspecified
                    ),
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier.fillMaxWidth(0.9f).height(44.dp)
                ) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.cart),
                            contentDescription = "Корзина",
                            tint = block
                        )
                        Text(
                            text = cartText,
                            fontSize = 20.sp,
                            color = block
                        )
                    }
                }
            }
        }
//        fullscreenImage?.let { imageUrl ->
//            Dialog(onDismissRequest = { fullscreenImage = null }) {
//                Box(
//                    Modifier
//                        .fillMaxSize()
//                        .background(Color.Black)
//                        .clickable { fullscreenImage = null }
//                ) {
//                    AsyncImage(
//                        model = imageUrl,
//                        contentDescription = null,
//                        contentScale = ContentScale.Fit,
//                        modifier = Modifier
//                            .fillMaxSize()
//                            .align(Alignment.Center)
//                    )
//                }
//            }
//        }
    }
}
