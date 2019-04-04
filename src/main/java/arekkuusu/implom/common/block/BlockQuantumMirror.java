/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.capability.InventoryHelper;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelRendered;
import arekkuusu.implom.client.util.helper.ModelHelper;
import arekkuusu.implom.common.block.tile.TileQuantumMirror;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/*
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockQuantumMirror extends BlockBase {

	private static final AxisAlignedBB BB = new AxisAlignedBB(0.25D, 0.25D, 0.25D, 0.75D, 0.75D, 0.75D);

	public BlockQuantumMirror() {
		super(LibNames.QUANTUM_MIRROR, Material.GLASS);
		setSound(SoundType.GLASS);
		setHardness(2F);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!world.isRemote && !player.isSneaking()) {
			getTile(TileQuantumMirror.class, world, pos).ifPresent(tile -> InventoryHelper.handleItemTransfer(tile, player, hand));
		}
		return true;
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		if(!world.isRemote && player.isSneaking()) {
			getTile(TileQuantumMirror.class, world, pos).ifPresent(tile -> {
				tile.takeItem(player);
			});
		}
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!worldIn.isRemote) {
			getTile(TileQuantumMirror.class, worldIn, pos).ifPresent(tile -> {
				tile.fromItemStack(stack);
			});
		}
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(getItem((World) world, pos, state)); //Bad??
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		ItemStack stack = super.getItem(world, pos, state);
		getTile(TileQuantumMirror.class, world, pos).ifPresent(tile -> {
			tile.toItemStack(stack);
		});
		return stack;
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
		return BB;
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
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileQuantumMirror();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyModelRegistry.register(this, new ModelRendered());
		ModelHelper.registerModel(this, 0);
	}
}
