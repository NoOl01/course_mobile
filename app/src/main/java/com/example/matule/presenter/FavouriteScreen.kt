package com.example.matule.presenter

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.matule.R
import com.example.matule.common.Drawer
import com.example.matule.common.components.BottomBar
import com.example.matule.common.components.ProductCard
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.view.CartViewModel
import com.example.matule.domain.view.FavouriteViewModel
import com.example.matule.domain.view.ProfileViewModel
import com.example.matule.ui.theme.background
import com.example.matule.ui.theme.block
import kotlinx.coroutines.launch
import kotlin.math.abs

@Composable
fun FavouriteScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    cartViewModel: CartViewModel = viewModel(),
    favouriteViewModel: FavouriteViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val fav by favouriteViewModel.products.collectAsState()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val interactionSource = remember { MutableInteractionSource() }

    var isMenuOpen by remember { mutableStateOf(false) }

    val offsetX by animateFloatAsState(if (isMenuOpen) 180f else 0f, label = "")
    val rotation by animateFloatAsState(if (isMenuOpen) -5f else 0f, label = "")
    val scale by animateFloatAsState(if (isMenuOpen) 0.6f else 1f, label = "")
    val cornerRadius by animateDpAsState(
        targetValue = if (isMenuOpen) 40.dp else 0.dp, label = ""
    )
    val shadowSize by animateDpAsState(
        targetValue = if (isMenuOpen) 20.dp else 0.dp, label = ""
    )

    LaunchedEffect(Unit) {
        favouriteViewModel.getAllProducts(preferencesManager)
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Drawer(navController, scope, preferencesManager, profileViewModel) { }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .offset(x = offsetX.dp)
                .rotate(rotation)
                .scale(scale)
                .shadow(shadowSize, shape = RoundedCornerShape(cornerRadius))
                .clip(RoundedCornerShape(cornerRadius))
                .pointerInput(Unit) {
                    detectDragGestures { _, dragAmount ->
                        val (dx, dy) = dragAmount
                        if (abs(dx) > abs(dy) * 1.5f) {
                            when {
                                dx > 10 -> isMenuOpen = true
                                dx < -10 -> isMenuOpen = false
                            }
                        }
                    }
                }
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    if (isMenuOpen) isMenuOpen = false
                },
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(background)
                    .padding(horizontal = 6.dp)
                    .padding(top = 40.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(0.6f),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
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
                        text = "Избранные",
                        fontSize = 20.sp
                    )
                }
                fav?.result?.let { productsList ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 90.dp)
                            .verticalScroll(rememberScrollState())
                    ) {
                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            productsList.forEach { product ->
                                var isLiked by remember { mutableStateOf(product.is_liked) }
                                var inCart by remember { mutableStateOf(product.in_cart) }

                                ProductCard(
                                    name = product.name,
                                    price = product.price,
                                    image = product.image,
                                    isLiked = isLiked,
                                    inCart = inCart,
                                    toCart = {
                                        scope.launch {
                                            if (!inCart) {
                                                val err = cartViewModel.addToCart(
                                                    preferencesManager,
                                                    product.id
                                                )
                                                if (err.error == null) {
                                                    inCart = true
                                                }
                                            } else {
                                                navController.navigate("CartScreen")
                                            }
                                        }
                                    },
                                    toFavourite = {
                                        scope.launch {
                                            if (!isLiked) {
                                                val err = favouriteViewModel.addToFavourite(
                                                    preferencesManager,
                                                    product.id
                                                )
                                                if (err.error == null) {
                                                    isLiked = true
                                                }
                                            } else {
                                                val err = favouriteViewModel.deleteFromFavourite(
                                                    preferencesManager,
                                                    product.id
                                                )
                                                if (err.error == null) {
                                                    isLiked = false
                                                }
                                            }
                                        }
                                    },
                                    onClick = {
                                        navController.navigate("ProductScreen/${product.id}")
                                    }
                                )
                            }
                        }
                    }
                }
            }
            val navBackStackEntry: NavBackStackEntry? =
                navController.currentBackStackEntryAsState().value
            val currentRoute = navBackStackEntry?.destination?.route

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                BottomBar(navController, currentRoute)
            }
        }
    }
}