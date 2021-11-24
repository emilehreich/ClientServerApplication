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
    
    private static boolean sendFile(DataOutputStream os, String filename) throws IOException{

        boolean retval;

        if (filename.isEmpty()) {
            retval = false;
            
        } else {
            retval = true;
            // Send the file itself
            FileInputStream fis = getFileReader(filename);
            if (fis != null){
                int length = getFileLength(filename);
                byte[] content = fis.readNBytes(length);
                os.write(content);

                System.out.println("sent");
//                retval = length == transferred;
            }else{
                retval = false;
            }
        }
            
        return retval;
    }
    private static void handleResponse(DataInputStream inFromServer) throws IOException {
        
//        int num_values = 5;
//        System.out.println("There are " + num_values + " unique words in the document,→ \n");
//        for (int i = 0; i < num_values; i++) {
//            // Read the length of the word
//            int length = inFromServer.
//            // Allocate a big enough buffer for the word
//            byte[] bytearray = new byte[length];
//            // Actually read the word and convert it to a string
//
//            String word = new String(bytearray);
//            // Read the number of occurrences
//            int times = ...
//            System.out.println(word + ": " + times);
//        }
        assert inFromServer != null;
        byte[] in = inFromServer.readAllBytes();
        System.out.println(new String(in));


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
            clientSocket = new Socket("localhost",port);
            inFromUser = new BufferedReader(new InputStreamReader(System.in));
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new DataInputStream(clientSocket.getInputStream());
            System.out.println("Connected to server");

            System.out.print("Enter a file name: ");
            String filename = inFromUser.readLine();
            sendFile(outToServer, filename);
            handleResponse(inFromServer);
//            do {
//                System.out.print("Enter a file name: ");
//                String filename = inFromUser.readLine();
//
//                // sendfile will notify us whether this is the final file or not
//                repeatFlag = sendFile(outToServer, filename);
//                if (repeatFlag == true) {
//                    // If we didn't send a file,
//                    // we don't need to wait for a response
//                    handleResponse(inFromServer);
//            }while(repeatFlag == true);
//
        } catch (IOException ioex) {
            System.out.println("Failed to process request : " + ioex.getMessage());
        } finally {
            //Close all input/output/sockets
            assert inFromServer != null;
            inFromServer.close();
            inFromUser.close();
            outToServer.close();
        }

    }
//    }

}