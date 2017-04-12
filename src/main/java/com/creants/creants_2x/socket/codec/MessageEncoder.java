package com.creants.creants_2x.socket.codec;

import com.creants.creants_2x.socket.gate.entities.CASObject;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author LamHa
 *
 */
public class MessageEncoder extends MessageToByteEncoder<CASObject> {

	@Override
	protected void encode(ChannelHandlerContext ctx, CASObject message, ByteBuf out) throws Exception {
		try {
			out.writeBytes(Unpooled.copiedBuffer(message.toBinary()));
		} catch (Exception e) {
			throw new RuntimeException("Invalid messsage");
		}

	}

}
