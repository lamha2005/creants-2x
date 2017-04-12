package com.creants.creants_2x.socket.gate;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.creants.creants_2x.core.event.SystemNetworkConstant;
import com.creants.creants_2x.core.event.handler.AbstractRequestHandler;
import com.creants.creants_2x.core.event.handler.SystemHandlerManager;
import com.creants.creants_2x.core.util.DefaultMessageFactory;
import com.creants.creants_2x.socket.gate.entities.ICASObject;
import com.creants.creants_2x.socket.gate.wood.ChannelService;
import com.creants.creants_2x.socket.gate.wood.User;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * Class tiếp nhận message từ client. Xử lý business logic.<br>
 * Share giữa các channel giúp giảm thiểu resource (chú ý Channel Handler phải
 * là stateless).<br>
 * inbound là data từ ứng dụng đến server(remote peer)<br>
 * outbound là data từ server(remote peer) đến ứng dụng (ví dụ như hành động
 * write)
 * 
 * @author LamHa
 */
@Sharable
public class MessageHandler extends SimpleChannelInboundHandler<ICASObject> {
	private static final AtomicLong nextSessionId = new AtomicLong(System.currentTimeMillis());

	private SystemHandlerManager systemHandlerManager;
	private static final ChannelService channelService = ChannelService.getInstance();


	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();

		synchronized (nextSessionId) {
			long sessionId = nextSessionId.getAndIncrement();
			User user = channelService.connect(sessionId, channel);
			send(user, DefaultMessageFactory.createConnectMessage(sessionId));
		}

	}


	/*
	 * Chú ý khi xử lý message là có nhiều thread xử lý IO, do đó cố gắng không
	 * Block IO Thread có thể có vấn đề về performance vì phải duyệt sâu đối với
	 * những môi trường throughout cao. Netty hỗ trợ EventExecutorGroup để giải
	 * quyết vấn đề này khi add vào ChannelHandlers. Nó sẽ sử dụng EventExecutor
	 * thực thi tất các phương thức của ChannelHandler. EventExecutor sẽ sử dụng
	 * một thread khác để xử lý IO sau đó giải phóng EventLoop.
	 */
	@Override
	protected void channelRead0(final ChannelHandlerContext ctx, final ICASObject message) throws Exception {
		Channel channel = ctx.channel();
		User user = channelService.getUser(channel);

		String commandId = message.getUtfString("command_id");
		AbstractRequestHandler handler = systemHandlerManager.getHandler(commandId);
		if (handler != null) {
			handler.perform(user, message);
		} else {
		}
	}


	/**
	 * @param receiver
	 *            người nhận
	 * @param message
	 */
	public void send(IUser receiver, final ICASObject message) {
		Channel channel = channelService.getChannel(receiver.getSessionId());
		if (channel != null) {
			ChannelFuture future = channel.writeAndFlush(message);

			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					String cmdId = message.getUtfString(SystemNetworkConstant.KEYS_COMMAND_ID);
				}
			});
		}
	}


	/**
	 * Send cho nhóm user
	 * 
	 * @param receivers
	 *            danh sách người nhận
	 * @param message
	 */
	public void send(List<User> receivers, final ICASObject message) {
		for (IUser receiver : receivers) {
			send(receiver, message);
		}
	}


	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		// flush tất cả những message trước đó (những message đang pending) đến
		// remote peer, và đóng channel sau khi write hoàn thành
		// ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}


	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
	}


	public void removeUser(User user) {
		channelService.getChannel(user.getSessionId()).close();
		channelService.disconnect(user);
	}


	public void setSystemHandlerManager(SystemHandlerManager systemHandlerManager) {
		this.systemHandlerManager = systemHandlerManager;
	}

}
