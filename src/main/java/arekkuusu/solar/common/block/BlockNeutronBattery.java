/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.entanglement.IEntangledStack;
import arekkuusu.solar.api.entanglement.energy.data.ILumen;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.client.effect.FXUtil;
import arekkuusu.solar.client.effect.Light;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.baker.baked.BakedNeutronBattery;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileNeutronBattery;
import arekkuusu.solar.common.block.tile.TileNeutronBattery.Capacity;
import arekkuusu.solar.common.handler.data.ModCapability;
import arekkuusu.solar.common.item.ModItems;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockNeutronBattery extends BlockBase {

	public static final PropertyEnum<Capacity> CAPACITY = PropertyEnum.create("capacity", Capacity.class);
	public static final AxisAlignedBB BB = new AxisAlignedBB(0.1875, 0.0625, 0.1875, 0.8125, 0.9375, 0.8125);

	public BlockNeutronBattery() {
		super(LibNames.NEUTRON_BATTERY, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(CAPACITY, Capacity.BLUE));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if(!stack.isEmpty() && stack.getItem() == ModItems.NEUTRON_BATTERY) {
			if(!world.isRemote) getTile(TileNeutronBattery.class, world, pos).ifPresent(neutron -> {
				IEntangledStack entangled = ((IEntangledStack) stack.getItem());
				if(!entangled.getKey(stack).isPresent()) {
					neutron.getKey().ifPresent(uuid -> entangled.setKey(stack, uuid));
				}
			});
			return true;
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileNeutronBattery.class, world, pos).ifPresent(neutron -> {
				IEntangledStack entangled = (IEntangledStack) stack.getItem();
				if(!entangled.getKey(stack).isPresent()) {
					entangled.setKey(stack, UUID.randomUUID());
				}
				entangled.getKey(stack).ifPresent(neutron::setKey);
				if(placer instanceof EntityPlayer && ((EntityPlayer) placer).capabilities.isCreativeMode) {
					ILumen lumen = neutron.getCapability(ModCapability.LUMEN_CAPABILITY, EnumFacing.UP);
					if(lumen != null) {
						lumen.set(neutron.getCapacityLazy().max); //CHEATER!!
					}
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
		Optional<TileNeutronBattery> optional = getTile(TileNeutronBattery.class, world, pos);
		if(optional.isPresent()) {
			TileNeutronBattery neutron = optional.get();
			ItemStack stack = new ItemStack(Item.getItemFromBlock(this));
			neutron.getKey().ifPresent(uuid -> {
				((IEntangledStack) stack.getItem()).setKey(stack, uuid);
			});
			NBTHelper.setEnum(stack, state.getValue(CAPACITY), "neutron_nbt");
			return stack;
		}
		return super.getItem(world, pos, state);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		ItemStack stack = placer.getHeldItem(hand);
		return getDefaultState().withProperty(CAPACITY, NBTHelper.getEnum(Capacity.class, stack, "neutron_nbt").orElse(Capacity.BLUE));
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		Vector3 vec = Vector3.apply(pos.getX(), pos.getY(), pos.getZ());
		Capacity capacity = state.getValue(CAPACITY);
		for(int i = 0; i < 3 + rand.nextInt(4); i++) {
			Vector3 posVec = vec.add(
					0.35D + 0.3D * rand.nextFloat(),
					0.15D + 0.35D * rand.nextFloat(),
					0.35D + 0.3D * rand.nextFloat()
			);
			double speed = 0.005D + 0.005D * rand.nextDouble();
			Vector3 speedVec = Vector3.rotateRandom().multiply(speed);
			FXUtil.spawnLight(world, posVec, speedVec, 30, 2F, capacity.color, Light.GLOW);
			FXUtil.spawnLight(world, vec.add(0.5D), Vector3.Up().multiply(0.02D), 100, 2F, capacity.color, Light.GLOW);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(CAPACITY, Capacity.values()[meta]);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(CAPACITY).ordinal();
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CAPACITY);
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
		return new TileNeutronBattery(state.getValue(CAPACITY));
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for(Capacity capacity : Capacity.values()) {
			ItemStack stack = new ItemStack(this);
			NBTHelper.setEnum(stack, capacity, "neutron_nbt");
			items.add(stack);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, () -> new BakedNeutronBattery()
				.setParticle(ResourceLibrary.NEUTRON_BATTERY)
		);
		ModelHandler.registerModel(this, 0);
	}
}
