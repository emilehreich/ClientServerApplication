/ package ch.epfl.compnet;
import java.util.*;
import java.io.*;
import java.net.*;

public class TCPServer {

    private static Map<String, Integer> getOccurrences(String message) {

        Map<String, Integer> occurrences = new TreeMap<String, Integer>();
        String delimiter_regexp = "[^a-zA-Z]+";
        Scanner fileScan = new Scanner(message).useDelimiter(delimiter_regexp);

        while(fileScan.hasNext()){
            String word = fileScan.next();
            word = word.toLowerCase();

            Integer oldCount = occurrences.get(word);
            if ( oldCount == null ) {
            oldCount = 0;
            }
            occurrences.put(word, oldCount + 1);
        }

        fileScan.close();
        return occurrences;
    }
    

    private static void handleConnection(Socket connectionSocket) {
        DataInputStream inFromClient = null;
        DataOutputStream outToClient = null;

        // Open the input-output streams
        inFromClient = 

        // Read the file contents into message
        byte[] bytearray = ...
        ...
        
        // Call the response handler
        send_response(outToClient, message);
    }
    
    public static void main(String args[]) {
    //@TODO
    }
}