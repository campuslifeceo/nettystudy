package chatroom;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

public class ChatRoomServerHandler extends ChannelInboundHandlerAdapter{
	
	@Override 
	public void channelRead(ChannelHandlerContext ctx, Object msg){
//		ByteBuf in = (ByteBuf) msg;
//		try{
//			
//			System.out.print(in.toString(CharsetUtil.UTF_8));
//		} finally{
//			in.release();
//		}
		
		ctx.write(msg);
		ctx.flush();
		
	}
}
