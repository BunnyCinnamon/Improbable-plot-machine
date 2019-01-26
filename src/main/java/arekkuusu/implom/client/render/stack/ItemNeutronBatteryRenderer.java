package arekkuusu.implom.client.render.stack;

import arekkuusu.implom.api.helper.InventoryHelper;
import arekkuusu.implom.client.render.tile.TileNeutronBatteryRenderer;
import arekkuusu.implom.common.block.BlockNeutronBattery;
import arekkuusu.implom.common.handler.data.capability.InventoryNeutronCapability;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

public class ItemNeutronBatteryRenderer extends ItemRender {
	@Override
	public void renderByItem(ItemStack stack, float partialTicks) {
		bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);
		BlockNeutronBattery.BatteryCapacitor capacitor = InventoryHelper.getCapability(stack)
				.filter(data -> data instanceof InventoryNeutronCapability)
				.map(data -> ((InventoryNeutronCapability) data).getCapacitor())
				.orElse(null);
		TileNeutronBatteryRenderer.renderModel(capacitor, EnumFacing.UP, 0, 0, 0, partialTicks);
	}
}
