/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.block;

import arekkuusu.solar.common.item.ModItems;
import arekkuusu.solar.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * Created by <Arekkuusu> on 20/12/2017.
 * It's distributed as part of Solar.
 */
public class BlockAngstrom extends BlockBase {

	public BlockAngstrom() {
		super(LibNames.ANGSTROM, Material.IRON);
		setHarvestLevel(Tool.PICK, ToolLevel.STONE);
		setHardness(2F);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack stack = player.getHeldItem(hand);
		boolean isBlock = stack.getItem() instanceof ItemBlock && stack.getItem() != ModItems.ANGSTROM;
		if(!world.isRemote && isBlock) {
			//Replace
			ItemBlock item = (ItemBlock) stack.getItem();
			Block block = Block.getBlockFromItem(item);
			int meta = item.getMetadata(stack);
			IBlockState inState = block.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, player, hand);
			world.setBlockState(pos, inState);
			block.onBlockPlacedBy(world, pos, inState, player, stack);
			SoundType sound = block.getSoundType(inState, world, pos, player);
			world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), sound.getPlaceSound(), SoundCategory.BLOCKS, 0.75F, 0.8F);
			//Exchange
			ItemStack drop = new ItemStack(Item.getItemFromBlock(this));
			ItemHandlerHelper.giveItemToPlayer(player, drop);
			if(!player.capabilities.isCreativeMode) {
				stack.shrink(1);
			}
		}
		return isBlock;
	}
}
