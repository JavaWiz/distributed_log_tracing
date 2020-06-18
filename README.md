## Distributed Tracing in Micoservices using Sleuth and Zipkin

### What is Distributed Tracing ?
One of the major challenges in microservices is the ability to debug issues and monitor them. A simple action can trigger a chain of microservice calls and it would be tedious to trace these actions across the invoked microservices. This is because each microservice runs in an environment isolated from other microservices so they don’t share resources such as databases or log files. In addition to that, we might also want to track down why a certain microservice call is taking so much time in a given business flow.
The Distributed Tracing pattern addresses the above challenges developers face while building microservices. There are some helpful open-source tools that can be used for distributed tracing, when creating microservices with Spring Boot and Spring Cloud frameworks.

### Spring Cloud Sleuth
Spring Cloud Sleuth provides Spring Boot auto-configuration for distributed tracing.

Sleuth configures everything you need to get started. This includes where trace data (spans) are reported to, how many traces to keep (sampling), if remote fields (baggage) are sent, and which libraries are traced.

Specifically, Spring Cloud Sleuth…​

* Adds trace and span ids to the Slf4J MDC, so you can extract all the logs from a given trace or span in a log aggregator.

* Instruments common ingress and egress points from Spring applications (servlet filter, rest template, scheduled actions, message channels, feign client).

* If spring-cloud-sleuth-zipkin is available then the app will generate and report Zipkin-compatible traces via HTTP. By default it sends them to a Zipkin collector service on localhost (port 9411). Configure the location of the service using spring.zipkin.baseUrl.

### Spring Boot Config
Add Sleuth to your classpath:

```
<dependencies>
    <dependency>
        <groupId>org.springframework.cloud</groupId>
        <artifactId>spring-cloud-starter-sleuth</artifactId>
    </dependency>
</dependencies>
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-dependencies</artifactId>
            <version>${release.train.version}</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```
As long as Spring Cloud Sleuth is on the classpath any Spring Boot application will generate trace data.

In distributed tracing the data volumes can be very high so sampling can be important.This determines what amount of data you want to send to a centralized log analysis tool. If you want to send all the data or only a part of it. If you are exporting span data to Zipkin or Spring Cloud Stream, there is also an AlwaysSampler that exports everything and a PercentageBasedSampler that samples a fixed fraction of spans. We will be making use of the Always Sampler for this example.

### Zipkin

Zipkin is a Java-based distributed tracing system. It helps gather timing data needed to troubleshoot latency problems in service architectures. Features include both the collection and lookup of this data.

If you have a trace ID in a log file, you can jump directly to it. Otherwise, you can query based on attributes such as service, operation name, tags and duration. Some interesting data will be summarized for you, such as the percentage of time spent in a service, and whether or not operations failed.
### How do they work together ?
When the Orchestrator Service makes a HTTP call on the service, the call is intercepted by Sleuth and it adds the necessary tags to the request headers. After the Orchestrator Service receives the HTTP response, the data is sent asynchronously to Zipkin to prevent delays or failures relating to the tracing system from delaying or breaking the flow.
Sleuth adds two types of IDs to the log file, one called a trace ID and the other called a span ID. The span ID represents a basic unit of work, for example sending an HTTP request. The trace ID contains a set of span IDs, forming a tree-like structure. The trace ID will remain the same as one microservice calls the next.

### Installing Zipkin
Starting an instance of Zipkin for checking out Zipkin locally, there are three options: using Java, Docker or running from source.

Regardless of how you start Zipkin, browse to http://your_host:9411 to find traces!

We can refer [ zipkin quickstart ](https://zipkin.io/pages/quickstart.html)

In all the pom.xml add the spring cloud zipkin dependency as follows-

```
 <dependency>
	<groupId>org.springframework.cloud</groupId>
	<artifactId>spring-cloud-starter-zipkin</artifactId>
</dependency>
```

Also from the application.properties remove the property we had defined earlier - spring.zipkin.enabled=false
