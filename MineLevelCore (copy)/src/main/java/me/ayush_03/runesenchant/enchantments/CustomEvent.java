package me.ayush_03.runesenchant.enchantments;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class CustomEvent extends BlockBreakEvent {

   public CustomEvent(Block block, Player player) {
      super(block, player);
   }
}
