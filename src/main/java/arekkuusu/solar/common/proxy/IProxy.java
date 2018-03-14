/*******************************************************************************
 * Arekkuusu / Solar 2018
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.proxy;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * Created by <Arekkuusu> on 21/06/2017.
 * It's distributed as part of Solar.
 */
public interface IProxy {

	void preInit(FMLPreInitializationEvent event);

	void init(FMLInitializationEvent event);
}
