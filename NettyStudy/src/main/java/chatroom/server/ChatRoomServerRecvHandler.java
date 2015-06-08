package chatroom.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class ChatRoomServerRecvHandler extends ChannelInboundHandlerAdapter{
	
	@Override 
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		System.out.println("From client: " + msg);
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		cause.printStackTrace();
		ctx.close();
	}
	
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx){
		String welcome = "hello, how are you?\r\n";
		ctx.writeAndFlush(welcome);
	}
}
