/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.baker.baked.BakedNeutronBattery;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileNeutronBattery;
import arekkuusu.solar.common.block.tile.TileNeutronBattery.Capacity;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Created by <Arekkuusu> on 20/03/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockNeutronBattery extends BlockBase {

	public static final PropertyEnum<Capacity> CAPACITY = PropertyEnum.create("capacity", Capacity.class);

	public BlockNeutronBattery() {
		super(LibNames.NEUTRON_BATTERY, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(CAPACITY, Capacity.BLUE));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		ItemStack stack = placer.getHeldItem(hand);
		return getDefaultState().withProperty(CAPACITY, NBTHelper.getEnum(Capacity.class, stack, "capacity").orElse(Capacity.BLUE));
	}

	@Override
	public void randomDisplayTick(IBlockState state, World world, BlockPos pos, Random rand) {

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
			NBTHelper.setEnum(stack, capacity, "capacity");
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
