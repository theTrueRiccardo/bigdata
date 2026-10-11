# Beijing's taxi trajectories big data analyticis with Spark

This repository contains a Spark project that realizes a big data dashboard for
taxi trajectories in Beijing.

The project naturally divides into three parts:
1. Per-taxi analysis
2. Multidimensional analysis
3. Clustering

The dataset can be downloaded from https://www.kaggle.com/datasets/arashnic/tdriver

## Architecture
The project it's a web application that runs jobs on Spark engine. An application server, built with Play framework, is responsible for accepting requests from the UI. These requests trigger computations on Spark, for which the driver program resides in the application server itself. As such, Spark it's a compile time dependency for the application server.

## Requirements
The project uses different technologies to run. Specifically:
- Java as the platform on which the backend application runs
- Scala as the language for the backend
- Play as a framework to create application servers 
- SBT (https://www.scala-sbt.org/download/) to build and run Play framework
- Spark to run the analytics (https://spark.apache.org/downloads.html)

Ideally, one would use the highest versions for all the technologies involved, but that's not possible unfortunately. Spark doesn't support, at the present day (2026-10-10), a version of Scala higher than 2.13.18. Moreover, sbt build tool cannot
correctly retrieve artifacts of the Play framework if the version is higher than 1.9.6. This implies using an old version
of Scala and an old version of sbt, but all the rest can be at latest version (Spark, Play, Java).
