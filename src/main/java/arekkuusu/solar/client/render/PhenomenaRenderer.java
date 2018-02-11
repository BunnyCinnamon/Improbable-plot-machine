/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.client.render;

import arekkuusu.solar.api.state.State;
import arekkuusu.solar.common.block.tile.TilePhenomena;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.model.animation.AnimationTESR;
import net.minecraftforge.common.animation.Event;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.animation.CapabilityAnimation;
import net.minecraftforge.common.model.animation.IAnimationStateMachine;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.Properties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 09/09/2017.
 * It's distributed as part of Solar.
 */
@SideOnly(Side.CLIENT)
public class PhenomenaRenderer extends AnimationTESR<TilePhenomena> {

	@Override
	public void renderTileEntityFast(@Nonnull TilePhenomena phenomena, double x, double y, double z, float partialTick, int breakStage, float partial, @Nullable BufferBuilder renderer) {
		if(blockRenderer == null) blockRenderer = Minecraft.getMinecraft().getBlockRendererDispatcher();
		BlockPos pos = phenomena.getPos();
		IBlockAccess world = MinecraftForgeClient.getRegionRenderCache(phenomena.getWorld(), pos);
		IBlockState state = getState(phenomena, world, pos);
		if(state instanceof IExtendedBlockState) {
			IExtendedBlockState extendedState = (IExtendedBlockState) state;
			if(extendedState.getUnlistedNames().contains(Properties.AnimationProperty)) {
				float time = ((20 - phenomena.timer)) / 20F; // Using tile timer instead of world time.
				IAnimationStateMachine capability = phenomena.getCapability(CapabilityAnimation.ANIMATION_CAPABILITY, null);
				if(capability != null) {
					Pair<IModelState, Iterable<Event>> pair = capability.apply(time);
					extendedState = extendedState.withProperty(Properties.AnimationProperty, pair.getLeft());
					IBakedModel model = getModel(state);
					assert renderer != null;
					renderer.setTranslation(x - pos.getX(), y - pos.getY(), z - pos.getZ());
					blockRenderer.getBlockModelRenderer().renderModel(world, model, extendedState, pos, renderer, false);
				}
			}
		}
	}

	private IBlockState getState(TilePhenomena phenomena, IBlockAccess world, BlockPos pos) {
		IBlockState state = world.getBlockState(pos);
		return phenomena.timer > 0 ? state.withProperty(State.ACTIVE, true) : state;
	}

	private IBakedModel getModel(IBlockState state) {
		return blockRenderer.getBlockModelShapes().getModelForState(state);
	}
}
