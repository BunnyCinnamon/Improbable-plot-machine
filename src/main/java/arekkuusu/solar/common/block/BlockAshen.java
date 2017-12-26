/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.state.Direction;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 02/11/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockAshen extends BlockBase {

	private static final AxisAlignedBB BB = new AxisAlignedBB(0.0D, 0.0D, 0.0D, 1.0D, 0.98D, 1.0D);

	public BlockAshen() {
		super(LibNames.ASHEN, Material.SAND);
		setHarvestLevel(Tool.SHOVEL, ToolLevel.WOOD_GOLD);
		setHardness(0.1F);
		setTickRandomly(true);
		setSound(SoundType.SAND);
	}

	public void onEntityCollidedWithBlock(World worldIn, BlockPos pos, IBlockState state, Entity entityIn) {
		entityIn.motionX *= 0.8D;
		entityIn.motionZ *= 0.8D;
	}

	@Override
	public void onFallenUpon(World world, BlockPos pos, Entity entity, float fallDistance) {
		super.onFallenUpon(world, pos, entity, fallDistance);
		/*if(fallDistance >= 1.5F + world.rand.nextFloat() * 1.5F) {
			world.setBlockToAir(pos);
			entity.playSound(SoundEvents.BLOCK_SNOW_BREAK, 1F, 1F);
		}*/
	}

	@Override
	public void onLanded(World worldIn, Entity entity) {
		entity.motionY = 0.0D;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess world, BlockPos pos) {
		return state.withProperty(Direction.DIR_LISTED, Direction.getDirectionForState(state, world, pos));
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, Direction.DIR_LISTED);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
	}
}
