package com.cpzlabs.kdgn.mocks

import com.cpzlabs.kdgn.annotations.AutoMap
import com.cpzlabs.kdgn.annotations.AutoModel
import com.cpzlabs.kdgn.annotations.AutoModelField

object Academy : AutoPersistable {
    var country: String = ""
}

class University(name: String) : AutoPersistable {
    fun register(userMock: UserMock): String {
        // empty
        return "ID__${userMock.firstname}__${userMock.lastname}"
    }
}

@AutoMap
class Project(@AutoModelField(required = true) var projectId: Int, val projectName: String, projectPlace: String) : AutoPersistable {
    constructor(map: Map<String, String>) : this(0, map["projectName"] ?: "", map["projectPlace"] ?: "")

    fun startProject() {

    }

    fun endProject() {

    }
}
