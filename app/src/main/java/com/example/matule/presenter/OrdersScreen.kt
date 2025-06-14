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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.matule.common.components.OrderCard
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.view.OrderViewModel
import com.example.matule.domain.view.ProfileViewModel
import com.example.matule.ui.theme.background
import com.example.matule.ui.theme.block
import kotlin.math.abs

@Composable
fun OrdersScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel,
    orderViewModel: OrderViewModel = viewModel()
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val interactionSource = remember { MutableInteractionSource() }
    val orders by orderViewModel.allOrders.collectAsState()

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
        orderViewModel.getAllOrders(preferencesManager)
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
                        text = "Заказы",
                        fontSize = 20.sp
                    )
                }
                orders?.result?.let { orders ->
                    LazyColumn (
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(orders) { order ->
                            OrderCard(
                                order = order,
                                onClick = {
                                    navController.navigate("OrderInfoScreen/${order.id}")
                                }
                            )
                        }
                    }
                }
            }
            val navBackStackEntry: NavBackStackEntry? = navController.currentBackStackEntryAsState().value
            val currentRoute = navBackStackEntry?.destination?.route

            Box (
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter
            ) {
                BottomBar(navController, currentRoute)
            }
        }
    }
}