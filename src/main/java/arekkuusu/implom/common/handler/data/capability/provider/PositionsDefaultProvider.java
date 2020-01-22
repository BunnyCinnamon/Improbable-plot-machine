package arekkuusu.implom.common.handler.data.capability.provider;

import arekkuusu.implom.api.capability.Capabilities;
import arekkuusu.implom.common.block.tile.TileBase;
import arekkuusu.implom.common.handler.data.capability.nbt.PositionsNBTDataCapability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nullable;
import java.util.UUID;

public class PositionsDefaultProvider extends CapabilityProvider {

	public final PositionsNBTDataCapability positionsNBTDataCapability = new PositionsNBTDataCapability() {
		@Override
		public void setKey(@Nullable UUID uuid) {
			getAs(TileBase.class).ifPresent(holder -> remove(holder.getWorld(), holder.getPos(), null));
			super.setKey(uuid);
			getAs(TileBase.class).ifPresent(holder -> {
				add(holder.getWorld(), holder.getPos(), null);
				holder.markDirty();
				holder.sync();
			});
		}
	};

	public PositionsDefaultProvider(ICapabilitySerializable<?> holder) {
		super(holder);
		capabilities.add(new CapabilityWrapper<>(Capabilities.POSITIONS, positionsNBTDataCapability));
	}
}
