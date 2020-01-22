package arekkuusu.implom.common.handler.data.capability.provider;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.common.block.tile.TileQimranut;
import arekkuusu.implom.common.handler.data.capability.nbt.WorldAccessNBTDataCapability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;
import java.util.UUID;

public class QimranutCapabilityProvider extends CapabilityProvider {

	public final WorldAccessNBTDataCapability worldAccessNBTDataCapability = new WorldAccessNBTDataCapability() {
		@Override
		public void setKey(@Nullable UUID uuid) {
			super.setKey(uuid);
			getAs(TileQimranut.class).ifPresent(holder -> {
				holder.markDirty();
				holder.sync();
			});
		}
	};

	public QimranutCapabilityProvider(ICapabilitySerializable<?> holder) {
		super(holder);
		capabilities.add(new CapabilityWrapper<>(Capabilities.WORLD_ACCESS, worldAccessNBTDataCapability));
	}
}
