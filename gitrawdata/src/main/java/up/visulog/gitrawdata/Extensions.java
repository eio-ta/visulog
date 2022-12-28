package up.visulog.gitrawdata;

import com.google.gson.Gson;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

/**
 * This class represents the extension with their percentage
 */
public class Extensions {
    /**
     * <b>extensions</b>  is the result of extension as HashMap<"Name of extension",percentage>
     */
    HashMap<String,Double> extensions = new HashMap<String,Double>();

    /**
     * <b>extensions</b> Getter
     * @return the <b>HashMap</b> representing the extension name with its percentage
     */
    public HashMap<String, Double> getExtensions() {
        return extensions;
    }

    /**
     * This function changes from the JSON file created in <b>APIreponse</b> to a Java Object which here is <b>Extensions</b>
     * @param privateToken is a String that describes your private token to auth in your project
     * @param idProject is an int that describes the id of your project
     * @return a <b>HashMap</b> representing the extension name with its percentage
     */
    @SuppressWarnings("unchecked")
    public HashMap<String,Double> parseExtensionsFromLog(String privateToken, int idProject){
        //APIresponse to get the extensions from a project
        APIresponse languages = new APIresponse();
        //sending the request to the API
        languages.createLogFileForExtensions(privateToken,idProject);
        //Using GSON to parse the json file
        try (Reader reader = new FileReader("../GitLog/resultsExtensions.json")) {
            extensions = new Gson().fromJson(reader, HashMap.class);
            } catch (IOException e) {
            e.printStackTrace();
        }
        return extensions;
    }

    /**
     * Change the <b>Extensions</b> to a JSON formatted String
     * @return a string that contains the <b>Extensions</b> with their parameters
     */
    @Override
    public String toString() {
        return "Extensions{" +
                "extensions=" + extensions +
                '}';
    }
}
