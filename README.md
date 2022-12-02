## How to build executable jar

```shell
./gradlew fatJar
```

## How to run simulations
# export <parameters>;java -jar build/libs/gatling-example-1.0-SNAPSHOT-all.jar <simulation name(s)> # to run specific simulation(s)

#### examples
```shell
 export lg_id=lg_test_1;  export simulation_name=my_simulation_test_1; export env=debug_env_test_1; export test_type=debug_test_1; export build_id=build_test_1; export environment=https://challengers.flood.io; export duration=121; export ramp_duration=120; export ramp_users=5;java -jar build/libs/gatling-example-1.0-SNAPSHOT-all.jar FloodIoJava FloodIoJava
```

```shell
 export lg_id=lg_test_1;  export simulation_name=my_simulation_test_1; export env=debug_env_test_1; export test_type=debug_test_1; export build_id=build_test_1; export environment=https://challengers.flood.io; export duration=121; export ramp_duration=120; export ramp_users=5;java -jar build/libs/gatling-example-1.0-SNAPSHOT-all.jar FloodIoJava Flood_scala
```
