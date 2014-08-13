package mobcamo.core;

import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.Logger;
import mobcamo.ai.CamoAIUtils;
import mobcamo.handlers.CamoCraftingHandler;
import mobcamo.handlers.ConfigHandler;
import mobcamo.handlers.MainEventHandler;
import mobcamo.handlers.RegistryHandler;
import mobcamo.proxy.CommonProxy;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = "MobCamo", name = "MobCamo", version = "1.0.0")
public class MobCamo
{
	public static Logger logger;
	@Instance(value = "MobCamo")
	public static MobCamo instance;
	
	@SidedProxy(clientSide = "mobcamo.proxy.ClientProxy", serverSide = "mobcamo.proxy.CommonProxy")
	public static CommonProxy proxy;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		logger = event.getModLog();
		
		ConfigHandler.LoadConfigs(event.getSuggestedConfigurationFile());
	}
	
	@EventHandler
	public void load(FMLInitializationEvent event)
	{
		RegistryHandler.RegisterItems();
		RegistryHandler.RegisterRecipes();
		CamoAIUtils.SetupCamoMap();
		
		MainEventHandler handler = new MainEventHandler();
		MinecraftForge.EVENT_BUS.register(handler);
		FMLCommonHandler.instance().bus().register(handler);
		
		CamoCraftingHandler tmp = new CamoCraftingHandler();
		CraftingManager.getInstance().getRecipeList().add(tmp);
		FMLCommonHandler.instance().bus().register(tmp);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
	}
}
