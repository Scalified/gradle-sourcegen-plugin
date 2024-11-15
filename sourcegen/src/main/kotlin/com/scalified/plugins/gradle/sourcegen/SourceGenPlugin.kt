/*
 * MIT License
 *
 * Copyright (c) 2020 Scalified
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 *
 */

package com.scalified.plugins.gradle.sourcegen

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaPlugin
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.create
import org.gradle.kotlin.dsl.hasPlugin
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.slf4j.LoggerFactory
import java.io.File

/**
 * @author shell
 * @since 2020-03-10
 */
open class SourceGenPlugin : Plugin<Project> {

    private val logger = LoggerFactory.getLogger(SourceGenPlugin::class.java)

    override fun apply(project: Project) {
        listOf(JavaPlugin::class, IdeaPlugin::class).filterNot(project.plugins::hasPlugin).forEach { type ->
            project.plugins.apply(type)
            logger.info("Applied '${type.simpleName}' plugin")
        }

        val extension = project.extensions.create<SourceGenPluginExtension>(SOURCE_GEN).apply {
            logger.debug("Extension '$SOURCE_GEN' created")
            location.convention(LOCATION)
            logger.debug("Extension '{}' configured: {}", SOURCE_GEN, this)
        }

        project.afterEvaluate {
            val location = extension.location.get()
            val locationFile = file(location)

            createMissingDirectories(setOf(location))

            project.extensions.java.sourceSets.main.java.srcDir(locationFile)
            logger.debug("Added '$location' generated sources as java sources")

            project.plugins.idea.model.module.generatedSourceDirs.add(locationFile)
            logger.debug("Marked '$location' as an IDEA generated sources directory")

            tasks.javaCompile.get().options.generatedSourceOutputDirectory.set(locationFile)
            logger.debug("Added '$location' generated sources as a java compile generated source output directory")

            plugins.kaptOptional?.let { tasks.kapt.get() }?.apply {
                destinationDir.set(locationFile)
                kotlinSourcesDestinationDir.set(locationFile)
                logger.debug("Added '$location' generated sources as kapt sources destination directory")
            }

            tasks.clean.get().doFirst {
                if (locationFile.exists()) {
                    locationFile.listFiles()?.forEach(File::deleteRecursively)
                    logger.debug("Location '$location' cleaned")
                }
            }
        }
    }

}
