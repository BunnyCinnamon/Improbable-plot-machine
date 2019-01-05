/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.RandomCollection;
import arekkuusu.implom.common.IPM;
import arekkuusu.implom.common.handler.ConfigHandler;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Random;

/*
 * Created by <Arekkuusu> on 02/01/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockLargePot extends BlockBase {

	public static final PropertyInteger POT_VARIANT = PropertyInteger.create("variant", 0, 2);
	public static final AxisAlignedBB BB = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.9375, 0.875);
	public static final RandomCollection<Item> DROPS = new RandomCollection<>();

	static {
		IPM.LOG.info("[BlockLargePot] - Gathering loots");
		int count = 0;
		for(Map.Entry<String, Double> entry : ConfigHandler.GEN_CONFIG.loot.largePotDecorator.weights.entrySet()) {
			Item item = Item.getByNameOrId(entry.getKey());
			if(item != null) {
				DROPS.add(entry.getValue(), item);
				count++;
			} else {
				IPM.LOG.warn("[BlockLargePot] - Item {} could not be found", entry.getKey());
			}
		}
		IPM.LOG.info("[BlockLargePot] - Found {} loots", count);
	}

	public BlockLargePot() {
		super(LibNames.LARGE_POT, Material.CLAY);
		setDefaultState(getDefaultState().withProperty(POT_VARIANT, 0));
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setSound(SoundType.GLASS);
		setHardness(1F);
	}

	@Override
	public int quantityDroppedWithBonus(int fortune, Random random) {
		return random.nextInt(fortune * 2 + 4);
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return DROPS.next();
	}

	@Override
	public IBlockState getStateForPlacement(World world, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer, EnumHand hand) {
		return getDefaultState().withProperty(POT_VARIANT, world.rand.nextInt(3));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState().withProperty(POT_VARIANT, meta);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return state.getValue(POT_VARIANT);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, POT_VARIANT);
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
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BB;
	}
}
