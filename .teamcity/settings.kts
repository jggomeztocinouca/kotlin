import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.buildSteps.script

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2023.11" // Define la versión de la configuración de TeamCity

project {
    description = "Ejercicio Dev/Ops VS UCA" // Descripción del proyecto
    buildType(Build) // Añade un tipo de construcción al proyecto
}

// Define el tipo de construcción 'Build'
object Build : BuildType({
    name = "Build"  // Nombre del tipo de construcción
    description = "Ejemplo de pipeline"  // Descripción del tipo de construcción

    // Configuración del sistema de control de versiones (VCS)
    vcs {
        root(DslContext.settingsRoot)  // Define la raíz del VCS para el tipo de construcción
    }

    // Pasos de construcción: 1. Establecer el número de versión del build, 2. Construir un artefacto
    steps {
        // Primer paso: Establecer el número de versión del build
        script {
            name = "Establecer version VCS"
            // 1. Obtiene el número de versión del VCS
            // 2. Crea una versión corta del hash
            // 3. Obtiene el contador de builds
            // 4. Forma el número de build
            // 5. Establece el número de build en TeamCity
            scriptContent = """
                            #!/bin/bash
                            HASH=%build.vcs.number%
                            SHORT_HASH=${'$'}{HASH:0:7}
                            BUILD_COUNTER=%build.counter%
                            BUILD_NUMBER="1.0${'$'}BUILD_COUNTER.${'$'}SHORT_HASH"
                            echo "##teamcity[buildNumber '${'$'}BUILD_NUMBER']"
                            """.trimIndent()
        }
        // Segundo paso: Simulación de construcción de un artefacto
        script {
            name = "Construccion de artefacto"
            // 1. Crea un directorio para el artefacto
            // 2. Crea un archivo de texto como artefacto simulado
            scriptContent = """
                            mkdir bin
                            echo "built artifact" > bin/compiled.txt
                            """.trimIndent()
        }
    }

    // Disparadores de construcción
    triggers {
        vcs {
            // Un disparador VCS vacío, se activa con cada cambio en el repositorio
        }
    }
})
