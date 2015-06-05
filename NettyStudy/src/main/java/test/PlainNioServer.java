package test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class PlainNioServer {
	
	public void serve(int port) throws IOException{
		System.out.println("Listening for connections on port " + port);
		
		ServerSocketChannel serverChannel;
		Selector selector;
		
		
		serverChannel = ServerSocketChannel.open();
		ServerSocket ss = serverChannel.socket();
		InetSocketAddress address = new InetSocketAddress(port);
		ss.bind(address);
		serverChannel.configureBlocking(false);
		selector = Selector.open();
		serverChannel.register(selector, SelectionKey.OP_ACCEPT);
		final ByteBuffer msg = ByteBuffer.wrap("Hi,\r\n".getBytes());
		
		
		while(true){
			
			selector.select();
			
			Set<SelectionKey> readyKeys = selector.selectedKeys();
			Iterator<SelectionKey> it = readyKeys.iterator();
			
			while(it.hasNext()){
				SelectionKey key = it.next();
				it.remove();
				try{
					if(key.isAcceptable()){
						ServerSocketChannel server = (ServerSocketChannel) key.channel();
						SocketChannel client = server.accept();
						System.out.println("Accepted connection from " + client);
						client.configureBlocking(false);
						client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
					}
					
					if(key.isWritable()){
						SocketChannel client = (SocketChannel) key.channel();
						ByteBuffer buffer = (ByteBuffer) key.attachment();
						
						while(buffer.hasRemaining()){
							if(client.write(buffer) == 0){
								break;
							}
							client.close();
						}
					}
				} catch(IOException e){
					key.cancel();
					try{
						key.channel().close();
					} catch(IOException cex){
						
					}
				}
			}
		}
		
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		new PlainNioServer().serve(8080);
	}

}
