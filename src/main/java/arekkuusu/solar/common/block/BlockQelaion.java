/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.api.state.Direction;
import arekkuusu.solar.api.tool.FixedMaterial;
import arekkuusu.solar.client.effect.FXUtil;
import arekkuusu.solar.client.effect.Light;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.baker.baked.BakedQelaion;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileQelaion;
import arekkuusu.solar.common.item.ModItems;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.mirror.data.Quat;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Created by <Snack> on 24/02/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockQelaion extends BlockBase {

	public static final AxisAlignedBB BB = new AxisAlignedBB(0.0625, 0.0625, 0.0625,0.9375, 0.9375, 0.9375);
	public static final PropertyBool HAS_NODE = PropertyBool.create("has_node");

	public BlockQelaion() {
		super(LibNames.QELAION, FixedMaterial.DONT_MOVE);
		MinecraftForge.EVENT_BUS.register(this);
		setDefaultState(getDefaultState().withProperty(HAS_NODE, false));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
		setLightLevel(0.2F);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(world.isRemote) return true;
		ItemStack stack = player.getHeldItem(hand);
		if(stack.getItem() == ModItems.QELAION) {
			Optional<TileQelaion> optional = getTile(TileQelaion.class, world, pos);
			if(optional.isPresent()) {
				TileQelaion qelaion = optional.get();
				Optional<UUID> nodes = ((IEntangledStack) stack.getItem()).getKey(stack);
				Optional<UUID> parent = qelaion.getKey();
				if(nodes.isPresent() && parent.isPresent()) {
					qelaion.setNodes(nodes.get());
					return true;
				}
				return false;
			}
		} else if(stack.isEmpty()) {
			getTile(TileQelaion.class, world, pos).ifPresent(qelaion -> {
				if(!player.isSneaking()) {
					qelaion.put(facing);
				} else qelaion.setNodes(null);
			});
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileQelaion.class, world, pos).ifPresent(qelaion -> {
				IEntangledStack entangled = (IEntangledStack) stack.getItem();
				if(!entangled.getKey(stack).isPresent()) {
					entangled.setKey(stack, UUID.randomUUID());
				}
				entangled.getKey(stack).ifPresent(qelaion::setKey);
				if(NBTHelper.hasUniqueID(stack, "nodes")) {
					qelaion.setNodes(NBTHelper.getUniqueID(stack, "nodes"));
				}
			});
		}
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(getItem((World) world, pos, state)); //Bad??
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		Optional<TileQelaion> optional = getTile(TileQelaion.class, world, pos);
		if(optional.isPresent()) {
			TileQelaion qelaion = optional.get();
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
			qelaion.getKey().ifPresent(uuid -> {
				((IEntangledStack) stack.getItem()).setKey(stack, uuid);
			});
			if(qelaion.getNodes() != null) {
				NBTHelper.setUniqueID(stack, "nodes", qelaion.getNodes());
			}
			return stack;
		}
		return super.getItem(world, pos, state);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		getTile(TileQelaion.class, world, pos).ifPresent(qelaion -> {
			Vector3 posVec = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
			boolean on = state.getValue(HAS_NODE);
			for(EnumFacing facing : EnumFacing.values()) {
				if(qelaion.isInput(facing)) continue;
				for(int i = 0; i < 1 + rand.nextInt(3); i++) {
					Quat x = Quat.fromAxisAngle(Vector3.Forward(), (rand.nextFloat() * 2F - 1F) * 6);
					Quat z = Quat.fromAxisAngle(Vector3.Right(), (rand.nextFloat() * 2F - 1F) * 6);
					double speed = 0.025D + 0.0025D * rand.nextDouble();
					Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
							.asImmutable()
							.multiply(speed)
							.rotate(x.multiply(z));
					FXUtil.spawnLight(world, posVec, speedVec, 60, 2F, on ? 0x49FFFF : 0xFF0303, Light.GLOW);
				}
			}
		});
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(HAS_NODE) ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(HAS_NODE, meta != 0);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer.Builder(this).add(HAS_NODE).add(Direction.DIR_UNLISTED).build();
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		Optional<TileQelaion> optional = getTile(TileQelaion.class, world, pos);
		if(optional.isPresent()) {
			List<EnumFacing> closed = optional.get().getInputs();
			Direction direction = Direction.getDirectionFromFacings(closed.toArray(new EnumFacing[closed.size()]));
			return ((IExtendedBlockState) state).withProperty(Direction.DIR_UNLISTED, direction);
		}
		return super.getExtendedState(state, world, pos);
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
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileQelaion();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, BakedQelaion::new);
		ModelHandler.registerModel(this, 0, "");
	}
}