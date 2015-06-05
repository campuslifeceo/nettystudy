package test;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;

public class NettyOioServer {

	public void server(int port) throws InterruptedException{
		final ByteBuf buf = Unpooled.unreleasableBuffer(
				Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")));
		
		EventLoopGroup group = new OioEventLoopGroup();
		
		try{
			ServerBootstrap b = new ServerBootstrap();
			
			b.group(group)
				.channel(OioServerSocketChannel.class)
				.localAddress(new InetSocketAddress(port))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch){
						ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
							@Override
							public void channelActive(ChannelHandlerContext ctx){
								ctx.write(buf.duplicate()).addListener(ChannelFutureListener.CLOSE);
							}
						});
					}
				});
			ChannelFuture f = b.bind().sync();
			f.channel().closeFuture().sync();
		} finally{
			group.shutdownGracefully().sync();
		}
	}
	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		new NettyOioServer().server(8080);
	}

}
