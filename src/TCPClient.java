// package ch.epfl.compnet;
import java.io.*;
import java.net.*;
import java.util.*;


public class TCPClient {

    private static FileInputStream getFileReader(String filename) {

        FileInputStream fis = null;
        boolean fileExists = true;

        try {
            fis = new FileInputStream(filename);
        } catch (FileNotFoundException e) {
            fileExists = false;
        }

        return fis;
    }
    

    private static int getFileLength(String filename) {

        File file = new File(filename);

        return (int) file.length();
    }
    
    private static void printHashMap(Map<String, Integer> occurrences) {
        for (String name: occurrences.keySet()) {
            String key = name.toString();
            String value = occurrences.get(name).toString();

            System.out.println(key + " " + value);
        }
    
    }
    
    private static void sendBytes(FileInputStream fis, OutputStream os) throws IOException {
        // Construct a 1K buffer to hold bytes on their way to the socket.
        byte[] buffer = new byte[1024];
        int bytes = 0;

        // Copy requested file into the socket's output stream.
        while ((bytes = fis.read(buffer)) != -1) {
            os.write(buffer, 0, bytes);
        }
    }

    private static boolean sendFile(DataOutputStream os, String filename) throws IOException{

        boolean retval;

        if (filename.isEmpty()) {
            retval = false;
            os.writeInt(0);
        } else {
            retval = true;

            // Send the file length
            int length = getFileLength(filename);
            System.out.println("The file has length: " + length + " bytes");
            os.writeInt(length);

            // Send the file itself
            FileInputStream fis = getFileReader(filename);
            sendBytes(fis, os);
        }
            
        return retval;
    }
    private static void handleResponse(DataInputStream inFromServer) throws IOException {
        
        int num_values = inFromServer.readInt();

        System.out.println("There are " + num_values + " unique words in the document \n");

        for (int i = 0; i < num_values; i++) {
            // Read the length of the word
            int length = inFromServer.readInt();

            // Allocate a big enough buffer for the word
            byte[] bytearray = new byte[length];

            // Actually read the word and convert it to a string
            inFromServer.readFully(bytearray);
            String word = new String(bytearray);

            // Read the number of occurrences
            int times = inFromServer.readInt();

            System.out.println(word + ": " + times);
        }
    }

    public static void main(String argv[]) throws IOException {

        Socket clientSocket = null;

        BufferedReader inFromUser = null;
        DataOutputStream outToServer = null;
        DataInputStream inFromServer = null;

        Boolean repeatFlag;

        try {
            // Connect to the local server at 6789
            int port = 6789;
            clientSocket = new Socket("localhost", port);
            inFromUser = new BufferedReader(new InputStreamReader(System.in));
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new DataInputStream(clientSocket.getInputStream());
            do{
                System.out.print("Enter a file name: ");
                String filename = inFromUser.readLine();

                // sendfile will notify us whether this is the final file or not
                repeatFlag = sendFile(outToServer, filename);
                if (repeatFlag == true) {
                    // If we didn't send a file,
                    // we don't need to wait for a response
                    handleResponse(inFromServer);
                }

            } while(repeatFlag == true);


        } catch (IOException ioex) {
            System.out.println("Failed to process request : " + ioex.getMessage());
        } finally {
            // Close all input/output/sockets
            try { if(outToServer != null) outToServer.close(); } catch(IOException e) {}
            try { if(inFromServer != null) inFromServer.close(); } catch(IOException e) {}
            try { if(inFromUser != null) inFromUser.close(); } catch(IOException e) {}
            try { if(clientSocket != null) clientSocket.close(); } catch(IOException e) {}
        }

    }


}