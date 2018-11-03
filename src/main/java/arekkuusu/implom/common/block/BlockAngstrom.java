/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.common.item.ModItems;
import arekkuusu.implom.common.lib.LibNames;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;

/*
 * Created by <Arekkuusu> on 20/12/2017.
 * It's distributed as part of Improbable plot machine.
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
		boolean mayReplace = stack.getItem() instanceof ItemBlock && stack.getItem() != ModItems.ANGSTROM;
		if(mayReplace) {
			world.setBlockToAir(pos); //Free pos
			//Replace Angstrom with held block
			EnumActionResult result = stack.onItemUse(player, world, pos, hand, facing, hitX, hitY, hitZ);
			if(result != EnumActionResult.SUCCESS) {
				world.setBlockState(pos, state);
			} else if(!world.isRemote) {
				//Giv Angstrom back to player
				ItemStack drop = new ItemStack(this);
				ItemHandlerHelper.giveItemToPlayer(player, drop);
			}
		}
		return mayReplace;
	}
}
