package arekkuusu.implom.common.handler.data.capability.provider;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.common.block.tile.TileMutator;
import arekkuusu.implom.common.handler.data.capability.nbt.WorldAccessNBTDataCapability;
import arekkuusu.implom.common.network.PacketHelper;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.UUID;

public class MutatorCapabilityProvider extends CapabilityProvider {

	public final WorldAccessNBTDataCapability worldAccessInstance = new WorldAccessNBTDataCapability() {
		@Override
		public void setKey(@Nullable UUID uuid) {
			super.setKey(uuid);
			getAs(TileMutator.class).ifPresent(holder -> {
				holder.markDirty();
				holder.sync();
			});
		}

		@Override
		public void onChange() {
			if(FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER) {
				PacketHelper.sendMutatorPacket(this);
			}
		}
	};

	public MutatorCapabilityProvider(ICapabilitySerializable<?> holder) {
		super(holder);
		capabilities.add(new CapabilityWrapper<>(Capabilities.WORLD_ACCESS, worldAccessInstance));
	}
}
