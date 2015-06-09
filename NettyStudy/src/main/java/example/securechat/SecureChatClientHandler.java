package example.securechat;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class SecureChatClientHandler extends ChannelInboundHandlerAdapter{

	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		System.err.println(msg);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		cause.printStackTrace();
		ctx.close();
	}
}
