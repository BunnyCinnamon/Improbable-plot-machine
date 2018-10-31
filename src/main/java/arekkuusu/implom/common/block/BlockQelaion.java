/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.capability.quantum.IQuantum;
import arekkuusu.implom.api.capability.relativity.RelativityHelper;
import arekkuusu.implom.api.state.Direction;
import arekkuusu.implom.api.util.FixedMaterial;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.client.util.baker.DummyBakedRegistry;
import arekkuusu.implom.client.util.baker.baked.BakedQelaion;
import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.tile.TileQelaion;
import arekkuusu.implom.common.item.ModItems;
import arekkuusu.implom.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
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

/*
 * Created by <Arekkuusu> on 24/02/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockQelaion extends BlockBase {

	public static final AxisAlignedBB BB = new AxisAlignedBB(0.0625, 0.0625, 0.0625, 0.9375, 0.9375, 0.9375);
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
				Optional<UUID> nodes = RelativityHelper.getCapability(stack).flatMap(IQuantum::getKey);
				Optional<UUID> parent = RelativityHelper.getCapability(qelaion).flatMap(IQuantum::getKey);
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
				RelativityHelper.getCapability(qelaion).ifPresent(handler -> {
					if(!handler.getKey().isPresent()) {
						RelativityHelper.getCapability(stack).ifPresent(subHandler -> {
							if(!subHandler.getKey().isPresent()) subHandler.setKey(UUID.randomUUID());
							subHandler.getKey().ifPresent(handler::setKey);
						});
					}
				});
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
		getTile(TileQelaion.class, world, pos).ifPresent(qelaion -> {
			RelativityHelper.getCapability(qelaion).ifPresent(handler -> {
				handler.getKey().ifPresent(key -> {
					RelativityHelper.getCapability(stack).ifPresent(subHandler -> subHandler.setKey(key));
				});
			});
			qelaion.getNodes().ifPresent(nodes -> {
				NBTTagCompound tag = stack.getOrCreateSubCompound("BlockEntityTag");
				tag.setUniqueId("nodes", nodes);
			});
		});
		return stack;
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
					IPM.getProxy().spawnMute(world, posVec, speedVec, 60, 2F, on ? 0x49FFFF : 0xFF0303, Light.GLOW);
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
			Direction direction = Direction.getDirectionFromFacings(closed.toArray(new EnumFacing[0]));
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