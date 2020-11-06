# SimpleCovidStats

SimpleCovidStats is a Twitter scrapper that gathers and displays info about COVID-19 cases in Poland using Polish Ministry of Health Twitter [account](https://twitter.com/MZ_GOV_PL). It is designed in a way to display data in a very simple text format so it can be viewed from any web browser as well as web browsing tools like [curl](https://curl.haxx.se) 


## Use

Application handles only one endpoint with parameter `/days/{days}` which specifies the number of days back the app should be run against (1 is only for today). Currently application is avaiable on covidsimple.pl
![gif](https://github.com/gosu94/SimpleCovidStats/blob/master/scs.gif?raw=true)

## Requirements

**BUILD**

- [Java development kit >= 8](https://www.oracle.com/pl/java/technologies/javase/javase-jdk8-downloads.html)

**RUN**

- [Java runtime environment >= 8](https://www.java.com/pl/download/)

## Build and run

To build this application by yourself you can use 

`chmod +x gradlew && ./gradlew build on Linux and MacOS`

`gradlew build on Windows`

The .jar file will be build into `build/libs/` folder