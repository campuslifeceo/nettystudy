package chatroom.server;
  
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatRoomServer {
	
	private final int port;
	
	public ChatRoomServer(int port){
		this.port = port;
	}
	
	
	public void serve() throws InterruptedException, IOException{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			ServerBootstrap b = new ServerBootstrap();
			
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChatRoomServerInitializer())
			.option(ChannelOption.SO_BACKLOG, 128)
			.childOption(ChannelOption.SO_KEEPALIVE, true);
			
			Channel ch = b.bind(port).sync().channel();
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
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		
		
	}
	
	
	public static void main(String[] args) throws InterruptedException, IOException {
		// TODO Auto-generated method stub
		
		int port;
		if(args.length != 1){
			port = 8080;
			
		}else{
			port = Integer.parseInt(args[0]);
			
		}
		
		
		System.out.println("Server port: " + port);
		new ChatRoomServer(port).serve();
	}

}
