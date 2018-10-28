package arekkuusu.solar.common.item;

import arekkuusu.solar.api.capability.quantum.IQuantum;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemBoundPhoton extends ItemBase {

	public ItemBoundPhoton() {
		super(LibNames.BOUND_PHOTON);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile instanceof IQuantum) {
			IQuantum q = (IQuantum) tile;
			q.getKey().ifPresent(uuid -> NBTHelper.setUniqueID(player.getHeldItem(hand), "quantum", uuid));
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}
}
