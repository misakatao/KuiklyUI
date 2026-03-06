/*
 * Tencent is pleased to support the open source community by making KuiklyUI
 * available.
 * Copyright (C) 2025 Tencent. All rights reserved.
 * Licensed under the License of KuiklyUI;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * https://github.com/Tencent-TDS/KuiklyUI/blob/main/LICENSE
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.tencent.kuikly.demo.pages.compose

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.tencent.kuikly.compose.ComposeContainer
import com.tencent.kuikly.compose.foundation.background
import com.tencent.kuikly.compose.foundation.border
import com.tencent.kuikly.compose.foundation.clickable
import com.tencent.kuikly.compose.foundation.interaction.MutableInteractionSource
import com.tencent.kuikly.compose.foundation.interaction.collectIsDraggedAsState
import com.tencent.kuikly.compose.foundation.layout.Arrangement
import com.tencent.kuikly.compose.foundation.layout.Box
import com.tencent.kuikly.compose.foundation.layout.Column
import com.tencent.kuikly.compose.foundation.layout.Row
import com.tencent.kuikly.compose.foundation.layout.Spacer
import com.tencent.kuikly.compose.foundation.layout.fillMaxSize
import com.tencent.kuikly.compose.foundation.layout.fillMaxWidth
import com.tencent.kuikly.compose.foundation.layout.height
import com.tencent.kuikly.compose.foundation.layout.padding
import com.tencent.kuikly.compose.foundation.layout.size
import com.tencent.kuikly.compose.foundation.lazy.LazyColumn
import com.tencent.kuikly.compose.foundation.lazy.rememberLazyListState
import com.tencent.kuikly.compose.material3.Text
import com.tencent.kuikly.compose.material3.pullToRefreshItem
import com.tencent.kuikly.compose.material3.rememberPullToRefreshState
import com.tencent.kuikly.compose.setContent
import com.tencent.kuikly.compose.ui.Alignment
import com.tencent.kuikly.compose.ui.Modifier
import com.tencent.kuikly.compose.ui.graphics.Color
import com.tencent.kuikly.compose.ui.text.style.TextAlign
import com.tencent.kuikly.compose.ui.unit.Dp
import com.tencent.kuikly.compose.ui.unit.dp
import com.tencent.kuikly.compose.ui.unit.sp
import com.tencent.kuikly.core.annotations.Page
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Page("LazyColumnDemo4")
class LazyColumnDemo4 : ComposeContainer() {
    override fun willInit() {
        super.willInit()
        setContent {
            ComposeNavigationBar {
                LazyColumnTest4()
            }
        }
    }

    @Composable
    fun LazyColumnTest4() {
        val state = rememberLazyListState()
        val scope = rememberCoroutineScope()
        val interactionSource = remember { MutableInteractionSource() }
        val isDragged by interactionSource.collectIsDraggedAsState()
        var isRefreshing by remember { mutableStateOf(false) }
        val pullToRefreshState = rememberPullToRefreshState(isRefreshing)

        Column(modifier = Modifier.fillMaxSize()) {

            // 显示拖拽状态
            Text(
                "列表是否正在被拖拽: $isDragged",
                modifier = Modifier.padding(16.dp),
            )

            // 控制按钮组
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                // scrollToItem
                Box(
                    modifier =
                        Modifier
                            .height(40.dp)
                            .background(Color.Blue)
                            .clickable {
                                scope.launch {
                                    state.scrollToItem(state.firstVisibleItemIndex + 1)
                                }
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Text("下一个", color = Color.White)
                }

                // scrollToItem
                Box(
                    modifier =
                        Modifier
                            .height(40.dp)
                            .background(Color.Blue)
                            .clickable {
                                scope.launch {
                                    state.scrollToItem(state.firstVisibleItemIndex - 1)
                                }
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Text("上一个", color = Color.White)
                }

                // animateScrollToItem
                Box(
                    modifier =
                        Modifier
                            .size(width = 120.dp, height = 40.dp)
                            .background(Color.Green)
                            .clickable {
                                scope.launch {
                                    state.animateScrollToItem(15)
                                }
                            },
                    contentAlignment = Alignment.Center,
                ) {
                    Text("动画滚动到15", color = Color.White)
                }
            }

            Spacer(Modifier.height(10.dp))

            // requestScrollToItem
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .height(40.dp)
                        .background(Color.Magenta)
                        .clickable {
                            scope.launch {
                                state.requestScrollToItem(5)
                            }
                        },
                contentAlignment = Alignment.Center,
            ) {
                Text("请求滚动到5", color = Color.White)
            }

            Spacer(Modifier.height(20.dp))

            // LazyColumn with interactionSource
            LazyColumn(
                state = state,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(200.dp)
//                    .weight(1f)
                        .background(Color.LightGray),
                //                contentPadding = PaddingValues(16.dp),
//                verticalArrangement = Arrangement.spacedBy(8.dp),
//                interactionSource = interactionSource
            ) {
                // 使用默认指示器
                pullToRefreshItem(
                    state = pullToRefreshState,
                    onRefresh = {
                        scope.launch {
                            isRefreshing = true
                            delay(2000)
                            isRefreshing = false
                        }
                    },
                    scrollState = state,
                    content = { progress, refreshing, threshold ->
                        CustomRefreshIndicator(progress, refreshing, threshold)
                    }
                )
                items(300) { index ->
                    Box(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(60.dp + index.dp)
                                .background(
                                    when (index) {
                                        5 -> Color.Red
                                        10 -> Color.Green
                                        15 -> Color.Blue
                                        else -> Color.Gray
                                    },
                                ).border(1.dp, color = Color.Black),
                        //                            .padding(16.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            "Item $index",
                            color = Color.White,
                        )
                    }
                }
            }

            // 显示可见项信息
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
            ) {
                Text("可见项信息:", color = Color.Black)
                state.layoutInfo.visibleItemsInfo.forEach { itemInfo ->
                    Text(
                        "Index: ${itemInfo.index}, " +
                                "Offset: ${itemInfo.offset}, " +
                                "Size: ${itemInfo.size}",
                        color = Color.DarkGray,
                        modifier = Modifier.padding(start = 16.dp, top = 4.dp),
                    )
                }
            }
        }
    }
}

@Composable
private fun CustomRefreshIndicator(
    pullProgress: Float,
    isRefreshing: Boolean,
    refreshThreshold: Dp
) {
    // 根据状态计算背景色和效果
    val backgroundColor = when {
        isRefreshing -> Color.Blue.copy(alpha = 0.2f)
        pullProgress >= 1f -> Color.Green.copy(alpha = 0.15f)
        pullProgress > 0.5f -> Color.Cyan.copy(alpha = 0.1f)
        pullProgress > 0f -> Color.Gray.copy(alpha = 0.05f)
        else -> Color.Transparent
    }

    val borderColor = when {
        isRefreshing -> Color.Blue
        pullProgress >= 1f -> Color.Green
        pullProgress > 0.5f -> Color.Cyan
        else -> Color.Gray.copy(alpha = 0.3f)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(refreshThreshold)
            .background(backgroundColor)
            .padding(1.dp)
            .background(Color.White)
            .padding(1.dp)
            .background(borderColor.copy(alpha = 0.1f)),
        contentAlignment = Alignment.Center
    ) {
        if (isRefreshing) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "🌀 Loading...",
                    fontSize = 18.sp,
                    color = Color.Blue,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "请稍候",
                    fontSize = 12.sp,
                    color = Color.Blue.copy(alpha = 0.7f),
                    textAlign = TextAlign.Center
                )
            }
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                val emoji = when {
                    pullProgress >= 1f -> "🚀"
                    pullProgress > 0.5f -> "⬆️"
                    else -> "⬇️"
                }

                val text = when {
                    pullProgress >= 1f -> "松开立即刷新"
                    pullProgress > 0.5f -> "继续下拉"
                    else -> "下拉刷新数据"
                }

                val textColor = when {
                    pullProgress >= 1f -> Color.Green
                    pullProgress > 0.5f -> Color.Cyan
                    else -> Color.Gray
                }

                Text(
                    text = emoji,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = text,
                    fontSize = 14.sp,
                    color = textColor,
                    textAlign = TextAlign.Center
                )

                if (pullProgress > 0f) {
                    Text(
                        text = "${(pullProgress * 100).toInt()}%",
                        fontSize = 10.sp,
                        color = textColor.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}