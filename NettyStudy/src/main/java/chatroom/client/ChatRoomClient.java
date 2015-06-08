package chatroom.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatRoomClient {

	public void client(String host, int port) throws InterruptedException, IOException{
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			Bootstrap b= new Bootstrap();
			
			b.group(workerGroup)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChatRoomClientInitializer());
			
			Channel ch = b.connect(host, port).sync().channel();
			
			ChannelFuture f = null;
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			while(true){
				String line = br.readLine();
				
				if(line == null){
					break;
				}
				
				f = ch.writeAndFlush(line + "\r\n");
				
				if("bye".equals(line.toLowerCase())){
					ch.closeFuture().sync();
					break;
				}
			}
			
			if(f != null){
				f.sync();
			}
			
		} finally{
			workerGroup.shutdownGracefully();
		}
	}
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		new ChatRoomClient().client("localhost", 8080);
	}

}
