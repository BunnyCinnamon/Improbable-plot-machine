package arekkuusu.solar.common.item;

import arekkuusu.solar.api.capability.quantum.WorldData;
import arekkuusu.solar.api.helper.NBTHelper;
import arekkuusu.solar.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.client.helper.KeyCondition$;
import net.katsstuff.teamnightclipse.mirror.client.helper.Tooltip;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public class ItemBoundPhoton extends ItemBase implements IUUIDDescription {

	public ItemBoundPhoton() {
		super(LibNames.BOUND_PHOTON);
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		Tooltip.inline().condition(KeyCondition$.MODULE$.shiftKeyDown())
				.ifTrueJ(builder -> builder
						.condition(() -> NBTHelper.hasTag(stack, "BlockEntityTag"))
						.ifTrueJ(sub -> {
							NBTTagCompound tag = stack.getOrCreateSubCompound("BlockEntityTag");
							NBTTagCompound keys = tag.getCompoundTag(WorldData.NBT_TAG);
							for(String key : keys.getKeySet()) {
								NBTTagCompound subTag = keys.getCompoundTag(key);
								if(subTag.hasUniqueId("key")) {
									sub = getInfo(sub, Objects.requireNonNull(subTag.getUniqueId("key")));
								}
							}
							return sub;
						}).orElseJ(sub -> sub.addI18n("tlp.uuid_key_empty", TextFormatting.DARK_GRAY).newline()).apply()
				).apply().build(tooltip);
	}

	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		TileEntity tile = worldIn.getTileEntity(pos);
		if(tile != null) {
			ItemStack stack = player.getHeldItem(hand);
			NBTTagCompound writtenNBT = tile.writeToNBT(new NBTTagCompound());
			if(!NBTHelper.hasTag(stack, "BlockEntityTag")) {
				if(writtenNBT.hasKey(WorldData.NBT_TAG)) {
					NBTTagCompound compound = stack.getOrCreateSubCompound("BlockEntityTag");
					compound.setTag(WorldData.NBT_TAG, writtenNBT.getCompoundTag(WorldData.NBT_TAG));
				}
			} else {
				NBTTagCompound compound = stack.getOrCreateSubCompound("BlockEntityTag");
				NBTTagCompound nbt = writtenNBT.copy();
				nbt.merge(compound);
				nbt.setInteger("x", pos.getX());
				nbt.setInteger("y", pos.getY());
				nbt.setInteger("z", pos.getZ());
				tile.readFromNBT(nbt);
				tile.markDirty();
			}
		}
		return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
	}

	@Override
	public boolean isEnchantable(ItemStack stack) {
		return false;
	}
}
