# aspectj-android-gradle-plugin

Aspectj Anrdoid 插件，支持Android工程进行面向切面编程

使用方法：

```java
buildscript {
    repositories {
        jcenter()
    }
    dependencies {
        classpath 'com.aspectj:AspectAndroid:1.0.1'
    }
}
```

在工程Module中应用插件

```java
apply plugin: 'com.android.aspectj'
```
同步工程后，在工程目录下会自动生成一个aopSrc源文件目录，建议开发者把AOP源文件放到该目录下面
这样当你不使用该插件时，这些源文件就不会参与编译
