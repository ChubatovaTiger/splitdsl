import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.PullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.pullRequests
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2019_2.vcs.GitVcsRoot

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

version = "2021.2"

project {

    vcsRoot(HttpsGithubComChubatovaTigerChubatovaGradleTestsBackupRefsHeadsMaster)
    vcsRoot(HttpsGithubComChubatovaTigerGradletests2)

    buildType(Build_4)

    params {
        param("teamcity.feature.splitTests.enabled", "true")
    }
}

object Build_4 : BuildType({
    id("Build")
    name = "Build"

    artifactRules = "+:1.txt => par"

    params {
        password("a", "credentialsJSON:426a4a84-8553-4e35-8fec-0b3ada0e9e56")
        param("b", "bvaluechubatovachangedfromui")
    }

    vcs {
        root(HttpsGithubComChubatovaTigerChubatovaGradleTestsBackupRefsHeadsMaster)
        root(HttpsGithubComChubatovaTigerGradletests2, "+:. => proj2")
    }

    steps {
        gradle {
            tasks = "clean build"
            gradleWrapperPath = ""
        }
        gradle {
            name = "Gradle proj 2"
            executionMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            tasks = "clean build"
            buildFile = "proj2/build.gradle"
        }
        script {
            executionMode = BuildStep.ExecutionMode.RUN_ON_FAILURE
            scriptContent = """
                echo %b%
                echo %a% > 1.txt
            """.trimIndent()
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        pullRequests {
            vcsRootExtId = "${HttpsGithubComChubatovaTigerChubatovaGradleTestsBackupRefsHeadsMaster.id}"
            provider = github {
                authType = token {
                    token = "credentialsJSON:63a67384-170c-4d43-b873-4c1fed696b01"
                }
                filterAuthorRole = PullRequests.GitHubRoleFilter.MEMBER
            }
        }
        feature {
            type = "splitTests"
            param("numberOfParts", "3")
        }
    }
})

object HttpsGithubComChubatovaTigerChubatovaGradleTestsBackupRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/ChubatovaTiger/ChubatovaGradleTestsBackup#refs/heads/master"
    url = "https://github.com/ChubatovaTiger/ChubatovaGradleTestsBackup"
    branch = "refs/heads/master"
    branchSpec = ""
})

object HttpsGithubComChubatovaTigerGradletests2 : GitVcsRoot({
    name = "https://github.com/ChubatovaTiger/Gradletests2"
    url = "https://github.com/ChubatovaTiger/Gradletests2"
    branch = "refs/heads/main"
})
