## Basic Web Server

### Building and running
Run "./gradlew jar" to build jar
Run java -jar simplewebserver-0.1.jar

Alternatively import into Intellij Idea and run main

### About
Main thread in BasicWebServer accepts new sockets and pushes into a BlockingQueue.
Then we have a ThreadedQueueConsumer which takes sockets from the queue, passes to a handler  and sends to an ExecutorService.
The ExecutorService executes (currently in CachedThreadPool).

It is possible to implement a number of different handlers to process the request.
Currently a basic and not fully implemented file-based handler is in place. It only is capable of handling GET.
As an example, a handler that communicates with a relational database could be implemented and its class could then be passed
when instantiating BasicWebServer.
The FileSystemRequestHandler is able to handle keep-alive and when this flag is seen, the socket is sent back to the queue rather than being closed.

The ApplicationConfiguration and LoggingService are built with a similar approach.
Both are singletons that receive implementations of their respective interfaces.
This allows us to use these without passing them around, while still having the benefit of flexible implementations.
The application configuration currently comes from a config file, but we could call a REST endpoint or a database.


#### References
    - https://medium.com/@ssaurel/create-a-simple-http-web-server-in-java-3fc12b29d5fd
    - https://stackoverflow.com/questions/23962359/thread-pool-in-a-web-server-in-java