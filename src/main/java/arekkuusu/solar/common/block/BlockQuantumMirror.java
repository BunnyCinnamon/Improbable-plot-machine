/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.SolarApi;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.api.material.FixedMaterial;
import arekkuusu.solar.client.render.baked.QuantumMirrorBakedModel;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileQuantumMirror;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockQuantumMirror extends BlockBase implements ITileEntityProvider {

	private final AxisAlignedBB box = new AxisAlignedBB(0.25D,0.25D,0.25D, 0.75D, 0.75D, 0.75D);

	public BlockQuantumMirror() {
		super(LibNames.QUANTUM_MIRROR, FixedMaterial.DONT_MOVE);
		setHardness(2F);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = world.getTileEntity(pos);

		if(!world.isRemote && tile instanceof TileQuantumMirror) {
			TileQuantumMirror quantum = (TileQuantumMirror) tile;

			if(!player.isSneaking()) {
				quantum.handleItemTransfer(player, hand);
			}
		}
		return true;
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		TileEntity tile = world.getTileEntity(pos);

		if(!world.isRemote && tile instanceof TileQuantumMirror) {
			TileQuantumMirror quantum = (TileQuantumMirror) tile;
			ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

			if(player.isSneaking()) {
				quantum.takeItem(player, stack);
			}
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		TileEntity tile = placer.world.getTileEntity(pos);

		if(!world.isRemote && tile instanceof TileQuantumMirror) {
			NBTTagCompound tag = NBTHelper.getNBT(stack, SolarApi.QUANTUM_DATA);
			if(tag != null) {
				((TileQuantumMirror) tile).setKey(tag.getUniqueId("key"));
			}
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);

		if(!world.isRemote && tile instanceof TileQuantumMirror) {
			TileQuantumMirror quantum = (TileQuantumMirror) tile;
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));

			if(quantum.getKey() != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setUniqueId("key", quantum.getKey());
				NBTHelper.setNBT(stack, SolarApi.QUANTUM_DATA, tag);

				spawnAsEntity(world, pos, stack);
			}
			world.updateComparatorOutputLevel(pos, state.getBlock());
		}
		super.breakBlock(world, pos, state);
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		TileEntity tile = world.getTileEntity(pos);

		if(!world.isRemote && tile instanceof TileQuantumMirror) {
			TileQuantumMirror quantum = (TileQuantumMirror) tile;
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));

			if(quantum.getKey() != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setUniqueId("key", quantum.getKey());
				NBTHelper.setNBT(stack, SolarApi.QUANTUM_DATA, tag);
			}

			return stack;
		}
		return super.getItem(world, pos, state);
	}

	@Override
	public boolean canProvidePower(IBlockState state) {
		return true;
	}

	@Override
	public int getWeakPower(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing side) {
		TileEntity tile = world.getTileEntity(pos);
		if(tile != null && tile instanceof TileQuantumMirror) {
			UUID key = ((TileQuantumMirror) tile).getKey();
			if(key != null) {
				ItemStack stack = SolarApi.getQuantumStack(key, 0);
				Item redstone = Item.getItemFromBlock(Blocks.REDSTONE_BLOCK);
				return !stack.isEmpty() && stack.getItem() == redstone ? 15 : 0;
			}
		}

		return 0;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return NULL_AABB;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return box;
	}

	public EnumPushReaction getMobilityFlag(IBlockState state) {
		return EnumPushReaction.DESTROY;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new TileQuantumMirror();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(Item.getItemFromBlock(this), pair -> new QuantumMirrorBakedModel());
		ModelHandler.registerModel(this, 0);
	}
}
