/*******************************************************************************
 * Arekkuusu / Solar 2017
 *
 * This project is licensed under the MIT.
 * The source code is available on github:
 * https://github.com/ArekkuusuJerii/Solar#solar
 ******************************************************************************/
package arekkuusu.solar.common.network;

import arekkuusu.solar.api.helper.Vector3;
import arekkuusu.solar.client.effect.ParticleUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.network.PacketBuffer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;

/**
 * Created by <Arekkuusu> on 17/09/2017.
 * It's distributed as part of Solar.
 */
public class QQuartzEffectMessage implements IMessage {

	private Vector3 from;
	private Vector3 to;

	public QQuartzEffectMessage() {}

	public QQuartzEffectMessage(Vector3 from, Vector3 to) {
		this.from = from;
		this.to = to;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		from = readVec(beef);
		to = readVec(beef);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBuffer beef = new PacketBuffer(buf);
		writeVec(from, beef);
		writeVec(to, beef);
	}

	private void writeVec(Vector3 vec, PacketBuffer beef) {
		beef.writeDouble(vec.x);
		beef.writeDouble(vec.y);
		beef.writeDouble(vec.z);
	}

	private Vector3 readVec(PacketBuffer beef) {
		return new Vector3(beef.readDouble(), beef.readDouble(), beef.readDouble());
	}

	public static class QQuartzEffectMessageHandler implements IMessageHandler<QQuartzEffectMessage, IMessage> {

		@Override
		@Nullable
		@SuppressWarnings("MethodCallSideOnly")
		public IMessage onMessage(QQuartzEffectMessage message, MessageContext ctx) {
			if(ctx.side == Side.CLIENT) {
				Minecraft.getMinecraft().addScheduledTask(() -> {
					World world = Minecraft.getMinecraft().player.world;

					for(int i = 0; i < 15; i++) {
						Vector3 offset = Vector3.getRandomVec(0.05D).add(message.to);
						Vector3 speed = Vector3.getRandomVec(0.01D);
						ParticleUtil.spawnTunnelingPhoton(world, offset,
								speed, 0x49FFFF, 60, 0.35F + (world.rand.nextFloat() * 0.5F));
					}

					for(int i = 0; i < 15; i++) {
						Vector3 offset = Vector3.getRandomVec(0.1D).add(message.from);
						Vector3 speed = Vector3.getRandomVec(0.1D);
						ParticleUtil.spawnTunnelingPhoton(world, offset,
								speed, 0xFF0303, 60, 0.35F + (world.rand.nextFloat() * 0.5F));
					}
				});
			}
			return null;
		}
	}
}
