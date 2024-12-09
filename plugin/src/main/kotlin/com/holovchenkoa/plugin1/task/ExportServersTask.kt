package com.holovchenkoa.plugin1.task

import com.holovchenkoa.plugin1.extension.Server
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.ListProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class ExportServersTask: DefaultTask() {

    @get:Input
    abstract val servers: ListProperty<String>
    @get:OutputFile
    abstract val outputFile: RegularFileProperty

    init {
        outputFile.convention(project.layout.buildDirectory.file("servers.txt"))
    }

    @TaskAction
    fun execute() {

        servers.get().let {
            println("Saved servers: $it")
            outputFile.get().asFile.writeText(it.joinToString())
        }
    }
}