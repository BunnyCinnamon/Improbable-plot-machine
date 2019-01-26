package arekkuusu.implom.api.capability.data;

import com.google.common.collect.Lists;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.INBTSerializable;

import javax.annotation.Nullable;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

@INBTData.NBTHolder(modId = "improbableplotmachine", name = "positions_nbt")
public class PositionsNBTData implements INBTData<NBTTagList> {

	public final List<Position> positions = Lists.newLinkedList();

	@Override
	public void deserialize(NBTTagList nbt) {
		positions.clear();
		for(NBTBase tag : nbt) {
			Position data = new Position(this);
			data.deserializeNBT((NBTTagCompound) tag);
			positions.add(data);
		}
	}

	@Override
	public NBTTagList serialize() {
		NBTTagList list = new NBTTagList();
		for(Position position : positions) {
			list.appendTag(position.serializeNBT());
		}
		return list;
	}

	@Override
	public boolean canDeserialize() {
		return !positions.isEmpty();
	}

	public static class Position implements INBTSerializable<NBTTagCompound> {

		private final PositionsNBTData data;

		public Position(PositionsNBTData data) {
			this.data = data;
		}

		public Position(BlockPos pos, EnumFacing facing, World world) { //For dummies only
			this.pos = pos;
			this.facing = facing;
			this.worldId = world.provider.getDimension();
			this.data = null;
		}

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
			data.markDirty();
		}

		public void setPos(BlockPos pos) {
			this.pos = pos;
			data.markDirty();
		}

		@Nullable
		public BlockPos getPos() {
			return pos;
		}

		public void setFacing(@Nullable EnumFacing facing) {
			this.facing = facing;
			data.markDirty();
		}

		@Nullable
		public EnumFacing getFacing() {
			return facing;
		}

		@Override
		public void deserializeNBT(NBTTagCompound tag) {
			this.pos = new BlockPos(tag.getInteger("x"), tag.getInteger("y"), tag.getInteger("z"));
			this.worldId = tag.getInteger("worldId");
			if(tag.hasKey("facing")) {
				this.facing = EnumFacing.byName(tag.getString("facing"));
			} else this.facing = null;
		}

		@Override
		public NBTTagCompound serializeNBT() {
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

		@Override
		public boolean equals(Object o) {
			if(this == o) return true;
			if(o == null || getClass() != o.getClass()) return false;
			Position position = (Position) o;
			return Objects.equals(pos, position.pos) &&
					facing == position.facing &&
					Objects.equals(worldId, position.worldId);
		}

		@Override
		public int hashCode() {
			return Objects.hash(pos, facing, worldId);
		}
	}
}
