import jetbrains.buildServer.configs.kotlin.v2019_2.*
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.dockerSupport
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.merge
import jetbrains.buildServer.configs.kotlin.v2019_2.buildFeatures.sshAgent
import jetbrains.buildServer.configs.kotlin.v2019_2.buildSteps.gradle
import jetbrains.buildServer.configs.kotlin.v2019_2.projectFeatures.dockerRegistry
import jetbrains.buildServer.configs.kotlin.v2019_2.triggers.schedule
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

    vcsRoot(HttpsGithubComChubatovaTigerChubatovaGradleTestsBackupRefsHeadsSplit)

    buildType(Build_3)

    features {
        dockerRegistry {
            id = "PROJECT_EXT_5"
            name = "Docker Registry"
            url = "https://docker.io"
            userName = "teamcitycloud"
            password = "credentialsJSON:1de1e281-750e-4cd7-bd5e-8a16f4e51e8d"
        }
    }
}

object Build_3 : BuildType({
    id("Build")
    name = "Build"

    vcs {
        root(HttpsGithubComChubatovaTigerChubatovaGradleTestsBackupRefsHeadsSplit)
        root(DslContext.settingsRoot)
    }

    steps {
        gradle {
            tasks = "clean build"
            dockerImage = "jetbrains/teamcity-agent-staging:EAP-linux"
        }
    }

    triggers {
        vcs {
            branchFilter = ""

            enforceCleanCheckout = true
            enforceCleanCheckoutForDependencies = true
            buildParams {
                param("parfrom vcstrigger", "1")
            }
        }
        schedule {
            schedulingPolicy = cron {
                seconds = "1"
                minutes = "*/10"
            }
            branchFilter = ""
            triggerBuild = always()
        }
    }

    features {
        sshAgent {
            teamcitySshKey = "sshkeyamazon"
        }
        sshAgent {
            teamcitySshKey = "tcqaSpaceSsh"
        }
        dockerSupport {
            loginToRegistry = on {
                dockerRegistryId = "PROJECT_EXT_5"
            }
        }
        merge {
            branchFilter = "master"
        }
    }
})

object HttpsGithubComChubatovaTigerChubatovaGradleTestsBackupRefsHeadsSplit : GitVcsRoot({
    name = "https://github.com/ChubatovaTiger/ChubatovaGradleTestsBackup#refs/heads/split"
    url = "https://github.com/ChubatovaTiger/ChubatovaGradleTestsBackup"
    branch = "refs/heads/split"
})
