/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.base.BlockBase;
import arekkuusu.implom.common.entity.EntityEyeOfSchrodinger;
import arekkuusu.implom.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Optional;
import java.util.Random;

/*
 * Created by <Arekkuusu> on 25/08/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockMonolithicEye extends BlockBase {

	public BlockMonolithicEye() {
		super(LibNames.MONOLITHIC_EYE, Material.ROCK);
		setDefaultState(getDefaultState().withProperty(Properties.ACTIVE, false));
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
		Optional<EntityLivingBase> optional = getClosestEnemy(worldIn, pos);
		if(optional.isPresent()) {
			if(rand.nextDouble() < 0.1D) {
				int eyeAmount = worldIn.getEntitiesWithinAABB(EntityEyeOfSchrodinger.class, new AxisAlignedBB(pos).grow(10)).size();
				if(eyeAmount < Constants.MAX_EYE_AMOUNT) {
					spawnEyeEntity(worldIn, pos, rand);
				}
			}
			worldIn.getEntitiesWithinAABB(EntityEyeOfSchrodinger.class, new AxisAlignedBB(pos).grow(5)).forEach(e -> {
				if(!e.hasTargetedEntity()) e.setAttackTarget(optional.get());
			});
		}
		if(state.getValue(Properties.ACTIVE) != optional.isPresent()) setBlockState(worldIn, pos, optional.isPresent());
		worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
	}

	private void setBlockState(World world, BlockPos pos, boolean active) {
		world.setBlockState(pos, getDefaultState().withProperty(Properties.ACTIVE, active));
	}

	private Optional<EntityLivingBase> getClosestEnemy(World world, BlockPos pos) {
		List<EntityLivingBase> list = world.getEntitiesWithinAABB(EntityLivingBase.class, new AxisAlignedBB(pos).grow(10), entity -> {
			if(entity instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entity;
				return !player.capabilities.disableDamage && player.isEntityAlive() && !player.isSpectator();
			} else {
				return entity instanceof EntityMob && !(entity instanceof EntityEyeOfSchrodinger);
			}
		});
		EntityLivingBase enemy = null;
		double maxDistance = Integer.MAX_VALUE;
		for(EntityLivingBase entity : list) {
			double distance = entity.getDistanceSq(pos);
			if(distance < maxDistance) {
				enemy = entity;
				maxDistance = distance;
			}
		}
		return Optional.ofNullable(enemy);
	}

	private void spawnEyeEntity(World world, BlockPos pos, Random rand) {
		EntityEyeOfSchrodinger entity = new EntityEyeOfSchrodinger(world);
		Vector3 spawn = new Vector3.WrappedVec3i(pos.offset(EnumFacing.random(rand))).asImmutable().add(0.5D);
		entity.setPosition(spawn.x(), spawn.y(), spawn.z());
		world.spawnEntity(entity);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		if(rand.nextInt(15) == 0 && state.getValue(Properties.ACTIVE)) {
			for(EnumFacing facing : EnumFacing.values()) {
				Vector3 from = Vector3.Center().add(pos.getX(), pos.getY(), pos.getZ());
				Vector3 vec = new Vector3.WrappedVec3i(facing.getDirectionVec()).asImmutable().multiply(0.025D);
				IPM.getProxy().spawnNeutronBlast(world, from, vec, 60, 0.25F, 0xFF0303, Light.GLOW, ResourceLibrary.GLOW_PARTICLE, false);
			}
		}
	}

	@Override
	public int tickRate(World worldIn) {
		return 10;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(Properties.ACTIVE) ? 1 : 0;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return defaultState().withProperty(Properties.ACTIVE, meta == 1);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, Properties.ACTIVE);
	}

	@Override
	public boolean canRenderInLayer(IBlockState state, BlockRenderLayer layer) {
		return layer == BlockRenderLayer.SOLID || layer == BlockRenderLayer.CUTOUT;
	}

	public static class Constants {
		public static final int MAX_EYE_AMOUNT = 5;
	}
}
