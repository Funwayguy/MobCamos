package mobcamo.handlers;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import mobcamo.ai.CamoAIUtils;
import mobcamo.handlers.properties.DropProperties;
import mobcamo.items.ArmorMob;
import mobcamo.items.ArmorSpider;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;

public class MainEventHandler
{
	public HashMap<Class<? extends EntityLivingBase>, DropProperties> camoDrops = new HashMap<Class<? extends EntityLivingBase>, DropProperties>();
	
	public void RegisterDrops()
	{
		AddDropMapping(EntityCreeper.class, RegistryHandler.creeperSkin);
		AddDropMapping(EntitySkeleton.class, RegistryHandler.boneShard);
		AddDropMapping(EntitySpider.class, RegistryHandler.spiderLeg);
		AddDropMapping(EntityGhast.class, RegistryHandler.ghastTentacle);
	}
	
	public void AddDropMapping(Class<? extends EntityLivingBase> entityLiving, Item item)
	{
		if(camoDrops.containsValue(entityLiving))
		{
			camoDrops.put(entityLiving, camoDrops.get(entityLiving).AddItem(item));
		} else
		{
			camoDrops.put(entityLiving, new DropProperties().AddItem(item));
		}
	}
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if(event.entity instanceof EntityLivingBase)
		{
			EntityLivingBase entityLiving = (EntityLivingBase)event.entity;
			
			if(CamoAIUtils.HasCamo(entityLiving) && entityLiving instanceof EntityCreature)
			{
				CamoAIUtils.ReplaceAI((EntityCreature)entityLiving);
			}
			
			CamoAIUtils.refreshTarget(event.entity);
		}
	}
	
	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		CamoAIUtils.refreshTarget(event.entityLiving);
	}
	
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event)
	{
		if(event.entityLiving.worldObj.isRemote )
		{
			return;
		}
		
		if(event.source.getEntity() != null && !(event.source.getEntity() instanceof EntityPlayer))
		{
			return;
		}
		
		if(camoDrops.containsKey(event.entityLiving.getClass()))
		{
			Item item = camoDrops.get(event.entityLiving.getClass()).getDrop(event.entityLiving.getRNG(), 25);
			
			if(item != null)
			{
				event.entityLiving.dropItem(item, 1);
			}
		}
		
		if(event.entityLiving instanceof IMob)
		{
			event.entityLiving.dropItem(RegistryHandler.mobShard, 1);
		}
	}
	
	@SubscribeEvent
	public void onEntityAttack(LivingAttackEvent event)
	{
		//Attacker
		if(event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase)
		{
			int maxTier = CamoAIUtils.getMaxTier((EntityLivingBase)event.source.getEntity());
			
			if(maxTier == 2)
			{
				List<Class<? extends EntityLivingBase>> backupTypes = CamoAIUtils.getBackupTypes((EntityLivingBase)event.source.getEntity());
				
				Iterator<Class<? extends EntityLivingBase>> iterator = backupTypes.iterator();
				
				while(iterator.hasNext())
				{
					CamoAIUtils.requestBackup((EntityLivingBase)event.source.getEntity(), event.entityLiving, iterator.next());
				}
			}
		}
		
		//Victim
		if(event.entityLiving instanceof EntityLivingBase && event.source.getEntity() != null && event.source.getEntity() instanceof EntityLivingBase)
		{
			int maxTier = CamoAIUtils.getMaxTier(event.entityLiving);
			
			if(maxTier == 2)
			{
				List<Class<? extends EntityLivingBase>> backupTypes = CamoAIUtils.getBackupTypes(event.entityLiving);
				
				Iterator<Class<? extends EntityLivingBase>> iterator = backupTypes.iterator();
				
				while(iterator.hasNext())
				{
					CamoAIUtils.requestBackup(event.entityLiving, (EntityLivingBase)event.source.getEntity(), iterator.next());
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onJump(LivingJumpEvent event)
	{
		if(CamoAIUtils.getMaxTier(event.entityLiving) == 2 && event.entityLiving.getEquipmentInSlot(1).getItem() instanceof ArmorSpider)
		{
			event.entityLiving.motionY += 0.2D;
		}
	}
	
	@SubscribeEvent
	public void onItemToolTip(ItemTooltipEvent event)
	{
		if(event.itemStack.getItem() instanceof ArmorMob)
		{
			ArmorMob armor = (ArmorMob)event.itemStack.getItem();
			event.toolTip.add("Tier: " + (armor.getTier(event.itemStack) + 1));
		}
	}
	
	@SubscribeEvent
	public void onItemCrafted(ItemCraftedEvent event)
	{
	}
}
