package com.example.matule.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.matule.domain.BASE_URL
import com.example.matule.ui.theme.hint
import kotlin.math.PI
import kotlin.math.absoluteValue
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin

@Composable
fun PagerWithSemiOvalSlider(
    pagerState: PagerState,
    pageCount: Int,
    images: List<String>,
    modifier: Modifier = Modifier,
    onClickImage: () -> Unit
) {
    var angle by remember { mutableFloatStateOf(0f) }
    val interactionSource = remember { MutableInteractionSource() }

    LaunchedEffect(angle) {
        val targetPage = ((angle / 180f) * (pageCount - 1)).roundToInt().coerceIn(0, pageCount - 1)
        if (pagerState.currentPage != targetPage) {
            pagerState.animateScrollToPage(targetPage)
        }
    }

    LaunchedEffect(pagerState.currentPage) {
        angle = (pagerState.currentPage.toFloat() / (pageCount - 1)) * 180f
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.35f),
        contentAlignment = Alignment.Center
    ) {
        HorizontalPager(
            state = pagerState,
            contentPadding = PaddingValues(horizontal = 20.dp),
            modifier = Modifier
                .fillMaxWidth()
        ) { page ->
            val pageOffset =
                ((pagerState.currentPage - page) + pagerState.currentPageOffsetFraction).absoluteValue
            val scale = 1f - pageOffset * 0.9f

            Box(
                modifier = Modifier
                    .graphicsLayer {
                        scaleX = scale
                        scaleY = scale
                        translationY = pageOffset * 30.dp.toPx()
                    }
                    .clickable(
                        interactionSource = interactionSource,
                        indication = null
                    ) { onClickImage() }
            ) {
                AsyncImage(
                    model = "$BASE_URL/${images[page]}",
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1.5f)
                )
            }
        }
        SemiOvalSlider(
            value = angle,
            onValueChange = { angle = it },
            trackColor = Color(0xFF2363F6),
            thumbColor = hint,
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = 110.dp),
            pageCount = pageCount
        )
    }
}


@Composable
fun SemiOvalSlider(
    modifier: Modifier = Modifier,
    value: Float,
    onValueChange: (Float) -> Unit,
    trackColor: Color,
    thumbColor: Color,
    pageCount: Int
) {
    Canvas(
        modifier = modifier
            .size(100.dp)
            .padding(16.dp)
            .pointerInput(Unit) {
                detectDragGestures { change, _ ->
                    val position = change.position
                    val center = Offset(size.width / 2f, size.height / 2f)
                    val dx = position.x - center.x
                    val dy = position.y - center.y
                    val theta = (atan2(dy, dx) * 180 / PI).toFloat()

                    val steps = pageCount - 1
                    val stepSize = 180f / steps

                    val rawAngle = (theta + 360f) % 360f

                    if (rawAngle <= 180f) {
                        val steppedAngle = 180f - ((rawAngle / stepSize).roundToInt() * stepSize)
                        onValueChange(steppedAngle.coerceIn(0f, 180f))
                    }
                }
            }
    ) {
        val widthScale = 1.0f
        val heightScale = 0.9f

        val width = size.width * widthScale
        val height = size.height * heightScale

        val left = (size.width - width) / 2f
        val top = (size.height - height) / 2f
        val arcRect = Rect(Offset(left, top), Size(width, height))

        drawArc(
            color = trackColor,
            startAngle = -10f,
            sweepAngle = 200f,
            useCenter = false,
            style = Stroke(width = 10f),
            topLeft = arcRect.topLeft,
            size = arcRect.size
        )

        val thumbAngleRad = Math.toRadians(180 - value.toDouble())

        val rx = arcRect.width / 2f
        val ry = arcRect.height / 2f
        val cx = arcRect.left + rx
        val cy = arcRect.top + ry

        val thumbX = cx + rx * cos(thumbAngleRad).toFloat()
        val thumbY = cy + ry * sin(thumbAngleRad).toFloat()

        val thumbWidth = 100f
        val thumbHeight = 50f
        val cornerRadius = 25f

        drawRoundRect(
            color = thumbColor,
            topLeft = Offset(thumbX - thumbWidth / 2f, thumbY - thumbHeight / 2f),
            size = Size(thumbWidth, thumbHeight),
            cornerRadius = CornerRadius(cornerRadius, cornerRadius)
        )

        val arrowSize = 10f
        val arrowStroke = Stroke(width = 3f, cap = StrokeCap.Round)

        val leftArrowX = thumbX - 25f

        drawLine(
            color = Color.White,
            start = Offset(leftArrowX + arrowSize, thumbY - arrowSize),
            end = Offset(leftArrowX, thumbY),
            strokeWidth = arrowStroke.width,
            cap = arrowStroke.cap
        )

        drawLine(
            color = Color.White,
            start = Offset(leftArrowX + arrowSize, thumbY + arrowSize),
            end = Offset(leftArrowX, thumbY),
            strokeWidth = arrowStroke.width,
            cap = arrowStroke.cap
        )

        val rightArrowX = thumbX + 25f

        drawLine(
            color = Color.White,
            start = Offset(rightArrowX - arrowSize, thumbY - arrowSize),
            end = Offset(rightArrowX, thumbY),
            strokeWidth = arrowStroke.width,
            cap = arrowStroke.cap
        )

        drawLine(
            color = Color.White,
            start = Offset(rightArrowX - arrowSize, thumbY + arrowSize),
            end = Offset(rightArrowX, thumbY),
            strokeWidth = arrowStroke.width,
            cap = arrowStroke.cap
        )
    }
}