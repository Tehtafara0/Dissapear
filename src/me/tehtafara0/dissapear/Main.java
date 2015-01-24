package me.tehtafara0.dissapear;
 
import java.util.ArrayList;
 
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
 
public class Main extends JavaPlugin implements Listener {
       
        private ArrayList<String> usingClock;
       
        public void onEnable() {
                Bukkit.getServer().getPluginManager().registerEvents(this, this);
               
                this.usingClock = new ArrayList<String>();
        }
       
        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent e) {
                if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) {
                        return;
                }
               
                if (e.getItem().getType() != Material.WATCH) {
                        return;
                }
               
                if (
                                !e.getItem().hasItemMeta() ||
                                !e.getItem().getItemMeta().hasDisplayName() ||
                                !e.getItem().getItemMeta().getDisplayName().equals("Hide/Unhide Players")
                ) {
                        return;
                }
               
                if (usingClock.contains(e.getPlayer().getName())) { // Turning it off.
                        usingClock.remove(e.getPlayer().getName());
                       
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                if (p != e.getPlayer()) {
                                        e.getPlayer().showPlayer(p);                                   
                                }
                        }
                }
               
                else { // Turning it on.
                        usingClock.add(e.getPlayer().getName());
                       
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                if (p != e.getPlayer()) {
                                        e.getPlayer().hidePlayer(p);
                                }
                        }
                }
        }
       
        @EventHandler
        public void onPlayerJoin(PlayerJoinEvent e) { // This could be expensive to run every time someone joins.
                // Check if the player already has a clock, or just don't do this here (maybe when the player joins for the first time).
                ItemStack magicClock = new ItemStack(Material.WATCH, 1);
               
                ItemMeta magicClockMeta = magicClock.getItemMeta();
                magicClockMeta.setDisplayName("Hide/Unhide Players");
                magicClock.setItemMeta(magicClockMeta);
               
                e.getPlayer().getInventory().addItem(magicClock);
               
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        if (p != e.getPlayer()) {
                                if (usingClock.contains(p.getName())) {
                                        p.hidePlayer(e.getPlayer()); // If they are currently using the clock, hide the new player.
                                }
                               
                                else {
                                        p.showPlayer(e.getPlayer()); // Else, show the new player.
                                }
                        }
                }
        }
}