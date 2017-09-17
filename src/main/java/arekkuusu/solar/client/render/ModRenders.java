/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.render.entity.CustomItemRenderer;
import arekkuusu.solar.client.render.entity.EyeOfSchrodingerRenderer;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.block.tile.*;
import arekkuusu.solar.common.entity.EntityEyeOfSchrodinger;
import arekkuusu.solar.common.entity.EntityQuingentilliardItem;
import arekkuusu.solar.common.entity.EntitySpecialItem;
import arekkuusu.solar.common.item.ModItems;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 29/06/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public final class ModRenders {

	public static void preInit() {
		registerEntity(EntitySpecialItem.class, (RenderManager manager) -> new CustomItemRenderer(manager, true));
		registerEntity(EntityEyeOfSchrodinger.class, EyeOfSchrodingerRenderer::new);
		registerEntity(EntityQuingentilliardItem.class, (RenderManager manager) -> new CustomItemRenderer(manager, false));
	}

	public static void init() {
		registerTESR(TileSingularity.class, new TileSingularityRenderer());
		registerTESR(TilePrismFlower.class, new TilePrismFlowerRenderer());
		registerTESR(TileQuantumMirror.class, new QuantumMirrorRenderer());
		registerTESR(RenderDummy.Quingentilliard.class, new QuingentilliardRenderer());
		registerTESR(TileCrystalVoid.class, new CrystalVoidRenderer());
		registerTESR(TilePhenomena.class, new TilePhenomenaRenderer());

		registerTESRItemStack(ModBlocks.quantum_mirror, TileQuantumMirror.class);
		registerTESRItemStack(ModItems.quingentilliard, RenderDummy.Quingentilliard.class);
		registerTESRItemStack(ModBlocks.crystal_void, TileCrystalVoid.class);
	}

	private static <T extends TileEntity> void registerTESR(Class<T> tile, TileEntitySpecialRenderer<T> render) {
		ClientRegistry.bindTileEntitySpecialRenderer(tile, render);
	}

	@SuppressWarnings("deprecation")
	private static <T extends TileEntity> void registerTESRItemStack(Item item, Class<T> tile) {
		ForgeHooksClient.registerTESRItemStack(item, 0, tile);
	}

	@SuppressWarnings("deprecation")
	private static <T extends TileEntity> void registerTESRItemStack(Block block, Class<T> tile) {
		ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(block), 0, tile);
	}

	private static <T extends Entity> void registerEntity(Class<T> entity, IRenderFactory<? super T> render) {
		RenderingRegistry.registerEntityRenderingHandler(entity, render);
	}
}
