package arekkuusu.solar.api.capability.relativity.data;

import arekkuusu.solar.api.capability.relativity.RelativityHandler;
import net.minecraft.tileentity.TileEntity;

import javax.annotation.Nullable;
import java.util.UUID;

public class RelativeRedstoneTileWrapper<T extends TileEntity> extends RelativeTileWrapper<T> implements IRelativeRedstone {

	public RelativeRedstoneTileWrapper(T tile) {
		super(tile);
	}

	@Override
	public void onPowerUpdate() {
		//FOR RENT
	}

	@Override
	public void remove() {
		if(!getWorld().isRemote) {
			if(RelativityHandler.removeRelative(this) && getWorld().isBlockPowered(getPos())) {
				RelativityHandler.setPower(this, 0, true);
			}
		}
	}

	@Override
	public void setKey(@Nullable UUID key) {
		UUID prevKey = getKey().orElse(null);
		super.setKey(key);
		if(key != prevKey) {
			onPowerUpdate();
		}
	}
}
