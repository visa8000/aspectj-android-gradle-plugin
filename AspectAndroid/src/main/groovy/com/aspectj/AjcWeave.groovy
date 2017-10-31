package com.aspectj

import org.aspectj.bridge.IMessage
import org.aspectj.bridge.MessageHandler
import org.aspectj.tools.ajc.Main
import org.gradle.api.GradleException
import org.gradle.api.Project


public class AjcWeave {
    Project project
    public ArrayList<File> inPath = new ArrayList<File>()
    public ArrayList<File> aspectPath = new ArrayList<File>()
    public ArrayList<File> classPath = new ArrayList<File>()
    public String bootClassPath
    public String outputjar

    AjcWeave(Project proj) {
        project = proj
    }

    void weave() {
        def args = [
                "-showWeaveInfo",
                "-1.5",
                "-Xlint:ignore",
                "-outjar", outputjar,
                "-classpath", classPath.join(File.pathSeparator),
                "-bootclasspath", bootClassPath
        ]

        if (!getInPath().isEmpty()) {
            args << '-inpath'
            args << getInPath().join(File.pathSeparator)
        }
        if (!getAspectPath().isEmpty()) {
            args << '-aspectpath'
            args << getAspectPath().join(File.pathSeparator)
        }

        MessageHandler handler = new MessageHandler(true);
        Main m = new Main();
        m.run(args as String[], handler);
        for (IMessage message : handler.getMessages(null, true)) {
            switch (message.getKind()) {
                case IMessage.ABORT:
                case IMessage.ERROR:
                case IMessage.FAIL:
                    throw new GradleException(message.message, message.thrown)
                case IMessage.WARNING:
                    break;
                case IMessage.INFO:
                    break;
                case IMessage.DEBUG:
                    break;
            }
        }
        m.quit()
    }

    public ArrayList<File> getInPath() {
        return inPath;
    }

    public ArrayList<File> getAspectPath() {
        return aspectPath;
    }
}
