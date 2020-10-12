package com.example.demo.controller

import com.example.demo.model.DataPoint
import com.example.demo.repository.DataPointRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.time.LocalDateTime

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
        dataPointRepository.addDataPoint(DataPoint(1, "device1", "user1", LocalDateTime.parse("2020-10-12T11:26:25")))
        dataPointRepository.addDataPoint(DataPoint(1, "device1", "user1", LocalDateTime.parse("2020-10-12T11:27:25")))
        dataPointRepository.addDataPoint(DataPoint(1, "device2", "user2", LocalDateTime.parse("2020-10-12T11:26:25")))
        dataPointRepository.addDataPoint(DataPoint(1, "device2", "user2", LocalDateTime.parse("2020-10-12T11:27:25")))
        dataPointRepository.addDataPoint(DataPoint(2, "device1", "user1", LocalDateTime.parse("2020-10-12T11:36:25")))
        dataPointRepository.addDataPoint(DataPoint(2, "device1", "user1", LocalDateTime.parse("2020-10-12T11:37:25")))
        dataPointRepository.addDataPoint(DataPoint(2, "device2", "user2", LocalDateTime.parse("2020-10-12T11:36:25")))
        dataPointRepository.addDataPoint(DataPoint(2, "device2", "user2", LocalDateTime.parse("2020-10-12T11:37:25")))
        dataPointRepository.addDataPoint(DataPoint(3, "device1", "user1", LocalDateTime.parse("2020-10-13T11:26:25")))
        dataPointRepository.addDataPoint(DataPoint(3, "device1", "user1", LocalDateTime.parse("2020-10-13T11:27:25")))
        dataPointRepository.addDataPoint(DataPoint(3, "device2", "user2", LocalDateTime.parse("2020-10-13T11:26:25")))
        dataPointRepository.addDataPoint(DataPoint(3, "device2", "user2", LocalDateTime.parse("2020-10-13T11:27:25")))
    }

    @Test
    fun successDevicesAvgTime() {
        mockMvc.perform(get("/statistics/devices/device1/avg"))
                .andExpect(status().isOk);
    }

    @Test
    fun successDevicesMovingAverageTime() {
        mockMvc.perform(get("/statistics/devices/device1/avg"))
                .andExpect(status().isOk);
    }

    @Test
    fun successUserAverageTime() {
    }

    @Test
    fun successUserMovingAverageTime() {
    }
}