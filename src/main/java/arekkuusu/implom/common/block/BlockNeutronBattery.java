/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.helper.InventoryHelper;
import arekkuusu.implom.api.helper.NBTHelper;
import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelRendered;
import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.tile.TileNeutronBattery;
import arekkuusu.implom.common.item.ItemQuartz;
import arekkuusu.implom.common.item.ModItems;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/*
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockNeutronBattery extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = FacingAlignedBB.create(
			new Vector3(3, 1, 3),
			new Vector3(13, 15, 13),
			EnumFacing.UP
	).build();

	public BlockNeutronBattery() {
		super(LibNames.NEUTRON_BATTERY, IPMMaterial.MONOLITH);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
		setLightLevel(0.2F);
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!worldIn.isRemote) {
			getTile(TileNeutronBattery.class, worldIn, pos).ifPresent(tile -> {
				tile.fromItemStack(stack);
			});
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(!worldIn.isRemote) {
			getTile(TileNeutronBattery.class, worldIn, pos).ifPresent(tile -> {
				InventoryHelper.handleItemTransfer(tile, playerIn, hand);
			});
		}
		return true;
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		drops.add(getItem((World) world, pos, state)); //Bad??
	}

	@Override
	public ItemStack getItem(World world, BlockPos pos, IBlockState state) {
		ItemStack stack = super.getItem(world, pos, state);
		getTile(TileNeutronBattery.class, world, pos).ifPresent(tile -> {
			tile.toItemStack(stack);
		});
		return stack;
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		getTile(TileNeutronBattery.class, world, pos).map(tile -> tile.wrapper.inventoryInstance.getCapacitor()).ifPresent(capacitor -> {
			Vector3 vec = Vector3.apply(pos.getX(), pos.getY(), pos.getZ());
			Vector3 facingVec = new Vector3.WrappedVec3i(state.getValue(BlockDirectional.FACING).getDirectionVec()).asImmutable();
			for(int i = 0; i < 3 + rand.nextInt(4); i++) {
				Vector3 posVec = vec.add(
						0.35D + 0.3D * rand.nextFloat(),
						0.15D + 0.35D * rand.nextFloat(),
						0.35D + 0.3D * rand.nextFloat()
				);
				double speed = 0.005D + 0.005D * rand.nextDouble();
				Vector3 speedVec = Vector3.rotateRandom().multiply(speed);
				IPM.getProxy().spawnMute(world, posVec, speedVec, 30, 2F, capacitor.color, Light.GLOW);
				IPM.getProxy().spawnMute(world, vec.add(0.5D), facingVec.multiply(0.02D), 100, 2F, capacitor.color, Light.GLOW);
			}
		});
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
		return new TileNeutronBattery();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		Arrays.stream(ItemQuartz.Quartz.values()).filter(q -> q.size == ItemQuartz.Quartz.Size.MEDIUM).forEach(q -> {
			ItemStack stack = new ItemStack(this);
			ItemStack quartz = NBTHelper.setEnum(new ItemStack(ModItems.QUARTZ), q, ItemQuartz.Constants.NBT_QUARTZ);
			InventoryHelper.getCapability(stack).ifPresent(instance -> instance.insertItem(0, quartz, false));
			items.add(stack);
		});
		ItemStack stack = new ItemStack(this); //Empty capacitor
		items.add(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyModelRegistry.register(this, new ModelRendered()
				.setParticle(ResourceLibrary.NEUTRON_BATTERY)
		);
		ModelHandler.registerModel(this, 0);
	}

	public enum BatteryCapacitor {
		WHITE(8, 0xA4A4A4),
		BLUE(64, 0x00FFE1),
		GREEN(512, 0x42BC49),
		YELLOW(4096, 0xF2CB30),
		PINK(16777216, 0xC13DAA);

		int capacity;
		int color;

		BatteryCapacitor(int capacity, int color) {
			this.capacity = capacity;
			this.color = color;
		}

		public int getCapacity() {
			return capacity;
		}

		public int getColor() {
			return color;
		}
	}

	public static class Constants {
		public static final Map<ItemQuartz.Quartz, BatteryCapacitor> QUARTZ_TO_BATTERY = new HashMap<>(BatteryCapacitor.values().length);
		public static final Map<BatteryCapacitor, ItemQuartz.Quartz> BATTERY_TO_QUARTZ = new HashMap<>(BatteryCapacitor.values().length);
		public static final String NBT_NEUTRON = "neutron";

		static {
			Constants.putQuartzBattery(ItemQuartz.Quartz.WHITE_MEDIUM, BatteryCapacitor.WHITE);
			Constants.putQuartzBattery(ItemQuartz.Quartz.BLUE_MEDIUM, BatteryCapacitor.BLUE);
			Constants.putQuartzBattery(ItemQuartz.Quartz.GREEN_MEDIUM, BatteryCapacitor.GREEN);
			Constants.putQuartzBattery(ItemQuartz.Quartz.YELLOW_MEDIUM, BatteryCapacitor.YELLOW);
			Constants.putQuartzBattery(ItemQuartz.Quartz.PINK_MEDIUM, BatteryCapacitor.PINK);
		}

		public static void putQuartzBattery(ItemQuartz.Quartz quartz, BatteryCapacitor capacitor) {
			QUARTZ_TO_BATTERY.put(quartz, capacitor);
			BATTERY_TO_QUARTZ.put(capacitor, quartz);
		}
	}
}
