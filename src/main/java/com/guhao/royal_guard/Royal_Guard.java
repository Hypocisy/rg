//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.guhao.royal_guard;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("royal_guard")
public class Royal_Guard {
    public static ForgeConfigSpec.IntValue rg;
    public static ForgeConfigSpec CONFIG_SPEC;
    public static final Logger LOGGER = LogManager.getLogger(Royal_Guard.class);
    public static final String MODID = "royal_guard";
    public static final String LEGACY_MODID = "guhao";
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(new ResourceLocation("royal_guard", "royal_guard"), () -> {
        return "1";
    }, "1"::equals, "1"::equals);
    private static int messageID = 0;
    public static String VERSION = "N/A";

    public static ResourceLocation loc(String path) {
        return new ResourceLocation("royal_guard", path);
    }

    public static String locStr(String path) {
        return loc(path).toString();
    }

    public Royal_Guard() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        rg = builder.comment("RG").defineInRange("RG--tick", Integer.MAX_VALUE, 1, Integer.MAX_VALUE);
        CONFIG_SPEC = builder.build();
        ModLoadingContext.get().registerConfig(Type.COMMON, CONFIG_SPEC);
        Sounds.SOUNDS.register(bus);
    }

    public static <T> void addNetworkMessage(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer);
        ++messageID;
    }
}
