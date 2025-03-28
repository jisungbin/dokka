/*
 * Copyright 2014-2024 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license.
 */
import dokkabuild.DokkaBuildProperties

/**
 * A convention plugin that sets up common config and sensible defaults for all subprojects.
 *
 * It provides the [DokkaBuildProperties] extension, for accessing common build properties.
 */

plugins {
    base
}

val dokkaBuild = extensions.create<DokkaBuildProperties>(
    DokkaBuildProperties.EXTENSION_NAME,
    provider { project.version.toString() },
)

tasks.withType<AbstractArchiveTask>().configureEach {
    // https://docs.gradle.org/current/userguide/working_with_files.html#sec:reproducible_archives
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.withType<Test>().configureEach {
    develocity.testRetry {
        maxRetries = dokkaBuild.isCI.map { isCI -> if (isCI) 3 else 0 }
        failOnPassedAfterRetry = true
    }
}
