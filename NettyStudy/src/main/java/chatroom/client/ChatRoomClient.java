package chatroom.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class ChatRoomClient {

	public void client(String host, int port) throws InterruptedException{
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try{
			Bootstrap b= new Bootstrap();
			
			b.group(workerGroup)
				.channel(NioSocketChannel.class)
				.option(ChannelOption.SO_KEEPALIVE, true)
				.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch){
						ch.pipeline().addLast(new ChatRoomClientHandler());
					}
				});
			
			ChannelFuture f = b.connect(host,port).sync();
			
			f.channel().closeFuture().sync();
			
		} finally{
			workerGroup.shutdownGracefully();
		}
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
