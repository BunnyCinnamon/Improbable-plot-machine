package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.api.multiblock.MultiblockRectanguloid;
import arekkuusu.implom.api.state.Properties;
import arekkuusu.implom.common.block.fluid.ModFluids;
import arekkuusu.implom.common.handler.data.capability.FluidDefaultTank;
import arekkuusu.implom.common.handler.data.capability.provider.CapabilityProvider;
import arekkuusu.implom.common.handler.multiblock.MultiblockHotAir;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class TileHotBlastAirVent extends TileMultiblockOniichan implements FluidDefaultTank.ITankHandler, ITickable {

	public static final MultiblockHotAir MULTIBLOCK_HOT_AIR = new MultiblockHotAir(3, MultiblockRectanguloid.WallType.ANY);

	public final FluidDefaultTank fluidDefaultTank = new FluidDefaultTank(0, this) {

		@Override
		public boolean isFluidType(Fluid fluid) {
			return fluid == ModFluids.HOT_AIR;
		}

		@Override
		public boolean canFillFluidType(FluidStack fluid) {
			return false;
		}
	};
	public final CapabilityProvider provider = new CapabilityProvider.Builder(this)
			.put(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, fluidDefaultTank)
			.build();
	public final FluidStack fillAmount = new FluidStack(ModFluids.HOT_AIR, 0);
	private int tick;

	public TileHotBlastAirVent() {
		super(MULTIBLOCK_HOT_AIR);
	}

	@Override
	public void update() {
		if(isClientWorld()) return;
		if(tick % 20 == 0) checkStructure();

		if(active && fillAmount.amount > 0 && notFull() && tick % 10 == 0) {
			fluidDefaultTank.fillInternal(fillAmount, true);
		}
		tick = (tick + 1) % 20;
	}

	public boolean notFull() {
		return fluidDefaultTank.getFluidAmount() < fluidDefaultTank.getCapacity();
	}

	@Override
	public void onTankChange(@Nullable FluidStack old, @Nullable FluidStack updated) {
		if(!isClientWorld()) {
			markDirty();
			sync();
		}
	}

	public boolean isActiveLazy() {
		return getStateValue(Properties.ACTIVE, getPos()).orElse(false);
	}

	@Override
	public void updateStructureData() {
		if(structure == null || isActiveLazy()) return;
		int inventorySize = (structure.x) * ((structure.y) - 3) * (structure.z);
		int liquidsSize = inventorySize * Fluid.BUCKET_VOLUME;
		int amountFill = (int) MathHelper.clamp((inventorySize / 6F) * 50F, 1F, 100F);
		fluidDefaultTank.setCapacity(liquidsSize);
		fillAmount.amount = amountFill;
	}

	@Override
	public boolean hasSecretCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return provider.hasCapability(capability, facing) || super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getSecretCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		return provider.hasCapability(capability, facing)
				? provider.getCapability(capability, facing)
				: super.getCapability(capability, facing);
	}

	/* NBT */
	private static final String TAG_PROVIDER = "provider";
	private static final String TAG_AMOUNT_FILL = "amount";

	@Override
	void readNBT(NBTTagCompound compound) {
		super.readNBT(compound);
		provider.deserializeNBT(compound.getCompoundTag(TAG_PROVIDER));
		fillAmount.amount = compound.getInteger(TAG_AMOUNT_FILL);
	}

	@Override
	void writeNBT(NBTTagCompound compound) {
		super.writeNBT(compound);
		compound.setTag(TAG_PROVIDER, provider.serializeNBT());
		compound.setInteger(TAG_AMOUNT_FILL, fillAmount.amount);
	}

	@Override
	void readSync(NBTTagCompound compound) {
		super.readSync(compound);
		provider.deserializeNBT(compound.getCompoundTag(TAG_PROVIDER));
	}

	@Override
	void writeSync(NBTTagCompound compound) {
		super.writeSync(compound);
		compound.setTag(TAG_PROVIDER, provider.serializeNBT());
	}
}
