package com.example.matule.presenter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
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
import androidx.compose.ui.graphics.asImageBitmap
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
import com.example.matule.ui.theme.hint
import com.example.matule.ui.theme.subtextDark
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter
import kotlin.math.abs

@Composable
fun OrderInfoScreen(
    navController: NavController,
    orderId: Long,
    profileViewModel: ProfileViewModel,
    orderViewModel: OrderViewModel = viewModel()
){
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val interactionSource = remember { MutableInteractionSource() }
    val order by orderViewModel.order.collectAsState()
    val profileInfo by profileViewModel.profile.collectAsState()

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
        orderViewModel.getOrderInfo(preferencesManager, orderId)
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
                        .fillMaxWidth()
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
                        text = orderId.toString(),
                        fontSize = 20.sp
                    )
                    Spacer(Modifier.width(20.dp))
                }
                order?.result?.let { ord ->
                    Spacer(Modifier.height(10.dp))
                    Column (
                        modifier = Modifier.padding(horizontal = 12.dp)
                    ) {
                        OrderCard(
                            ord,
                            onClick = {}
                        )
                    }
                    profileInfo?.result?.let { prof ->
                        Spacer(Modifier.height(10.dp))
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 14.dp)
                                .clip(shape = RoundedCornerShape(14.dp))
                                .background(block)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row (
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Статус заказа: ",
                                        fontSize = 20.sp
                                    )
                                    Text(
                                        text = ord.status,
                                        fontSize = 20.sp
                                    )
                                }
                                Spacer(Modifier.height(16.dp))
                                Text(
                                    text = "Контактная информация",
                                    fontSize = 20.sp
                                )
                                Spacer(Modifier.height(16.dp))
                                profileInfo?.result?.let { prof ->
                                    Row(
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
                                            text = "Адрес: ",
                                            fontSize = 18.sp
                                        )
                                        Text(
                                            modifier = Modifier
                                                .fillMaxWidth(),
                                            text = prof.address.toString(),
                                            fontSize = 18.sp,
                                            color = subtextDark
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(Modifier.height(30.dp))
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            modifier = Modifier
                                .clip(shape = RoundedCornerShape(15.dp))
                                .size(250.dp),
                            bitmap = generateQrCode(text = "ID: ${ord.id}, Count: ${ord.count}").asImageBitmap(),
                            contentDescription = "QR Code"
                        )
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

@SuppressLint("UseKtx")
fun generateQrCode(text: String, size: Int = 512): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size)

    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.RGB_565)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bitmap.setPixel(x, y, if (bitMatrix[x, y]) android.graphics.Color.BLACK else android.graphics.Color.WHITE)
        }
    }
    return bitmap
}