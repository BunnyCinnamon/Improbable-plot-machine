/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.common.entity.EntityEyeOfSchrodinger;
import arekkuusu.implom.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.Random;

/*
 * Created by <Arekkuusu> on 25/08/2017.
 * It's distributed as part of Improbable plot machine.
 */
public class BlockSchrodingerGlyph extends BlockBase {

	public BlockSchrodingerGlyph() {
		super(LibNames.SCHRODINGER_GLYPH, Material.ROCK);
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(4F);
		setResistance(2000F);
	}

	@Override
	public void onBlockAdded(World world, BlockPos pos, IBlockState state) {
		world.scheduleUpdate(pos, this, tickRate(world));
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		int amount = worldIn.getEntitiesWithinAABB(EntityEyeOfSchrodinger.class, new AxisAlignedBB(pos).grow(10)).size();
		if(amount < Constants.MAX_EYE_AMOUNT) {
			for(int i = 0; i < Constants.MAX_EYE_AMOUNT - amount; i++) {
				if(rand.nextBoolean()) continue;
				EntityEyeOfSchrodinger entity = new EntityEyeOfSchrodinger(worldIn);
				Vector3 spawn = new Vector3.WrappedVec3i(pos.offset(EnumFacing.random(rand))).asImmutable().add(0.5D);
				entity.setPosition(spawn.x(), spawn.y(), spawn.z());
				worldIn.spawnEntity(entity);
				double d3 = (double) entity.getPosition().getX() + rand.nextDouble() * 0.10000000149011612D;
				double d8 = (double) entity.getPosition().getY() + rand.nextDouble();
				double d13 = (double) entity.getPosition().getZ() + rand.nextDouble();
				((WorldServer) entity.world).spawnParticle(EnumParticleTypes.SMOKE_NORMAL, false, d3, d8, d13, 15, 0.0D, 0.0D, 0.0D, 0.1D);
			}
		}
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}

	@Override
	public int tickRate(World worldIn) {
		return 10;
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
	}

	public static class Constants {
		public static final int MAX_EYE_AMOUNT = 5;
	}
}
