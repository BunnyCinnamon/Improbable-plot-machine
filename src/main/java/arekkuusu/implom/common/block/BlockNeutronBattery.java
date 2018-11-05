/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.capability.energy.LumenHelper;
import arekkuusu.implom.api.util.FixedMaterial;
import arekkuusu.implom.client.effect.Light;
import arekkuusu.implom.client.render.SpecialModelRenderer;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelRendered;
import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.tile.TileNeutronBattery;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.Lists;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/*
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockNeutronBattery extends BlockBaseFacing {

	public static final AxisAlignedBB BB = new AxisAlignedBB(0.1875, 0.0625, 0.1875, 0.8125, 0.9375, 0.8125);

	public BlockNeutronBattery() {
		super(LibNames.NEUTRON_BATTERY, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
		setLightLevel(0.2F);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		if(!world.isRemote) {
			getTile(TileNeutronBattery.class, world, pos).ifPresent(battery -> {
				LumenHelper.getComplexCapability(battery, battery.getFacingLazy()).ifPresent(handler -> {
					if(!handler.getKey().isPresent()) {
						LumenHelper.getComplexCapability(stack).ifPresent(subHandler -> {
							if(!subHandler.getKey().isPresent()) subHandler.setKey(UUID.randomUUID());
							subHandler.getKey().ifPresent(key -> handler.setKey(key));
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
		getTile(TileNeutronBattery.class, world, pos).ifPresent(battery -> {
			LumenHelper.getComplexCapability(battery, battery.getFacingLazy()).ifPresent(handler -> {
				handler.getKey().ifPresent(key -> {
					LumenHelper.getComplexCapability(stack).ifPresent(subHandler -> subHandler.setKey(key));
				});
			});
			battery.getCapacitor().ifPresent(capacitor -> {
				NBTTagCompound root = stack.getOrCreateSubCompound("BlockEntityTag");
				root.setTag(BatteryCapacitor.NBT_TAG, capacitor.serializeNBT());
			});
		});
		return stack;
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {
		getTile(TileNeutronBattery.class, world, pos).flatMap(TileNeutronBattery::getCapacitor).ifPresent(capacitor -> {
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
		return new TileNeutronBattery();
	}

	@Override
	public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items) {
		for(BatteryCapacitor capacitor : CAPACITORS) {
			ItemStack stack = new ItemStack(this);
			NBTTagCompound root = stack.getOrCreateSubCompound("BlockEntityTag");
			root.setTag(BatteryCapacitor.NBT_TAG, capacitor.serializeNBT());
			items.add(stack);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyModelRegistry.register(this, new ModelRendered()
				.setOverride(SpecialModelRenderer::setTempItemRenderer)
				.setParticle(ResourceLibrary.NEUTRON_BATTERY)
		);
		ModelHandler.registerModel(this, 0);
	}

	public static class BatteryCapacitor implements INBTSerializable<NBTTagCompound>, IStringSerializable {
		public static String NBT_TAG = "neutron_capacitor";

		String name;
		int capacity;
		int color;

		public BatteryCapacitor(String name, int capacity, int color) {
			this.name = name;
			this.capacity = capacity;
			this.color = color;
		}

		public BatteryCapacitor() {
		}

		@Override
		public String getName() {
			return name;
		}

		public BatteryCapacitor setName(String name) {
			this.name = name;
			return new BatteryCapacitor(name, capacity, color);
		}

		public int getCapacity() {
			return capacity;
		}

		public BatteryCapacitor setCapacity(int capacity) {
			this.capacity = capacity;
			return new BatteryCapacitor(name, capacity, color);
		}

		public int getColor() {
			return color;
		}

		public BatteryCapacitor setColor(int color) {
			this.color = color;
			return new BatteryCapacitor(name, capacity, color);
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

		public BatteryCapacitor copy() {
			return new BatteryCapacitor(name, capacity, color);
		}
	}

	public static final List<BatteryCapacitor> CAPACITORS = Lists.newArrayList();
	public static final BatteryCapacitor WHITE = new BatteryCapacitor("white", 8, 0xA4A4A4);
	public static final BatteryCapacitor BLUE = new BatteryCapacitor("blue", 64, 0x00FFE1);
	public static final BatteryCapacitor GREEN = new BatteryCapacitor("green", 512, 0x42BC49);
	public static final BatteryCapacitor YELLOW = new BatteryCapacitor("yellow", 4096, 0xF2CB30);
	public static final BatteryCapacitor PINK = new BatteryCapacitor("pink", 16777216, 0xC13DAA);

	static {
		CAPACITORS.add(WHITE);
		CAPACITORS.add(BLUE);
		CAPACITORS.add(GREEN);
		CAPACITORS.add(YELLOW);
		CAPACITORS.add(PINK);
	}
}
