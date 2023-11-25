package cinnamon.implom.common.block.fluid;

import cinnamon.implom.IPM;
import cinnamon.implom.LibNames;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.FlowingFluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public final class ModFluids {

    public static RegistryObject<FluidType> HOT_AIR_TYPE = IPM.FLUID_TYPES.register(
            LibNames.HOT_AIR, () -> new FluidType(FluidType.Properties.create().lightLevel(0).density(100).viscosity(500).temperature(1300)) {
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL = new ResourceLocation(IPM.MOD_ID, "block/hot_air_still");
                        private static final ResourceLocation FLOW = new ResourceLocation(IPM.MOD_ID, "block/hot_air_flow");

                        public ResourceLocation getStillTexture() {
                            return STILL;
                        }

                        public ResourceLocation getFlowingTexture() {
                            return FLOW;
                        }
                    });
                }
            }
    );

    public static RegistryObject<FluidType> LUMEN_TYPE = IPM.FLUID_TYPES.register(
            LibNames.LUMEN, () -> new FluidType(FluidType.Properties.create().lightLevel(0).density(100).viscosity(500).temperature(1300)) {
                public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
                    consumer.accept(new IClientFluidTypeExtensions() {
                        private static final ResourceLocation STILL = new ResourceLocation(IPM.MOD_ID, "block/lumen_still");
                        private static final ResourceLocation FLOW = new ResourceLocation(IPM.MOD_ID, "block/lumen_flow");

                        public ResourceLocation getStillTexture() {
                            return STILL;
                        }

                        public ResourceLocation getFlowingTexture() {
                            return FLOW;
                        }
                    });
                }
            }
    );

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
