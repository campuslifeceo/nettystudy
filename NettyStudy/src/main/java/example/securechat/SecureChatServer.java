package example.securechat;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class SecureChatServer {
	final static int PORT = Integer.parseInt(System.getProperty("port", "8023"));
	
	public static void main(String [] args) throws CertificateException, SSLException, InterruptedException{
		SelfSignedCertificate ssc = new SelfSignedCertificate();
		
		SslContext ssl = SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
		
		EventLoopGroup boss = new NioEventLoopGroup();
		EventLoopGroup worker = new NioEventLoopGroup();
		
		try{
			ServerBootstrap b = new ServerBootstrap();
			
			b.group(boss, worker)
				.channel(NioServerSocketChannel.class)
				.handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new SecureChatServerHandlerInitializer(ssl));
			
			b.bind(PORT).sync().channel().closeFuture().sync();
		} finally{
			boss.shutdownGracefully();
			worker.shutdownGracefully();
		}
	}
}
