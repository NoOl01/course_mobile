package com.example.matule.presenter

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.matule.R
import com.example.matule.common.Drawer
import com.example.matule.common.components.CustomTextField
import com.example.matule.data.PreferencesManager
import com.example.matule.domain.BASE_URL
import com.example.matule.domain.view.ProfileViewModel
import com.example.matule.ui.theme.background
import com.example.matule.ui.theme.block
import com.example.matule.ui.theme.text
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.random.Random

@SuppressLint("UseOfNonLambdaOffsetOverload")
@Composable
fun ProfileScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val preferencesManager = remember { PreferencesManager(context) }
    val interactionSource = remember { MutableInteractionSource() }
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

    var firstName by remember { mutableStateOf("${profileInfo?.result?.first_name}") }
    var lastName by remember { mutableStateOf("${profileInfo?.result?.last_name}") }
    var email by remember { mutableStateOf("${profileInfo?.result?.email}") }
    var address by remember { mutableStateOf("${profileInfo?.result?.address}") }
    var edit by remember { mutableStateOf(false) }
    var isChanged by remember { mutableStateOf(false) }
    var avatarReloadKey by remember { mutableStateOf("") }

    val editIcon: Painter =
        if (edit) painterResource(R.drawable.save) else painterResource(R.drawable.edit)

    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
        uri?.let {
            scope.launch {
                profileViewModel.updateProfileAvatar(context, preferencesManager, it)
                profileViewModel.getProfileInfo(preferencesManager)
                avatarReloadKey = Random.nextInt().toString()
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Log.d("ProfileDebug", "profile avatar url: ${profileInfo?.result?.address}")
        Drawer(navController, scope, preferencesManager, profileViewModel, avatarReloadKey) { }

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
                    .background(block)
                    .padding(horizontal = 6.dp)
                    .padding(top = 40.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = background
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.back_arrow),
                            contentDescription = "назад"
                        )
                    }
                    Text(
                        text = "Профиль",
                        fontSize = 20.sp
                    )
                    IconButton(
                        onClick = {
                            scope.launch {
                                if (edit && isChanged) {
                                    profileViewModel.updateProfile(
                                        preferencesManager,
                                        firstName,
                                        lastName,
                                        address,
                                        email
                                    )
                                }
                                edit = !edit
                            }
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = background
                        )
                    ) {
                        Icon(
                            painter = editIcon,
                            contentDescription = "Редактировать профиль",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        profileInfo?.result?.let { profile ->
                            IconButton(
                                modifier = Modifier.size(70.dp),
                                onClick = {
                                    imagePicker.launch("image/*")
                                }
                            ) {
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
                                            .size(70.dp),
                                    )
                                }
                            }
                            if (!edit) {
                                Text(
                                    text = "$firstName $lastName",
                                    fontSize = 24.sp
                                )
                                Spacer(Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "Ваш баланс: ${profile.balance}₽",
                                        fontSize = 20.sp
                                    )
                                }
                                Spacer(Modifier.height(10.dp))
                            }
                        }
                    }
                    if (edit) {
                        Column {
                            Text(
                                text = "Имя",
                                fontSize = 30.sp,
                                color = text
                            )
                            Spacer(Modifier.height(10.dp))
                            CustomTextField(
                                value = firstName,
                                onValueChange = { firstName = it; isChanged = true },
                                placeholder = "Ваше имя",
                                readOnly = !edit
                            )
                        }
                        Spacer(Modifier.height(20.dp))
                        Column {
                            Text(
                                text = "Фамилия",
                                fontSize = 30.sp,
                                color = text
                            )
                            Spacer(Modifier.height(10.dp))
                            CustomTextField(
                                value = lastName,
                                onValueChange = { lastName = it; isChanged = true },
                                "Ваша фамилия",
                                readOnly = !edit
                            )
                        }
                        Spacer(Modifier.height(20.dp))
                    }
                    Column {
                        Text(
                            text = "Email",
                            fontSize = 30.sp,
                            color = text
                        )
                        Spacer(Modifier.height(10.dp))
                        CustomTextField(
                            value = email,
                            onValueChange = { email = it; isChanged = true },
                            "Ваш email",
                            readOnly = !edit
                        )
                    }
                    Spacer(Modifier.height(20.dp))
                    Column {
                        Text(
                            text = "Адрес",
                            fontSize = 30.sp,
                            color = text
                        )
                        Spacer(Modifier.height(10.dp))
                        CustomTextField(
                            value = address,
                            onValueChange = { address = it; isChanged = true },
                            "Ваш адрес",
                            readOnly = !edit
                        )
                    }
                }
            }
        }
    }
}