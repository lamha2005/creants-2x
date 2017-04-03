package com.creants.creants_2x.socket.codec;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import com.creants.creants_2x.socket.gate.wood.Message;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author LamHa
 *
 */
public class MessageEncoder extends MessageToByteEncoder<Message> {

	private static final int HEADER_LENGTH = 6;

	@Override
	protected void encode(ChannelHandlerContext ctx, Message message, ByteBuf out) throws Exception {
		try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
				DataOutputStream bodyDOS = new DataOutputStream(bos)) {
			// write command
			bodyDOS.writeShort(message.getCommandId());

			List<Short> keyList = message.getKeyList();
			byte[] body = new byte[] {};
			if (keyList != null && keyList.size() > 0) {
				for (int i = 0, size = keyList.size(); i < size; i++) {
					byte[] value = message.getBytesAt(i);
					bodyDOS.writeShort(keyList.get(i).shortValue());
					bodyDOS.writeInt(value.length);
					bodyDOS.write(value);
				}
				bodyDOS.flush();
			}
			body = bos.toByteArray();

			ByteBuf data = Unpooled.buffer(HEADER_LENGTH + body.length);
			data.writeByte(message.getProtocolVersion());
			data.writeInt(body.length);
			data.writeBoolean(message.isEncrypt());
			data.writeBytes(body);

			out.writeBytes(data);
		} catch (IOException e) {
			throw new RuntimeException("Invalid messsage");
		}

	}

}
