/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.fluid;

import arekkuusu.implom.client.util.helper.IModel;
import arekkuusu.implom.common.lib.LibMod;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

/**
 * Created by <Arekkuusu> on 4/5/2018.
 * It's distributed as part of Improbable plot machine.
 */
public class BlockFluid extends BlockFluidClassic implements IModel {

	public BlockFluid(Fluid fluid, Material material) {
		super(fluid, material);
		setUnlocalizedName(fluid.getName());
		setRegistryName(LibMod.MOD_ID, fluid.getName());
		setDensity(fluid.getDensity());
	}

	@Override
	public int getFlammability(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return getFluid().getTemperature();
	}

	@Override
	public boolean isFlammable(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return getFluid().getTemperature() > 0;
	}

	@Override
	public int getFireSpreadSpeed(IBlockAccess world, BlockPos pos, EnumFacing face) {
		return getFluid().getTemperature() > 0 ? 30 : 0;
	}

	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
		return getFluid().getTemperature() == 0;
	}

	@Override
	public void registerModel() {
		Item item = Item.getItemFromBlock(this);
		ModelBakery.registerItemVariants(item);
		ModelResourceLocation mrl = new ModelResourceLocation(LibMod.MOD_ID + ":fluid", getFluid().getName());
		ModelLoader.setCustomMeshDefinition(item, stack -> mrl);
		ModelLoader.setCustomStateMapper(this, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return mrl;
			}
		});
	}
}
