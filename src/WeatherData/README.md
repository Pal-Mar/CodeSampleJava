# Introduction
Program for requesting open data from the Finnish Meteorological Institute (FMI) Open Data service. This version has a command prompt user interface.

- See https://en.ilmatieteenlaitos.fi/open-data for more information on service.
- See https://en.ilmatieteenlaitos.fi/open-data-manual-accessing-data on for more information on how data is accessed.

Current code-sample version uses predetermined use-cases, also called "StoredQueries". The test-case for this version is "daily weather observations" with a preset time, and is of the form "fmi::observations::weather::daily::simple". Certain parameters are preset for brevity. 

The program builds the query for the data request based on user selections, gets the requested data from the FMI server and finally parses the response (XML) into a user-friendly output.

# Program output example
**Welcome to the WeatherApp!** (Code Sample version)

This program allows you to access the FMI Open weather data

========================================

Now let's get started!

Here are your options:
- search - searches for data from FMI
- quit - ends this program
 
*search (USER INPUT)*

.

Search for data from Helsinki, Hanko or Lohja

*Helsinki (USER INPUT)*

*Print entire XML-file - not shown in this README*

.

Here is the data in XML-format -  do you wish to parse it? Yes / No

*yes (USER INTPUT)*

Weather data retrieved on 2023-03-05 at 00:00:00Z

Showing results for 2023-03-03T00:00:00Z

- Observation 'rrday' indicating Amount of precipitation is 0.7C
- Observation 'tday' indicating Average air temperature is 1.7C
- Observation 'snow' indicating Snow Depth is 11.0cm
- Observation 'tmin' indicating Minimum temperature is -0.2C
- Observation 'tmax' indicating Maximum temperature is 4.6C

.

Here are your options:
- search - searches for data from FMI
- quit - ends this program
 
*quit (USER INPUT)*

*Program ends*


# Final notes
## NOTE AND DISCLAIMER

Data retrieved from FMI open data service comes with the following licence: "Creative Commons Attribution 4.0 International license (valid as of 1 March 2015). The Creative Commons Attribution 4.0 International license (CC BY 4.0) shall be used by the Finnish Meteorological Institute's open data service. The license shall apply to the FMI's, the Finnish Transport Agency's, and the Radiation and Nuclear Safety Authority's open data sets as well as air quality data sets that are stored in the open data web-service. https://en.ilmatieteenlaitos.fi/open-data-licence" !!!

## LICENCE

Please note that this program and its source code are NOT released under that licence, please see licence file for more information. All rights are retained.
