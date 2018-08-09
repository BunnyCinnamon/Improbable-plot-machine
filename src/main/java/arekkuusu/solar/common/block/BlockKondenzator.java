/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.api.util.FixedMaterial;
import arekkuusu.solar.client.util.ResourceLibrary;
import arekkuusu.solar.client.util.baker.DummyBakedRegistry;
import arekkuusu.solar.client.util.helper.ModelHandler;
import arekkuusu.solar.common.block.tile.TileKondenzator;
import arekkuusu.solar.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.mirror.client.baked.BakedPerspective;
import net.katsstuff.mirror.client.baked.BakedRender;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 8/9/2018.
 * It's distributed as part of Solar.
 */
@SuppressWarnings("deprecation")
public class BlockKondenzator extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.125, 0, 0.125, 0.875, 1, 0.875))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.125, 0, 0.125, 0.875, 1, 0.875))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.125, 0.125, 0, 0.875, 0.875, 1F))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.125, 0.125, 0, 0.875, 0.875, 1))
			.put(EnumFacing.EAST, new AxisAlignedBB(0, 0.125, 0.875, 1F, 0.875, 0.125))
			.put(EnumFacing.WEST, new AxisAlignedBB(0, 0.125, 0.125, 1, 0.875, 0.875))
			.build();

	public BlockKondenzator() {
		super(LibNames.KONDENZATOR, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.ENTITYBLOCK_ANIMATED;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		return BB_MAP.getOrDefault(facing, FULL_BLOCK_AABB);
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileKondenzator();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModel() {
		DummyBakedRegistry.register(this, () -> new BakedRender()
				.setTransformsJava(ImmutableMap.<ItemCameraTransforms.TransformType, TRSRTransformation>builder()
						.put(ItemCameraTransforms.TransformType.GUI, BakedPerspective.mkTransform(0F, -2F, 0F, 30F, 45F, 0F, 0.63F))
						.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, -2.5F, 0F, 75F, 45F, 0F, 0.38F))
						.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, -2.5F, 0F, 75F, 45F, 0F, 0.38F))
						.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, -3F, 0F, 0F, 45F, 0F, 0.38F))
						.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, -3F, 0F, 0F, 225F, 0F, 0.38F))
						.put(ItemCameraTransforms.TransformType.GROUND, BakedPerspective.mkTransform(0F, 3.5F, 0F, 0F, 0F, 0F, 0.25F))
						.put(ItemCameraTransforms.TransformType.FIXED, BakedPerspective.mkTransform(0F, 1F, 0F, 0F, 0F, 0F, 0.5F))
						.build())
				.setParticle(ResourceLibrary.KONDENZATOR)
		);
		ModelHandler.registerModel(this, 0, "");
	}
}
