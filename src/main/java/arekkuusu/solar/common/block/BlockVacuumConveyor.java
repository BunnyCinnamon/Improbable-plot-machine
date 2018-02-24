/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.api.tool.FixedMaterial;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.baker.baked.BakedRender;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileVacuumConveyor;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Optional;

import static net.minecraft.block.BlockDirectional.FACING;

/**
 * Created by <Snack> on 06/01/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockVacuumConveyor extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.1875, 0.15625, 0.1875, 0.8125, 0.84375, 0.8125))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.1875, 0.15625, 0.1875, 0.8125, 0.84375, 0.8125))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.1875, 0.1875, 0.15625, 0.8125, 0.8125, 0.84375))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.1875, 0.1875, 0.15625, 0.8125, 0.8125, 0.84375))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.15625, 0.1875, 0.1875, 0.84375, 0.8125, 0.8125))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.15625, 0.1875, 0.1875, 0.84375, 0.8125, 0.8125))
			.build();

	public BlockVacuumConveyor() {
		super(LibNames.VACUUM_CONVEYOR, FixedMaterial.BREAK);
		setDefaultState(getDefaultState().withProperty(FACING, EnumFacing.DOWN));
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileVacuumConveyor.class, world, pos).ifPresent(vacuum -> {
				NBTHelper.getNBTTag(stack, "lookup").ifPresent(tag -> {
					vacuum.setLookup(new ItemStack(tag));
				});
			});
		}
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!world.isRemote) {
			getTile(TileVacuumConveyor.class, world, pos).ifPresent(vacuum -> {
				vacuum.setLookup(player.getHeldItem(hand));
			});
		}
		return true;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(getItem((World) world, pos, state)); //Bad??
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		Optional<TileVacuumConveyor> optional = getTile(TileVacuumConveyor.class, world, pos);
		if(optional.isPresent()) {
			TileVacuumConveyor vacuum = optional.get();
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
			if(!vacuum.getLookup().isEmpty()) {
				NBTHelper.setNBT(stack, "lookup", vacuum.getLookup().writeToNBT(new NBTTagCompound()));
			}
			return stack;
		}
		return super.getItem(world, pos, state);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		EnumFacing side = EnumFacing.getDirectionFromEntityLiving(pos, placer);
		return defaultState().withProperty(FACING, side.getOpposite());
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(FACING);
		return BB_MAP.getOrDefault(facing, FULL_BLOCK_AABB);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileVacuumConveyor();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, () -> new BakedRender()
				.setParticle(ResourceLibrary.VACUUM_CONVEYOR)
		);
		ModelHandler.registerModel(this, 0, "");
	}
}
