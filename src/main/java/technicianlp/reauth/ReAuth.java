package technicianlp.reauth;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.network.FMLNetworkConstants;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;

@Mod("reauth")
@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public final class ReAuth {

    public static final Logger log = LogManager.getLogger("ReAuth");
    public static final Configuration config;
    public static final AuthHelper auth;
    public static IModInfo modInfo;

    static {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            config = new Configuration();
            auth = new AuthHelper();
        } else {
            config = null;
            auth = null;
        }
    }

    public ReAuth() {
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, config.getSpec(), "../reauth.toml");
            modInfo = ModLoadingContext.get().getActiveContainer().getModInfo();




        } else {
            log.warn("###########################################");
            log.warn("#       ReAuth was loaded on Server       #");
            log.warn("# Consider removing it to save some SHEEP #");
            log.warn("###########################################");
        }
    }

    @SubscribeEvent
    public static void setup(FMLCommonSetupEvent event) {
        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.DISPLAYTEST, () -> Pair.of(() -> FMLNetworkConstants.IGNORESERVERONLY, (a, b) -> true));
//        ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, sc) -> );

        if (config.storeUsername()){
            String username = config.getUsername();
            if (!username.isEmpty()) {
                auth.offline(username);
            }
        }

    }

    @SubscribeEvent
    public static void setup(ModConfig.ModConfigEvent event) {
        config.setConfig(event.getConfig());
    }

    @SuppressWarnings("unchecked")
    public static <E> E getField(Field field, Object object) {
        try {
            return (E) field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Failed Reflective Access", e);
        }
    }
}
