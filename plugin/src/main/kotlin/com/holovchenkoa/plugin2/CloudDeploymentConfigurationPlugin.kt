package com.holovchenkoa.plugin2

import com.holovchenkoa.plugin1.ServerConfigurationPlugin
import com.holovchenkoa.plugin1.task.ExportServersTask
import com.holovchenkoa.plugin2.extension.AWS
import com.holovchenkoa.plugin2.extension.CloudPlatforms
import com.holovchenkoa.plugin2.task.DeploymentTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized

class CloudDeploymentConfigurationPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.pluginManager.apply(ServerConfigurationPlugin::class.java)

        val platformConfiguration = project.extensions.create("deploymentConfiguration", CloudPlatforms::class.java)
        val cloudProviders = platformConfiguration.cloudProviders


        // Having domain objects we can do everything in the configuration phase
        // and there is no need to use project.afterEvaluate to process all added objects

        cloudProviders.whenObjectAdded { cloudProvider ->
            project.tasks.register("deployTo${cloudProvider.getName().capitalized()}", DeploymentTask::class.java).configure {
                it.group = "servers"
                // dependency should be added automatically based on inputs.file
//                it.dependsOn(project.tasks.withType(ExportServersTask::class.java))
                it.inputs.file(project.tasks.withType(ExportServersTask::class.java).first().outputFile)

                it.inputFile.set(project.tasks.withType(ExportServersTask::class.java).first().outputFile)
                it.targetPlatform = cloudProvider
            }
        }

        // should be added after cloudProviders.whenObjectAdded.
        cloudProviders.add(project.objects.named(AWS::class.java, "aws"))
    }
}