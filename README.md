# warmup-springboot3-starter

## 这是什么?

这是一个能帮你优化 springboot3 首次请求处理耗时的开源库。

## 快速开始

在你的 pom.xml 文件中加入以下依赖：

```xml
<dependency>
    <groupId>com.baizhukui</groupId>
    <artifactId>warmup-springboot3-starter</artifactId>
    <version>1.0.0</version>
</dependency>
```

这就是你需要做的全部事情！

## 我们做了什么？

我们发现 springboot 应用的第一次请求处理耗时明显高于第二次以及之后的请求。

对于一个简单的 springboot + jpa 的应用，第一次请求的处理耗时可以达到第二次的数倍甚至十倍。在一些大规模的项目中，第一次请求的处理耗时可能会更久。

无论你是那个碰巧在程序启动后第一个访问的用户，还是测试人员，长达数秒的等待时间肯定都是非常糟糕的体验。

所以，我们决定编写一个库，通过在应用程序启动后模拟一些http请求、数据库查询请求等行为实现对应用的预热，预热过后应用的首次请求处理耗时可以大幅降低（对于我自己的小项目来说，降低了约50%）。

目前，我们预热了以下场景：

- http 请求中的 json 解析和参数校验
- mybatis plus 和 jpa 的查询

当然，你也可以通过自己编写一些代码来做到这一点。

## 配置

对于本地开发自测这种频繁启动的场景，预热所带来的启动耗时增加可能是个麻烦，你可以通过在 application.yml 中将以下配置项设为false来关闭预热。

```yaml
warmup:
  enabled: false
```

## 鸣谢

本项目的主要灵感来源于：https://github.com/shelltea/warmup-spring-boot-starter

我在其基础上对 springboot3 进行了适配，并增加了mybatis plus 和 jpa的预热功能。







