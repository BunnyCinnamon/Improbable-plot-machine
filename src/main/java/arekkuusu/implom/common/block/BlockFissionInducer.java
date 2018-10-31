/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.FixedMaterial;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyBakedRegistry;
import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.common.block.tile.TileFissionInducer;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedRender;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

/*
 * Created by <Arekkuusu> on 4/4/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockFissionInducer extends BlockBaseFacing {

	public static final int MAX_CAPACITY = 8000;
	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.25, 0.125, 0.25, 0.75, 0.875, 0.75))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.25, 0.125, 0.25, 0.75, 0.875, 0.75))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.25, 0.25, 0.875, 0.75, 0.75, 0.125))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.25, 0.25, 0.125, 0.75, 0.75, 0.875))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.875, 0.25, 0.25, 0.125, 0.75, 0.75))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.875, 0.25, 0.25, 0.125, 0.75, 0.75))
			.build();

	public BlockFissionInducer() {
		super(LibNames.FISSION_INDUCER, FixedMaterial.DONT_MOVE);
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(5F);
		setResistance(2000F);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		Vector3 vec = Vector3.apply(pos.getX(), pos.getY(), pos.getZ());
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		AxisAlignedBB bb = BB_MAP.get(facing);
		for(int i = 0; i < 3 + rand.nextInt(4); i++) {
			double speed = 0.005D + 0.005D * rand.nextDouble();
			Vector3 speedVec = Vector3.rotateRandom().multiply(speed);
			double x = bb.minX + rand.nextDouble() * bb.maxX;
			double y = bb.minY + rand.nextDouble() * bb.maxY;
			double z = bb.minZ + rand.nextDouble() * bb.maxZ;
			Vector3 posVec = vec.add(x, y, z);
			world.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posVec.x(), posVec.y(), posVec.z(), speedVec.x(), speedVec.y(), speedVec.z());
		}
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
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileFissionInducer();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, () -> new BakedRender()
				.setParticle(ResourceLibrary.FISSION_INDUCER)
		);
		ModelHandler.registerModel(this, 0);
	}
}
