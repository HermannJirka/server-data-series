package com.example.demo.controller

import com.example.demo.service.StatisticService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/statistics")
class StatisticController(val statisticsService: StatisticService) {

    //    GET /statistics/devices/{device}/avg - return list of 15 minutes averages of time serie from first datapoint to current time. Matching device key
    @GetMapping("/devices/{device}/avg")
    fun getDevicesAvgTime(@PathVariable("device") device: String): MutableList<Double> {
       return statisticsService.deviceAverageTime(device)
    }

    //    GET /statistics/devices/{device}/moving_avg?window_size={window_size} - return list of moving averages of 15 minutes average buckets. I.E. moving averages of result /statistics/devices/{device}/avg
    @GetMapping("/statistics/devices/{device}/moving_avg?window_size={window_size}")
    fun getDevicesMovingAverageTime(@PathVariable("device") device: String, @RequestParam("window_size") windowSize: Int) {

    }

    //    GET /statistics/users/{user}/avg - return list of 15 minutes averages of time serie from first datapoint to current time. Matching user key
    @GetMapping("/statistics/users/{user}/avg")
    fun getUserAverageTime(@PathVariable("user") user: String) {

    }

    //    GET /statistics/users/{user}/moving_avg?window_size={window_size} - return list of moving averages of 15 minutes average buckets. I.E. moving averages of result /statistics/devices/{device}/avg
    @GetMapping("/statistics/users/{user}/moving_avg?window_size={window_size}")
    fun getUserMovingAverageTime(@PathVariable("device") user: String, @RequestParam("window_size") windowSize: Int) {

    }
}