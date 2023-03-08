package WeatherData.Logic;


import WeatherData.Logic.Parser;
import WeatherData.Logic.Connection;


/**
 * Class provides functionality to request data from the Finnish
 * Meteorological Institute Open Data service. See https://en.ilmatieteenlaitos.fi/open-data
 * for further information on how to access data sets.
 *
 * Current code-sample version uses predetermined use-cases, also called
 * 'StoredQueries' in the form of "fmi::observations::weather::daily::simple"
 * which is the test-case for this version, see 
 *
 * @author PalMar
 */
public class SearchLogic {

    private Connection connection;
    private String bodyOfMessage;
    private Parser parser;
    private final String serverURL = "http://opendata.fmi.fi/wfs?service=WFS&version=2.0.0&request=getFeature&";
    private final String typeOfQuery = "storedquery_id=fmi::observations::weather::daily::simple&";

    /**
     * This method is in charge of requests of data from the FMI server, in this
     * code-sample version certain parameters are preset for brevity. This
     * implementation uses only one type of stored query: daily weather
     * observations and with a preset time - here
     * @param place chosen location in String - here Helsinki, Lohja or Hanko
     */
    public void getDataFromFMI(String place) {
        connection = new Connection();
        String query = buildQuery(place);
        connection.connectToServer(query);
        bodyOfMessage = connection.getBodyOfMessage();
    }

    /**
     * This method builds the query to the server, is a very simple version.
     * This should really be a separate class for separation of concern, but
     * kept here for brevity of this version of the program.
     *
     * @param place chosen location to search for in String
     * @return query in String with parameters for place and time
     */
    private String buildQuery(String place) {
        place = "place=" + place + "&";

        // here parameters for GetFeature-request are given, as outside of scope
        // code sample-version to implement selection of all possible parameters
        String startTime = "starttime=2023-03-03T00:00:00Z&";
        String endTime = "endtime=2023-03-03T05:59:00Z&";
        String query = serverURL + typeOfQuery + place + startTime + endTime;

        /* Three different working testUrl for testing and development purposes.          
        String query = "http://opendata.fmi.fi/wfs?service=WFS&version=2.0.0&request=getFeature&storedquery_id=fmi::observations::weather::daily::simple&place=helsinki&starttime=2023-03-03T00:00:00Z&endtime=2023-03-03T05:59:00Z&";
        String query = "http://opendata.fmi.fi/wfs?service=WFS&version=2.0.0&request=getFeature&storedquery_id=fmi::observations::weather::daily::simple&place=helsinki&";
        String query = "http://opendata.fmi.fi/wfs?service=WFS&version=2.0.0&request=getFeature&storedquery_id=fmi::observations::weather::daily::simple&";
         */
        return query;
    }

    /**
     * Instantiates a new Parser requests a parsed version of the XML-type
     * response received from Connection. 
     */
    public void parseInput() {
        parser = new Parser();
        parser.parseMessage(bodyOfMessage);
    }

    /**
     * Returns the parsed message to the user interface.
     * @return Information from request in a easy-to-read format
     */
    public String getParsedMessage() {
        return parser.getParsedMessage();
    }

    /**
     * Returns the message in the original XML-style format.
     *
     * @return XML-style message in String
     */
    public String getBodyOfMessage() {
        return this.bodyOfMessage;
    }

    /**
     * Returns the status of the attempted connection
     * @return current status of connection
     */
    public String getConnectionStatus() {
        return this.connection.getConnectionStatus();
    }
}
