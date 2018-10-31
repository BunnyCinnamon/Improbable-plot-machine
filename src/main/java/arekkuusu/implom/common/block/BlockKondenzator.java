/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.FixedMaterial;
import arekkuusu.implom.client.util.ResourceLibrary;
import arekkuusu.implom.client.util.baker.DummyBakedRegistry;
import arekkuusu.implom.client.util.helper.ModelHandler;
import arekkuusu.implom.common.block.tile.TileKondenzator;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedPerspective;
import net.katsstuff.teamnightclipse.mirror.client.baked.BakedRender;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.material.Material;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.model.TRSRTransformation;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.*;

/*
 * Created by <Arekkuusu> on 8/9/2018.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockKondenzator extends BlockBaseFacing {

	public static final int MAX_LUMEN = 100;
	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = ImmutableMap.<EnumFacing, AxisAlignedBB>builder()
			.put(EnumFacing.UP, new AxisAlignedBB(0.0625, 0.75, 0.0625, 0.9375, 0.9375, 0.9375))
			.put(EnumFacing.DOWN, new AxisAlignedBB(0.0625, 0.0625, 0.0625, 0.9375, 0.25, 0.9375))
			.put(EnumFacing.NORTH, new AxisAlignedBB(0.0625, 0.0625, 0.0625, 0.9375, 0.9375, 0.25))
			.put(EnumFacing.SOUTH, new AxisAlignedBB(0.0625, 0.0625, 0.75, 0.9375, 0.9375, 0.9375))
			.put(EnumFacing.EAST, new AxisAlignedBB(0.75, 0.0625, 0.9375, 0.9375, 0.9375, 0.0625))
			.put(EnumFacing.WEST, new AxisAlignedBB(0.0625, 0.0625, 0.0625, 0.25, 0.9375, 0.9375))
			.build();

	public BlockKondenzator() {
		super(LibNames.KONDENZATOR, FixedMaterial.DONT_MOVE);
		setDefaultState(getDefaultState().withProperty(BlockDirectional.FACING, EnumFacing.UP));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(1F);
		MinecraftForge.EVENT_BUS.register(BlockKondenzator.class);
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
		DummyBakedRegistry.register(this, () -> new BakedRender()
				.setTransformsJava(ImmutableMap.<ItemCameraTransforms.TransformType, TRSRTransformation>builder()
						.put(ItemCameraTransforms.TransformType.GUI, BakedPerspective.mkTransform(0F, -3F, 0F, 30F, 45F, 0F, 0.63F))
						.put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, -2.5F, 0F, 75F, 45F, 0F, 0.38F))
						.put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, -2.5F, 0F, 75F, 45F, 0F, 0.38F))
						.put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, BakedPerspective.mkTransform(0F, -3F, 0F, 0F, 45F, 0F, 0.38F))
						.put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, BakedPerspective.mkTransform(0F, -3F, 0F, 0F, 225F, 0F, 0.38F))
						.put(ItemCameraTransforms.TransformType.GROUND, BakedPerspective.mkTransform(0F, 3.5F, 0F, 0F, 0F, 0F, 0.25F))
						.put(ItemCameraTransforms.TransformType.FIXED, BakedPerspective.mkTransform(0F, 0F, 4F, 90F, 0F, 0F, 0.5F))
						.build())
				.setParticle(ResourceLibrary.KONDENZATOR)
		);
		ModelHandler.registerModel(this, 0, "");
	}

	public static final Map<BlockPos, Progress> MUTATION_PROGRESS = new HashMap<>();

	@SubscribeEvent
	public static void updateProgress(TickEvent.WorldTickEvent event) {
		Iterator<Map.Entry<BlockPos, Progress>> iterator = MUTATION_PROGRESS.entrySet().iterator();
		while(iterator.hasNext()) {
			Map.Entry<BlockPos, Progress> entry = iterator.next();
			Progress progress = entry.getValue();
			BlockPos pos = entry.getKey();
			IBlockState state = progress.world.getBlockState(pos);
			if(state.getMaterial() == Material.GLASS && state.getBlock() != ModBlocks.IMBUED_QUARTZ) {
				if(progress.timer > 0 && progress.lastUpdated++ >= 240) {
					progress.timer -= 20;
					if(progress.timer < 0) progress.timer = 0;
					progress.lastUpdated = 0;
				}
			} else iterator.remove();
		}
	}

	public static class Progress {
		Set<BlockPos> from = new HashSet<>();
		int lastUpdated;
		World world;
		int timer;

		public int getTimer() {
			return timer;
		}

		public int getMultiplier() {
			return from.size();
		}
	}

	public static Progress setProgress(TileKondenzator tile, int progress) {
		MUTATION_PROGRESS.compute(tile.getProgressPos(), (ignored, p) -> {
			if(p == null) p = new Progress();
			if(progress > 0) {
				p.from.add(tile.getPos());
				p.lastUpdated = 0;
				p.world = tile.getWorld();
				p.timer = progress;
			} else {
				p.from.remove(tile.getPos());
				if(p.from.isEmpty()) p = null;
			}
			return p;
		});
		return getProgress(tile.getProgressPos());
	}

	public static Progress getProgress(BlockPos pos) {
		return MUTATION_PROGRESS.getOrDefault(pos, new Progress());
	}
}
