package cinnamon.implom.client;

import cinnamon.implom.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class ClientProxy implements Proxy {

    @Override
    public Level getWorld() {
        return Minecraft.getInstance().level;
    }
}
