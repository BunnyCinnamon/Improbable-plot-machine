/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block;

import arekkuusu.solar.common.item.ModItems;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Random;

/*
 * Created by <Arekkuusu> on 02/11/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockAshen extends BlockBase {

	private static final AxisAlignedBB BB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.98D, 1.0D);

	public BlockAshen() {
		super(LibNames.ASHEN, Material.SAND);
		setHarvestLevel(Tool.SHOVEL, ToolLevel.WOOD_GOLD);
		setSound(SoundType.SAND);
		setHardness(0.1F);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return ModItems.ASH;
	}

	@Override
	public int quantityDropped(Random random) {
		return 4;
	}

	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.motionX *= 0.8D;
		entityIn.motionZ *= 0.8D;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
	}
}
