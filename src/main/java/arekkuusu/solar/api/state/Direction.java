/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.api.state;

import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Arrays;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Created by <Arekkuusu> on 03/11/2017.
 * It's distributed as part of Solar.
 */
public enum Direction implements IStringSerializable { //Forgive me... 64 in total
	//NONE
	NON(false, false, false, false, false, false),
	//Single
	UP(false, true, false, false, false, false),
	DOWN(true, false, false, false, false, false),
	NORTH(false, false, true, false, false, false),
	SOUTH(false, false, false, true, false, false),
	EAST(false, false, false, false, true, false),
	WEST(false, false, false, false, false, true),
	//Double
	UP_DOWN(true, true, false, false, false, false),
	UP_NORTH(false, true, true, false, false, false),
	UP_SOUTH(false, true, false, true, false, false),
	UP_EAST(false, true, false, false, false, true),
	UP_WEST(false, true, false, false, true, false),
	DOWN_NORTH(true, false, true, false, false, false),
	DOWN_SOUTH(true, false, false, true, false, false),
	DOWN_EAST(true, false, false, false, false, true),
	DOWN_WEST(true, false, false, false, true, false),
	NORTH_SOUTH(false, false, true, true, false, false),
	EAST_WEST(false, false, false, false, true, true),
	NORTH_EAST(false, false, true, false, false, true),
	NORTH_WEST(false, false, true, false, true, false),
	SOUTH_EAST(false, false, false, true, false, true),
	SOUTH_WEST(false, false, false, true, true, false),
	//Triple
	UP_NORTH_SOUTH(false, true, true, true, false, false),
	UP_EAST_WEST(false, true, false, false, true, true),
	UP_NORTH_EAST(false, true, true, false, false, true),
	UP_NORTH_WEST(false, true, true, false, true, false),
	UP_SOUTH_EAST(false, true, false, true, false, true),
	UP_SOUTH_WEST(false, true, false, true, true, false),
	DOWN_NORTH_SOUTH(true, false, true, true, false, false),
	DOWN_EAST_WEST(true, false, false, false, true, true),
	DOWN_NORTH_EAST(true, false, true, false, false, true),
	DOWN_NORTH_WEST(true, false, true, false, true, false),
	DOWN_SOUTH_EAST(true, false,  false, true, false, true),
	DOWN_SOUTH_WEST(true, false, false, true, true, false),
	UP_DOWN_NORTH(true, true, true, false, false, false),
	UP_DOWN_SOUTH(true, true, false, true, false, false),
	UP_DOWN_EAST(true, true, false, false, false, true),
	UP_DOWN_WEST(true, true, false, false, true, false),
	NORTH_SOUTH_EAST(false, false, true, true, false, true),
	NORTH_EAST_WEST(false, false, true, false, true, true),
	NORTH_SOUTH_WEST(false, false, true, true, true, false),
	SOUTH_EAST_WEST(false, false, false, true, true, true),
	//Quadruple
	EDGE_DOWN_NORTH(false, true, false, true, true, true),
	EDGE_DOWN_SOUTH(false, true, true, false, true, true),
	EDGE_DOWN_EAST(false, true, true, true, true, false),
	EDGE_DOWN_WEST(false, true, true, true, false, true),
	EDGE_UP_NORTH(true, false, false, true, true, true),
	EDGE_UP_SOUTH(true, false, true, false, true, true),
	EDGE_UP_EAST(true, false, true, true, true, false),
	EDGE_UP_WEST(true, false, true, true, false, true),
	EDGE_NORTH_EAST(true, true, true, false, false, true),
	EDGE_NORTH_WEST(true, true, true, false, true, false),
	EDGE_SOUTH_EAST(true, true, false, true, false, true),
	EDGE_SOUTH_WEST(true, true, false, true, true, false),
	HORIZONTAL(false, false, true, true, true, true),
	VERTICAL_X(true, true, false, false, true, true),
	VERTICAL_Y(true, true, true, true, false, false),
	//Quintuple
	RING_HORIZONTAL_UP(false, true, true, true, true, true),
	RING_HORIZONTAL_DOWN(true, false, true, true, true, true),
	RING_VERTICAL_NORTH(false, true, true, false, true, true),
	RING_VERTICAL_SOUTH(false, true, false, true, true, true),
	RING_VERTICAL_EAST(false, true, true, true, false, true),
	RING_VERTICAL_WEST(false, true, true, true, true, false),
	//FULL
	FULL(true, true, true, true, true, true);

	public static final PropertyEnum<Direction> DIR_LISTED = PropertyEnum.create("direction", Direction.class);
	public static final UnlistedDirection DIR_UNLISTED = new UnlistedDirection();
	private final boolean[] booleans;

	Direction(boolean... booleans) {
		this.booleans = booleans;
	}

	public Set<EnumFacing> getFacings() {
		Set<EnumFacing> facings = Sets.newHashSet();
		EnumFacing[] values = EnumFacing.values();
		for(int i = 0; i < booleans.length; i++) {
			if(booleans[i]) facings.add(values[i]);
		}
		return facings;
	}

	public boolean apply(Boolean[] booleans) {
		for(int i = 0; i < 6; i++) {
			if(this.booleans[i] != booleans[i]) return false;
		}
		return true;
	}

	@Override
	public String getName() {
		return name().toLowerCase(Locale.ROOT);
	}

	public static Direction getDirectionFromFacings(EnumFacing... facings) {
		EnumFacing[] values = EnumFacing.values();
		Boolean[] booleans = new Boolean[6];
		for(int i = 0; i < 6; i++) {
			EnumFacing facing = values[i];
			booleans[i] = Arrays.stream(facings).anyMatch(f -> f == facing);
		}
		return Arrays.stream(Direction.values()).filter(c -> c.apply(booleans)).findAny().orElse(FULL);
	}

	public static Direction getDirectionForBlock(Block block, IBlockAccess world, BlockPos origin) {
		Boolean[] booleans = Arrays.stream(EnumFacing.values()).map(facing -> {
			IBlockState found = world.getBlockState(origin.offset(facing));
			return found.getBlock() == block;
		}).toArray(Boolean[]::new);
		return Arrays.stream(Direction.values()).filter(c -> c.apply(booleans)).findAny().orElse(FULL);
	}

	public static Direction getDirectionForState(IBlockState state, IBlockAccess world, BlockPos origin) {
		Boolean[] booleans = Arrays.stream(EnumFacing.values()).map(facing -> {
			IBlockState found = world.getBlockState(origin.offset(facing));
			return found == state;
		}).toArray(Boolean[]::new);
		return Arrays.stream(Direction.values()).filter(c -> c.apply(booleans)).findAny().orElse(FULL);
	}

	public static class UnlistedDirection implements IUnlistedProperty<Direction> {

		@Override
		public String getName() {
			return "direction";
		}

		@Override
		public boolean isValid(Direction value) {
			return true;
		}

		@Override
		public Class<Direction> getType() {
			return Direction.class;
		}

		@Override
		public String valueToString(Direction value) {
			return value.getName();
		}
	}
}
