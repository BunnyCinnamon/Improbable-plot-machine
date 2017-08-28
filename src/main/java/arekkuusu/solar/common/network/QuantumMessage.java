/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.network;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.common.Solar;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.io.IOException;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 18/07/2017.
 * It's distributed as part of Solar.
 */
public class QuantumMessage implements IMessage {

	private ItemStack stack = ItemStack.EMPTY;
	private UUID uuid;

	public QuantumMessage() {
	}

	public QuantumMessage(UUID uuid, ItemStack stack) {
		this.uuid = uuid;
		this.stack = stack;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		uuid = beef.readUniqueId();
		try {
			stack = beef.readItemStack();
		} catch(IOException e) {
			Solar.LOG.warn("[Quantum Message] Failed to load ItemStack, please report this issue!");
			e.printStackTrace();
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		beef.writeUniqueId(uuid);
		beef.writeItemStack(stack);
	}

	public static class QuantumMessageHandler implements IMessageHandler<QuantumMessage, IMessage> {

		@Override
		public IMessage onMessage(QuantumMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					//SolarApi.setQuantumItem(message.uuid, message.stack);
				});
			}
			return null;
		}
	}
}
