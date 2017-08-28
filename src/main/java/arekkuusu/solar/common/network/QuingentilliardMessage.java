/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github: 
 ******************************************************************************/
package arekkuusu.solar.common.network;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.UUID;

/**
 * Created by <Arekkuusu> on 09/08/2017.
 * It's distributed as part of Solar.
 */
public class QuingentilliardMessage implements IMessage {

	private UUID uuid;

	public QuingentilliardMessage() {
	}

	@Override
	public void fromBytes(ByteBuf buf) {

	}

	@Override
	public void toBytes(ByteBuf buf) {

	}

	public static class QuingentilliardMessageHandler implements IMessageHandler<QuingentilliardMessage, IMessage> {

		@Override
		public IMessage onMessage(QuingentilliardMessage message, MessageContext ctx) {
			if(ctx.side == Side.SERVER) {
				System.out.println("N	E	R	D	S");
			}
			return null;
		}
	}
}
