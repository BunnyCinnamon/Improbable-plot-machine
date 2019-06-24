package arekkuusu.implom.common.block;

import arekkuusu.implom.common.block.base.BlockBase;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;
import java.util.Random;
import java.util.stream.Stream;

public class BlockIgnitedCoal extends BlockBase {

	public BlockIgnitedCoal() {
		super(LibNames.IGNITED_COAL, Material.ROCK);
		setLightLevel(0.5F);
		setTickRandomly(true);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		BlockPos blockpos = pos.up();
		IBlockState iblockstate = worldIn.getBlockState(blockpos);

		if((iblockstate.getBlock() == Blocks.WATER || iblockstate.getBlock() == Blocks.FLOWING_WATER)
				|| (rand.nextDouble() < 0.001D && Stream.of(EnumFacing.values()).anyMatch(f -> worldIn.isAirBlock(pos.offset(f))))) {
			worldIn.setBlockToAir(blockpos);
			worldIn.setBlockState(pos, Blocks.COAL_BLOCK.getDefaultState());
			worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);
			if(worldIn instanceof WorldServer) {
				((WorldServer) worldIn).spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double) blockpos.getX() + 0.5D, (double) blockpos.getY() + 0.25D, (double) blockpos.getZ() + 0.5D, 8, 0.5D, 0.25D, 0.5D, 0.0D);
			}
		}
	}

	public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
		if(!entityIn.isImmuneToFire() && entityIn instanceof EntityLivingBase && !EnchantmentHelper.hasFrostWalkerEnchantment((EntityLivingBase) entityIn)) {
			entityIn.attackEntityFrom(DamageSource.HOT_FLOOR, 1.0F);
		}
	}

	@Override
	public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos, EntityPlayer player) {
		return new ItemStack(Blocks.COAL_BLOCK);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(Objects.requireNonNull(getRegistryName()), "");
			}
		});
	}

	@SubscribeEvent
	public void onCoalRightClick(PlayerInteractEvent.RightClickBlock event) {
		if(!event.getWorld().isRemote && !event.getEntityPlayer().isSneaking()
				&& event.getItemStack().getItem() == Items.FLINT_AND_STEEL) {
			IBlockState state = event.getWorld().getBlockState(event.getPos());
			if(state.getBlock() == Blocks.COAL_BLOCK) {
				event.getWorld().setBlockState(event.getPos(), ModBlocks.IGNITED_COAL.getDefaultState());
				event.setUseItem(Event.Result.ALLOW);
			}
		}
	}
}
