package com.holovchenkoa.plugin2.extension

import org.gradle.api.ExtensiblePolymorphicDomainObjectContainer
import org.gradle.api.Named
import org.gradle.api.Project
import javax.inject.Inject

interface NamedContainer : Named

abstract class CloudProvider: NamedContainer {
    abstract fun deploy(servers: List<String>)
}

open class AWS : CloudProvider(), Named {
    override fun getName(): String = name
    override fun deploy(servers: List<String>) {
        println("Deploying in AWS. Servers count: ${servers.size}")
        servers.forEach { println("Deploying ${this.name}") }
    }
}

open class Azure @Inject constructor(private val name: String, var excludeServers: List<String> ) : CloudProvider() {

    override fun getName(): String = name
    override fun deploy(servers: List<String>) {
        println("Deploying in Azure. Servers count: ${servers.size}")

        val isNotBlocked: (String) -> Boolean = {server ->  excludeServers.none { server.startsWith(it) } }
        servers.forEach {
            if (isNotBlocked(it)) {
                println("Deploying ${it}")
            } else {
                println("Server: ${it} skipped. Blacklisted.")
            }
        }
    }

    fun excludeServer(server: String) {
        excludeServers += server
    }
}

open class CloudPlatforms(project: Project) {
    val cloudProviders: ExtensiblePolymorphicDomainObjectContainer<CloudProvider> = project.objects.polymorphicDomainObjectContainer(
        CloudProvider::class.java)
}