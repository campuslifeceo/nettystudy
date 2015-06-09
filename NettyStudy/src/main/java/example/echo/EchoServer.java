package example.echo;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class EchoServer {
	
	static final int PORT = Integer.parseInt(System.getProperty("port","8080"));
	static final boolean SSL = System.getProperty("ssl") != null;
	public static void main(String[] args) throws CertificateException, SSLException, InterruptedException {
		// TODO Auto-generated method stub
		final SslContext sslCtx;
		
		if(SSL){
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
			
		} else{
			sslCtx = null;
		}
		
		
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		
		try{
			ServerBootstrap b = new ServerBootstrap();
			
			b.group(boss, worker)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				.option(ChannelOption.SO_BACKLOG, 100)
				.childHandler(new ChannelInitializer<SocketChannel>(){

					@Override
					protected void initChannel(SocketChannel ch)
							throws Exception {
						// TODO Auto-generated method stub
						ChannelPipeline pp = ch.pipeline();
						pp.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
						pp.addLast(new StringDecoder());
						pp.addLast(new StringEncoder());
						pp.addLast(new MyChannelHandler());
					}
					
				});
			
			ChannelFuture f = b.bind(PORT).sync();
			f.channel().closeFuture().sync();
		} finally{
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}

}


class MyChannelHandler extends ChannelInboundHandlerAdapter{
	@Override
	public void channelActive(ChannelHandlerContext ctx){
		ctx.writeAndFlush("Hello, welcome " + ctx.channel().remoteAddress() + "\n\r");
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		ctx.write(msg+"\r\n");
	}
	
	@Override 
	public void channelReadComplete(ChannelHandlerContext ctx){
		ctx.flush();
	}
}
