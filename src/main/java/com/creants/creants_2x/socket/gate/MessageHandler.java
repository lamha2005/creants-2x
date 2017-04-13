package com.creants.creants_2x.socket.gate;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import com.creants.creants_2x.core.event.SystemNetworkConstant;
import com.creants.creants_2x.core.event.handler.AbstractRequestHandler;
import com.creants.creants_2x.core.event.handler.SystemHandlerManager;
import com.creants.creants_2x.core.util.DefaultMessageFactory;
import com.creants.creants_2x.core.util.QAntTracer;
import com.creants.creants_2x.socket.gate.entities.IQAntObject;
import com.creants.creants_2x.socket.gate.entities.QAntObject;
import com.creants.creants_2x.socket.gate.wood.ChannelService;
import com.creants.creants_2x.socket.gate.wood.QAntUser;

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
public class MessageHandler extends SimpleChannelInboundHandler<IQAntObject> {
	private static final AtomicLong nextSessionId = new AtomicLong(System.currentTimeMillis());
	private static final ChannelService channelService = ChannelService.getInstance();

	private SystemHandlerManager systemHandlerManager;


	@Override
	public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		Channel channel = ctx.channel();
		synchronized (nextSessionId) {
			long sessionId = nextSessionId.getAndIncrement();
			QAntTracer.debug(this.getClass(), "- create session: " + sessionId);
			QAntUser user = channelService.connect(sessionId, channel);
			QAntObject response = DefaultMessageFactory.createConnectMessage(sessionId);
			QAntTracer.debug(this.getClass(), "- response: " + response.getDump());
			send(user, response);
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
	protected void channelRead0(final ChannelHandlerContext ctx, final IQAntObject message) throws Exception {
		Channel channel = ctx.channel();
		QAntUser user = channelService.getUser(channel);

		String commandId = message.getUtfString(SystemNetworkConstant.KEYS_COMMAND_ID);
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
	public void send(IQAntUser receiver, final IQAntObject message) {
		Channel channel = channelService.getChannel(receiver.getSessionId());
		if (channel != null) {
			ChannelFuture future = channel.writeAndFlush(message);

			future.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					String cmdId = message.getUtfString(SystemNetworkConstant.KEYS_COMMAND_ID);
					QAntTracer.debug(this.getClass(), "- Send command:" + cmdId);
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
	public void send(List<QAntUser> receivers, final IQAntObject message) {
		for (IQAntUser receiver : receivers) {
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


	public void removeUser(QAntUser user) {
		channelService.getChannel(user.getSessionId()).close();
		channelService.disconnect(user);
	}


	public void setSystemHandlerManager(SystemHandlerManager systemHandlerManager) {
		this.systemHandlerManager = systemHandlerManager;
	}

}
