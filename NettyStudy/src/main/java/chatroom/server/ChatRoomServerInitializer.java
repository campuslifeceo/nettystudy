package chatroom.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class ChatRoomServerInitializer extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// TODO Auto-generated method stub
		ChannelPipeline pp = ch.pipeline();
		pp.addLast(new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
		pp.addLast(new StringDecoder());
		pp.addLast(new StringEncoder());
		pp.addLast(new ChatRoomServerRecvHandler());
	}
	
}
