package arekkuusu.implom.api.capability;

import arekkuusu.implom.api.capability.nbt.IInventoryNBTDataCapability;
import arekkuusu.implom.api.capability.nbt.IPositionsNBTDataCapability;
import arekkuusu.implom.api.capability.nbt.IRedstoneNBTCapability;
import arekkuusu.implom.api.capability.nbt.IWorldAccessNBTDataCapability;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Capabilities {
	@CapabilityInject(ILumenCapability.class)
	public static final Capability<ILumenCapability> LUMEN = null;
	@CapabilityInject(IWorldAccessNBTDataCapability.class)
	public static final Capability<IWorldAccessNBTDataCapability> WORLD_ACCESS = null;
	@CapabilityInject(IPositionsNBTDataCapability.class)
	public static final Capability<IPositionsNBTDataCapability> POSITIONS = null;
	@CapabilityInject(IRedstoneNBTCapability.class)
	public static final Capability<IRedstoneNBTCapability> REDSTONE = null;
	@CapabilityInject(IInventoryNBTDataCapability.class)
	public static final Capability<IInventoryNBTDataCapability> INVENTORY = null; //Same as IItemHandler, do not use to get a capability!
}
