package arekkuusu.implom.client;

import arekkuusu.implom.Proxy;
import net.minecraft.client.Minecraft;
import net.minecraft.world.World;

public class ClientProxy implements Proxy {

    @Override
    public World getWorld() {
        return Minecraft.getInstance().world;
    }
}
