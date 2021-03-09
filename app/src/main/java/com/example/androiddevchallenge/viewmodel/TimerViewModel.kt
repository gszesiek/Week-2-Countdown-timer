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
package com.example.androiddevchallenge.viewmodel

import android.os.CountDownTimer
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.androiddevchallenge.model.Time

class TimerViewModel : ViewModel() {

    var timer: CountDownTimer? = null
    private val _timeLeft = MutableLiveData<Time>(Time(0, 0))
    val timeLeft: LiveData<Time> = _timeLeft

    private val _action = MutableLiveData<String>("work")
    val action: LiveData<String> = _action

    private val _startButton = MutableLiveData<String>("Start")
    val startButton: LiveData<String> = _startButton

    private val _pauseButton = MutableLiveData<String>("Pause")
    val pauseButton: LiveData<String> = _pauseButton

    fun onClicked(button: String) {
        when (button) {
            "work" -> {
                onStart(Time(1, 10))
                _action.value = "work"
            }
            "done" -> {
                println(action)
                if (action.value == "work") {
                    onRest(Time(0, 30))
                } else {
                    onStart(Time(1, 30))
                }
            }
            "pause" -> {
                onPause()
            }
            "go" -> {
                onStart(timeLeft.value!!)
            }
        }
    }

    fun fromMilliseconds(milliseconds: Long): Time {
        val minutes = ((milliseconds / 60000) % 60).toInt()
        val seconds = ((milliseconds / 1000) % 60).toInt()
        return Time(minutes, seconds)
    }

    fun timeInMillisec(timeLeft: Time) = timeLeft.minutes * 60 * 1000L + timeLeft.seconds * 1000L

    fun onStart(setTime: Time) {
        timer?.cancel()
        _timeLeft.value = setTime
        _action.value = "work"
        _startButton.value = "Reset"
        _pauseButton.value = "Pause"

        timer = object : CountDownTimer(timeInMillisec(timeLeft.value!!), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = fromMilliseconds(millisUntilFinished)
            }

            override fun onFinish() {
                onRest(Time(0, 30))
            }
        }
        timer?.start()
    }

    fun onRest(setTime: Time) {
        timer?.cancel()
        _timeLeft.value = setTime
        _action.value = "rest"

        timer = object : CountDownTimer(timeInMillisec(timeLeft.value!!), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                _timeLeft.value = fromMilliseconds(millisUntilFinished)
            }

            override fun onFinish() {
                onStart(Time(1, 30))
            }
        }
        timer?.start()
    }

    fun onPause() {
        timer?.cancel()
        _pauseButton.value = "Go"
    }
}
