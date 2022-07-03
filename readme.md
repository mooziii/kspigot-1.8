# legacy-kspigot

This is a backport of [KSpigot](https://github.com/jakobkmar/KSpigot) to minecraft 1.8.8

⚠️ **I highly recommend to not use 1.8.8 at all** ⚠️
**Use the modern versions of minecraft instead**

![Latest version](https://img.shields.io/badge/latest%20version-1.8.0-pink?style=for-the-badge)


KSpigot is a Kotlin library for the popular [spigot server software](https://spigotmc.org/) for minecraft. It adds
lots of useful features, builders and extensions for the Spigot API itself - but KSpigot also brings new things like an
Inventory GUI API, or ~~Brigardier~~ (if 1.8 had brigadier) support.

## Dependency

KSpigot for 1.8 is available on Maven Local (XD).

Like really to use it you have to publish it yourself to your local maven repository using

`gradlew publishToMavenLocal`

Gradle:

Repository:
```kt
mavenLocal()
```

Dependency:
```kt
implementation("net.axay:kspigot:1.8")
```

THIS WONT RECEIVE UPDATES AT ALL.