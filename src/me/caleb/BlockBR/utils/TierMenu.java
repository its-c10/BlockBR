package me.caleb.BlockBR.utils;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import me.caleb.BlockBR.Main;

public abstract class TierMenu extends AbstractMenu implements Listener, InventoryHolder{
	
	public ArrayList<String> rewardNames = new ArrayList<String>();
	public ArrayList<Material> rewardMats = new ArrayList<Material>();
	public ArrayList<Integer> rewardAmounts = new ArrayList<Integer>();
	public ArrayList<ArrayList<Enchantment>> rewardEnchants = new ArrayList<ArrayList<Enchantment>>();
	public ArrayList<ArrayList<Integer>> rewardLevels = new ArrayList<ArrayList<Integer>>();
	public Rewards r;
	
	public TierMenu(Main plugin, String guiTitle) {
		super(plugin, guiTitle, 54, false);
		// TODO Auto-generated constructor stub
	}
	
	public void initializeRewards(String tier, int level) {
		
		r = new Rewards(plugin);
		
		rewardAmounts = r.getAllRewardsAmounts(tier, level);
		rewardMats = r.getAllRewardsMaterials(tier, level);
		rewardNames = r.getAllRewardsNames(tier, level);
		rewardEnchants = r.getAllRewardsEnchants(tier, level);
		rewardLevels = r.getAllRewardsEnchantmentLevels(tier, level);
		
		try {
			for(int x = 0;x <= 53;x++) {
				
				if(x < rewardAmounts.size()) {
					
					ArrayList<Enchantment> itemEnchants;
					ArrayList<Integer> itemLevels;
		
					if(!rewardEnchants.get(x).isEmpty()) {
						
						itemEnchants = rewardEnchants.get(x);
						itemLevels = rewardLevels.get(x);
						
						inv.addItem(createGuiItem(rewardMats.get(x), Chat.chat("&l" + rewardNames.get(x)),itemEnchants,itemLevels,rewardAmounts.get(x)));
						
					}else {
						inv.addItem(createGuiItem(rewardMats.get(x), Chat.chat("&l" + rewardNames.get(x)),rewardAmounts.get(x)));
					}

				}else {
					//If the spot is null, then check if the spot is 28. If it's 28 then set it to red wool. If not, then set it to air
					if(inv.getItem(x) == null) {
						if(x == 45) {
							inv.addItem(createGuiItem(Material.RED_WOOL,Chat.chat("&lGO BACK"),Chat.chat("Click this to go back to the previous page")));
						}else {
							inv.setItem(x, createGuiItem());
						}
					}
					
				}
				
			}
		}catch(IndexOutOfBoundsException e){
			e.printStackTrace();
		}
	}
	
	@EventHandler
    public void onInventoryClick(InventoryClickEvent e){
		if (e.getInventory().getHolder() != this) {
            return;
        }
        
        if (e.getClick().equals(ClickType.NUMBER_KEY)){
            e.setCancelled(true);
        }
        
        e.setCancelled(true);

        Player p = (Player) e.getWhoClicked();
        ItemStack clickedItem = e.getCurrentItem();
        
        // verify current item is not null
        if (clickedItem == null || clickedItem.getType() == Material.AIR) return;
       
        if (e.getRawSlot() == 45) {
        	
        	p.closeInventory();
        	RewardsMenu rm = new RewardsMenu(plugin, "Rewards", 18, true);
        	gi.getInfo(p);
        	rm.initializeItems(gi.tier, gi.amount, gi.level, gi.threshold);
        	rm.openInventory(p);
        }
        
    }
	
}
