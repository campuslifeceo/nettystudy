package example.securechat;

import java.net.InetAddress;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.util.concurrent.GlobalEventExecutor;

public class SecureChatServerHandler extends ChannelInboundHandlerAdapter{
	
	static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	
	@Override
	public void channelActive(final ChannelHandlerContext ctx){
		ctx.pipeline().get(SslHandler.class).handshakeFuture().addListener(
				new GenericFutureListener<Future<Channel>>(){

					@Override
					public void operationComplete(Future<Channel> future)
							throws Exception {
						// TODO Auto-generated method stub
						String welcome = "Welcome to " + InetAddress.getLocalHost().getHostName() + " secure chat. \r\n";
						welcome += "Your session is secured by " + 
									ctx.pipeline().get(SslHandler.class).engine().getSession().getCipherSuite() + " .\r\n";
						welcome += "Current alive channels: " + channels.size() + "\n\r";
						
						ctx.writeAndFlush(welcome);
						
						channels.add(ctx.channel());
						
						
					}
					
				});
	}
	
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg){
		System.out.println(msg);
		for(Channel ch : channels){
			if(ch != ctx.channel()){
				ch.writeAndFlush("[" +ch.remoteAddress()+"]: " + msg.toString().trim() + "\n");
			}else{
				ch.writeAndFlush("[" +"You"+"]: " + msg.toString().trim() + "\n");
			}
		}
		
		if("bye".equalsIgnoreCase(msg.toString().trim())){
			ctx.close();
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause){
		cause.printStackTrace();
		ctx.close();
	}
}
