package cinnamon.implom;

import net.minecraft.world.level.Level;

public interface Proxy {

    default Level getWorld() {
        return null;
    }
}
