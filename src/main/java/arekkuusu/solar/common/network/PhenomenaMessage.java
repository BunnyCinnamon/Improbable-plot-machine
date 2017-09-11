/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.network;

import arekkuusu.solar.common.block.tile.TilePhenomena;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 18/07/2017.
 * It's distributed as part of Solar.
 */
public class PhenomenaMessage implements IMessage {

	private BlockPos pos;

	public PhenomenaMessage() {}

	public PhenomenaMessage(BlockPos pos) {
		this.pos = pos;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		pos = beef.readBlockPos();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		beef.writeBlockPos(pos);
	}

	public static class PhenomenaMessageHandler implements IMessageHandler<PhenomenaMessage, IMessage> {

		@Nullable
		@Override
		@SuppressWarnings("MethodCallSideOnly")
		public IMessage onMessage(PhenomenaMessage message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT && message.pos != null) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					TileEntity tile = Minecraft.getMinecraft().player.world.getTileEntity(message.pos);
					if(tile != null && tile instanceof TilePhenomena) {
						((TilePhenomena) tile).makePhenomenon();
					}
				});
			}
			return null;
		}
	}
}
