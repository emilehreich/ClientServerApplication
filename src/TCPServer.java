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
            int times = occurrences.get(key);

            // Send the length of the word first
            int length = word.length();
            out.writeInt(length);

            // Then, send the actual word
            byte[] b = word.getBytes();
            out.write(b, 0, length);

            // Finally, send the number of times the word appears
            out.writeInt(times);
        }
        out.writeBytes(response);

    }


    private static void handleConnection(Socket connectionSocket) throws IOException {

            DataInputStream inFromClient = null;
            DataOutputStream outToClient = null;


            // Open the input-output streams
            try{

                // Set reads to timeout after 50 seconds (50000 milliseconds)
                connectionSocket.setSoTimeout(50000);

                inFromClient = new DataInputStream(connectionSocket.getInputStream());
                outToClient = new DataOutputStream(connectionSocket.getOutputStream());


                // This variable controls when the loop should terminate
                boolean repeatFlag = true;

                do {
                    System.out.println("Waiting to receive a file or close after 50 seconds...");
    
                    // Read the length of the file
                    int length = inFromClient.readInt();
                    System.out.println("The file has length: " + length + " bytes");
    
                    if (length == 0) {
                        // Terminate the connection
                        repeatFlag = false;
                    } else {
                        // Read the file contents into message
                        byte[] bytearray = new byte[length];
                        inFromClient.readFully(bytearray);
                        String message = new String(bytearray);
                        System.out.println(message);
    
                        // Call the response handler
                        send_response(outToClient, message);
                    }
                } while (repeatFlag == true);

            }catch(IOException s){
                System.out.println("I/O Error");
            }finally{
                // Close all input/output/sockets
                // The stream-closing operations need to be nested in try-catch blocks
                // Solution inspired by:
                // http://javarevisited.blogspot.ch/2014/10/right-way-to-close-inputstream-file-resource-in-java.html

                try { if (outToClient != null) outToClient.close();
                } catch(IOException e) {}
                try { if (inFromClient != null) inFromClient.close();
                } catch(IOException e) {}
                try { if (connectionSocket != null) connectionSocket.close();
                } catch(IOException e) {}
             }

    }
    
    public static void main(String args[]) {
        ServerSocket welcomeSocket = null;
        Socket connectionSocket = null;

        try {
            // Create a socket that listens to port 6789
            welcomeSocket = new ServerSocket(6789);

            while(true) {
                try {
                    // Get a new connection
                    System.out.println("Waiting for a new client...");
                    connectionSocket = welcomeSocket.accept();
                    
                    // pass the connection socket to the handler
                    System.out.println("Handling new client...");
                    handleConnection(connectionSocket);
                } catch (IOException ioex) {}
            }
        } catch (IOException ioex) {
            System.out.println("Failed to open welcomeSocket : " + ioex.getMessage());
        } finally {
            try { if (welcomeSocket != null) welcomeSocket.close(); } catch(IOException e) {}
        }
    }
}