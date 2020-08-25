package arekkuusu.implom;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;

public final class IPMConfig {

    public static class Common {

        public final ForgeConfigSpec.IntValue ingotAmount;
        public final ForgeConfigSpec.IntValue oreMultiplier;

        public Common(ForgeConfigSpec.Builder builder) {
            builder.comment("Common configuration settings")
                    .push("common");
            //Material
            {
                builder.comment("Enable/Disable/Play with materials")
                        .push("material");
                //Smelting
                {
                    builder.comment("Edit smelting ratios")
                            .push("smelting");
                    ingotAmount = builder
                            .comment("Ingot smelting liquid amount")
                            .defineInRange("ingotAmount", 144, 0, Integer.MAX_VALUE);
                    oreMultiplier = builder
                            .comment("Ore smelting ratio, x0.5, x1, x2, etc")
                            .defineInRange("oreMultiplier", 1, 0, Integer.MAX_VALUE);
                    builder.pop();
                }
                builder.pop();
            }
            builder.pop();
        }
    }

    public static class Client {

        public Client(ForgeConfigSpec.Builder builder) {
            builder.comment("Client only settings, mostly things related to rendering")
                    .push("client");
            builder.pop();
        }
    }

    public static final class Holder {

        public static final Common COMMON;
        public static final ForgeConfigSpec COMMON_SPEC;

        public static final Client CLIENT;
        public static final ForgeConfigSpec CLIENT_SPEC;

        static {
            final Pair<Common, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Common::new);
            COMMON_SPEC = specPair.getRight();
            COMMON = specPair.getLeft();
        }

        static {
            final Pair<Client, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(Client::new);
            CLIENT_SPEC = specPair.getRight();
            CLIENT = specPair.getLeft();
        }
    }

    public static final class Setup {

        public static void client(final ModConfig config) {
        }

        public static void server(final ModConfig config) {
            // DamageFrames
            Runtime.Smelting.ingotAmount = Holder.COMMON.ingotAmount.get();
            Runtime.Smelting.oreMultiplier = Holder.COMMON.oreMultiplier.get();
        }
    }

    public static final class Runtime {

        public static class Smelting {
            public static int ingotAmount;
            public static int oreMultiplier;
        }

        public static class Rendering {
        }
    }
}