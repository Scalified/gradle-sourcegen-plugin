/*
 * MIT License
 *
 * Copyright (c) 2019 Scalified
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
 */

package com.scalified.plugins.gradle.sourcegen

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.gradle.plugins.ide.idea.model.IdeaModule
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * @author shell* @since 2019-12-13
 */
class SourceGenPlugin implements Plugin<Project> {

	private static final Logger LOGGER = LoggerFactory.getLogger(SourceGenPlugin)

	private static final String SOURCE_SET_NAME = "generated"

	@Override
	void apply(Project project) {
		def extension = project.extensions.create(SourceGenExtension.NAME, SourceGenExtension)
		LOGGER.debug("Created $SourceGenExtension.NAME extension")

		project.afterEvaluate {
			createDirectories(project, extension)
			configureDirectories(project, extension)
			configureSourceSets(project, extension)
			configureTasks(project, extension)
		}
	}

	private static def createDirectories(Project project, SourceGenExtension extension) {
		def file = project.file(extension.generated)
		if (!file.exists()) {
			file.mkdirs()
			LOGGER.debug("Created $file.absolutePath directory")
		}
	}

	private static def configureDirectories(Project project, SourceGenExtension extension) {
		if (!project.plugins.hasPlugin(IdeaPlugin)) {
			project.plugins.apply(IdeaPlugin)
			LOGGER.debug("Idea Plugin Applied")
		}
		def ideaModule = project.idea.module as IdeaModule
		def file = project.file(extension.generated)
		ideaModule.generatedSourceDirs += file
		LOGGER.debug("Marked $file.absolutePath as IDEA generated directory")
	}

	private static def configureSourceSets(Project project, SourceGenExtension extension) {
		def sourceSets = project.sourceSets as SourceSetContainer
		def mainSourceSet = sourceSets[SourceSet.MAIN_SOURCE_SET_NAME]
		def generatedSourceSet = sourceSets.create(SOURCE_SET_NAME)

		def file = project.file(extension.generated)

		generatedSourceSet.java.srcDirs = project.files(file)
		generatedSourceSet.compileClasspath += (mainSourceSet.compileClasspath + mainSourceSet.output)
		generatedSourceSet.runtimeClasspath += (mainSourceSet.runtimeClasspath + mainSourceSet.output)
		LOGGER.debug("Added $file.absolutePath directory to main source set")

		LOGGER.trace("Configured $SourceSet.MAIN_SOURCE_SET_NAME source set")
		project.tasks.compileJava.options.annotationProcessorGeneratedSourcesDirectory = file
	}

	private static def configureTasks(Project project, SourceGenExtension extension) {
		project.tasks.clean.doFirst {
			def file = project.file(extension.generated)
			if (file.exists()) {
				file.deleteDir()
			}
		}
	}

}
