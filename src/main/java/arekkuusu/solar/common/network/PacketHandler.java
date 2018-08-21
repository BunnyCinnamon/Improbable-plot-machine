/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.network;

import arekkuusu.solar.api.capability.inventory.EntangledIItemHandler;
import arekkuusu.solar.common.block.tile.TilePhenomena;
import arekkuusu.solar.common.lib.LibMod;
import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar.
 */
public class PacketHandler {

	public static final List<IPacketHandler> HANDLERS = Lists.newArrayList();

	public static final IPacketHandler QUANTUM_SYNC_ALL = (compound, context) -> {
		NBTTagList tags = (NBTTagList) compound.getTag("tags");
		for(NBTBase nbt : tags) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			UUID uuid = tag.getUniqueId("uuid");
			EntangledIItemHandler.getEntanglement(uuid).deserializeNBT(tag);
		}
	};

	public static final IPacketHandler QUANTUM_SYNC = (compound, context) -> {
		UUID uuid = compound.getUniqueId("uuid");
		EntangledIItemHandler.getEntanglement(uuid).deserializeNBT(compound);
	};

	public static final IPacketHandler QUANTUM_CHANGE = (compound, context) -> {
		ItemStack stack = new ItemStack((NBTTagCompound) compound.getTag("stack"));
		UUID uuid = compound.getUniqueId("uuid");
		int slot = compound.getInteger("slot");
		EntangledIItemHandler.setEntanglementStack(uuid, stack, slot);
	};

	public static final IPacketHandler PHENOMENA = (compound, context) -> {
		BlockPos pos = new BlockPos(
				compound.getInteger("x"),
				compound.getInteger("y"),
				compound.getInteger("z")
		);
		World world = Minecraft.getMinecraft().player.world;
		if(world.isValid(pos) && world.isBlockLoaded(pos)) {
			TileEntity tile = Minecraft.getMinecraft().player.world.getTileEntity(pos);
			if(tile instanceof TilePhenomena) {
				((TilePhenomena) tile).makePhenomenon();
			}
		}
	};

	public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(LibMod.MOD_ID);
	private static int id = 0;

	public static void init() {
		register(ServerToClientPacket.Handler.class, ServerToClientPacket.class, Side.CLIENT);
		register(ClientToServerPacket.Handler.class, ClientToServerPacket.class, Side.SERVER);

		HANDLERS.add(QUANTUM_SYNC_ALL);
		HANDLERS.add(QUANTUM_SYNC);
		HANDLERS.add(QUANTUM_CHANGE);
		HANDLERS.add(PHENOMENA);
	}

	private static <H extends IMessageHandler<M, IMessage>, M extends IMessage> void register(Class<H> handler, Class<M> message, Side side) {
		NETWORK.registerMessage(handler, message, id++, side);
	}
}
