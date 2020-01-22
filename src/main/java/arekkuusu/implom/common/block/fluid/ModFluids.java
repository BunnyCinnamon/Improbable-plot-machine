/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block.fluid;

import arekkuusu.implom.common.lib.LibMod;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Created by <Arekkuusu> on 4/5/2018.
 * It's distributed as part of Improbable plot machine.
 */
public final class ModFluids {

	public final static Set<Block> FLUIDS = Sets.newHashSet();

	public static Fluid HOT_AIR;

	static {
		HOT_AIR = create("hot_air",
				f -> new BlockFluid(f, Material.AIR),
				f -> f.setLuminosity(15).setGaseous(true).setDensity(-1).setTemperature(1300)
						.setEmptySound(SoundEvents.ITEM_BUCKET_EMPTY)
						.setFillSound(SoundEvents.ITEM_BUCKET_FILL)
		);
	}

	private static Fluid create(String name, Function<Fluid, Block> f, Consumer<Fluid> c) {
		Fluid fluid = new Fluid(name,
				new ResourceLocation(LibMod.MOD_ID + ":blocks/fluid/" + name + "_still"),
				new ResourceLocation(LibMod.MOD_ID + ":blocks/fluid/" + name + "_flow")
		);
		if(!FluidRegistry.registerFluid(fluid)) {
			fluid = FluidRegistry.getFluid(name);
		}
		else {
			Block block = f.apply(fluid);
			c.accept(fluid.setBlock(block).setUnlocalizedName(block.getUnlocalizedName()));
			FluidRegistry.addBucketForFluid(fluid);
			FLUIDS.add(block);
		}
		return fluid;
	}
}
