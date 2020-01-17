package com.cpzlabs.kdgn.mocks

interface UserSpec
interface AutoPersistable

// sourcery:persistence: defaultValue = "1"
data class UserMock(
    // sourcery:persistence: defaultValue = "2"
    val lastname: String,
    // sourcery:persistence: defaultValue = "3"
    val firstname: String
): UserSpec, AutoPersistable
