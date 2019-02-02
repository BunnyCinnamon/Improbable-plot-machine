/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.client;

import arekkuusu.implom.client.render.entity.EyeOfSchrodingerRenderer;
import arekkuusu.implom.client.render.entity.LumenRenderer;
import arekkuusu.implom.client.render.stack.*;
import arekkuusu.implom.client.render.tile.*;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.block.tile.*;
import arekkuusu.implom.common.entity.EntityEyeOfSchrodinger;
import arekkuusu.implom.common.entity.EntityLumen;
import arekkuusu.implom.common.item.ModItems;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by <Arekkuusu> on 29/06/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SideOnly(Side.CLIENT)
public final class ModRenders {

	public static void preInit() {
		registerEntity(EntityEyeOfSchrodinger.class, EyeOfSchrodingerRenderer::new);
		registerEntity(EntityLumen.class, LumenRenderer::new);
		IPM.LOG.info("[YOU WAN SUM PIE!]");
	}

	public static void init() {
		registerTESR(TileQuantumMirror.class, new TileQuantumMirrorRenderer());
		registerTESR(TilePhenomena.class, new TilePhenomenaRenderer());
		registerTESR(TileQuanta.class, new TileQuantaRenderer());
		registerTESR(TileHyperConductor.class, new TileHyperConductorRenderer());
		registerTESR(TileVacuumConveyor.class, new TileVacuumConveyorRenderer());
		registerTESR(TileMechanicalTranslocator.class, new TileMechanicalTranslocatorRenderer());
		registerTESR(TileQimranut.class, new TileQimranutRenderer());
		registerTESR(TileNeutronBattery.class, new TileNeutronBatteryRenderer());
		registerTESR(TilePholarizer.class, new TilePholarizerRenderer());
		registerTESR(TileFissionInducer.class, new TileFissionInducerRenderer());
		registerTESR(TileElectron.class, new TileElectronRenderer());
		registerTESR(TileQuartzConsumer.class, new TileQuartzConsumerRenderer());
		registerTESR(TileSymmetricNegator.class, new TileSymmetricNegatorRenderer());
		registerTESR(TileSymmetricExtension.class, new TileSymmetricExtensionRenderer());
		registerTESR(TileKondenzator.class, new TileKondenzatorRenderer());
		registerTESR(TileMutator.class, new TileMutatorRenderer());

		ModItems.QUANTUM_MIRROR.setTileEntityItemStackRenderer(new ItemQuantumMirrorRenderer());
		ModItems.QUANTA.setTileEntityItemStackRenderer(new ItemQuantaRenderer());
		ModItems.HYPER_CONDUCTOR.setTileEntityItemStackRenderer(new ItemHyperConductorRenderer());
		ModItems.VACUUM_CONVEYOR.setTileEntityItemStackRenderer(new ItemVacuumConveyorRenderer());
		ModItems.MECHANICAL_TRANSLOCATOR.setTileEntityItemStackRenderer(new ItemMechanicalTranslocatorRenderer());
		ModItems.QIMRANUT.setTileEntityItemStackRenderer(new ItemQimranutRenderer());
		ModItems.NEUTRON_BATTERY.setTileEntityItemStackRenderer(new ItemNeutronBatteryRenderer());
		ModItems.PHOLARIZER.setTileEntityItemStackRenderer(new ItemPholarizerRenderer());
		ModItems.FISSION_INDUCER.setTileEntityItemStackRenderer(new ItemFissionInducerRenderer());
		ModItems.ELECTRON.setTileEntityItemStackRenderer(new ItemElectronRenderer());
		ModItems.SYMMETRIC_NEGATOR.setTileEntityItemStackRenderer(new ItemSymmetricNegatorRenderer());
		ModItems.SYMMETRIC_EXTENSION.setTileEntityItemStackRenderer(new ItemSymmetricExtensionRenderer());
		ModItems.KONDENZATOR.setTileEntityItemStackRenderer(new ItemKondenzatorRenderer());
		ModItems.MUTATOR.setTileEntityItemStackRenderer(new ItemMutatorRenderer());
		IPM.LOG.info("[NOM PIE!]");
	}

	private static <T extends TileEntity> void registerTESR(Class<T> tile, TileEntitySpecialRenderer<T> render) {
		ClientRegistry.bindTileEntitySpecialRenderer(tile, render);
	}

	private static <T extends Entity> void registerEntity(Class<T> entity, IRenderFactory<? super T> render) {
		RenderingRegistry.registerEntityRenderingHandler(entity, render);
	}
}
