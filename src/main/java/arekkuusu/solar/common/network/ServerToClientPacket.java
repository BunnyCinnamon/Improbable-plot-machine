/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.network;

import arekkuusu.solar.common.Solar;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 10/12/2017.
 * It's distributed as part of Solar.
 */
public class ServerToClientPacket implements IMessage {

	private NBTTagCompound data;
	private IPacketHandler handler;

	public ServerToClientPacket() {

	}

	public ServerToClientPacket(IPacketHandler handler, NBTTagCompound data) {
		this.handler = handler;
		this.data = data;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		try {
			handler = PacketHandler.HANDLERS.get(buf.readInt());
			data = beef.readCompoundTag();
		} catch(Exception e) {
			Solar.LOG.error("[Packet] Failed to receive packet");
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		try {
			buf.writeInt(PacketHandler.HANDLERS.indexOf(handler));
			beef.writeCompoundTag(data);
		} catch(Exception e) {
			Solar.LOG.error("[Packet] Failed to send packet");
			e.printStackTrace();
		}
	}

	public static class Handler implements IMessageHandler<ServerToClientPacket, IMessage> {

		@Override
		@Nullable
		public IMessage onMessage(ServerToClientPacket message, MessageContext ctx) {
			Minecraft.getMinecraft().addScheduledTask(() -> {
				if(message.data != null && message.handler != null) {
					message.handler.handleData(message.data, ctx);
				}
			});
			return null;
		}
	}
}
