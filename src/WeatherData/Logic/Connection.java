package WeatherData.Logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class provides connection to server and returns potential response For
 * testing purposes, depends on HttpURLconnection for connection.
 * Class provides functionality for creating a connection and then reading 
 * any potential message inside the received response.
 *
 * @author PalMar
 */
public class Connection {

    private String bodyOfMessage;
    private HttpURLConnection connection;
    private String connectionStatus;
/**
 * Constructor for Connection class, initializes necessary variables.
 */
    public Connection() {
        this.bodyOfMessage = "";
        this.connectionStatus = "";
    }

    /**
    *  Attempt connection to specified URL, here only GetFeature-requests to
    * the FMI server with predetermined search parameters and chosen location.
    * @param query URL used for connection - built in SearchLogic buildQuery-method
     */
    public void connectToServer(String query) {
        URL url = null;
        try {
            url = new URL(query);
            // url = new URL(testUrl);
        } catch (MalformedURLException ex) {
            System.out.println("Network Error");
            connectionStatus = "error";
        }

        try {
            connection = (HttpURLConnection) url.openConnection();
            connectionStatus = connection.getResponseCode() + " " + connection.getResponseMessage();
            readInputStream();
            connection.disconnect();
        } catch (IOException e) {
            System.out.println("IOException occurred");
            connectionStatus = "error";
        }
    }

    /**
     * Reads input stream from established connection and GetFeature request. 
     * Takes entire message and stores in one String - stored in class variable.
     *  While data received is in XML-style, it is received and processed as a 
     * String for the purposes of this program. While functionality for creating 
     * an XML-file out of the inputStream exists, it was interesting to parse 
     * the data as a String instead of using predefined keywords and imported 
     * functionality.
     */
    private void readInputStream() {
        BufferedReader reader = null;
        String wholeMessage = "";
        try {
            if (connection.getResponseCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String strCurrentLine;
                while ((strCurrentLine = reader.readLine()) != null) {
                    wholeMessage = wholeMessage + "\n" + strCurrentLine;
                }
            } else {
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
                String strCurrentLine;
                while ((strCurrentLine = reader.readLine()) != null) {
                    wholeMessage = wholeMessage + strCurrentLine;
                }
            }
        } catch (Exception e) {
            System.out.println("File Error - try again");
        }
        this.bodyOfMessage = wholeMessage;
    }

    /**
     * Returns information on status of connection. Consists of response-code
     * and response message from HTTPUrlConnection-class - see relevant
     * documentation for more information on content of message.
     *
     * @return Status of connection
     */
    public String getConnectionStatus() {
        return this.connectionStatus;
    }

    /**
     * This method returns any message received from a GET request. While data
     * received is in XML-format, it is received and processed as a String for 
     * the purposes of this program. While functionality for creating XML-file 
     * out of inputStream exists, it was interesting to parse the data as a 
     * String instead of using predefined keywords and imported functionality.
     *
     * @return content of GET request from server as String
     */
    public String getBodyOfMessage() {
        return this.bodyOfMessage;
    }
}
