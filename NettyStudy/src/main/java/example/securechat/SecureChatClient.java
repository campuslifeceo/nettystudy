package example.securechat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.net.ssl.SSLException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

public class SecureChatClient {

	//static final String HOST = System.getProperty("host", "127.0.0.1");
	static final int PORT = Integer.parseInt(System.getProperty("port", "8023"));
	public static final String host = "10.58.183.107";

	public void serve() throws InterruptedException, IOException{
		final SslContext ssl = SslContextBuilder.forClient()
				.trustManager(InsecureTrustManagerFactory.INSTANCE).build();
		
		EventLoopGroup group = new NioEventLoopGroup();
		
		try{
			Bootstrap b = new Bootstrap();
			
			b.group(group)
				.channel(NioSocketChannel.class)
				.handler(new SecureChatClientInitializer(ssl));
			
			Channel ch = b.connect(host, PORT).sync().channel();
			
			ChannelFuture f = null;
			
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try{
				while(true){
					String tmp = br.readLine();
					if(tmp != null){
						f = ch.writeAndFlush(tmp + "\n\r");
					} else{
						break;
					}
					
					
					if("bye".equalsIgnoreCase(tmp)){
						ch.closeFuture().sync();
						break;
					}
					
					

					if (f != null){
						f.sync();
					}
				}
			}finally{
				
				if(br != null){
					br.close();
				}

			}
			
		} finally{
			group.shutdownGracefully();
		}
	}
	
	public static void main(String[] args) throws InterruptedException, IOException  {
		// TODO Auto-generated method stub
		
		new SecureChatClient().serve();
	}

}
