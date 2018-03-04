/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.item;

import arekkuusu.solar.api.helper.RayTraceHelper;
import arekkuusu.solar.common.block.ModBlocks;
import net.katsstuff.mirror.data.Vector3;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

/**
 * Created by <Arekkuusu> on 21/12/2017.
 * It's distributed as part of Solar.
 */
public class ItemAngstrom extends ItemBaseBlock {

	public ItemAngstrom() {
		super(ModBlocks.ANGSTROM);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand); //Not entirely convinced it works
		RayTraceResult result = world.isRemote ? RayTraceHelper.tracePlayerHighlight((EntityPlayerSP) player) : RayTraceHelper.tracePlayerHighlight((EntityPlayerMP) player);
		if(result.typeOfHit != RayTraceResult.Type.BLOCK) {
			if(!world.isRemote) {
				Vector3 vec = Vector3.apply(player.posX, player.posY + player.getEyeHeight(), player.posZ)
						.add(new Vector3(player.getLookVec()).multiply(2.5D));
				BlockPos pos = new BlockPos(vec.toVec3d());
				IBlockState replaced = world.getBlockState(pos);
				if(world.isAirBlock(pos) || replaced.getBlock().isReplaceable(world, pos)) {
					IBlockState state = ModBlocks.ANGSTROM.getDefaultState();
					SoundType type = ModBlocks.ANGSTROM.getSoundType(state, world, pos, player);
					world.setBlockState(pos, state);
					world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), type.getPlaceSound(), SoundCategory.BLOCKS, 0.75F, 0.8F);
				}
				if(!player.capabilities.isCreativeMode) {
					stack.shrink(1);
				}
			}
			return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
		}
		return ActionResult.newResult(EnumActionResult.PASS, stack);
	}
}
