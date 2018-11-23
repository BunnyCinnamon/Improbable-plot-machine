package arekkuusu.implom.client.render;

import arekkuusu.implom.common.block.tile.TileCrystalSynthesizer;

public class CrystallicSynthesizerRenderer extends SpecialModelRenderer<TileCrystalSynthesizer> {

	@Override
	void renderTile(TileCrystalSynthesizer te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		renderModel(x, y, z, partialTicks);
	}

	@Override
	void renderStack(double x, double y, double z, float partialTicks) {
		renderModel(x, y, z, partialTicks);
	}

	private void renderModel(double x, double y, double z, float partialTicks) {

	}
}
