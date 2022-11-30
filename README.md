## How to build executable jar

```shell
./gradlew fatJar
```

## How to run simulations
# java -jar <parameter(s)> build/libs/gatling-example-1.0-SNAPSHOT-all.jar <simulation name(s)> # to run specific simulation(s)

#### examples
```shell
 java -jar -Dramp_users=5 -Dramp_duration=120 -Dduration=121 -Denvironment=https://challengers.flood.io build/libs/gatling-example-1.0-SNAPSHOT-all.jar FloodIoJava
```

```shell
 java -jar -Dramp_users=5 -Dramp_duration=120 -Dduration=121 -Denvironment=https://challengers.flood.io build/libs/gatling-example-1.0-SNAPSHOT-all.jar Flood_scala
```
