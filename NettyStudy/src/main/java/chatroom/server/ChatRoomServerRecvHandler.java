package chatroom.server;

import java.util.HashSet;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class ChatRoomServerRecvHandler extends ChannelInboundHandlerAdapter{
	
	
	
	@Override 
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		System.out.println("Client " + ctx.channel().remoteAddress().toString() + " :" + msg);
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		cause.printStackTrace();
		ctx.close();
	}
	
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx){
		
		String welcome = "hello, " + ctx.channel().remoteAddress().toString() + "\r\n";
		
		ctx.writeAndFlush(welcome);
	}
}
