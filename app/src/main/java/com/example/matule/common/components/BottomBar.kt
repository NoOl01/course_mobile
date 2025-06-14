package com.example.matule.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.matule.R
import com.example.matule.ui.theme.accent
import com.example.matule.ui.theme.background
import com.example.matule.ui.theme.block
import com.example.matule.ui.theme.subtextDark

@Composable
fun BottomBar(navController: NavController, currentRoute: String?) {
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(block)
            .padding(horizontal = 16.dp)
            .padding(bottom = 30.dp, top = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            painter = painterResource(R.drawable.menu_home),
            contentDescription = "Home",
            tint = if (currentRoute == "MainScreen") accent else subtextDark,
            modifier = Modifier
                .size(26.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                navController.navigate("MainScreen")
            }
        )
        Icon(
            painter = painterResource(R.drawable.menu_heart),
            contentDescription = "Home",
            tint = if (currentRoute == "FavouriteScreen") accent else subtextDark,
            modifier = Modifier
                .size(26.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                navController.navigate("FavouriteScreen")
            }
        )
        Icon(
            painter = painterResource(R.drawable.cart),
            contentDescription = "Home",
            tint = if (currentRoute == "CartScreen") accent else subtextDark,
            modifier = Modifier
                .size(26.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                navController.navigate("CartScreen")
            }
        )
        Icon(
            painter = painterResource(R.drawable.menu_notification),
            contentDescription = "Home",
            tint = if (currentRoute == "NotificationScreen") accent else subtextDark,
            modifier = Modifier
                .size(26.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                navController.navigate("NotificationScreen")
            }
        )
        Icon(
            painter = painterResource(R.drawable.menu_profile),
            contentDescription = "Home",
            tint = if (currentRoute == "ProfileScreen") accent else subtextDark,
            modifier = Modifier
                .size(26.dp)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null
                ) {
                navController.navigate("ProfileScreen")
            }
        )
    }
}