# Beijing's taxi trajectories big data analyticis with Spark

This repository contains a Spark project that realizes a big data dashboard for
taxi trajectories in Beijing.

The project naturally divides into three parts:
1. Per-taxi analysis
2. Multidimensional analysis
3. Clustering

## Architecture
The project it's a web application that runs jobs on Spark engine. An application server, built with Play framework, is responsible of accepting requests from the UI. These requests trigger computations on Spark, for which the driver program resides in the application server itself. As such, Spark is not only another backend component, but also a compile time dependency for the application server.

## Requirements
The project uses different technologies to run. Specifically:
- Java 11 or higher in order to use Play framework
- SBT (https://www.scala-sbt.org/download/) to build and run Play framework
- Spark to run the analytics
