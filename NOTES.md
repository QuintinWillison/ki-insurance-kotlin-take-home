# Candidate Notes

## Initial Observations

The [README Notes](README.md#notes) indicate that the code supplied has been written against Kotlin `1.6.0-RC2` which is relatively old (prior to November 2021) and 'just' a release-candidate - this could probably do with updating - at the time of writing this observation, either to

- `1.6.21` (April 2022); or
- `1.8.0` (December 2022)

When I initially ran `mvn package` either from command line (version `3.8.1` via ASDF) or from IntelliJ (Bundled Maven `3.8.1`) then I was getting ominous build warning:

```
[INFO] --- kotlin-maven-plugin:1.6.0-RC2:compile (compile) @ ki-takehome-kotlin ---
[WARNING] No sources found skipping Kotlin compile
```

Followed by realisation that the built jar has a different filename to what was outlined in the readme, plus it doesn't work:

```
kotlin_take-home % java -jar target/ki-takehome-kotlin-1.0-SNAPSHOT.jar card_payments_example.csv card 1.20
Error: Could not find or load main class com.ki.LocalRunner
```
