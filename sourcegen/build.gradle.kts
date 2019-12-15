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
 *
 */
plugins {
	groovy
    `java-gradle-plugin`
	id("com.gradle.plugin-publish") version "0.10.1"
}

repositories {
	mavenCentral()
}

pluginBundle {
	website = "https://scalified.com/"
	vcsUrl = "https://github.com/Scalified/gradle-sourcegen-plugin"
	description = "Gradle SourceGen Plugin"
	tags = listOf("source", "generated", "src")
}

gradlePlugin {
	plugins {
		create("SourceGen Plugin") {
			id = "com.scalified.plugins.gradle.sourcegen"
			displayName = "Gradle SourceGen Plugin"
			description = "A Plugin that configures directory for generated sources"
			implementationClass = "com.scalified.plugins.gradle.sourcegen.SourceGenPlugin"
			version = project.version
		}
	}
}
