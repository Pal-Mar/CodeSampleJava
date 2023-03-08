package WeatherData;

import WeatherData.UserInterface.UserInterface;
import WeatherData.Logic.SearchLogic;
import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * Program for requesting open data from FMI Open Data service - This version
 * has a command prompt user interface.
 *
 * See https://en.ilmatieteenlaitos.fi/open-data for more information on
 * service.
 *
 * See https://en.ilmatieteenlaitos.fi/open-data-manual-accessing-data on for
 * more information on how data is accessed.
 *
 * !!! Data retrieved from FMI open data service comes with the following
 * licence: "Creative Commons Attribution 4.0 International license (valid as of
 * 1 March 2015). The Creative Commons Attribution 4.0 International license (CC
 * BY 4.0) shall be used by the Finnish Meteorological Institute's open data
 * service. The license shall apply to the FMI's, the Finnish Transport
 * Agency's, and the Radiation and Nuclear Safety Authority's open data sets as
 * well as air quality data sets that are stored in the open data web-service.
 * https://en.ilmatieteenlaitos.fi/open-data-licence" !!!
 *
 * Please note that this program and its source code are NOT released under that
 * licence, please see licence file for more information.
 * 
 * All rights are retained.
 *
 *
 * @author PalMar
 */
public class WeatherDataProject {

    /**
     * Runs WeatherDataProject program. Hands over responsibility to
     * UserInterface and only returns here upon completion or for example
     * network error.
     *
     * @param args the command line arguments - currently not in use
     */
    public static void main(String[] args) {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        SearchLogic logic = new SearchLogic();
        UserInterface userInterface = new UserInterface(reader, logic);

        /* This moves running of program to UserInterface - returns command from
        * userInterface when program has reached unfixable error due to for
        * example network error - and when program has been told to quit
         */
        userInterface.start();
    }
}
