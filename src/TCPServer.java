// package ch.epfl.compnet;
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

    /*
        Sends the response to the client: file statistics
     */
    private static void send_response(DataOutputStream out, String message) throws IOException {
        assert message != null && out != null;

        Map<String, Integer> occurrences = getOccurrences(message);
        int num_values = occurrences.size();

        String response = ("There are " + num_values + " unique words in the document \n");

        for (String key: occurrences.keySet()) {
            String word = key.toString();
            String times = occurrences.get(key).toString();
            response = response.concat(word + ": " + times + "\n");
        }
        out.writeBytes(response);

    }


    private static void handleConnection(Socket connectionSocket) throws IOException {

            DataInputStream inFromClient = null;
            DataOutputStream outToClient = null;


            // Open the input-output streams
            try{
                System.out.println("debug: opening streams");

                inFromClient = new DataInputStream(connectionSocket.getInputStream());
                outToClient = new DataOutputStream(connectionSocket.getOutputStream());
            }catch(IOException s){
                System.out.println("I/O Error");
            }
            // Read the file contents into message
            assert inFromClient != null;
            byte[] bytearray = inFromClient.readAllBytes();

            String message = new String(bytearray);

            // Call the response handler
            send_response(outToClient, message);
//        inFromClient.close();
//        outToClient.close();


    }
    
    public static void main(String args[]) throws IOException {
    //@TODO
        while (true){
            
        }


    }
}