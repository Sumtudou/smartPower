<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.linln.modules</groupId>
    <artifactId>#{module}</artifactId>
    <packaging>jar</packaging>
    <name>#{module} module</name>

    <parent>
        <groupId>com.linln</groupId>
        <artifactId>modules</artifactId>
        <version>1.0</version>
    </parent>

    <dependencies>
        <dependency>
            <groupId>com.linln</groupId>
            <artifactId>common</artifactId>
            <version>${project.version}</version>
        </dependency>
        <dependency>
            <groupId>com.linln.modules</groupId>
            <artifactId>system</artifactId>
            <version>${project.version}</version>
        </dependency>
    </dependencies>
</project>