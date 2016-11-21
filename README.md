WebServer
=========

A multi-threaded (e.g. file-based) web server with thread-pooling implemented in Java.


Usage:
java -jar fileserver.jar <port> <numthreads> <webroot>

port: the port at which the server should listen. Default is 8086
numThreads: number of threads allowed. Default number of threads is 10
webroot: the root folder(relative path). Default path is /wwwroot/

Request:
http://localhost8086/helloworld.txt

http://localhost8086/d1/helloworld2.txt