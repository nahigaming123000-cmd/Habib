#!/bin/sh

APP_HOME=$(cd "${0%/*}" && pwd)
APP_BASE_NAME=$(basename "$0")

DEFAULT_JVM_OPTS='"-Xmx64m" "-Xms64m"'

CLASSPATH=$APP_HOME/gradle/wrapper/gradle-wrapper.jar

JAVACMD=java
which java >/dev/null 2>&1 || die "ERROR: JAVA_HOME is not set"

exec "$JAVACMD" -cp "$CLASSPATH" org.gradle.wrapper.GradleWrapperMain "$@"
