package WeatherData.Logic;


import java.util.Scanner;

/**
 * Class performs analysis of data from GetFeature request to FMI server. Class
 * in current code-sample state takes only one type of query as input (in XML-
 * formatting and returns a user-friendly output. In a larger program, the
 * compilation of output string should be in its own class (for example
 * OutputBuilder), but functionality left here for sake of brevity.
 *
 * @author PalMar
 */
public class Parser {

    private String parsedMessage;
    private int rowsInSet;

    /**
     * Constructor for Parser. Output aka parsedMessage initialized, class also
     * initializes a counter for output purposes. Counter keeps track of how
     * many rows of data are printed so date is appended at correct intervals.
     * Could be implemented without rowsInSet-counter, but requires additional
     * functionality and kept as such for sake of brevity of code-sample.
     */
    public Parser() {
        this.rowsInSet = 0;
        this.parsedMessage = "";
    }

    /**
     * Method parses content of GetFeature request - performs checks for whether
     * to include line or not in output. First line appended to output is
     * TimeStamp information about when request for data was made. "Showing
     * result"- check indicates if line contains time observation, is printed
     * only once for every data set with same observation time. All other lines
     * with observations added to output.
     *
     * @param bodyOfMessage GetFeature request data in XML-style
     */
    public void parseMessage(String bodyOfMessage) {
        Scanner scanner = new Scanner(bodyOfMessage);
        String answer = "";
        String previousLine = ""; // used for storing previous line for output
        String output = "";

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            answer = parseLine(line, previousLine);

            if (answer.contains("Showing result") & rowsInSet == 1) {
                output = output + "\n" + answer + "\n";
            } else if (answer.contains("Showing result") & rowsInSet != 1) {
            } else if (true != answer.equals("")) {
                output = output + "\n" + answer;
            }
            previousLine = line;

            if (rowsInSet == 6) {
                output = output + "\n";
                rowsInSet = 0;
            }
        }
        scanner.close();
        this.parsedMessage = output;
    }

    /**
     * Performs a check whether line to parse is one of the ones selected for
     * inclusion in parsedMessage = output. This method selects lines that
     * contain timeStamp, time, snow, tmin, tmax, rrday and tday. Here
     * corresponding real-world meaning are hard coded, but could be retrieved
     * from a saved list, if one was to create this correspondence from the data
     * available from FMI - outside the scope of this code sample.
     *
     * @param line current line
     * @param previousLine used to evaluate whether current line is to be sent
     * for parsing or not
     * @return line in parsed format or empty String if no formatting was done
     */
    private String parseLine(String line, String previousLine) {
        if (line.contains("timeStamp")) {
            return parseTimeStampLineContent(line);
        } else if (line.contains("<BsWfs:Time>")) {
            return parseTimeLineContent(line);
        } else if (previousLine.contains("snow")) {
            return parseLineContent(previousLine, line, "Snow Depth", "cm");
        } else if (previousLine.contains("tmin")) {
            return parseLineContent(previousLine, line, "Minimum temperature", "C");
        } else if (previousLine.contains("tmax")) {
            return parseLineContent(previousLine, line, "Maximum temperature", "C");
        } else if (previousLine.contains("rrday")) {
            return parseLineContent(previousLine, line, "Amount of precipitation", "C");
        } else if (previousLine.contains("tday")) {
            return parseLineContent(previousLine, line, "Average air temperature", "C");
        } else {
            return "";
        }
    }

    /**
     * Method parses any line that contains 'timeStamp', returns only contained
     * date and time. Example of line: timeStamp=\"2023-01-03T18:02:59\" =
     * timeStamp=\"[YYYY-DD-MM]T[HH-MM-SS]Z]". Output is in form
     * "2023-01-03T18:02:59Z" = "[YYYY-DD-MM]T[HH-MM-SS]Z]".
     *
     * @param Line in String in format: timeStamp=\"[YYYY-DD-MM]T[HH-MM-SS]Z]".
     * @return Line in String in format: "Weather data retrieved on [date] at
     * [time]"
     */
    private String parseTimeStampLineContent(String line) {
        String[] splitLine = line.split("\"");
        String[] dateTimeLine = splitLine[1].split("T");
        return "Weather data retrieved on " + dateTimeLine[0] + " at " + dateTimeLine[1];
    }

    /**
     * Method parses any line with tag <BsWfs:Time>. Line is in the format
     * <BsWfs:Time>YYYY-DD-MMTHH:MM:SSZ</BsWfs:Time> and from this the date and
     * time information is taken as such. Example of parsed and formatted line:
     * "Showing results for 2023-03-03T00:00:00Z".
     *
     * @param line Line with "<BsWfs:Time>2023-03-03T00:00:00Z</BsWfs:Time>\n"
     * @return Returns line in form "Showing results for [dateAndTime]"
     */
    private String parseTimeLineContent(String line) {
        rowsInSet = rowsInSet + 1;
        String[] splitLine = line.split("<|>");
        return "Showing results for " + splitLine[2];
    }

    /**
     * This method parses the lines containing observation data, returns the
     * desired values in a String[]. Parsed using "<|>" as regex for splitting
     * lines. Example of lines parsed: line =
     * <BsWfs:ParameterName>tday</BsWfs:ParameterName>\n previousLine =
     * <BsWfs:ParameterValue>1.7</BsWfs:ParameterValue>\n
     *
     * @param previousLine Line with parameter name, such as tmin or snow, see
     * https://opendata.fmi.fi/meta?observableProperty=observation&language=eng
     * or
     * https://opendata.fmi.fi/meta?observableProperty=observation&parameter=snow&language=eng
     * for further details
     * @param line Contains actual observation, for example temperature or snow
     * height
     * @param explanation Textual description of observation type, for example
     * "snow depth"
     * @param unit Textual representation of observation unit, for example "cm"
     * @return Information on observation in desired output format
     */
    private String parseLineContent(String previousLine, String line,
            String explanation, String unit) {

        String[] parsedLine = new String[10];
        String[] splitPreviousLine = previousLine.split("<|>");
        String[] splitLine = line.split("<|>");

        parsedLine[0] = splitPreviousLine[2];
        parsedLine[1] = splitLine[2];

        return buildParsedLine(parsedLine[0], explanation, parsedLine[1], unit);
    }

    /**
     * This method builds the individual output line according to desired
     * format. Using snow depth as an example: "Observation 'snow' indicating
     * Snow Depth is 11.0 cm" = "Observation [parameter] indicating
     * [observation} is [measurement] [unit}"
     *
     * @param parameter Observation type in GetFeature format, e.g. "snow"
     * @param explanation Observation description as per FMI information, e.g.
     * "Snow Depth"
     * @param measurement Observation data, e.g. "11.0"
     * @param unit Observation unit, as per FMI information, e.g. "cm"
     * @return One line containing: "Observation [parameter] indicating
     * [observation} is [measurement] [unit}"
     */
    private String buildParsedLine(String parameter, String explanation, String measurement, String unit) {
        return "Observation '" + parameter + "' indicating " + explanation + " is " + measurement + unit;
    }

    /**
     * Returns parsed version of GetFeature request content.
     *
     * @return Message in predetermined user-friendly format
     */
    public String getParsedMessage() {
        return parsedMessage;
    }
}
