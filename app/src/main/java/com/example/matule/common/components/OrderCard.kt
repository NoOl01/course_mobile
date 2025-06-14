package com.example.matule.common.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.matule.domain.BASE_URL
import com.example.matule.domain.models.responses.AllOrdersResult
import com.example.matule.ui.theme.background
import com.example.matule.ui.theme.block
import java.time.Duration
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@Composable
fun OrderCard(
    order: AllOrdersResult,
    onClick: () -> Unit
){
    val interactionSource = remember { MutableInteractionSource() }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = interactionSource,
                indication = null
            ){
                onClick()
            },
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .clip(shape = RoundedCornerShape(15.dp))
                .background(block),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    AsyncImage(
                        model = "$BASE_URL/${order.product.images[0].file_path}",
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(shape = RoundedCornerShape(15.dp))
                            .background(background)
                    )
                    Spacer(Modifier.width(12.dp))
                    Column (
                        modifier = Modifier.fillMaxHeight().padding(vertical = 10.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = order.product.name,
                            fontSize = 26.sp
                        )
                        Text(
                            text = "₽${order.price}",
                            fontSize = 22.sp
                        )
                    }
                }
                Column (
                    modifier = Modifier.fillMaxHeight().padding(vertical = 10.dp),
                    verticalArrangement = Arrangement.Bottom
                ) {
                    Text(
                        text = formatOrderTime(order.time)
                    )
                }
            }
        }
    }
}

fun formatOrderTime(input: String): String {
    return try {
        val orderTime = ZonedDateTime.parse(input)
        val now = ZonedDateTime.now()

        val duration = Duration.between(orderTime, now)
        val minutesAgo = duration.toMinutes()

        if (minutesAgo < 60) {
            "$minutesAgo ${getMinuteWordForm(minutesAgo)} назад"
        } else {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
            orderTime.format(formatter)
        }
    } catch (_: Exception) {
        "неизвестная дата"
    }
}

fun getMinuteWordForm(minutes: Long): String {
    val lastDigit = minutes % 10
    val lastTwoDigits = minutes % 100

    return when {
        lastTwoDigits in 11..14 -> "минут"
        lastDigit == 1L -> "минута"
        lastDigit in 2..4 -> "минуты"
        else -> "минут"
    }
}