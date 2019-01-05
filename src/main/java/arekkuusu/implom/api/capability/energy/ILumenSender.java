package arekkuusu.implom.api.capability.energy;

public interface ILumenSender {
	default boolean canSend(int amount) {
		return true;
	}
}
