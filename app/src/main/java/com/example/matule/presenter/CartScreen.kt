package com.example.matule.presenter

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.matule.R
import com.example.matule.common.Drawer
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.BASE_URL
import com.example.matule.domain.view.CartViewModel
import com.example.matule.domain.view.ProfileViewModel
import com.example.matule.ui.theme.accent
import com.example.matule.ui.theme.background
import com.example.matule.ui.theme.block
import com.example.matule.ui.theme.hint
import com.example.matule.ui.theme.red
import com.example.matule.ui.theme.subtextDark
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs

@SuppressLint("AutoboxingStateCreation")
@Composable
fun CartScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    cartViewModel: CartViewModel = viewModel(),
    newProfileViewModel: ProfileViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val cart by cartViewModel.products.collectAsState()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val interactionSource = remember { MutableInteractionSource() }
    val profileInfo by newProfileViewModel.profile.collectAsState()

    var isMenuOpen by remember { mutableStateOf(false) }
    var isCheckOutOpen by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

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
        cartViewModel.getAllProducts(preferencesManager)
        newProfileViewModel.getProfileInfo(preferencesManager)
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
                    .padding(top = 40.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(0.6f)
                        .padding(horizontal = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            if (!isCheckOutOpen) {
                                navController.popBackStack()
                            } else {
                                isCheckOutOpen = false
                            }
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
                        text = "Корзина",
                        fontSize = 20.sp
                    )
                }
                if (cart?.result.isNullOrEmpty()) {
                    var loading by remember { mutableStateOf(true) }
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        LaunchedEffect(Unit) {
                            delay(1000)
                            loading = false
                        }
                        if (loading) {
                            CircularProgressIndicator()
                        } else {
                            Text(
                                text = "Корзина пуста",
                                fontSize = 30.sp
                            )
                        }
                    }
                }
                cart?.result?.let { cartRes ->
                    var cartItems by remember { mutableStateOf(cartRes.toMutableList()) }

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 6.dp)
                        ) {
                            Spacer(Modifier.height(20.dp))
                            if (cartItems.isEmpty()) {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Корзина пуста",
                                        fontSize = 30.sp
                                    )
                                }
                            }

                            if (!isCheckOutOpen) {
                                LazyColumn {
                                    items(cartItems, key = { it.id }) { c ->
                                        var count by remember { mutableIntStateOf(c.count) }
                                        var isCounterOpen by remember { mutableStateOf(false) }
                                        var isDeleteOpen by remember { mutableStateOf(false) }
                                        var totalDx = 0f

                                        val animatedCounterWidth by animateDpAsState(
                                            targetValue = if (isCounterOpen) 50.dp else 0.dp,
                                            animationSpec = tween(durationMillis = 300)
                                        )

                                        val animatedDeleteWidth by animateDpAsState(
                                            targetValue = if (isDeleteOpen) 50.dp else 0.dp,
                                            animationSpec = tween(durationMillis = 300)
                                        )

                                        val animatedMainWidth by animateFloatAsState(
                                            targetValue = if (isDeleteOpen) 0.75f else 0.9f,
                                            animationSpec = tween(durationMillis = 300)
                                        )

                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable(
                                                    interactionSource = interactionSource,
                                                    indication = null
                                                ) { }
                                                .pointerInput(Unit) {
                                                    detectDragGestures(
                                                        onDrag = { _, dragAmount ->
                                                            val (dx, dy) = dragAmount
                                                            if (abs(dx) > abs(dy) * 1.5f) {
                                                                totalDx += dx
                                                            }
                                                        },
                                                        onDragEnd = {
                                                            if (totalDx > 50) {
                                                                if (isDeleteOpen) {
                                                                    isDeleteOpen = false
                                                                } else {
                                                                    isCounterOpen = true
                                                                }
                                                            } else if (totalDx < -50) {
                                                                if (isCounterOpen) {
                                                                    isCounterOpen = false
                                                                } else {
                                                                    isDeleteOpen = true
                                                                }
                                                            }
                                                            totalDx = 0f
                                                        },
                                                        onDragCancel = {
                                                            totalDx = 0f
                                                        }
                                                    )
                                                },
                                            horizontalArrangement = Arrangement.SpaceBetween
                                        ) {
                                            Column(
                                                modifier = Modifier
                                                    .width(animatedCounterWidth)
                                                    .height(100.dp)
                                                    .clip(shape = RoundedCornerShape(15.dp))
                                                    .background(accent),
                                                horizontalAlignment = Alignment.CenterHorizontally
                                            ) {
                                                Column(
                                                    modifier = Modifier
                                                        .padding(vertical = 16.dp)
                                                        .fillMaxSize(),
                                                    horizontalAlignment = Alignment.CenterHorizontally,
                                                    verticalArrangement = Arrangement.SpaceBetween
                                                ) {
                                                    Icon(
                                                        painter = painterResource(R.drawable.plus),
                                                        contentDescription = "+",
                                                        tint = Color.White,
                                                        modifier = Modifier.clickable(
                                                            interactionSource = interactionSource,
                                                            indication = null
                                                        ) {
                                                            scope.launch {
                                                                val err =
                                                                    cartViewModel.updateInCartCount(
                                                                        preferencesManager,
                                                                        c.id,
                                                                        1,
                                                                        "plus"
                                                                    )
                                                                if (err.error == null) {
                                                                    cartItems =
                                                                        cartItems.map { item ->
                                                                            if (item.id == c.id) {
                                                                                item.copy(count = item.count + 1)
                                                                            } else {
                                                                                item
                                                                            }
                                                                        }.toMutableList()
                                                                    Log.d(
                                                                        "CART",
                                                                        cartItems.toString()
                                                                    )
                                                                    count++
                                                                }
                                                            }
                                                        }
                                                    )
                                                    Text(
                                                        text = count.toString(),
                                                        fontSize = 24.sp,
                                                        color = Color.White
                                                    )
                                                    Icon(
                                                        painter = painterResource(R.drawable.minus),
                                                        contentDescription = "+",
                                                        tint = Color.White,
                                                        modifier = Modifier.clickable(
                                                            interactionSource = interactionSource,
                                                            indication = null
                                                        ) {
                                                            scope.launch {
                                                                if (count > 1) {
                                                                    val err =
                                                                        cartViewModel.updateInCartCount(
                                                                            preferencesManager,
                                                                            c.id,
                                                                            1,
                                                                            "minus"
                                                                        )
                                                                    if (err.error == null) {
                                                                        cartItems =
                                                                            cartItems.map { item ->
                                                                                if (item.id == c.id) {
                                                                                    item.copy(count = item.count - 1)
                                                                                } else {
                                                                                    item
                                                                                }
                                                                            }.toMutableList()
                                                                        Log.d(
                                                                            "CART",
                                                                            cartItems.toString()
                                                                        )
                                                                        count--
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth(animatedMainWidth)
                                                    .height(100.dp)
                                                    .clip(shape = RoundedCornerShape(15.dp))
                                                    .background(block),
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Row(
                                                    modifier = Modifier.padding(horizontal = 10.dp)
                                                ) {
                                                    AsyncImage(
                                                        model = "$BASE_URL/${c.image}",
                                                        contentDescription = null,
                                                        modifier = Modifier
                                                            .size(80.dp)
                                                            .clip(shape = RoundedCornerShape(15.dp))
                                                            .background(background)
                                                    )
                                                    Spacer(Modifier.width(12.dp))
                                                    Column {
                                                        Text(
                                                            text = c.name,
                                                            fontSize = 26.sp
                                                        )
                                                        Text(
                                                            text = "₽${c.price}",
                                                            fontSize = 22.sp
                                                        )
                                                    }
                                                }
                                            }
                                            Column(
                                                modifier = Modifier
                                                    .width(animatedDeleteWidth)
                                                    .height(100.dp)
                                                    .clip(shape = RoundedCornerShape(15.dp))
                                                    .background(red)
                                                    .clickable(
                                                        interactionSource = interactionSource,
                                                        indication = null
                                                    ) {
                                                        scope.launch {
                                                            val err = cartViewModel.deleteFromCart(
                                                                preferencesManager,
                                                                c.id
                                                            )
                                                            if (err.error == null) {
                                                                cartItems =
                                                                    cartItems.filter { it.id != c.id }
                                                                        .toMutableList()
                                                            }
                                                        }
                                                    },
                                                horizontalAlignment = Alignment.CenterHorizontally,
                                                verticalArrangement = Arrangement.Center
                                            ) {
                                                Icon(
                                                    painter = painterResource(R.drawable.trash),
                                                    contentDescription = "Удалить",
                                                    tint = Color.White
                                                )
                                            }
                                        }
                                    }
                                }
                            } else {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 14.dp)
                                        .clip(shape = RoundedCornerShape(14.dp))
                                        .background(block)
                                ) {
                                    Column(modifier = Modifier.padding(14.dp)) {
                                        Text(
                                            text = "Контактная информация",
                                            fontSize = 20.sp
                                        )
                                        Spacer(Modifier.height(16.dp))
                                        profileInfo?.result?.let { prof ->
                                            Row (
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = "Ваш баланс:",
                                                    fontSize = 18.sp
                                                )
                                                Spacer(Modifier.width(10.dp))
                                                Text(
                                                    text = "₽${prof.balance}"
                                                )
                                            }
                                            Spacer(Modifier.height(16.dp))
                                            Row {
                                                Box(
                                                    modifier = Modifier
                                                        .size(50.dp)
                                                        .clip(shape = RoundedCornerShape(15.dp))
                                                        .background(color = background),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Icon(
                                                        modifier = Modifier
                                                            .size(30.dp),
                                                        painter = painterResource(R.drawable.contact_email),
                                                        contentDescription = null,
                                                        tint = hint
                                                    )
                                                }
                                                Spacer(Modifier.width(20.dp))
                                                Column {
                                                    Text(
                                                        text = prof.email,
                                                        fontSize = 20.sp
                                                    )
                                                    Text(
                                                        text = "Email",
                                                        fontSize = 16.sp,
                                                        color = subtextDark
                                                    )
                                                }
                                            }
                                            Spacer(Modifier.width(20.dp))
                                            Column {
                                                Text(
                                                    text = "Адрес",
                                                    fontSize = 18.sp
                                                )
                                                Text(
                                                    text = prof.address.toString(),
                                                    fontSize = 16.sp,
                                                    color = subtextDark
                                                )
                                            }
                                        }
                                        Text(
                                            text = error,
                                            fontSize = 20.sp,
                                            color = red
                                        )
                                        if (error == "Отсутствует адрес"){
                                            Button(onClick = {
                                                navController.navigate("ProfileScreen")
                                            }) {
                                                Text("Перейти в профиль")
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(block)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 30.dp, horizontal = 14.dp)
                            ) {
                                val sum by remember(cartItems) {
                                    derivedStateOf { cartItems.sumOf { it.price.toDouble() * it.count } }
                                }
                                val delivery by remember(cartItems) {
                                    derivedStateOf { cartItems.sumOf { it.price.toDouble() * 0.1 } }
                                }
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Сумма",
                                        fontSize = 24.sp,
                                        color = subtextDark
                                    )
                                    Text(
                                        text = "₽$sum",
                                        fontSize = 24.sp
                                    )
                                }
                                Spacer(Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Доставка",
                                        fontSize = 24.sp,
                                        color = subtextDark
                                    )
                                    Text(
                                        text = "₽$delivery",
                                        fontSize = 24.sp
                                    )
                                }
                                Spacer(Modifier.height(10.dp))
                                Canvas(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                ) {
                                    val strokeWidth = 2.dp.toPx()
                                    drawLine(
                                        color = subtextDark,
                                        start = Offset(0f, size.height / 2),
                                        end = Offset(size.width, size.height / 2),
                                        strokeWidth = strokeWidth,
                                        pathEffect = PathEffect.dashPathEffect(
                                            floatArrayOf(
                                                30f,
                                                30f
                                            ), 0f
                                        )
                                    )
                                }
                                Spacer(Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Итого",
                                        fontSize = 24.sp
                                    )
                                    Text(
                                        text = "₽${sum + delivery}",
                                        fontSize = 24.sp,
                                        color = accent
                                    )
                                }
                                Spacer(Modifier.height(20.dp))
                                Button(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = ButtonColors(
                                        containerColor = accent,
                                        contentColor = Color.Unspecified,
                                        disabledContainerColor = accent,
                                        disabledContentColor = Color.Unspecified
                                    ),
                                    shape = RoundedCornerShape(14.dp),
                                    onClick = {
                                        if (!isCheckOutOpen){
                                            isCheckOutOpen = true
                                        } else {
                                            if (profileInfo?.result?.address == null){
                                                error = "Отсутствует адрес"
                                            }
                                        }
                                    }
                                ) {
                                    Text(
                                        text = if (!isCheckOutOpen) "Оформить заказ" else "Подтвердить",
                                        fontSize = 24.sp,
                                        color = Color.White
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