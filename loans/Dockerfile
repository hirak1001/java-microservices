#Start with a base image containing Java runtime
FROM openjdk:22-jdk-slim

# MAINTAINER instruction is deprecated in favor of using label
# MAINTAINER https://github.com/hirak1001
#Information around who maintains the image
LABEL "org.opencontainers.image.authors"="https://github.com/hirak1001"

# Add the application's jar to the image
COPY target/loans-0.0.1-SNAPSHOT.jar loans-0.0.1-SNAPSHOT.jar

# execute the application
ENTRYPOINT ["java", "-jar", "loans-0.0.1-SNAPSHOT.jar"]