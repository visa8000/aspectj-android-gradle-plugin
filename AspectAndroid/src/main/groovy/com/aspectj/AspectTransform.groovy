package com.aspectj

import com.android.build.api.transform.*
import com.android.utils.FileUtils
import org.gradle.api.Project

class AspectTransform extends Transform {

    Project project

    public AspectTransform(Project proj) {
        project = proj
    }

    @Override
    String getName() {
        return "AspectAndroid"
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return Collections.singleton(QualifiedContent.DefaultContentType.CLASSES)
    }

    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return EnumSet.of(QualifiedContent.Scope.PROJECT
                , QualifiedContent.Scope.PROJECT_LOCAL_DEPS
                , QualifiedContent.Scope.EXTERNAL_LIBRARIES
                , QualifiedContent.Scope.SUB_PROJECTS
                , QualifiedContent.Scope.SUB_PROJECTS_LOCAL_DEPS)
    }

    @Override
    boolean isIncremental() {
        return false
    }

    @Override
    void transform(Context context
                   , Collection<TransformInput> inputs
                   , Collection<TransformInput> referencedInputs
                   , TransformOutputProvider outputProvider
                   , boolean isIncremental) throws IOException, TransformException, InterruptedException {

        outputProvider.deleteAll()

        AjcWeave ajcWeave = new AjcWeave(project)
        ajcWeave.bootClassPath = project.android.bootClasspath.join(File.pathSeparator)

        inputs.each { TransformInput transformInput ->
            transformInput.directoryInputs.each {
                ajcWeave.aspectPath << it.file
                ajcWeave.inPath << it.file
                ajcWeave.classPath << it.file
            }
            transformInput.jarInputs.each {
                ajcWeave.aspectPath << it.file
                ajcWeave.classPath << it.file
                ajcWeave.inPath << it.file
            }
        }

        File jarFile = outputProvider.getContentLocation("aspected", getOutputTypes(), getScopes(),
                Format.JAR);
        FileUtils.mkdirs(jarFile.getParentFile());
        FileUtils.deleteIfExists(jarFile);
        ajcWeave.outputjar = jarFile.getAbsolutePath()

        ajcWeave.weave()
    }
}