/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyModelRegistry;
import arekkuusu.implom.client.util.baker.model.ModelRendered;
import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.common.block.tile.TileKondenzator;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedPerspective;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/*
 * Created by <Arekkuusu> on 8/9/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockKondenzator extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = FacingAlignedBB.create(
			new Vector3(1, 12, 1),
			new Vector3(15, 15, 15),
			EnumFacing.UP
	).build();

	public BlockKondenzator() {
		super(LibNames.KONDENZATOR, IPMMaterial.MONOLITH);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.DOWN));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		return getTile(TileKondenzator.class, world, pos).map(t -> t.add(player.getHeldItem(hand))).orElse(false);
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
		DummyModelRegistry.register(this, new ModelRendered()
				.setTransforms(ImmutableMap.<ItemCameraTransforms.TransformType, TRSRTransformation>builder()
						.put(ItemCameraTransforms.TransformType.GUI, BakedPerspective.mkTransform(0F, -6F, 0F, 30F, 45F, 0F, 0.63F))
						.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, -2.5F, 0F, 75F, 45F, 0F, 0.38F))
						.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, -2.5F, 0F, 75F, 45F, 0F, 0.38F))
						.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, -3F, 0F, 0F, 45F, 0F, 0.38F))
						.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, -3F, 0F, 0F, 225F, 0F, 0.38F))
						.put(ItemCameraTransforms.TransformType.GROUND, BakedPerspective.mkTransform(0F, -4.5F, 0F, 0F, 0F, 0F, 0.25F))
						.put(ItemCameraTransforms.TransformType.FIXED, BakedPerspective.mkTransform(0F, 0, 4F, 90F, 0F, 0F, 0.5F))
						.build()
				).setParticle(ResourceLibrary.KONDENZATOR)
		);
		ModelHandler.registerModel(this, 0, "");
	}

	public static class Constants {
		public static final int LUMEN_CAPACITY = 100;
		public static final int IMBUING_TIME = 100;
		public static final int IMBUING_INCREASE_INTERVAL = 40;
		public static final int IMBUING_DECREASE_INTERVAL = 80;
	}
}
