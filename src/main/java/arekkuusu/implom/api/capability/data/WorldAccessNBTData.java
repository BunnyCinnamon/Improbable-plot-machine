package arekkuusu.implom.api.capability.data;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;

@INBTData.NBTHolder(modId = "improbableplotmachine", name = "worldaccess_nbt")
public class WorldAccessNBTData implements INBTData<NBTTagCompound> {

	private BlockPos pos;
	private EnumFacing facing;
	private Integer worldId; //Stores the world id, NOT the world instance, as it can unload when it feels like it
	private WeakReference<World> cacheWorld; //Stores the world until it is unloaded / eaten by dragon lolis

	@Nullable
	public World getWorld() {
		World world = null;
		if(cacheWorld != null) world = cacheWorld.get();
		if(world == null) cacheWorld = new WeakReference<>(DimensionManager.getWorld(worldId));
		return world;
	}

	public void setWorld(World world) {
		this.worldId = world.provider.getDimension();
		this.cacheWorld = null;
		markDirty();
	}

	public void setPos(BlockPos pos) {
		this.pos = pos;
		markDirty();
	}

	@Nullable
	public BlockPos getPos() {
		return pos;
	}

	public void setFacing(@Nullable EnumFacing facing) {
		this.facing = facing;
		markDirty();
	}

	@Nullable
	public EnumFacing getFacing() {
		return facing;
	}

	@Override
	public boolean canDeserialize() {
		return pos != null;
	}

	@Override
	public void deserialize(NBTTagCompound tag) {
		this.pos = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
		this.worldId = tag.getInteger("worldId");
		if(tag.hasKey("facing")) {
			this.facing = EnumFacing.byName(tag.getString("facing"));
		} else this.facing = null;
	}

	@Override
	public NBTTagCompound serialize() {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("x", pos.getX());
		tag.setInteger("y", pos.getY());
		tag.setInteger("z", pos.getZ());
		tag.setInteger("worldId", worldId);
		if(facing != null) {
			tag.setString("facing", facing.getName());
		}
		return tag;
	}
}
