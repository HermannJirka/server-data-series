package com.example.demo.controller

import com.example.demo.model.AverageResponse
import com.example.demo.model.MovingAverageResponse
import com.example.demo.service.StatisticService
import com.example.demo.service.StatisticService.DataType
import com.example.demo.service.StatisticService.DataType.DEVICE
import com.example.demo.service.StatisticService.DataType.USER
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/statistics")
class StatisticController(val statisticsService: StatisticService) {

    @GetMapping("/devices/{device}/avg")
    fun getDevicesAvgTime(@PathVariable("device") device: String): List<AverageResponse> {
       return statisticsService.deviceAverageTime(device)
    }

    @GetMapping("/statistics/devices/{device}/moving_avg?window_size={window_size}")
    fun getDevicesMovingAverageTime(@PathVariable("device") device: String, @RequestParam("window_size") windowSize: Int): ArrayList<MovingAverageResponse> {
        return statisticsService.deviceMovingAverageTime(DEVICE,device,windowSize)
    }

    @GetMapping("/statistics/users/{user}/avg")
    fun getUserAverageTime(@PathVariable("user") user: String): List<AverageResponse> {
        return statisticsService.userAverageTime(user)
    }

    @GetMapping("/statistics/users/{user}/moving_avg?window_size={window_size}")
    fun getUserMovingAverageTime(@PathVariable("user") user: String, @RequestParam("window_size") windowSize: Int): ArrayList<MovingAverageResponse> {
        return statisticsService.userMovingAverageTime(USER,user,windowSize)
    }
}