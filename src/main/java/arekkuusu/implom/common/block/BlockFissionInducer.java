/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelRendered;
import arekkuusu.implom.client.util.helper.ModelHelper;
import arekkuusu.implom.common.block.tile.TileFissionInducer;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
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

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = FacingAlignedBB.create(
			new Vector3(4, 1, 4),
			new Vector3(12, 15, 12),
			EnumFacing.UP
	).build();

	public BlockFissionInducer() {
		super(LibNames.FISSION_INDUCER, IPMMaterial.MONOLITH);
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
		DummyModelRegistry.register(this, new ModelRendered()
				.setParticle(ResourceLibrary.FISSION_INDUCER)
		);
		ModelHelper.registerModel(this, 0);
	}
}
