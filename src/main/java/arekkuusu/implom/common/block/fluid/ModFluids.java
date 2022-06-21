package arekkuusu.implom.common.block.fluid;

import arekkuusu.implom.IPM;
import arekkuusu.implom.LibNames;
import net.minecraft.fluid.FlowingFluid;
import net.minecraftforge.fml.RegistryObject;

public final class ModFluids {

    public static RegistryObject<FlowingFluid> FLOWING_HOT_AIR = IPM.FLUIDS.register(
			LibNames.HOT_AIR + "_flowing", GasFluid.Flowing::new
	);
    public static RegistryObject<FlowingFluid> HOT_AIR = IPM.FLUIDS.register(
			LibNames.HOT_AIR, GasFluid.Source::new
	);

    public static void init(){
	}
}
