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
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.jetbrains.kotlin.gradle.internal.KaptTask
import org.slf4j.LoggerFactory

/**
 * @author shell
 * @since 2020-03-10
 */
private const val JAVA_COMPILE_TASK_NAME = "compileJava"

private const val KAPT_PLUGIN = "org.jetbrains.kotlin.kapt"

private const val KAPT_TASK = "kaptKotlin"

private const val CLEAN_TASK_NAME = "clean"

private const val ANNOTATION_PROCESSOR_CONFIGURATION = "annotationProcessor"

private const val JAXB_API_DEPENDENCY = "javax.xml.bind:jaxb-api:2.3.1"

open class SourceGenPlugin : Plugin<Project> {

	private val logger = LoggerFactory.getLogger(SourceGenPlugin::class.java)

	override fun apply(project: Project) {
		val extension = project.extensions.create(SOURCE_GEN_EXTENSION_NAME, SourceGenExtension::class.java)
		logger.debug("Created $SOURCE_GEN_EXTENSION_NAME plugin extension")

		project.afterEvaluate {
			createDirectories(project, extension)
			configureDependencies(project)
			configureTasks(project, extension)
			configureDirectories(project, extension)
		}
	}

	private fun createDirectories(project: Project, extension: SourceGenExtension) {
		val file = project.file(extension.location)
		if (!file.exists()) {
			file.mkdirs()
			logger.debug("Created ${file.absolutePath} directory")
		}
	}

	private fun configureDependencies(project: Project) {
		project.dependencies.add(ANNOTATION_PROCESSOR_CONFIGURATION, JAXB_API_DEPENDENCY)
		logger.debug("Added $JAXB_API_DEPENDENCY to $ANNOTATION_PROCESSOR_CONFIGURATION configuration")
	}

	private fun configureTasks(project: Project, extension: SourceGenExtension) {
		val file = project.file(extension.location)
		val javaCompileTask = project.tasks.getByName(JAVA_COMPILE_TASK_NAME) as JavaCompile
		javaCompileTask.options.annotationProcessorGeneratedSourcesDirectory = file
		logger.debug("Configured JavaCompile task")

		if (project.plugins.hasPlugin(KAPT_PLUGIN)) {
			val kaptTask = project.tasks.getByName(KAPT_TASK) as KaptTask
			kaptTask.destinationDir = file
		}

		val cleanTask = project.tasks.getByName(CLEAN_TASK_NAME)
		cleanTask.doFirst {
			if (file.exists()) {
				file.listFiles()?.forEach {
					it.deleteRecursively()
				}
			}
		}
	}

	private fun configureDirectories(project: Project, extension: SourceGenExtension) {
		if (!project.plugins.hasPlugin(IdeaPlugin::class.java)) {
			project.plugins.apply(IdeaPlugin::class.java)
			logger.debug("Idea Plugin applied")
		}
		val ideaModule = project.plugins.getPlugin(IdeaPlugin::class.java).model.module
		ideaModule.generatedSourceDirs.add(project.file(extension.location))
		logger.debug("Marked ${extension.location} as IDEA generated sources directory")
	}

}
