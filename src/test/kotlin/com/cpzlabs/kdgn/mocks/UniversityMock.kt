package com.cpzlabs.kdgn.mocks

object Academy: AutoPersistable {
    var country: String = ""
}

class University(name: String): AutoPersistable

class Project(var projectId: Int, val projectName: String, projectPlace: String): AutoPersistable
