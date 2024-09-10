# warmup-springboot3-starter

English | [简体中文](./README_zh-CN.md)

## What is this?

This is an open-source library that helps you optimize the initial request processing time of Spring Boot 3.

## Quick Start

Add the following dependency to your `pom.xml` file:

```xml
<dependency>
    <groupId>com.baizhukui</groupId>
    <artifactId>warmup-springboot3-starter</artifactId>
    <version>1.0.1</version>
</dependency>
```

That's all you need to do!

## What have we done?

We found that the processing time of the first request for a Spring Boot application is significantly higher than the second and subsequent requests.

For a simple Spring Boot + JPA application, the processing time of the first request can be several or even ten times that of the second. In some large-scale projects, the processing time of the first request may be even longer.

Whether you are the user who happens to visit first after the program starts or a tester, a waiting time of several seconds is definitely a very bad experience.

Therefore, we decided to write a library that preheats the application by simulating some HTTP requests, database query requests, and other behaviors after the application starts, which can greatly reduce the processing time of the first request (for my own small project, it was reduced by about 50%).

Currently, we have preheated the following scenarios:

- JSON parsing and parameter validation in HTTP requests
- Queries in MyBatis Plus and JPA

Of course, you can also achieve this by writing some code yourself.

## Configuration

For frequent startup scenarios such as local development self-testing, the increased startup time brought by preheating may be a hassle. You can disable preheating by setting the following configuration to `false` in `application.yml`.

```yaml
warmup:
  enabled: false
```

## Acknowledgements

The main inspiration for this project comes from: https://github.com/shelltea/warmup-spring-boot-starter

I adapted it for Spring Boot 3 and added preheating features for MyBatis Plus and JPA.
