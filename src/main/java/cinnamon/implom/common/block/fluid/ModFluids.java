package cinnamon.implom.common.block.fluid;

import cinnamon.implom.IPM;
import cinnamon.implom.LibNames;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.registries.RegistryObject;

public final class ModFluids {

    public static RegistryObject<FlowingFluid> FLOWING_HOT_AIR = IPM.FLUIDS.register(
            LibNames.HOT_AIR + "_flowing", HotAirFluid.Flowing::new
    );
    public static RegistryObject<FlowingFluid> HOT_AIR = IPM.FLUIDS.register(
            LibNames.HOT_AIR, HotAirFluid.Source::new
    );
    public static RegistryObject<FlowingFluid> FLOWING_LUMEN = IPM.FLUIDS.register(
            LibNames.LUMEN + "_flowing", LumenFluid.Flowing::new
    );
    public static RegistryObject<FlowingFluid> LUMEN = IPM.FLUIDS.register(
            LibNames.LUMEN, LumenFluid.Source::new
    );

    public static void init() {
    }
}
