/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.client.render.entity.EyeOfSchrodingerRenderer;
import arekkuusu.solar.client.render.entity.LumenRenderer;
import arekkuusu.solar.common.block.ModBlocks;
import arekkuusu.solar.common.block.tile.*;
import arekkuusu.solar.common.entity.EntityEyeOfSchrodinger;
import arekkuusu.solar.common.entity.EntityLumen;
import net.minecraft.block.Block;
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
		registerEntity(EntityEyeOfSchrodinger.class, EyeOfSchrodingerRenderer::new);
		registerEntity(EntityLumen.class, LumenRenderer::new);
	}

	public static void init() {
		registerTESR(TileQuantumMirror.class, new QuantumMirrorRenderer());
		registerTESR(TilePhenomena.class, new PhenomenaRenderer());
		registerTESR(TileQSquared.class, new QSquaredRenderer());
		registerTESR(TileTheorema.class, new TheoremaRenderer());
		registerTESR(TileHyperConductor.class, new HyperConductorRenderer());
		registerTESR(TileVacuumConveyor.class, new VacuumConveyorRenderer());
		registerTESR(TileMechanicalTranslocator.class, new MechanicalTranslocatorRenderer());
		registerTESR(TileQimranut.class, new QimranutRenderer());
		registerTESR(TileNeutronBattery.class, new NeutronBatteryRenderer());
		registerTESR(TilePholarizer.class, new PholarizerRenderer());
		registerTESR(TileFissionInducer.class, new FissionInducerRenderer());

		registerTESRItemStack(ModBlocks.QUANTUM_MIRROR, TileQuantumMirror.class);
		registerTESRItemStack(ModBlocks.Q_SQUARED, TileQSquared.class);
		registerTESRItemStack(ModBlocks.THEOREMA, TileTheorema.class);
		registerTESRItemStack(ModBlocks.HYPER_CONDUCTOR, TileHyperConductor.class);
		registerTESRItemStack(ModBlocks.VACUUM_CONVEYOR, TileVacuumConveyor.class);
		registerTESRItemStack(ModBlocks.MECHANICAL_TRANSLOCATOR, TileMechanicalTranslocator.class);
		registerTESRItemStack(ModBlocks.QIMRANUT, TileQimranut.class);
		registerTESRItemStack(ModBlocks.NEUTRON_BATTERY, TileNeutronBattery.class);
		registerTESRItemStack(ModBlocks.PHOLARIZER, TilePholarizer.class);
		registerTESRItemStack(ModBlocks.FISSION_INDUCER, TileFissionInducer.class);
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
