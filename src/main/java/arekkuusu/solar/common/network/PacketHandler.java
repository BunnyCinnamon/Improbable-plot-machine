/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 ******************************************************************************/
package arekkuusu.solar.common.network;

import arekkuusu.solar.common.lib.LibMod;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar.
 */
public class PacketHandler {

	public static final SimpleNetworkWrapper INSTANCE = new SimpleNetworkWrapper(LibMod.MOD_ID);
	private static int id = 0;

	public static void init() {
		register(QuantumMessage.QuantumMessageHandler.class, QuantumMessage.class, Side.CLIENT);
		register(QuingentilliardMessage.QuingentilliardMessageHandler.class, QuingentilliardMessage.class, Side.SERVER);
	}

	private static <H extends IMessageHandler<M, IMessage>, M extends IMessage> void register(Class<H> handler, Class<M> message, Side side) {
		INSTANCE.registerMessage(handler, message, id++, side);
	}

	public static void sendToAllAround(IMessage message, NetworkRegistry.TargetPoint target) {
		INSTANCE.sendToAllAround(message, target);
	}

	public static void sendTo(IMessage message, EntityPlayerMP player) {
		INSTANCE.sendTo(message, player);
	}

	public static void updatePosition(World world, BlockPos pos) {
		final IBlockState state = world.getBlockState(pos);
		world.notifyBlockUpdate(pos, state, state, 8);
	}

	public static NetworkRegistry.TargetPoint fromWorldPos(World world, BlockPos pos, int range) {
		return new NetworkRegistry.TargetPoint(world.provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range);
	}

	public static NetworkRegistry.TargetPoint fromTileEntity(TileEntity te, int range) {
		return new NetworkRegistry.TargetPoint(te.getWorld().provider.getDimension(), te.getPos().getX(), te.getPos().getY(), te.getPos().getZ(), range);
	}

	public static NetworkRegistry.TargetPoint fromEntity(Entity entity, int range) {
		return new NetworkRegistry.TargetPoint(entity.world.provider.getDimension(), entity.posX, entity.posY, entity.posZ, range);
	}
}
