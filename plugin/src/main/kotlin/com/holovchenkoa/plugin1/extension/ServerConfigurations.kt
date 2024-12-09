package com.holovchenkoa.plugin1.extension

import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import org.gradle.api.provider.Property
import javax.inject.Inject

open class ServerConfigurationExtension(project: Project) {
    val servers: NamedDomainObjectContainer<Server> = project.container(Server::class.java) { name ->
        project.objects.newInstance(ServerImpl::class.java, name)
    }
}

// Example of a custom named domain object.
// https://docs.gradle.org/current/userguide/collections.html#nameddomainobjectcontainer
interface Server {
    val name: String // each named object should have this property
    val port: Property<Int>
}

abstract class ServerImpl @Inject constructor(override val name: String) : Server
