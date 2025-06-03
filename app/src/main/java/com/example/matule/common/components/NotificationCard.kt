package com.example.matule.common.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

fun formatDate(dateStr: String): String {
    val zonedDateTime = ZonedDateTime.parse(dateStr)
    val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
    return zonedDateTime.format(formatter)
}

@Composable
fun NotificationCard(
    title: String,
    description: String,
    dateTime: String
){
    val formattedDateTime = formatDate(dateTime)

    Card {
        Column (
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            Text(
                text = title,
                fontSize = 30.sp,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = description,
                fontSize = 20.sp,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = formattedDateTime
            )
        }
    }
}