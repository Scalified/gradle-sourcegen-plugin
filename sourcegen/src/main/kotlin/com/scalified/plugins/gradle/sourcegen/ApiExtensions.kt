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
 */

package com.scalified.plugins.gradle.sourcegen

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.getPlugin
import org.gradle.kotlin.dsl.named
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.jetbrains.kotlin.gradle.internal.KaptTask

/**
 * @author shell
 * @since 2020-12-05
 */
internal val PluginContainer.kaptOptional: Plugin<*>?
    get() = findPlugin("org.jetbrains.kotlin.kapt")

internal val PluginContainer.idea: IdeaPlugin
    get() = getPlugin(IdeaPlugin::class)

internal val ExtensionContainer.java: JavaPluginExtension
    get() = getByType<JavaPluginExtension>()

internal val TaskContainer.clean: TaskProvider<Task>
    get() = named("clean")

internal val TaskContainer.javaCompile: TaskProvider<JavaCompile>
    get() = named<JavaCompile>("compileJava")

internal val TaskContainer.kapt: TaskProvider<KaptTask>
    get() = named<KaptTask>("kaptKotlin")

internal val SourceSetContainer.main: SourceSet
    get() = getByName("main")

internal fun Project.createMissingDirectories(paths: Set<String>) {
    paths.map(this::file).forEach { file ->
        if (!file.exists()) {
            file.mkdirs()
            logger.debug("Created '${file.absolutePath}' directory")
        }
    }
}
