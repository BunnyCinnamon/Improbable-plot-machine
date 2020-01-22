package arekkuusu.implom.common.block;

import arekkuusu.implom.api.util.IPMMaterial;
import arekkuusu.implom.common.block.base.BlockBaseFacing;
import arekkuusu.implom.common.lib.LibNames;
import com.google.common.collect.ImmutableMap;
import net.katsstuff.teamnightclipse.mirror.data.Vector3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDirectional;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SuppressWarnings("deprecation")
public class BlockBlastFurnacePipeGauge extends BlockBaseFacing {

	private static final ImmutableMap<EnumFacing, AxisAlignedBB> BB_MAP = FacingAlignedBB.create(
			new Vector3(4.5, 13, 4.5),
			new Vector3(11.5, 16, 11.5),
			EnumFacing.UP
	).build();

	public BlockBlastFurnacePipeGauge() {
		super(LibNames.BLAST_FURNACE_PIPE_GAUGE, IPMMaterial.FIRE_BRICK);
		MinecraftForge.EVENT_BUS.register(this);
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		EnumFacing facing = state.getValue(BlockDirectional.FACING);
		return BB_MAP.getOrDefault(facing, FULL_BLOCK_AABB);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onDrawScreenPost(RenderGameOverlayEvent.Post event) {
		Minecraft mc = Minecraft.getMinecraft();

		if(event.getType() == RenderGameOverlayEvent.ElementType.ALL) {
			RayTraceResult ray = mc.objectMouseOver;
			if(ray != null) {
				BlockPos pos = ray.typeOfHit == RayTraceResult.Type.BLOCK ? ray.getBlockPos() : null;
				IBlockState state = pos != null ? mc.world.getBlockState(pos) : null;
				Block block = state == null ? null : state.getBlock();
				if(block == this) {
					EnumFacing facing = state.getValue(BlockDirectional.FACING);
					BlockPos offset = pos.offset(facing);
					TileEntity tile = mc.world.getTileEntity(offset);
					if(tile != null) {
						IFluidHandler handler = tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite());
						if(handler != null) {
							int xc = event.getResolution().getScaledWidth() / 2;
							int yc = event.getResolution().getScaledHeight() / 2;
							int max = 0;
							int amount = 0;

							if(handler instanceof IFluidTank) {
								max = ((IFluidTank) handler).getCapacity();
								amount = ((IFluidTank) handler).getFluidAmount();
							}
							else {
								IFluidTankProperties tankProperty = handler.getTankProperties()[0];
								max = tankProperty.getCapacity();
								if(tankProperty.getContents() != null)
									amount += tankProperty.getContents().amount;
							}

							String s = I18n.format("status.gauge");
							String s1 = amount + "/" + max;
							mc.fontRenderer.drawStringWithShadow(s, xc - mc.fontRenderer.getStringWidth(s) / 2, yc + 10, 0xFFFFFF);
							mc.fontRenderer.drawStringWithShadow(s1, xc - mc.fontRenderer.getStringWidth(s) / 2, yc + 20, 0xFFFFFF);
						}
					}
				}
			}
		}
	}
}
