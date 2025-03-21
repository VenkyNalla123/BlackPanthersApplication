
# Use Tomcat as the base image
FROM tomcat:9.0

# Copy the generated WAR to Tomcat's webapps directory
COPY target/black-panthers-registration-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war

# Expose the default Tomcat port
EXPOSE 8080

# Run Tomcat when the container starts
CMD ["catalina.sh", "run"]
