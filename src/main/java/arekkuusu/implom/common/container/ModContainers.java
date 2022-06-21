package arekkuusu.implom.common.container;

import arekkuusu.implom.IPM;
import arekkuusu.implom.LibNames;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(IPM.MOD_ID)
public final class ModContainers {

    public static RegistryObject<ContainerType<BlastFurnaceControllerContainer>> BLAST_FURNACE_CONTROLLER = IPM.CONTAINERS.register(
            LibNames.BLAST_FURNACE_CONTROLLER, () -> IForgeContainerType.create((windowId, inv, data) -> {
                return new BlastFurnaceControllerContainer(windowId, IPM.getProxy().getWorld(), data.readBlockPos(), inv);
            })
    );

    public static void init() {
    }
}
