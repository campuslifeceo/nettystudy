package test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

public class PlainOioServer {
	
	public void serve(int port) throws IOException{
		final ServerSocket socket = new ServerSocket(port);
		
		try{
			while(true){
				final Socket clientSocket = socket.accept();
				
				System.out.println("Accepted socket from " + clientSocket);
				
				new Thread(new Runnable(){
					@Override
					public void run(){
						OutputStream out;
						
						try{
							out = clientSocket.getOutputStream();
							out.write("Hi!\r\n".getBytes(Charset.forName("UTF-8")));
							out.flush();
							clientSocket.close();
						} catch(IOException e){
							e.printStackTrace();
							try{
								clientSocket.close();
							} catch(IOException ex){
								
							}
						}
					}
				}).start();
			}
		} catch(IOException e){
			e.printStackTrace();
		}
	}
	
	
	public static void main(String [] args) throws IOException{
		new PlainOioServer().serve(8080);
	}
}
