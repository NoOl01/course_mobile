package com.example.matule.presenter

import android.annotation.SuppressLint
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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.matule.R
import com.example.matule.common.Drawer
import com.example.matule.common.components.ProductCard
import com.example.matule.common.components.SearchButton
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.view.CartViewModel
import com.example.matule.domain.view.CategoryViewModel
import com.example.matule.domain.view.FavouriteViewModel
import com.example.matule.domain.view.ProductViewModel
import com.example.matule.domain.view.ProfileViewModel
import com.example.matule.ui.theme.background
import com.example.matule.ui.theme.block
import com.example.matule.ui.theme.text
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun MainScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    categoryViewModel: CategoryViewModel = viewModel(),
    productViewModel: ProductViewModel = viewModel(),
    favouriteViewModel: FavouriteViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val categories by categoryViewModel.categories.collectAsState()
    val products by productViewModel.products.collectAsState()
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
        categoryViewModel.getAllCategories()
        productViewModel.getAllProducts(preferencesManager)
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = {
                        isMenuOpen = true
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.hamburger),
                            contentDescription = "Меню",
                            tint = Color.Unspecified
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.highlight),
                            contentDescription = null
                        )
                        Text(
                            text = "Главная",
                            fontSize = 34.sp
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.cart),
                            contentDescription = "Корзина",
                        )
                    }
                }
                Spacer(Modifier.height(14.dp))
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    SearchButton { }
                }

                categories?.result?.let { categoriesList ->
                    Spacer(Modifier.height(10.dp))
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp)
                    ) {
                        Text(
                            text = "Категории",
                            fontSize = 30.sp
                        )
                        Spacer(Modifier.height(10.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(20.dp)
                        ) {
                            items(categoriesList) { category ->
                                Button(
                                    modifier = Modifier
                                        .width(140.dp)
                                        .height(50.dp),
                                    shape = RoundedCornerShape(10.dp),
                                    colors = ButtonColors(
                                        containerColor = block,
                                        contentColor = text,
                                        disabledContentColor = block,
                                        disabledContainerColor = text
                                    ),
                                    onClick = {}
                                ) {
                                    category?.let {
                                        Text(
                                            text = it.name,
                                            fontSize = 20.sp
                                        )
                                    }
                                }
                            }
                        }
                        Spacer(Modifier.height(20.dp))
                        products?.result?.let { productsList ->
                            FlowRow(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp)
                            ) {
                                productsList.forEach { product ->
                                    var isLiked by remember { mutableStateOf(product.is_liked)}
                                    var inCart by remember { mutableStateOf(product.in_cart)}

                                    ProductCard(
                                        name = product.name,
                                        price = product.price,
                                        image = product.image,
                                        isLiked = isLiked,
                                        inCart = inCart,
                                        toCart = {
                                            scope.launch {
                                                if (!inCart){
                                                    val err = cartViewModel.addToCart(preferencesManager, product.id)
                                                    if (err.error == null){
                                                        inCart = true
                                                    }
                                                } else {
                                                    navController.navigate("CartScreen")
                                                }
                                            }
                                        },
                                        toFavourite = {
                                            scope.launch {
                                                if (!isLiked){
                                                    val err = favouriteViewModel.addToFavourite(preferencesManager, product.id)
                                                    if (err.error == null){
                                                        isLiked = true
                                                    }
                                                } else {
                                                    val err = favouriteViewModel.deleteFromFavourite(preferencesManager, product.id)
                                                    if (err.error == null){
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
            }
        }
    }
}