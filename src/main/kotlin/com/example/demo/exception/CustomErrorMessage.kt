package com.example.demo.exception

class BadRequestException(message: String): Exception(message)

class NotFoundException(message: String): Exception(message)

class DuplicateDataPointException(message: String): Exception(message)
