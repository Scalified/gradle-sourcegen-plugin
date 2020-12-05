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
import org.gradle.api.Task
import org.gradle.api.plugins.Convention
import org.gradle.api.plugins.JavaPluginConvention
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.plugins.ide.idea.IdeaPlugin
import org.jetbrains.kotlin.gradle.internal.KaptTask

/**
 * @author shell
 * @since 2020-12-05
 */
internal val PluginContainer.kapt: Plugin<*>?
	get() = findPlugin("org.jetbrains.kotlin.kapt")

internal val PluginContainer.idea: IdeaPlugin?
	get() = findPlugin(IdeaPlugin::class.java)

internal val Convention.java: JavaPluginConvention?
	get() = findPlugin(JavaPluginConvention::class.java)

internal val TaskContainer.clean: Task
	get() = getByName("clean")

internal val TaskContainer.javaCompile: JavaCompile
	get() = getByName("compileJava") as JavaCompile

internal val TaskContainer.kapt: KaptTask
	get() = getByName("kaptKotlin") as KaptTask

internal val SourceSetContainer.main: SourceSet
	get() = getByName("main")
