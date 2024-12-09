package com.holovchenkoa.plugin2.task

import com.holovchenkoa.plugin2.extension.CloudProvider
import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.TaskAction

abstract class DeploymentTask : DefaultTask() {

    @get:Input
    abstract var targetPlatform: CloudProvider
//    @get:Input
//    abstract val serverNames: ListProperty<String>  //= project.objects.listProperty(String::class.java)
    @get:InputFile
    abstract val inputFile: RegularFileProperty

    init {
        inputFile.convention(project.layout.buildDirectory.file("servers.txt"))
    }

    @TaskAction
    fun deploy() {
        val servers = inputFile.get().asFile.readText().split(",")
        targetPlatform.deploy(servers)
    }
}
