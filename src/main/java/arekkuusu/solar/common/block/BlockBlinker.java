/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.api.material.FixedMaterial;
import arekkuusu.solar.client.render.baked.BlinkerBakedModel;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileBlinker;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

/**
 * Created by <Arekkuusu> on 03/09/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockBlinker extends BlockBase implements ITileEntityProvider {

	public BlockBlinker()  {
		super(LibNames.BLINKER, FixedMaterial.DONT_MOVE);
		setHarvestLevel("pickaxe", 1);
		setHardness(2F);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileBlinker.class, world, pos).ifPresent(blinker -> {
				Optional<NBTTagCompound> optional = NBTHelper.getNBT(stack, SolarApi.QUANTUM_DATA);
				optional.ifPresent(nbtTagCompound -> blinker.setKey(nbtTagCompound.getUniqueId("key")));
			});
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		getTile(TileBlinker.class, world, pos).ifPresent(blinker -> {
			blinker.remove();
			ItemStack stack = getItem(world, pos, state);
			spawnAsEntity(world, pos, stack);
		});
		super.breakBlock(world, pos, state);
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		Optional<TileBlinker> optional = getTile(TileBlinker.class, world, pos);
		if(optional.isPresent()) {
			TileBlinker blinker = optional.get();

			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));

			blinker.getKey().ifPresent(uuid -> {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setUniqueId("key", uuid);
				NBTHelper.setNBT(stack, SolarApi.QUANTUM_DATA, tag);
			});

			return stack;
		}
		return super.getItem(world, pos, state);
	}

	@Override
	public void neighborChanged(IBlockState state, World world, BlockPos pos, Block block, BlockPos fromPos) {
		if(!world.isRemote && block != this) {
			getTile(TileBlinker.class, world, pos).ifPresent(blinker -> {
				boolean wasPowered = TileBlinker.isPowered(blinker);
				boolean isPowered = world.isBlockPowered(pos);
				if((isPowered || block.getDefaultState().canProvidePower()) && isPowered != wasPowered) {
					Vec3d vec = new Vec3d(fromPos).subtract(new Vec3d(pos));
					EnumFacing facing = EnumFacing.getFacingFromVector((float) vec.x, (float) vec.y, (float) vec.z);

					TileBlinker.setPower(blinker, world.getRedstonePower(fromPos, facing));
				}
			});
		}
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		//TODO SET BLOCK HERE
		world.notifyNeighborsOfStateChange(pos, state.getBlock(), true);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		Optional<TileBlinker> optional = getTile(TileBlinker.class, world, pos);
		return TileBlinker.getPower(optional.orElse(null));
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return defaultState().withProperty(BlockDirectional.FACING, facing);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(BlockDirectional.FACING).getIndex();
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return defaultState().withProperty(BlockDirectional.FACING, EnumFacing.values()[meta]);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockDirectional.FACING);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileBlinker();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(Item.getItemFromBlock(this), BlinkerBakedModel::new);
		ModelHandler.registerModel(this, 0, "");
	}
}
