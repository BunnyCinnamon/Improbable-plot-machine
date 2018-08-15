/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.handler.data;

import arekkuusu.solar.common.lib.LibMod;
import com.google.common.collect.Sets;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

import java.util.Set;

/**
 * Created by <Arekkuusu> on 10/12/2017.
 * It's distributed as part of Solar.
 */
public class WorldMonolithData extends WorldSavedData {

	private static final String NAME = LibMod.MOD_ID + ":monolith_data";
	private static final String MONOLITH_LIST = "monoliths";

	private final Set<BlockPos> monoliths = Sets.newHashSet();

	public WorldMonolithData(String name) {
		super(name);
	}

	@SuppressWarnings("ConstantConditions")
	public static WorldMonolithData get(World world) {
		MapStorage storage = world.getMapStorage();
		WorldMonolithData data = (WorldMonolithData) storage.getOrLoadData(WorldMonolithData.class, NAME);

		if(data == null) {
			data = new WorldMonolithData(NAME);
			storage.setData(NAME, data);
		}

		return data;
	}

	public boolean isMonolithChunk(BlockPos pos) {
		BlockPos chunk = new BlockPos(pos.getX() >> 4, 0, pos.getZ() >> 4);
		return monoliths.contains(chunk);
	}

	public void addMonolithChunk(BlockPos pos) {
		BlockPos chunk = new BlockPos(pos.getX() >> 4, 0, pos.getZ() >> 4);
		monoliths.add(chunk);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		NBTTagList monoliths = (NBTTagList) compound.getTag(MONOLITH_LIST);
		monoliths.forEach(nbt -> {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			this.monoliths.add(new BlockPos(tag.getInteger("x"), 0, tag.getInteger("z")));
		});
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		NBTTagList monoliths = new NBTTagList();
		for(BlockPos monolith : this.monoliths) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setInteger("x", monolith.getX());
			tag.setInteger("z", monolith.getZ());
		}
		compound.setTag(MONOLITH_LIST, monoliths);
		return compound;
	}
}
