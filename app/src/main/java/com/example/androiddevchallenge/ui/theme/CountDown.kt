/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.ui.theme

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.androiddevchallenge.model.Time
import com.example.androiddevchallenge.viewmodel.TimerViewModel

@Composable
fun CrossfitTimer(timerViewModel: TimerViewModel = viewModel()) {

    val time: Time by timerViewModel.timeLeft.observeAsState(Time())
    val action by timerViewModel.action.observeAsState("work")
    val startButton by timerViewModel.startButton.observeAsState("Start")
    val pauseButton by timerViewModel.pauseButton.observeAsState("Pause")

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .fillMaxHeight()
            .wrapContentSize(Alignment.Center)
    ) {
        Text(
            text = "CROSSFIT",
            modifier = Modifier.fillMaxWidth(),
            fontSize = 48.sp,
            textAlign = TextAlign.Center
        )
        CountDown(
            time.minutes, time.seconds,
            { timerViewModel.onClicked(it) },
            action,
            startButton,
            pauseButton
        )
    }
}

@Composable
fun CountDown(
    min: Int,
    sec: Int,
    onClicked: (String) -> Unit,
    action: String,
    startButton: String,
    pauseButton: String
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
    ) {
        Box(
            modifier = Modifier
                .border(width = 4.dp, color = Gray, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "${min.toString().padStart(2, '0')}",
                Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                style = typography.h4,
            )
        }

        Text(
            ":",
            Modifier.padding(16.dp),
            textAlign = TextAlign.Center,
            style = typography.h4,
        )

        Box(
            modifier = Modifier
                .border(width = 4.dp, color = Gray, shape = RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "${sec.toString().padStart(2, '0')}",
                Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                style = typography.h4,
            )
        }
    }
    Crossfade(targetState = action) { action ->
        when (action) {
            "work" -> {
                Text(
                    text = "WORK",
                    fontSize = 32.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
            "rest" -> {
                Text(
                    text = "REST",
                    fontSize = 32.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .height(200.dp)
    ) {
        if (action == "work") {
            Button(
                onClick = { onClicked("work") },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .width(90.dp)
            ) {
                Text(text = "$startButton")
            }
            Button(
                onClick = { onClicked(if (pauseButton == "Pause") "pause" else "go") },
                modifier = Modifier
                    .padding(16.dp)
                    .width(90.dp)
            ) {
                Text(text = "$pauseButton")
            }
        }
        Button(
            onClick = { onClicked("done") },
            modifier = Modifier
                .padding(top = 16.dp)
                .width(90.dp)
        ) {
            Text(text = "Done")
        }
    }
}
