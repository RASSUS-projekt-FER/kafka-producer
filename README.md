# Pokretanje
1. Pokretanje Kafke: https://kafka.apache.org/quickstart (prve 2 točke)
1. Pokretanje programa:
  * obični run Device.java sa argumentom <device_id> (npr. 1)
  * naredbom `mvn exec:java -Dexec.args="<device_id>"` putem naredbenog retka


# Make fat jar and run  
mvn clean package  
java -jar .\target\kafka-1.0-jar-with-dependencies.jar 1  

