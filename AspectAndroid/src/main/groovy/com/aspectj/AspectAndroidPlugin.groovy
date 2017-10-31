package com.aspectj

import com.android.build.gradle.AppExtension
import com.android.build.gradle.AppPlugin
import com.android.build.gradle.LibraryPlugin
import org.gradle.api.Plugin
import org.gradle.api.Project

public class AspectAndroidPlugin implements Plugin<Project> {
    @Override
    void apply(Project project) {
        project.rootProject.subprojects { subProject ->
            subProject.afterEvaluate {
                if (subProject.plugins.hasPlugin(LibraryPlugin)) {
                    subProject.dependencies {
                        compile 'org.aspectj:aspectjrt:1.8.10'
                    }
                }
            }
        }

        if (project.plugins.hasPlugin(AppPlugin)) {
            project.dependencies {
                compile 'org.aspectj:aspectjrt:1.8.10'
            }

            def aopSrc = new File(project.getProjectDir(), "aopSrc")
            aopSrc.mkdirs();

            project.android.sourceSets.main {
                java.srcDir 'aopSrc'
            }

            project.android.dexOptions {
                additionalParameters += '--no-strict'
            }

            AppExtension appExtension = project.extensions.getByType(AppExtension)
            appExtension.registerTransform(new AspectTransform(project))
        }
    }
}
