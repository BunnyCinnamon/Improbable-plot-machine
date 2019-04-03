/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.network;

import arekkuusu.implom.api.IPMApi;
import arekkuusu.implom.api.capability.data.ItemStackNBTData;
import arekkuusu.implom.api.capability.data.WorldAccessNBTData;
import arekkuusu.implom.api.capability.nbt.IInventoryNBTDataCapability;
import arekkuusu.implom.api.capability.nbt.IWorldAccessNBTDataCapability;
import arekkuusu.implom.common.block.tile.TilePhenomena;
import arekkuusu.implom.common.lib.LibMod;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
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
 * It's distributed as part of Improbable plot machine.
 */
public class PacketHandler {

	public static final List<IPacketHandler> HANDLERS = Lists.newArrayList();

	public static final IPacketHandler PARTICLE = (((compound, context) -> {

	}));

	public static final IPacketHandler QUANTUM_MIRROR = ((compound, context) -> {
		UUID uuid = compound.getUniqueId("key");
		ItemStack stack = new ItemStack(compound.getCompoundTag("itemstack"));
		ItemStackNBTData data = (ItemStackNBTData) IPMApi.getInstance().dataMap.computeIfAbsent(uuid, (k) -> Maps.newHashMap())
				.computeIfAbsent(IInventoryNBTDataCapability.class, (k) -> new ItemStackNBTData());
		data.setStack(stack);
	});

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

	public static final IPacketHandler MUTATOR = ((compound, context) -> {
		UUID uuid = compound.getUniqueId("key");
		WorldAccessNBTData data = (WorldAccessNBTData) IPMApi.getInstance().dataMap.computeIfAbsent(uuid, (k) -> Maps.newHashMap())
				.computeIfAbsent(IWorldAccessNBTDataCapability.class, (k) -> new WorldAccessNBTData());
		data.deserialize(compound.getCompoundTag("data"));
	});

	public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(LibMod.MOD_ID);
	private static int id = 0;

	public static void init() {
		register(ServerToClientPacket.Handler.class, ServerToClientPacket.class, Side.CLIENT);
		register(ClientToServerPacket.Handler.class, ClientToServerPacket.class, Side.SERVER);
		HANDLERS.add(PHENOMENA);
		HANDLERS.add(QUANTUM_MIRROR);
		HANDLERS.add(MUTATOR);
		HANDLERS.add(PARTICLE);
	}

	private static <H extends IMessageHandler<M, IMessage>, M extends IMessage> void register(Class<H> handler, Class<M> message, Side side) {
		NETWORK.registerMessage(handler, message, id++, side);
	}
}
