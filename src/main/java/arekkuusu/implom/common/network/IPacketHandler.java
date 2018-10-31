/*
 * Arekkuusu / Improbable plot machine. 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Improbable-plot-machine
 */
package arekkuusu.implom.common.network;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/*
 * Created by <Arekkuusu> on 10/12/2017.
 * It's distributed as part of Improbable plot machine.
 */
public interface IPacketHandler {

	void handleData(NBTTagCompound compound, MessageContext context);
}
