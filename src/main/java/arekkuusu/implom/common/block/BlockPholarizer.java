package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.FixedMaterial;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyBakedRegistry;
import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.tile.TilePholarizer;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedRender;
import net.katsstuff.teamnightclipse.mirror.data.Quat;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Random;

@SuppressWarnings("deprecation")
public class BlockPholarizer extends BlockBaseFacing {

	public static final PropertyEnum<Polarization> POLARIZATION = PropertyEnum.create("polarization", Polarization.class);
	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.25, 0.125, 0.25, 0.75, 0.875, 0.75))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.25, 0.125, 0.25, 0.75, 0.875, 0.75))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.25, 0.25, 0.875, 0.75, 0.75, 0.125))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.25, 0.25, 0.125, 0.75, 0.75, 0.875))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.875, 0.25, 0.25, 0.125, 0.75, 0.75))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.875, 0.25, 0.25, 0.125, 0.75, 0.75))
			.build();

	public BlockPholarizer() {
		super(LibNames.PHOLARIZER, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.DOWN)
				.withProperty(POLARIZATION, Polarization.NEGATIVE));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if(!world.isRemote && stack.isEmpty()) {
			Polarization polarization = state.getValue(POLARIZATION).isPositive() ? Polarization.NEGATIVE : Polarization.POSITIVE;
			world.setBlockState(pos, state.withProperty(POLARIZATION, polarization));
		}
		return stack.isEmpty();
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		Vector3 posVec = new Vector3.WrappedVec3i(pos).asImmutable().add(0.5D);
		for(int i = 0; i < 3 + rand.nextInt(6); i++) {
			Quat x = Quat.fromAxisAngle(Vector3.Forward(), (rand.nextFloat() * 2F - 1F) * 45);
			Quat z = Quat.fromAxisAngle(Vector3.Right(), (rand.nextFloat() * 2F - 1F) * 45);
			Vector3 randVec = Vector3.randomVector().multiply(0.1D);
			double speed = 0.005D + rand.nextDouble() * 0.005D;
			Vector3 speedVec = new Vector3.WrappedVec3i(facing.getDirectionVec())
					.asImmutable()
					.multiply(speed)
					.rotate(x.multiply(z));
			IPM.getProxy().spawnMute(world, posVec.add(randVec), speedVec, 45, 0.5F, 0x29FF75, Light.GLOW);
		}
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return defaultState().withProperty(BlockDirectional.FACING, facing.getOpposite())
				.withProperty(POLARIZATION, placer.isSneaking() ? Polarization.NEGATIVE : Polarization.POSITIVE);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		return BB_MAP.getOrDefault(facing, FULL_BLOCK_AABB);
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int i = state.getValue(BlockDirectional.FACING).ordinal();
		if(state.getValue(POLARIZATION) == Polarization.POSITIVE) {
			i |= 8;
		}
		return i;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		EnumFacing enumfacing = EnumFacing.values()[meta & 7];
		return this.getDefaultState().withProperty(BlockDirectional.FACING, enumfacing)
				.withProperty(POLARIZATION, (meta & 8) > 0 ? Polarization.POSITIVE : Polarization.NEGATIVE);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, BlockDirectional.FACING, POLARIZATION);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TilePholarizer();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, () -> new BakedRender()
				.setParticle(ResourceLibrary.PHOLARIZER)
		);
		ModelHandler.registerModel(this, 0);
	}

	public enum Polarization implements IStringSerializable {
		POSITIVE,
		NEGATIVE;

		public boolean isPositive() {
			return this == POSITIVE;
		}

		@Override
		public String getName() {
			return name().toLowerCase(Locale.ROOT);
		}
	}
}
