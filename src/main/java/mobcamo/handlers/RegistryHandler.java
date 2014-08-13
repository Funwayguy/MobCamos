package mobcamo.handlers;

import cpw.mods.fml.common.registry.GameRegistry;
import mobcamo.items.ArmorCreeper;
import mobcamo.items.ArmorGhast;
import mobcamo.items.ArmorSkeleton;
import mobcamo.items.ArmorSpider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;

public class RegistryHandler
{
	public static Item mobShard;
	
	public static ArmorMaterial creeperArmor;
	public static Item creeperSkin;
	
	public static ItemArmor creeperHat;
	public static ItemArmor creeperShirt;
	public static ItemArmor creeperPants;
	public static ItemArmor creeperShoes;
	
	public static ArmorMaterial skeletonArmor;
	public static Item boneShard;
	
	public static ItemArmor skeletonHat;
	public static ItemArmor skeletonShirt;
	public static ItemArmor skeletonPants;
	public static ItemArmor skeletonShoes;
	
	public static ArmorMaterial spiderArmor;
	public static Item spiderLeg;
	
	public static ItemArmor spiderHat;
	public static ItemArmor spiderShirt;
	public static ItemArmor spiderPants;
	public static ItemArmor spiderShoes;
	
	public static ArmorMaterial ghastArmor;
	public static Item ghastTentacle;
	
	public static ItemArmor ghastHat;
	public static ItemArmor ghastShirt;
	public static ItemArmor ghastPants;
	public static ItemArmor ghastShoes;
	
	public static CreativeTabs camoTab = new CreativeTabs("Mob Camos")
	{
		@Override
		public Item getTabIconItem()
		{
			return creeperHat;
		}
	};
	
	public static CreativeTabs skinsTab = new CreativeTabs("Mob Skins")
	{
		@Override
		public Item getTabIconItem()
		{
			return RegistryHandler.creeperSkin;
		}
	};
	
	public static void RegisterItems()
	{
		mobShard = new Item().setTextureName("nether_star").setUnlocalizedName("mobShard").setCreativeTab(skinsTab);
		GameRegistry.registerItem(mobShard, "mobShard");
		
		creeperArmor = EnumHelper.addArmorMaterial("creeperArmor", 100, new int[]{2, 6, 5, 2}, 0);
		creeperArmor.customCraftingMaterial = creeperSkin;
		creeperSkin = new Item().setTextureName("leather").setUnlocalizedName("creeperSkin").setCreativeTab(skinsTab);
		
		creeperHat = (ItemArmor)new ArmorCreeper(creeperArmor, 4, 0).setUnlocalizedName("creeperCamoHat");
		creeperShirt = (ItemArmor)new ArmorCreeper(creeperArmor, 4, 1).setUnlocalizedName("creeperCamoShirt");
		creeperPants = (ItemArmor)new ArmorCreeper(creeperArmor, 4, 2).setUnlocalizedName("creeperCamoPants");
		creeperShoes = (ItemArmor)new ArmorCreeper(creeperArmor, 4, 3).setUnlocalizedName("creeperCamoShoes");
		
		GameRegistry.registerItem(creeperSkin, "creeperSkin");
		GameRegistry.registerItem(creeperHat, "creeperCamoHat");
		GameRegistry.registerItem(creeperShirt, "creeperCamoShirt");
		GameRegistry.registerItem(creeperPants, "creeperCamoPants");
		GameRegistry.registerItem(creeperShoes, "creeperCamoShoes");
		
		skeletonArmor = EnumHelper.addArmorMaterial("skeletonArmor", 100, new int[]{2, 6, 5, 2}, 0);
		skeletonArmor.customCraftingMaterial = boneShard;
		boneShard = new Item().setTextureName("leather").setUnlocalizedName("boneShard").setCreativeTab(skinsTab);
		
		skeletonHat = (ItemArmor)new ArmorSkeleton(skeletonArmor, 4, 0).setUnlocalizedName("skeletonCamoHat");
		skeletonShirt = (ItemArmor)new ArmorSkeleton(skeletonArmor, 4, 1).setUnlocalizedName("skeletonCamoShirt");
		skeletonPants = (ItemArmor)new ArmorSkeleton(skeletonArmor, 4, 2).setUnlocalizedName("skeletonCamoPants");
		skeletonShoes = (ItemArmor)new ArmorSkeleton(skeletonArmor, 4, 3).setUnlocalizedName("skeletonCamoShoes");
		
		GameRegistry.registerItem(boneShard, "boneShard");
		GameRegistry.registerItem(skeletonHat, "skeletonCamoHat");
		GameRegistry.registerItem(skeletonShirt, "skeletonCamoShirt");
		GameRegistry.registerItem(skeletonPants, "skeletonCamoPants");
		GameRegistry.registerItem(skeletonShoes, "skeletonCamoShoes");
		
		spiderArmor = EnumHelper.addArmorMaterial("spiderArmor", 100, new int[]{2, 6, 5, 2}, 0);
		spiderArmor.customCraftingMaterial = spiderLeg;
		spiderLeg = new Item().setTextureName("leather").setUnlocalizedName("spiderLeg").setCreativeTab(skinsTab);
		
		spiderHat = (ItemArmor)new ArmorSpider(spiderArmor, 4, 0).setUnlocalizedName("spiderCamoHat");
		spiderShirt = (ItemArmor)new ArmorSpider(spiderArmor, 4, 1).setUnlocalizedName("spiderCamoShirt");
		spiderPants = (ItemArmor)new ArmorSpider(spiderArmor, 4, 2).setUnlocalizedName("spiderCamoPants");
		spiderShoes = (ItemArmor)new ArmorSpider(spiderArmor, 4, 3).setUnlocalizedName("spiderCamoShoes");
		
		GameRegistry.registerItem(spiderLeg, "spiderLeg");
		GameRegistry.registerItem(spiderHat, "spiderCamoHat");
		GameRegistry.registerItem(spiderShirt, "spiderCamoShirt");
		GameRegistry.registerItem(spiderPants, "spiderCamoPants");
		GameRegistry.registerItem(spiderShoes, "spiderCamoShoes");
		
		ghastArmor = EnumHelper.addArmorMaterial("ghastArmor", 100, new int[]{2, 6, 5, 2}, 0);
		ghastArmor.customCraftingMaterial = ghastTentacle;
		ghastTentacle = new Item().setTextureName("leather").setUnlocalizedName("ghastTentacle").setCreativeTab(skinsTab);
		
		ghastHat = (ItemArmor)new ArmorGhast(ghastArmor, 4, 0).setUnlocalizedName("ghastCamoHat");
		ghastShirt = (ItemArmor)new ArmorGhast(ghastArmor, 4, 1).setUnlocalizedName("ghastCamoShirt");
		ghastPants = (ItemArmor)new ArmorGhast(ghastArmor, 4, 2).setUnlocalizedName("ghastCamoPants");
		ghastShoes = (ItemArmor)new ArmorGhast(ghastArmor, 4, 3).setUnlocalizedName("ghastCamoShoes");
		
		GameRegistry.registerItem(ghastTentacle, "ghastTentacle");
		GameRegistry.registerItem(ghastHat, "ghastHat");
		GameRegistry.registerItem(ghastShirt, "ghastShirt");
		GameRegistry.registerItem(ghastPants, "ghastPants");
		GameRegistry.registerItem(ghastShoes, "ghastShoes");
	}
	
	public static void RegisterRecipes()
	{
		GameRegistry.addRecipe(new ItemStack(creeperHat), "xxx", "x x", 'x', new ItemStack(creeperSkin));
		GameRegistry.addRecipe(new ItemStack(creeperShirt), "x x", "xxx", "xxx", 'x', new ItemStack(creeperSkin));
		GameRegistry.addRecipe(new ItemStack(creeperPants), "xxx", "x x", "x x", 'x', new ItemStack(creeperSkin));
		GameRegistry.addRecipe(new ItemStack(creeperShoes), "x x", "x x", 'x', new ItemStack(creeperSkin));
		
		GameRegistry.addRecipe(new ItemStack(skeletonHat), "xxx", "x x", 'x', new ItemStack(boneShard));
		GameRegistry.addRecipe(new ItemStack(skeletonShirt), "x x", "xxx", "xxx", 'x', new ItemStack(boneShard));
		GameRegistry.addRecipe(new ItemStack(skeletonPants), "xxx", "x x", "x x", 'x', new ItemStack(boneShard));
		GameRegistry.addRecipe(new ItemStack(skeletonShoes), "x x", "x x", 'x', new ItemStack(boneShard));
	}
}
