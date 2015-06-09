package example.securechat;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.ssl.SslContext;

public class SecureChatServerHandlerInitializer extends ChannelInitializer<SocketChannel>{

	private final SslContext ssl;
	public SecureChatServerHandlerInitializer(SslContext ssl){
		this.ssl = ssl;
	}
	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
		
		ChannelPipeline pp = ch.pipeline();
		pp.addLast(ssl.newHandler(ch.alloc()));
		pp.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pp.addLast(new StringDecoder());
		pp.addLast(new SecureChatServerHandler());
	}
	
}
