/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.tool.FixedMaterial;
import arekkuusu.solar.api.state.State;
import arekkuusu.solar.client.render.baked.BakedHyperConductor;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileHyperConductor;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 25/10/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockHyperConductor extends BlockBase {

	public BlockHyperConductor() {
		super(LibNames.HYPER_CONDUCTOR, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(State.POWER, 0));
		setHarvestLevel(Tool.PICK, ToolLevel.IRON);
		setHardness(1F);
		setTickRandomly(true);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState s, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			BlockPos vec = new BlockPos(8, 8, 8);
			BlockPos from = pos.add(vec);
			BlockPos to = pos.subtract(vec);
			getTile(TileHyperConductor.class, world, pos).ifPresent(tile -> {
				BlockPos.getAllInBox(from, to).forEach(tile::addElectron);
			});
		}
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(block != this) {
			getTile(TileHyperConductor.class, world, pos).ifPresent(conductor -> {
				if(conductor.getElectrons().contains(fromPos)) return;
				boolean wasPowered = conductor.isPowered();
				boolean isPowered = world.isBlockPowered(pos);
				if((isPowered || block.getDefaultState().canProvidePower()) && isPowered != wasPowered) {
					conductor.setPowered(isPowered);
				}
			});
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		getTile(TileHyperConductor.class, world, pos).ifPresent(conductor -> {
			if(state.getValue(State.POWER) > 0) {
				conductor.setPowered(false);
			}
		});
		super.breakBlock(world, pos, state);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(State.POWER);
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(State.POWER, meta);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, State.POWER);
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.CUTOUT_MIPPED || layer == BlockRenderLayer.SOLID;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileHyperConductor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(Item.getItemFromBlock(this), BakedHyperConductor::new);
		ModelHandler.registerModel(this, 0, "");
	}
}
