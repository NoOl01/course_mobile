package com.example.matule.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.matule.R
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.BASE_URL
import com.example.matule.domain.view.ProfileViewModel
import com.example.matule.ui.theme.accent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun Drawer(
    navController: NavController,
    scope: CoroutineScope,
    preferencesManager: PreferencesManager,
    profileViewModel: ProfileViewModel,
    avatarReloadKey: String = "",
    onClose: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val profileInfo by profileViewModel.profile.collectAsState()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .background(accent)
            .padding(16.dp, top = 60.dp),
        horizontalAlignment = Alignment.Start
    ) {
        profileInfo?.result?.let { profile ->
            if (profile.avatar == "") {
                Image(
                    painter = painterResource(R.drawable.user_avatar),
                    contentDescription = "Фото профиля по умолчанию",
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(50))
                        .size(70.dp)
                )
            } else {
                AsyncImage(
                    model = BASE_URL + profile.avatar + "?avatarReloadKey=$avatarReloadKey",
                    contentDescription = "Фото профиля",
                    modifier = Modifier
                        .clip(shape = RoundedCornerShape(50))
                        .size(70.dp)
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = if (profileInfo?.result != null) profileInfo!!.result!!.first_name else "Загрузка...",
            fontSize = 30.sp
        )
        Spacer(Modifier.height(20.dp))

        DrawerItem("Профиль", onClick = {navController.navigate("ProfileScreen")}, R.drawable.profile_drawe, interactionSource)
        DrawerItem("Корзина", onClick = {navController.navigate("CartScreen")}, R.drawable.bag_drawe, interactionSource)
        DrawerItem("Избранное", onClick = {navController.navigate("FavouriteScreen")}, R.drawable.favorite_drawe, interactionSource)
        DrawerItem("Заказы", onClick = {navController.navigate("OrdersScreen")}, R.drawable.orders_drawe, interactionSource)
        DrawerItem("Уведомления", onClick = {navController.navigate("NotificationScreen")}, R.drawable.notification_drawe_r, interactionSource)
        DrawerItem("Настройки", onClick = {navController.navigate("SettingsScreen")}, R.drawable.settings_drawe, interactionSource)

        HorizontalDivider(
            color = Color(0x3BF7F7F9),
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        Row (verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                    scope.launch {
                        preferencesManager.deleteAuthData()
                        onClose()
                        navController.navigate("LoginScreen") {
                            popUpTo(navController.graph.startDestinationId) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    }
                }) {
            Icon(
                painter = painterResource(id = R.drawable.exit_drawe),
                contentDescription = "Выйти",
                tint = Color.White
            )
            Text(
                "Выйти",
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier
                    .padding(14.dp)
            )
        }
    }
}

@Composable
fun DrawerItem(text: String, onClick: () -> Unit, image: Int, interactionSource: MutableInteractionSource) {
    Row (verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ) { onClick() }) {
        Icon(
            painter = painterResource(id = image),
            contentDescription = text,
            tint = Color.White
        )
        Text(
            text,
            fontSize = 18.sp,
            color = Color.White,
            modifier = Modifier
                .padding(14.dp)
        )
    }
}