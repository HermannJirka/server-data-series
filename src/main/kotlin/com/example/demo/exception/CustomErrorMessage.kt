package com.example.demo.exception

import org.springframework.http.HttpStatus
import java.lang.Exception

class BadRequestException(message: String): Exception(message)

class NotFoundException(message: String): Exception(message)

class DuplicateDataPointException(message: String): Exception(message)
