# Gradle plugin examples

## Description
The project demonstrates how gradle plugins can be written.
There are examples of custom domain object container implementations

## How to use

1. Pull the project and publish the plugins to maven local repository:
``gradlew publishToMavenLocal``
2. apply a plugin to your project. 
The deployment plugin is an extension of the servers plugin. Thus, only one plugin can be applied.

build.gradle.kts
```
plugins {
  id("com.holovchenkoa.servers") version "0.0.0-SNAPSHOT"
  id("com.holovchenkoa.deployment") version "0.0.0-SNAPSHOT"
}
```
Plugins create a bunch of tasks that can be found in "servers" folder

### Servers plugin

```
clusters {
    register("mssql-cluster") {
        nodes.add("mssql-node1")
        nodes.add("mssql-node2")
    }
    register("RDP-cluster") {
        // empty cluster
    }
}

serversConfiguration {
    this.servers.register("mssql-node1") {
        port = 1433
    }
    this.servers.register("mssql-node2") {
        port = 14330
    }
}
```

### Deployment plugin
Plugin creates tasks dynamically based on added configuration.
By default, only one ("AWS") server is added and only one "deployToAws" task is available in "servers" folder.

```
deploymentConfiguration {

val doNotDeploy = listOf("mssql-node1")
val azureServer = project.objects.newInstance(Azure::class.java, "myAzureCloud", doNotDeploy)

// Note: There are difference between Azure and AWS classes. 
// Thus Aws can be created only using  project.objects.named()
// And Azure can be created only using project.objects.newInstance()
//    val azureServer = project.objects.named(Azure::class.java, "myAzureCloudNamed")


cloudProviders.add(azureServer)

    // Custom domain named object implementation example
    cloudProviders.add(object : CloudProvider() {
        override fun deploy(servers: List<String>) {
            println("Deploying in private cloud: Servers count: ${servers.size}")
            servers.forEach(::println)
        }

        override fun getName(): String = "PrivateCloud"

    })
}
```

### Tasks

To see available tasks execute ``gradlew tasks``
Here tasks names:

Servers tasks
-------------
deployToAws
deployToMyAzureCloud
deployToPrivateCloud
exportServers
listClusters