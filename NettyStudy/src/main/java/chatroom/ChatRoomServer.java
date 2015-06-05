package chatroom;
  
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class ChatRoomServer {
	
	private final int port;
	
	public ChatRoomServer(int port){
		this.port = port;
	}
	
	
	public void serve() throws InterruptedException{
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			ServerBootstrap b = new ServerBootstrap();
			
			b.group(bossGroup, workerGroup)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				public void initChannel(SocketChannel ch){
					ch.pipeline().addLast(new ChatRoomServerRecvHandler());
					ch.pipeline().addLast(new ChatRoomServerSendHandler());
				}
			});
			
			ChannelFuture f = b.bind(port).sync();
			f.channel().closeFuture().sync();
		} finally{
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}
		
		
	}
	
	
	public static void main(String[] args) throws InterruptedException {
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
