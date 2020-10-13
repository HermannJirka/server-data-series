package com.example.demo.controller

import com.example.demo.model.AverageResponse
import com.example.demo.model.DataType.DEVICE
import com.example.demo.model.DataType.USER
import com.example.demo.model.MovingAverageResponse
import com.example.demo.service.StatisticService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/statistics")
class StatisticController(private val statisticsService: StatisticService) {

    @GetMapping("/devices/{device}/avg")
    fun getDevicesAvgTime(@PathVariable("device") device: String): List<AverageResponse> = statisticsService.deviceAverageTime(device)


    @GetMapping("/devices/{device}/moving_avg")
    fun getDevicesMovingAverageTime(@PathVariable device: String, @RequestParam("window_size") windowSize: Int): List<MovingAverageResponse> = statisticsService.deviceMovingAverageTime(DEVICE, device, windowSize)


    @GetMapping("/users/{user}/avg")
    fun getUserAverageTime(@PathVariable user: String): List<AverageResponse> = statisticsService.userAverageTime(user)


    @GetMapping("/users/{user}/moving_avg")
    fun getUserMovingAverageTime(@PathVariable("user") user: String, @RequestParam("window_size") windowSize: Int): List<MovingAverageResponse> = statisticsService.userMovingAverageTime(USER, user, windowSize)
}