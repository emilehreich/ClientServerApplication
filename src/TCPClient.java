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
        int length = (int) file.length();

        return length;
    }
    
    private static void printHashMap(Map<String, Integer> occurrences) {
        for (String name: occurrences.keySet()) {
            String key = name.toString();
            String value = occurrences.get(name).toString();

            System.out.println(key + " " + value);
        }
    
    }
    
    private static boolean sendFile(DataOutputStream os, String filename) throws IOException{
        if (filename.isEmpty()) {
            retval = false;
            
        } else {
            retval = true;
            // Send the file itself
            FileInputStream fis = getFileReader(filename);
            if (fis != null){
                os.send(fis)
            }else{
                retval = false;
            }
        }
            
        return retval;
    }

    public static void main(String argv[]) {

        Socket clientSocket = null;
        BufferedReader inFromUser = null;
        DataOutputStream outToServer = null;
        DataInputStream inFromServer = null;
        Boolean repeatFlag;

        try {
            // Connect to the local server at 6789
            private int local_server = 6790
            clientSocket = new Socket("local Server", local_server)
            inFromUser = ...
            outToServer = new DataOutputStream(clientSocket.getOutputStream());
            inFromServer = new DataInputStream(clientSocket.getInputStream());
            System.out.println("Connected to server");

            do {
                System.out.print("Enter a file name: ");
                String filename = inFromUser.readLine();

                // sendfile will notify us whether this is the final file or not
                repeatFlag = sendFile(outToServer, filename);
                if (repeatFlag == true) {
                    // If we didn't send a file,
                    // we don't need to wait for a response
                    handleResponse(inFromServer);
            }while(repeatFlag == true);

        } catch (IOException ioex) {
            System.out.println("Failed to process request : " + ioex.getMessage());
        } finally {
            //Close all input/output/sockets
        ...
        }
        
        }
    }
}