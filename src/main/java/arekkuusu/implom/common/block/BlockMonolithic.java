/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.common.lib.LibNames;
import net.katsstuff.teamnightclipse.mirror.client.helper.Tooltip;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/*
 * Created by <Arekkuusu> on 11/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
@SuppressWarnings("deprecation")
public class BlockMonolithic extends BlockBase {

	public BlockMonolithic() {
		super(LibNames.MONOLITHIC, IPMMaterial.MONOLITH);
		setBlockUnbreakable();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World player, List<String> tooltip, ITooltipFlag advanced) {
		Tooltip.inline().addI18n("tlp.monolithic_description", Tooltip.DarkGrayItalic()).build(tooltip);
	}

	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		IBlockState fromState = worldIn.getBlockState(pos.offset(EnumFacing.UP));
		if(fromState.getMaterial() == Material.FIRE) {
			worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
		}
	}

	@Override
	public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
		IBlockState fromState = worldIn.getBlockState(pos.offset(EnumFacing.UP));
		if(fromState.getMaterial() == Material.FIRE) {
			if(0.5D / 100D > rand.nextDouble()) {
				Vector3 vec = new Vector3.WrappedVec3i(pos).asImmutable().add(0.5, 1.5, 0.5);
				EntityItem item = new EntityItem(worldIn, vec.x(), vec.y(), vec.z(), new ItemStack(this));
				worldIn.spawnEntity(item);
				worldIn.setBlockToAir(pos);
			} else {
				worldIn.scheduleUpdate(pos, this, tickRate(worldIn));
			}
		}
	}

	@Override
	public boolean isFireSource(World world, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public boolean canCreatureSpawn(IBlockState state, IBlockAccess world, BlockPos pos, EntityLiving.SpawnPlacementType type) {
		return false;
	}
}
