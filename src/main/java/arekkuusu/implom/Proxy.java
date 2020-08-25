package arekkuusu.implom;

import net.minecraft.world.World;

public interface Proxy {

    default World getWorld() {
        return null;
    }
}
