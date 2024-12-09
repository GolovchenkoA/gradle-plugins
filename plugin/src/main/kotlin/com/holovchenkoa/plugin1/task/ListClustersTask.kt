package com.holovchenkoa.plugin1.task

import com.holovchenkoa.plugin1.Cluster
import com.holovchenkoa.plugin1.extension.Server
import org.gradle.api.DefaultTask
import org.gradle.api.NamedDomainObjectCollection
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.TaskAction

abstract class ListClustersTask: DefaultTask() {

    @get:Input
    abstract var clusters: NamedDomainObjectCollection<Cluster>
    @get:Input
    abstract var clusterNodes: NamedDomainObjectCollection<Server>

    @TaskAction
    fun execute() {
        println("---------List clusters and their nodes---------")
        clusters.forEach {cluster ->
            if (cluster.nodes.isEmpty()) {
                println("Cluster: ${cluster.name} servers not found")
            }

            cluster.nodes.forEach { serverName ->
                val server = clusterNodes.findByName(serverName)
                if (server == null) {
                    println("Server ${serverName} not found")
                } else {
                    println("Cluster: ${cluster.name} server: ${server.name} port: ${server.port.get()}   ${server}")
                }

            }
        }
    }
}