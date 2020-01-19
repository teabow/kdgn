package com.cpzlabs.kdgn.mocks

object Academy : AutoPersistable {
    var country: String = ""
}

class University(name: String) : AutoPersistable

class Project(var projectId: Int, val projectName: String, projectPlace: String) : AutoPersistable {
    constructor(map: Map<String, String>) : this(0, map["projectName"] ?: "", map["projectPlace"] ?: "")
}
