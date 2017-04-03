package com.creants.creants_2x.socket.codec;

import java.util.List;

import com.creants.creants_2x.core.event.SystemNetworkConstant;
import com.creants.creants_2x.core.util.DefaultMessageFactory;
import com.creants.creants_2x.socket.gate.wood.Message;
import com.creants.creants_2x.socket.util.ConverterUtil;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * @author LamHa
 *
 */
public class MessageDecoder extends MessageToMessageDecoder<ByteBuf> {
	private static final short INVALID_DATA_LENGTH = 999;
	private static final int HEADER_LENGTH = 6;
	// max 0.5Mb
	public static final int MAX_BODY_LENGTH = 512000;

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		try {
			if (in.readableBytes() < HEADER_LENGTH)
				throw new RuntimeException("Invalid data length");

			in.readByte();// 1byte protocol version
			int bodyLength = in.readInt();
			in.readByte();// 1byte encrypt

			if (bodyLength <= 0) {
				out.add(DefaultMessageFactory.createErrorMessage(SystemNetworkConstant.COMMAND_UNKNOW,
						INVALID_DATA_LENGTH, "Invalid data"));
				in.clear();
				return;
			}

			if (bodyLength > MAX_BODY_LENGTH) {
				out.add(DefaultMessageFactory.createErrorMessage(SystemNetworkConstant.COMMAND_UNKNOW,
						INVALID_DATA_LENGTH, "Invalid data"));
				in.clear();
				throw new RuntimeException("Body length is too large");
			}

			byte[] bodyData = in.readBytes(bodyLength).array();
			Message message = DefaultMessageFactory
					.createMessage(ConverterUtil.convertBytes2Short(new byte[] { bodyData[0], bodyData[1] }));
			int i = 2;
			while (i < bodyLength - 2) {
				try {
					// get key
					short key = ConverterUtil.convertBytes2Short(new byte[] { bodyData[i], bodyData[i + 1] })
							.shortValue();
					int valueLength = ConverterUtil.convertBytes2Integer(
							new byte[] { bodyData[(i + 2)], bodyData[(i + 3)], bodyData[(i + 4)], bodyData[(i + 5)] });

					if (valueLength > bodyLength) {
						throw new RuntimeException(
								"Invalid data, invalid value length:" + valueLength + " for key " + key);
					}

					byte[] value = new byte[valueLength];
					System.arraycopy(bodyData, i + 6, value, 0, valueLength);
					message.putBytes(key, value);
					i += 6 + valueLength;
				} catch (Exception e) {
					in.clear();
					throw new RuntimeException("Invalid data:" + e.getMessage());
				}
			}

			// khi được add vào list sẽ remove
			out.add(message);
			in.clear();
		} catch (Exception e) {
			throw new RuntimeException("Invalid messsage from decoder");
		}

	}

}
