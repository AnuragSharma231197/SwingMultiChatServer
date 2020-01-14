/*
 * Client.java
 *
 * @author Dr Richard Jiang; minor edits by Dr Mark C. Sinclair
 * @version 21 Oct 2019
 */

import java.util.*;
import java.awt.Frame;
import java.io.*;
import java.net.*;
import javax.swing.*;

public class Client extends JFrame
{
	private static final long serialVersionUID = 10L;
	private String server, username;
	private JTextField serverAddress;
	private JTextField serverPort;
    private int port;
    private Socket clientsocket;
    private ObjectInputStream  sInput;
    private ObjectOutputStream sOutput;
    private ArrayList<String> chatList;
    private Frame frame;
    private myGUIClient cg;

    public Client(String server, int port, String username, myGUIClient cg) {
        this.server   = server;
        this.port     = port;
        this.username = username;
        this.cg=cg;
        chatList = new ArrayList<String>();
        
    }

    //Display an event (not a message) to the console
    //!!!Note - Revise it to GUI Style. Refer to myGUI in server side
    private void displayEvent(String msg) {
        chatList.add(msg);
        //display events in GUI
        cg.appendEvent(new Date().toString() + "\n"+msg);

    }

    //Display a message to the console
    //!!!Note - Revise it to GUI Style. Refer to myGUI in server side
    private void displayMsg(String msg) {
        chatList.add(msg);
        chatSave(msg);
        //display events in GUI
        cg.appendRoom(msg);     // append in the room window
    }

    //!!!Note - add your code here to output the chat list to log file
    private void chatSave(String str) {
    	 try{
    		 FileOutputStream out=new FileOutputStream(this.username+".txt", true);
    		 out.write(str.getBytes());
    		 out.flush();
    		 out.close();
    	 } 
    	 catch (IOException e) {
             // do nothing
         }
    }
    
    public ArrayList<String> readPreviousChat(String username) 
    {
    	ArrayList<String> list=new ArrayList<String>();
    	
    	try 
    	{
    		BufferedReader br=new BufferedReader(new FileReader(username+".txt"));
    		//StringBuilder build=new StringBuilder("");
    		String line;
    		while((line = br.readLine()) != null)
    		{
    		    list.add(line);
    		}
    		br.close();
    	}
    	catch(IOException e) 
    	{
    		ArrayList<String> random=new ArrayList<String>();
    		random.add("Chats Not Found");
    		
    		return random;
    	}
    	
    	return list;
    }
    
    public boolean run() {
        //Try to connect to server
        try {
            clientsocket = new Socket(server, port);
        } 
        // if it failed not much I can so
        catch(Exception ec) {
            displayEvent("Error connectiong to server:" + ec);
            return false;
        }
        String msg = "Connection accepted " + clientsocket.getInetAddress() + ":" +
            clientsocket.getPort();
        displayEvent(msg);

        // Creating both Data Stream
        try {
            sInput  = new ObjectInputStream(clientsocket.getInputStream());
            sOutput = new ObjectOutputStream(clientsocket.getOutputStream());
        } catch (IOException eIO) {
            displayEvent("Exception creating new Input/output Streams: " + eIO);
            return false;
        }

        // creates the Thread to listen from the server 
        new RunClientThread().start();

        // Send our username to the server this is the only message that we
        // will send as a String. All other messages will be ChatMessage objects
        try {
            sOutput.writeObject(username);
        } catch (IOException eIO) {
            displayEvent("Exception doing login : " + eIO);
            disconnect();
            return false;
        }
        // success we inform the caller that it worked
        return true;
    }

    class RunClientThread extends Thread {
        public void run() {
            while(true) {
                try {
                    String msg = (String) sInput.readObject();
                    displayMsg(msg);
                } catch(IOException e) {
                    displayEvent("Server has close the connection: " + e);
                }
                // can't happen with a String object but need the catch anyhow
                catch(ClassNotFoundException e2){}
            }
        }
    }

    /*
     * To send a message to the server
     */

    void sendMessage(String msg) {
        try {
            sOutput.writeObject(msg);
        } catch(IOException e) {
            displayEvent("Exception writing to server: " + e);
        }
    }

    /*
     * When something goes wrong
     * Close the Input/Output streams and disconnect
     * not much to do in the catch clause
     */
    private void disconnect() {
        try { 
            if (sInput != null) 
                sInput.close();
        } catch(Exception e) {
            // do nothing
        } 
        try {
            if (sOutput != null) 
                sOutput.close();
        } catch(Exception e) {
            // do nothing
        } 
        try {
            if (clientsocket != null) 
                clientsocket.close();
        } catch(Exception e) {
            // do nothing
        }

        //!!!Note - You may notify your GUI when closed.
        //cg.connectionFailed();
    }               
}
