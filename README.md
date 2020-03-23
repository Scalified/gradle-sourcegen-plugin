# Gradle SourceGen Plugin

[![Build Status](https://travis-ci.org/Scalified/gradle-sourcegen-plugin.svg)](https://travis-ci.org/Scalified/gradle-sourcegen-plugin)
[![Gradle Plugin Portal](https://img.shields.io/maven-metadata/v?label=Plugin&metadataUrl=https%3A%2F%2Fplugins.gradle.org%2Fm2%2Fcom%2Fscalified%2Fplugins%2Fgradle%2Fsourcegen%2Fcom.scalified.plugins.gradle.sourcegen.gradle.plugin%2Fmaven-metadata.xml)](https://plugins.gradle.org/plugin/com.scalified.plugins.gradle.sourcegen)

## Description

[Gradle SourceGen Plugin](https://plugins.gradle.org/plugin/com.scalified.plugins.gradle.sourcegen) configures directory for generated sources

## Requirements

* [Gradle 5+](https://gradle.org/)

## Changelog

[Changelog](CHANGELOG.md)

## Applying

Build script snippet for plugins DSL for Gradle 2.1 and later:

```gradle
plugins {
  id "com.scalified.plugins.gradle.sourcegen" version "<version>"
}
```

Build script snippet for use in older Gradle versions or where dynamic configuration is required:

```gradle
buildscript {
  repositories {
    maven {
      url "https://plugins.gradle.org/m2/"
    }
  }
  dependencies {
    classpath "gradle.plugin.com.scalified.plugins.gradle:sourcegen:<version>"
  }
}

apply plugin: "com.scalified.plugins.gradle.sourcegen"
```

## Usage

After applying the plugin, the following takes place:

1. A directory, specified in the **sourcegen.location** property (**src/main/generated** by default) created (if missing)
2. The created directory is configured:
   * as an output for annotation processor generated sources
   * for clean up during **clean** task
   * as marked generated sources root in IntelliJ IDEA (only if it is reside within **src/main**)

## Configuration

Currently the following configuration parameters supported (default values are shown):

```gradle
sourcegen {
    location = 'src/main/generated' // directory for generated source code
}
```

If you need more configuration options, you may <a href="mailto:info@scalified.com?subject=[Gradle SourceGen Plugin]: Proposals And Suggestions">send a request</a> with description

## License

```
MIT License

Copyright (c) 2019 Scalified

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

## Scalified Links

* [Scalified](http://www.scalified.com)
* [Scalified Official Facebook Page](https://www.facebook.com/scalified)
* <a href="mailto:info@scalified.com?subject=[Gradle SourceGen Plugin]: Proposals And Suggestions">Scalified Support</a>
