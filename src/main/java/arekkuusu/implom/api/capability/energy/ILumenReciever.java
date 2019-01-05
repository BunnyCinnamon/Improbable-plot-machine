package arekkuusu.implom.api.capability.energy;

public interface ILumenReciever {
	default boolean canReceive(int amount) {
		return true;
	}
}
