package com.example.demo.controller

import com.example.demo.model.AverageResponse
import com.example.demo.model.DataPoint
import com.example.demo.repository.DataPointRepository
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime
import java.time.ZoneId

@SpringBootTest
@AutoConfigureMockMvc
internal class StatisticControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var mapper: ObjectMapper

    @Autowired
    lateinit var dataPointRepository: DataPointRepository

    @BeforeEach
    fun setDataForDataPointsRepository() {
        val dateTimeNow = LocalDateTime.now()
        val dateTimeBefore2 = dateTimeNow.minusHours(2)

        val nowBucket = (dateTimeNow.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 60000) / 15
        var lastBucket = (dateTimeBefore2.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli() / 60000) / 15

        var tempDate = dateTimeBefore2
        while (lastBucket <= nowBucket) {
            val dp1 = DataPoint(2, "device1", "user1", tempDate)
            val dp2 = DataPoint(4, "device1", "user1", tempDate.plusMinutes(1))
            val dp3 = DataPoint(4, "device2", "user2", tempDate)
            val dp4 = DataPoint(6, "device2", "user2", tempDate.plusMinutes(1))

            dataPointRepository.addDataPoint(dp1)
            dataPointRepository.addDataPoint(dp2)
            dataPointRepository.addDataPoint(dp3)
            dataPointRepository.addDataPoint(dp4)
            tempDate = tempDate.plusMinutes(15)
            lastBucket += 1
        }
    }

    @Test
    fun successDevicesAvgTime() {
        val contentAsString = mockMvc.perform(get("/statistics/devices/device1/avg"))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString

        val avgDataPoints: List<AverageResponse> = mapper.readValue(contentAsString)
        assertEquals(9, avgDataPoints.size)
        avgDataPoints.forEach { t -> assertEquals(3, t.average.toInt()) }
    }

    @Test
    fun successDevicesMovingAverageTime() {
        val contentAsString = mockMvc.perform(get("/statistics/devices/device1/moving_avg")
                .param("window_size", "3"))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString

        val avgDataPoints: List<AverageResponse> = mapper.readValue(contentAsString)
        assertEquals(7, avgDataPoints.size)
        avgDataPoints.forEach { t -> assertEquals(3, t.average.toInt()) }
    }

    @Test
    fun successUserAverageTime() {
        val contentAsString = mockMvc.perform(get("/statistics/users/user2/avg"))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString

        val avgDataPoints: List<AverageResponse> = mapper.readValue(contentAsString)
        assertEquals(9, avgDataPoints.size)
        avgDataPoints.forEach { t -> assertEquals(5, t.average.toInt()) }
    }

    @Test
    fun successUserMovingAverageTime() {
        val contentAsString = mockMvc.perform(get("/statistics/users/user2/moving_avg")
                .param("window_size", "2"))
                .andExpect(status().isOk)
                .andReturn().response.contentAsString

        val avgDataPoints: List<AverageResponse> = mapper.readValue(contentAsString)
        assertEquals(8, avgDataPoints.size)
        avgDataPoints.forEach { t -> assertEquals(5, t.average.toInt()) }
    }

    @Test
    fun notFoundUserForUserMovingAverageTime() {
        mockMvc.perform(get("/statistics/users/user3/moving_avg")
                .param("window_size", "2"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun notFoundDeviceForDeviceMovingAverageTime() {
        mockMvc.perform(get("/statistics/devices/device3/moving_avg")
                .param("window_size", "2"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun notFoundDeviceForDeviceAverageTime() {
        mockMvc.perform(get("/statistics/devices/device3/avg"))
                .andExpect(status().isNotFound)
    }

    @Test
    fun notFoundUserForUserAverageTime() {
        mockMvc.perform(get("/statistics/users/user3/avg"))
                .andExpect(status().isNotFound)
    }
}