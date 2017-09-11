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
import arekkuusu.solar.client.render.baked.QuantumMirrorBakedModel;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileQuantumMirror;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Created by <Arekkuusu> on 17/07/2017.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockQuantumMirror extends BlockBase {

	private final AxisAlignedBB box = new AxisAlignedBB(0.25D,0.25D,0.25D, 0.75D, 0.75D, 0.75D);

	public BlockQuantumMirror() {
		super(LibNames.QUANTUM_MIRROR, FixedMaterial.DONT_MOVE);
		setHardness(2F);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!world.isRemote && !player.isSneaking()) {
			getTile(TileQuantumMirror.class, world, pos).ifPresent(mirror -> {
				mirror.handleItemTransfer(player, hand);
			});
		}
		return true;
	}

	@Override
	public void onBlockClicked(World world, BlockPos pos, EntityPlayer player) {
		if(!world.isRemote) {
			getTile(TileQuantumMirror.class, world, pos).ifPresent(mirror -> {
				ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);

				if(player.isSneaking()) {
					mirror.takeItem(player, stack);
				}
			});
		}
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileQuantumMirror.class, world, pos).ifPresent(blinker -> {
				Optional<NBTTagCompound> optional = NBTHelper.getNBT(stack, SolarApi.QUANTUM_DATA);
				optional.ifPresent(nbtTagCompound -> blinker.setKey(nbtTagCompound.getUniqueId("key")));
			});
		}
	}

	@Override
	public void breakBlock(World world, BlockPos pos, IBlockState state) {
		getTile(TileQuantumMirror.class, world, pos).ifPresent(blinker -> {
			ItemStack stack = getItem(world, pos, state);
			spawnAsEntity(world, pos, stack);
		});
		super.breakBlock(world, pos, state);
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		Optional<TileQuantumMirror> optional = getTile(TileQuantumMirror.class, world, pos);
		if(optional.isPresent()) {
			TileQuantumMirror mirror = optional.get();

			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));

			mirror.getKey().ifPresent(uuid -> {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setUniqueId("key", uuid);
				NBTHelper.setNBT(stack, SolarApi.QUANTUM_DATA, tag);
			});

			return stack;
		}
		return super.getItem(world, pos, state);
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
		DummyBakedRegistry.register(Item.getItemFromBlock(this), (format, g) -> new QuantumMirrorBakedModel());
		ModelHandler.registerModel(this, 0);
	}
}
