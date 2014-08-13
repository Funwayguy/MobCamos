package mobcamo.handlers;

import mobcamo.items.ArmorMob;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.world.World;

public class CamoCraftingHandler implements IRecipe
{
	public ItemStack itemOut;
	
	public CamoCraftingHandler()
	{
	}

	@Override
	public boolean matches(InventoryCrafting inv, World world)
	{
		if(!inv.getInventoryName().equals("container.crafting"))
		{
			return false;
		}
		
		ItemStack shard = null;
		ItemStack armor = null;
		
		for(int i = 0; i < inv.getSizeInventory(); i++)
		{
			ItemStack stack = inv.getStackInSlot(i);
			
			if(stack == null)
			{
				continue;
			}
			
			if(stack.getItem() instanceof ArmorMob)
			{
				if(armor == null)
				{
					if(((ArmorMob)stack.getItem()).getTier(stack) >= 2)
					{
						return false;
					}
					
					armor = stack;
				} else
				{
					return false;
				}
			} else if(stack.getItem() == RegistryHandler.mobShard)
			{
				if(shard == null)
				{
					shard = stack;
				} else
				{
					return false;
				}
			}
		}
		
		if(shard != null && armor != null)
		{
			itemOut = armor.copy();
			((ArmorMob)itemOut.getItem()).setTier(itemOut, ((ArmorMob)armor.getItem()).getTier(armor) + 1);
			return true;
		} else
		{
			return false;
		}
	}

	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
	{
		return this.getRecipeOutput();
	}

	@Override
	public int getRecipeSize()
	{
		return 2;
	}

	@Override
	public ItemStack getRecipeOutput()
	{
		return itemOut;
	}
	
}
