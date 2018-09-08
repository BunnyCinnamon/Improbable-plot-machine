/*
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 */
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.capability.energy.LumenHelper;
import arekkuusu.solar.api.capability.energy.data.ComplexLumenStackWrapper;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.client.effect.Light;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.baker.baked.BakedNeutronBattery;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.Solar;
import arekkuusu.solar.common.block.tile.TileNeutronBattery;
import arekkuusu.solar.common.item.ModItems;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.Lists;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

/*
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockNeutronBattery extends BlockBaseFacing {

	public static final AxisAlignedBB BB = new AxisAlignedBB(0.1875, 0.0625, 0.1875, 0.8125, 0.9375, 0.8125);
	public final BatteryCapacitor capacitor;

	public BlockNeutronBattery(BatteryCapacitor capacitor) {
		super(LibNames.NEUTRON_BATTERY + "_" + capacitor.name, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
		setLightLevel(0.2F);
		this.capacitor = capacitor;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		if(!stack.isEmpty() && stack.getItem() == ModItems.NEUTRON_BATTERY) {
			if(!world.isRemote) getTile(TileNeutronBattery.class, world, pos).ifPresent(neutron -> {
				LumenHelper.getCapability(ComplexLumenStackWrapper.class, stack).ifPresent(i -> {
					if(!i.getKey().isPresent()) {
						neutron.getKey().ifPresent(i::setKey);
					}
				});
			});
			return true;
		}
		return false;
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileNeutronBattery.class, world, pos).ifPresent(battery -> {
				LumenHelper.getCapability(ComplexLumenStackWrapper.class, stack).ifPresent(i -> {
					if(!i.getKey().isPresent()) i.setKey(UUID.randomUUID());
					i.getKey().ifPresent(battery::setKey);
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
		Optional<TileNeutronBattery> optional = getTile(TileNeutronBattery.class, world, pos);
		if(optional.isPresent()) {
			TileNeutronBattery neutron = optional.get();
			ItemStack stack = new ItemStack(this);
			NBTHelper.setNBT(stack, "neutron_nbt", capacitor.serializeNBT());
			LumenHelper.getCapability(ComplexLumenStackWrapper.class, stack).ifPresent(i -> {
				neutron.getKey().ifPresent(i::setKey);
			});
			return stack;
		}
		return super.getItem(world, pos, state);
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
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
			Solar.PROXY.spawnMute(world, posVec, speedVec, 30, 2F, capacitor.color, Light.GLOW);
			Solar.PROXY.spawnMute(world, vec.add(0.5D), facingVec.multiply(0.02D), 100, 2F, capacitor.color, Light.GLOW);
		}
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
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
		return new TileNeutronBattery(capacitor);
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		ItemStack stack = new ItemStack(this);
		NBTHelper.setNBT(stack, "neutron_nbt", capacitor.serializeNBT());
		items.add(stack);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, () -> new BakedNeutronBattery()
				.setParticle(ResourceLibrary.NEUTRON_BATTERY)
		);
		ModelHandler.registerModel(this, 0);
	}

	public static class BatteryCapacitor implements INBTSerializable<NBTTagCompound>, IStringSerializable {

		String name;
		int capacity;
		int color;

		public String getName() {
			return name;
		}

		public int getCapacity() {
			return capacity;
		}

		public int getColor() {
			return color;
		}

		public BatteryCapacitor(String name, int capacity, int color) {
			this.name = name;
			this.capacity = capacity;
			this.color = color;
		}

		public BatteryCapacitor() {
		}

		@Override
		public NBTTagCompound serializeNBT() {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setString("name", name);
			tag.setInteger("capacity", capacity);
			tag.setInteger("color", color);
			return tag;
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			name = nbt.getString("name");
			capacity = nbt.getInteger("capacity");
			color = nbt.getInteger("color");
		}
	}

	public static final List<BatteryCapacitor> CAPACITORS = Lists.newArrayList();
	public static final BatteryCapacitor BLUE = new BatteryCapacitor("blue", 64, 0x00FFE1);
	public static final BatteryCapacitor GREEN = new BatteryCapacitor("green", 512, 0x42BC49);
	public static final BatteryCapacitor PINK = new BatteryCapacitor("pink", 4096, 0xC13DAA);

	static {
		CAPACITORS.add(BLUE);
		CAPACITORS.add(GREEN);
		CAPACITORS.add(PINK);
	}
}
