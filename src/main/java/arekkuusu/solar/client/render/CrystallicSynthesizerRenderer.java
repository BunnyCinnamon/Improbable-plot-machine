package arekkuusu.solar.client.render;

import arekkuusu.solar.common.block.tile.TileCrystallicSynthesizer;

public class CrystallicSynthesizerRenderer extends SpecialModelRenderer<TileCrystallicSynthesizer> {

	@Override
	void renderTile(TileCrystallicSynthesizer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(x, y, z, partialTicks);
	}

	private void renderModel(double x, double y, double z, float partialTicks) {

	}
}
