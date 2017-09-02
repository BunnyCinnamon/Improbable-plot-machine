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
import java.util.*;

/**
 * Created by <Arekkuusu> on 30/08/2017.
 * It's distributed as part of Solar.
 */
public class QSyncAllMessage implements IMessage {

	private Map<UUID, List<ItemStack>> map;

	public QSyncAllMessage(){
		this.map = new HashMap<>();
	}

	public QSyncAllMessage(Map<UUID, List<ItemStack>> map){
		this.map = map;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		//Read Data
		int mapSize = beef.readInt();
		for(int i = 0; i < mapSize; i++) {
			//Key
			UUID key = beef.readUniqueId();
			//Stacks
			int stacksSize = beef.readInt();
			List<ItemStack> stacks = new ArrayList<>(stacksSize);
			for(int j = 0; j < stacksSize; j++) {
				try {
					stacks.add(beef.readItemStack());
				} catch(IOException e) {
					Solar.LOG.error("[Sync All Message] Failed to read ItemStack from bytes for UUID: ", key.toString());
					e.printStackTrace();
				}
			}
			map.put(key, stacks);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		//Write Data
		beef.writeInt(map.size());
		for(Map.Entry<UUID, List<ItemStack>> entry : map.entrySet()) {
			//Key
			beef.writeUniqueId(entry.getKey());
			//Stacks
			List<ItemStack> stacks = entry.getValue();
			beef.writeInt(stacks.size());
			stacks.forEach(beef::writeItemStack);
		}
	}

	public static class QSyncAllMessageHandler implements IMessageHandler<QSyncAllMessage, IMessage> {

		@Override
		@Nullable
		@SuppressWarnings("MethodCallSideOnly")
		public IMessage onMessage(QSyncAllMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					SolarApi.QUANTUM_ITEMS.clear();
					SolarApi.QUANTUM_ITEMS.putAll(message.map);
				});
			}
			return null;
		}
	}
}
