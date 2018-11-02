package arekkuusu.implom.api.capability.relativity;

import arekkuusu.implom.api.capability.relativity.data.IRelative;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;

public class RelativityEvent {

	public static class Invalidate<H extends IRelative> extends Event {

		private final H handler;

		public Invalidate(H handler) {
			this.handler = handler;
		}

		public H getHandler() {
			return handler;
		}
	}

	public static class Validate<H extends IRelative> extends Event {

		private final H handler;

		public Validate(H handler) {
			this.handler = handler;
		}

		public H getHandler() {
			return handler;
		}
	}

	public static <H extends IRelative> void onRelativeInvalidate(H handler) {
		MinecraftForge.EVENT_BUS.post(new Invalidate<>(handler));
	}

	public static <H extends IRelative> void onRelativeValidate(H handler) {
		MinecraftForge.EVENT_BUS.post(new Validate<>(handler));
	}
}
