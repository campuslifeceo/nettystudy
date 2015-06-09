package test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;


public class TestPipelineOrderServer {
	
	
	public void serve(int port) throws InterruptedException{
		
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		
		try{
			ServerBootstrap b = new ServerBootstrap();
			b.group(boss, worker)
			.channel(NioServerSocketChannel.class)
			.childHandler(new ChannelInitializer<SocketChannel>(){

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					// TODO Auto-generated method stub
					ChannelPipeline pp = ch.pipeline();
					pp.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
					pp.addLast(new StringDecoder());
					pp.addLast(new StringEncoder());
					pp.addLast(new OrderChannelHandler());
					pp.addLast(new OrderChannelHandler2());
					pp.addLast(new OrderChannelHandler3());
					
				}
				
			});
		
			ChannelFuture f = b.bind(port).sync();
			
			
			f.channel().closeFuture().sync();
		} finally{
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
		
		
		
	}
	
	public static void main(String [] args) throws InterruptedException{
		new TestPipelineOrderServer().serve(8080);
	}
}

class OrderChannelHandler extends ChannelInboundHandlerAdapter{
	@Override
	public void channelActive(ChannelHandlerContext ctx){
		ctx.writeAndFlush("hello1, " + ctx.channel().remoteAddress() + "\n\r");
		
		//ctx.fireChannelActive();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		String nextMsg = msg + "1";
		ctx.fireChannelRead(nextMsg);
	}
}

class OrderChannelHandler2 extends ChannelInboundHandlerAdapter{
	@Override
	public void channelActive(ChannelHandlerContext ctx){
		ctx.writeAndFlush("hello2, " + ctx.channel().remoteAddress() + "\n\r");
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		String nextMsg = msg + "2";
		ctx.fireChannelRead(nextMsg);
	}
}

class OrderChannelHandler3 extends ChannelInboundHandlerAdapter{
	@Override
	public void channelActive(ChannelHandlerContext ctx){
		ctx.writeAndFlush("hello3, " + ctx.channel().remoteAddress() + "\n\r");
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		String nextMsg = msg + "3";
		ctx.writeAndFlush(nextMsg);
	}
}
