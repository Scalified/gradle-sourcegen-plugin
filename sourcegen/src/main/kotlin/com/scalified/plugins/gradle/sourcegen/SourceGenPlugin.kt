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
import org.gradle.kotlin.dsl.create
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.slf4j.LoggerFactory

/**
 * @author shell
 * @since 2020-03-10
 */
open class SourceGenPlugin : Plugin<Project> {

    private val logger = LoggerFactory.getLogger(SourceGenPlugin::class.java)

    override fun apply(project: Project) {
        val extension = project.extensions.create<SourceGenPluginExtension>(SOURCE_GEN).apply {
            logger.debug("'$SOURCE_GEN' extension created")
            location.convention(LOCATION)
            logger.debug("'{}' extension configured: {}", SOURCE_GEN, this)
        }
        project.afterEvaluate {
            createDirectories(project, extension)
            configureDirectories(project, extension)
            configureTasks(project, extension)
        }
    }

    private fun createDirectories(project: Project, extension: SourceGenPluginExtension) {
        val file = project.file(extension.location.get())
        if (!file.exists()) {
            file.mkdirs()
            logger.debug("Created ${file.absolutePath} directory")
        }
    }

    private fun configureDirectories(project: Project, extension: SourceGenPluginExtension) {
        val file = project.file(extension.location.get())
        project.extensions.java?.sourceSets?.main?.java?.srcDir(file)

        if (!project.plugins.hasPlugin(IdeaPlugin::class.java)) {
            project.plugins.apply(IdeaPlugin::class.java)
            logger.debug("Idea Plugin applied")
        }
        val ideaModule = project.plugins.idea?.model?.module
        ideaModule?.generatedSourceDirs?.add(project.file(extension.location.get()))
        logger.debug("Marked '{}' as IDEA generated sources directory", extension.location.get())
    }

    private fun configureTasks(project: Project, extension: SourceGenPluginExtension) {
        val file = project.file(extension.location.get())
        val javaCompileTask = project.tasks.javaCompile
        javaCompileTask.options.generatedSourceOutputDirectory.set(file)
        logger.debug("Configured JavaCompile task")

        project.plugins.kapt?.let {
            val kaptTask = project.tasks.kapt
            kaptTask.destinationDir.set(file)
            kaptTask.kotlinSourcesDestinationDir.set(file)
            logger.debug("Configured Kapt task")
        }

        project.tasks.clean.doFirst {
            if (file.exists()) {
                file.listFiles()?.forEach { it.deleteRecursively() }
            }
        }
    }

}
