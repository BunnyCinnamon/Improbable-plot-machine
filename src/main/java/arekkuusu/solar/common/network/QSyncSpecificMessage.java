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

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 01/09/2017.
 * It's distributed as part of Solar.
 */
public class QSyncSpecificMessage implements IMessage {

	private List<ItemStack> stacks;
	private UUID uuid;

	public QSyncSpecificMessage(){}

	public QSyncSpecificMessage(UUID uuid) {
		this.stacks = SolarApi.getQuantumStacks(uuid);
		this.uuid = uuid;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		uuid = beef.readUniqueId();
		//Stacks
		int size = beef.readInt();
		stacks = new ArrayList<>(size);
		for(int i = 0; i < size; i++) {
			try {
				stacks.add(beef.readItemStack());
			} catch(IOException e) {
				Solar.LOG.error("[Sync Specific Message] Failed to read ItemStack from bytes for UUID: ", uuid.toString());
				e.printStackTrace();
			}
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		beef.writeUniqueId(uuid);
		beef.writeInt(stacks.size());
		for(ItemStack stack : stacks) {
			beef.writeItemStack(stack);
		}
	}

	public static class QSyncSpecificMessageHandler implements IMessageHandler<QSyncSpecificMessage, IMessage> {

		@Override
		@Nullable
		@SuppressWarnings("MethodCallSideOnly")
		public IMessage onMessage(QSyncSpecificMessage message, MessageContext ctx) {
			if(ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() ->
						SolarApi.QUANTUM_ITEMS.replace(message.uuid, message.stacks)
				);
			}
			return null;
		}
	}
}
