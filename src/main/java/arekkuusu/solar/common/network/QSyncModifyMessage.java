/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.network;

import arekkuusu.solar.api.quantum.EntanglementHelper;
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
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 18/07/2017.
 * It's distributed as part of Solar.
 */
public class QSyncModifyMessage implements IMessage {

	private UUID uuid;
	private ItemStack stack;
	private int slot;

	public QSyncModifyMessage() {}

	public QSyncModifyMessage(UUID uuid, ItemStack stack, int slot) {
		this.uuid = uuid;
		this.stack = stack;
		this.slot = slot;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		uuid = beef.readUniqueId();
		try {
			stack = beef.readItemStack();
		} catch(IOException e) {
			Solar.LOG.error("[Sync Modify Message] Failed to read ItemStack from bytes for UUID: ", uuid.toString());
			e.printStackTrace();
		}
		slot = beef.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		beef.writeUniqueId(uuid);
		beef.writeItemStack(stack);
		beef.writeInt(slot);
	}

	public static class QSyncModifyMessageHandler implements IMessageHandler<QSyncModifyMessage, IMessage> {

		@Nullable
		@Override
		@SuppressWarnings("MethodCallSideOnly")
		public IMessage onMessage(QSyncModifyMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT && message.stack != null) {
				Minecraft.getMinecraft().addScheduledTask(() ->
						EntanglementHelper.setQuantumAsync(message.uuid, message.stack, message.slot)
				);
			}
			return null;
		}
	}
}
