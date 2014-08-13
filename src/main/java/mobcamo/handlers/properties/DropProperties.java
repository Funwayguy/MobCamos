package mobcamo.handlers.properties;

import java.util.ArrayList;
import java.util.Random;
import net.minecraft.item.Item;

public class DropProperties
{
	public ArrayList<Item> drops = new ArrayList<Item>();
	
	public DropProperties AddItem(Item item)
	{
		drops.add(item);
		return this;
	}
	
	public Item getDrop(Random rand, int rarity)
	{
		if(rarity <= 1 || rand.nextInt(rarity) == 0)
		{
			return drops.get(rand.nextInt(drops.size() - 1));
		} else
		{
			return null;
		}
	}
}
