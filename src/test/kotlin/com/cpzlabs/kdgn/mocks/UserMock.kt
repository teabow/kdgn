package com.cpzlabs.kdgn.mocks

interface UserSpec
interface AutoPersistable

// kdgn:persistence: defaultValue = "1"
data class UserMock(
    // kdgn:persistence: defaultValue = "2"
    val lastname: String,
    // kdgn:persistence: defaultValue = "3"
    val firstname: String
): UserSpec, AutoPersistable
