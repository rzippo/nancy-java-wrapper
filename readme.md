# Nancy wrapper for Java

This project provides a Java library that provides (some of) the functionalities of the [Nancy library](https://github.com/rzippo/nancy) for Deterministic Network Calculus.

The library interacts via HTTP requests with a [nancy-http](https://github.com/rzippo/nancy-http) server.
The host and port of the server can be configured via the `NancyHttpServer` class.

For an example of usage, see [here](nancy-wrapper/nancy-wrapper-test/src/main/java/it/unipi/nancy/test/Main.java).