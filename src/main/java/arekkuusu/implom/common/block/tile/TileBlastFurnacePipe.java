package arekkuusu.implom.common.block.tile;

import arekkuusu.implom.common.handler.data.capability.FluidDefaultTank;
import arekkuusu.implom.common.handler.data.capability.provider.CapabilityProvider;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

public class TileBlastFurnacePipe extends TileBase implements FluidDefaultTank.ITankHandler, ITickable {

	public static final int CRITICAL_AMOUNT = Fluid.BUCKET_VOLUME;

	public final FluidDefaultTank fluidDefaultTank = new FluidDefaultTank(Fluid.BUCKET_VOLUME, this);
	public final CapabilityProvider provider = new CapabilityProvider.Builder(this)
			.put(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, fluidDefaultTank)
			.build();
	private ChannelConnection[] connections = new ChannelConnection[6]; // Connections to Up, Down, North...
	private int tick;

	public TileBlastFurnacePipe() {
		Arrays.fill(this.connections, ChannelConnection.ANY);
	}

	@Override
	public void update() {
		if(isClientWorld()) return;
		if(tick % 2 == 0) {
			if(fluidDefaultTank.getFluidAmount() > 0) {
				List<IFluidHandler> fluidHandlers = getTransferFluidHandlers();
				if(!fluidHandlers.isEmpty()) {
					FluidStack[] fluidStacks = getTransferAmounts(fluidHandlers);
					for(int i = 0; i < fluidHandlers.size(); i++) {
						IFluidHandler fluidHandler = fluidHandlers.get(i);
						transfer(fluidHandler, fluidStacks[i]);
					}
				}
			}

			if(fluidDefaultTank.getFluidAmount() >= CRITICAL_AMOUNT) {
				//BAKURETSU LA LA LA
			}
		}

		tick = (tick + 1) % 20;
	}

	public List<IFluidHandler> getTransferFluidHandlers() {
		List<IFluidHandler> fluidHandlers = new ArrayList<>(6);
		for(EnumFacing facing : EnumFacing.VALUES) {
			ChannelConnection connection = getConnection(facing);
			if(connection != ChannelConnection.NONE) {
				setConnection(facing, ChannelConnection.NONE); //Found nothing by default
				TileEntity tile = getWorld().getTileEntity(pos.offset(facing));
				if(tile == null) continue;
				setConnection(facing, ChannelConnection.SOME); //Found a tile that might change, check later
				getFluidHandler(tile, facing).ifPresent(handler -> {
					for(IFluidTankProperties tankProperty : handler.getTankProperties()) {
						if(isTankAmountValid(tankProperty) && isTankPropertyValid(tankProperty)) {
							setConnection(facing, ChannelConnection.ANY); //Found one that can transfer into
							fluidHandlers.add(handler);
							break;
						}
						//Found one that might not work on this tank property, keep checking
					}
				});
			}
		}
		return fluidHandlers;
	}

	public boolean isTankAmountValid(IFluidTankProperties tankProperty) {
		FluidStack tankFluidStack = tankProperty.getContents();
		return tankFluidStack == null || tankFluidStack.amount < fluidDefaultTank.getFluidAmount();
	}

	public boolean isTankPropertyValid(IFluidTankProperties tankProperty) {
		FluidStack tankFluidStack = tankProperty.getContents();
		return (tankFluidStack == null || tankFluidStack.isFluidEqual(fluidDefaultTank.getFluid()))
				&& fluidDefaultTank.getFluid() != null
				&& tankProperty.canFillFluidType(fluidDefaultTank.getFluid())
				&& tankProperty.canFill();
	}

	/*public boolean isTankPropertyInvalid(IFluidTankProperties tankProperty) {
		FluidStack tankFluidStack = tankProperty.getContents();
		return ((fluidDefaultTank.getFluid() != null && !tankProperty.canFillFluidType(fluidDefaultTank.getFluid()))
				|| (fluidDefaultTank.getFluid() == null && !tankProperty.canFill()));
	}*/

	public Optional<IFluidHandler> getFluidHandler(TileEntity tile, EnumFacing facing) {
		return Optional.ofNullable(tile.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()));
	}

	public FluidStack[] getTransferAmounts(List<IFluidHandler> transfers) {
		assert fluidDefaultTank.getFluid() != null;
		FluidStack[] fluidStacks = new FluidStack[transfers.size()];
		transfers.sort((a, b) -> {
			FluidStack s0 = a.getTankProperties()[0].getContents();
			FluidStack s1 = b.getTankProperties()[0].getContents();
			int a0 = s0 != null ? s0.amount : 0;
			int a1 = s1 != null ? s1.amount : 0;
			return Integer.compare(a0, a1);
		});

		int totalTransferable = fluidDefaultTank.getFluidAmount();
		FluidStack transfer = new FluidStack(fluidDefaultTank.getFluid(), 0);
		for(int i = 0; i < fluidStacks.length; i++) {
			IFluidHandler handler = transfers.get(i);
			FluidStack stack = handler.getTankProperties()[0].getContents();
			int amountStored = stack != null ? stack.amount : 0;
			if(totalTransferable <= 0 || totalTransferable <= amountStored) {
				fluidStacks[i] = transfer.copy();
				continue;
			}
			int amountTransfer = (int) (((totalTransferable - amountStored) / 2) * 0.9);
			fluidStacks[i] = new FluidStack(transfer, amountTransfer);
			totalTransferable -= amountTransfer;
		}
		return fluidStacks;
	}

	public void transfer(IFluidHandler handler, FluidStack stack) {
		if(handler.fill(stack, false) > 0) {
			fluidDefaultTank.drain(handler.fill(stack, true), true);
		}
	}

	public void setConnection(EnumFacing facing, ChannelConnection connection) {
		int index = facing.getIndex();
		ChannelConnection oldConnection = this.connections[index];
		if(oldConnection != connection) {
			this.connections[index] = connection;
			markDirty();
		}
	}

	public ChannelConnection getConnection(EnumFacing facing) {
		int index = facing.getIndex();
		ChannelConnection connection = this.connections[index];
		return connection != null ? connection : ChannelConnection.NONE;
	}

	public void resetConnections() {
		Arrays.fill(this.connections, ChannelConnection.ANY);
		markDirty();
	}

	@Override
	public void onTankChange(@Nullable FluidStack old, @Nullable FluidStack updated) {
		if(!isClientWorld()) {
			markDirty();
			sync();
		}
	}

	@Override
	public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
		return this.provider.hasCapability(capability, facing) || super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
		if(this.provider.hasCapability(capability, facing) && facing != null) {
			setConnection(facing, ChannelConnection.ANY);
			return this.provider.getCapability(capability, facing);
		}
		else {
			return super.getCapability(capability, facing);
		}
	}

	/* NBT */
	private static final String TAG_PROVIDER = "provider";
	private static final String TAG_CONNECTIONS = "connections";

	@Override
	void writeNBT(NBTTagCompound compound) {
		compound.setTag(TAG_PROVIDER, this.provider.serializeNBT());
		int[] intArray = new int[6];
		for(int i = 0; i < this.connections.length; i++) {
			ChannelConnection connection = this.connections[i];
			intArray[i] = connection.ordinal();
		}
		compound.setIntArray(TAG_CONNECTIONS, intArray);
	}

	@Override
	void readNBT(NBTTagCompound compound) {
		this.provider.deserializeNBT(compound.getCompoundTag(TAG_PROVIDER));
		if(compound.hasKey(TAG_CONNECTIONS)) {
			this.connections = new ChannelConnection[6];
			int[] intArray = compound.getIntArray(TAG_CONNECTIONS);
			for(int i = 0; i < intArray.length; i++) {
				this.connections[i] = ChannelConnection.values()[intArray[i]];
			}
		}
	}

	@Override
	void readSync(NBTTagCompound compound) {
		provider.deserializeNBT(compound.getCompoundTag(TAG_PROVIDER));
	}

	@Override
	void writeSync(NBTTagCompound compound) {
		compound.setTag(TAG_PROVIDER, provider.serializeNBT());
	}

	public enum ChannelConnection implements IStringSerializable {
		ANY,
		SOME,
		NONE;

		@Override
		public String getName() {
			return this.toString().toLowerCase(Locale.ROOT);
		}
	}
}
