/*
 * Copyright (C) 2025 RyuNen344
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 * License-Filename: LICENSE.md
 */

package io.github.ryunen344.mutton.log

public abstract class Logger {
    public abstract fun log(tag: String, level: Level, throwable: Throwable? = null, message: (() -> String)? = null)

    public enum class Level {
        VERBOSE,
        DEBUG,
        INFO,
        WARN,
        ERROR,
    }
}

public fun Logger.verbose(tag: String, throwable: Throwable? = null, message: (() -> String)? = null) {
    log(tag, Logger.Level.VERBOSE, throwable, message)
}

public fun Logger.debug(tag: String, throwable: Throwable? = null, message: (() -> String)? = null) {
    log(tag, Logger.Level.DEBUG, throwable, message)
}

public fun Logger.info(tag: String, throwable: Throwable? = null, message: (() -> String)? = null) {
    log(tag, Logger.Level.INFO, throwable, message)
}

public fun Logger.warn(tag: String, throwable: Throwable? = null, message: (() -> String)? = null) {
    log(tag, Logger.Level.WARN, throwable, message)
}

public fun Logger.error(tag: String, throwable: Throwable? = null, message: (() -> String)? = null) {
    log(tag, Logger.Level.ERROR, throwable, message)
}

public val NoopLogger: Logger = object : Logger() {
    override fun log(tag: String, level: Level, throwable: Throwable?, message: (() -> String)?) {
        // noop
    }
}
