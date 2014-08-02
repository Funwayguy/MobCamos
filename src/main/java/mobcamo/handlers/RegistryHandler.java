package mobcamo.handlers;

import cpw.mods.fml.common.registry.GameRegistry;
import mobcamo.items.ArmorCreeper;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;

public class RegistryHandler
{
	public static Item creeperHat;
	public static Item creeperShirt;
	public static Item creeperPants;
	public static Item creeperShoes;
	
	public static CreativeTabs camoTab = new CreativeTabs("Mob Camos")
	{
		@Override
		public Item getTabIconItem()
		{
			return creeperHat;
		}
	};
	
	/*public static CreativeTabs skinsTab = new CreativeTabs("Mob Skins")
	{
		@Override
		public Item getTabIconItem()
		{
			return RegistryHandler.creeperSkin;
		}
	};*/
	
	public static void RegisterItems()
	{
		creeperHat = (ItemArmor)new ArmorCreeper(ItemArmor.ArmorMaterial.IRON, 4, 0);
		creeperShirt = (ItemArmor)new ArmorCreeper(ItemArmor.ArmorMaterial.IRON, 4, 1);
		creeperPants = (ItemArmor)new ArmorCreeper(ItemArmor.ArmorMaterial.IRON, 4, 2);
		creeperShoes = (ItemArmor)new ArmorCreeper(ItemArmor.ArmorMaterial.IRON, 4, 3);
		
		GameRegistry.registerItem(creeperHat, "creeperCamoHat");
		GameRegistry.registerItem(creeperShirt, "creeperCamoShirt");
		GameRegistry.registerItem(creeperPants, "creeperCamoPants");
		GameRegistry.registerItem(creeperShoes, "creeperCamoShoes");
	}
	
	public static void RegisterRecipes()
	{
	}
}
