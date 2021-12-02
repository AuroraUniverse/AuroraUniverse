package ru.etysoft.aurorauniverse.listeners;

import com.mysql.jdbc.Buffer;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.PistonMoveReaction;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerShearEntityEvent;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.Directional;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.commands.PluginCommands;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.*;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProtectionListener implements Listener {

    @EventHandler
    public void PvP(EntityDamageByEntityEvent event) {



        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if(Permissions.isAdmin(p, false)) return;
            Resident resident = Residents.getResident(p);
            try {
                ChunkPair chunkPair = ChunkPair.fromChunk(p.getLocation().getChunk());
                if (!Towns.getTown(chunkPair).isPvp(chunkPair)) {

                    event.setCancelled(true);
                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-pvp"), (Player) event.getDamager());
                }
            } catch (Exception e) {
                //can't pass event because null
            }
        }
        else if(event.getDamager() instanceof Player)
        {
            Player p = (Player) event.getDamager();
            if(Permissions.isAdmin(p, false)) return;
            Resident resident = Residents.getResident(p);
            Town entityTown = Towns.getTown(ChunkPair.fromChunk(event.getEntity().getLocation().getChunk()));
            if(entityTown != null)
            {
                try {
                    if(entityTown != resident.getTown())
                    {
                        event.setCancelled(true);
                    }
                } catch (TownNotFoundedException e) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerShearEntity (PlayerShearEntityEvent e) {
        Region region = AuroraUniverse.getTownBlock(ChunkPair.fromChunk(e.getEntity().getLocation().getChunk()));
        if(region != null)
        {
            Town town = region.getTown();
            if(town != null)
            {
                Resident resident = Residents.getResident(e.getPlayer());
                if(resident.hasTown())
                {
                    try {
                        if(resident.getTown() != town)
                        {
                            e.setCancelled(true);
                        }
                    } catch (TownNotFoundedException ex) {
                        ex.printStackTrace();
                    }
                }

            }

        }




    }

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {

        List<Block> finalBlockList = new ArrayList<>();

        for (Block block : event.blockList().toArray(new Block[event.blockList().size()])) {
            if (Towns.getTown(block.getChunk()) == null) {
                finalBlockList.add(block);
            } else {
                if (Towns.getTown(block.getChunk()).isExplosionEnabled()) {
                    finalBlockList.add(block);
                }
            }
        }
        event.blockList().clear();
        event.blockList().addAll(finalBlockList);

    }

    @EventHandler
    public void imageOnMapDupePrevent(InventoryClickEvent event) {

        if(event.getClickedInventory() == null) return;
        if(event.getCurrentItem() == null) return;
        if (event.getClickedInventory().getType() == InventoryType.GRINDSTONE && event.getSlotType() == InventoryType.SlotType.RESULT) {
            if(event.getCurrentItem().getType() == Material.FILLED_MAP && AuroraUniverse.getInstance().getConfig().getBoolean("prevent-map-grindstone"))
            {
                event.setCancelled(true);
            }
        }
    }


    @EventHandler
    public void UseEvent(PlayerInteractEvent event) {

        if (!event.getPlayer().hasPermission("aun.edittowns")) {

            if (event.getAction() == Action.PHYSICAL) {

                Block block = event.getClickedBlock();
                if (block == null) return;
                // If the block is farmland (soil)
                if (block.getType() == Material.FARMLAND) {

                    if(Towns.getTown(block.getChunk()) != null)
                    {
                        event.setUseInteractedBlock(org.bukkit.event.Event.Result.DENY);
                        event.setCancelled(true);
                        block.setBlockData(block.getBlockData(), true);
                    }

                }
            }
            if (event.hasBlock()) {
                ChunkPair chunk = ChunkPair.fromChunk(event.getClickedBlock().getChunk());
                Block block = event.getClickedBlock();
                List<Material> materials = new ArrayList<Material>();
                materials.add(Material.CHEST);
                materials.add(Material.ENDER_CHEST);
                materials.add(Material.TRAPPED_CHEST);
                materials.add(Material.LIGHT_BLUE_SHULKER_BOX);
                materials.add(Material.CHEST_MINECART);
                materials.add(Material.SHULKER_BOX);
                materials.add(Material.RED_SHULKER_BOX);
                materials.add(Material.WHITE_SHULKER_BOX);
                materials.add(Material.YELLOW_SHULKER_BOX);
                materials.add(Material.PINK_SHULKER_BOX);
                materials.add(Material.ORANGE_SHULKER_BOX);
                materials.add(Material.MAGENTA_SHULKER_BOX);
                materials.add(Material.LIME_SHULKER_BOX);
                materials.add(Material.LIGHT_GRAY_SHULKER_BOX);
                materials.add(Material.BLACK_SHULKER_BOX);
                materials.add(Material.BLUE_SHULKER_BOX);
                materials.add(Material.BROWN_SHULKER_BOX);
                materials.add(Material.CYAN_SHULKER_BOX);
                materials.add(Material.GREEN_SHULKER_BOX);
                materials.add(Material.GRAY_SHULKER_BOX);
                materials.add(Material.FURNACE);
                materials.add(Material.GRINDSTONE);
                materials.add(Material.ANVIL);
                materials.add(Material.DAMAGED_ANVIL);
                materials.add(Material.CHIPPED_ANVIL);
                materials.add(Material.BREWING_STAND);
                materials.add(Material.ITEM_FRAME);

                // 1.14+
                try {
                    materials.add(Material.BARREL);
                } catch (Exception e) {
                    Logger.warning("Can't find Barrel material! Is the server outdated?");
                }

                if (materials.contains(block.getType())) {
                    try {
                        if (Towns.hasMyTown(chunk, Residents.getResident(event.getPlayer()).getTown())) {
                            Town town = Towns.getTown(chunk);
                            if (town != null) {
                                if (!town.canUse(Residents.getResident(event.getPlayer()), chunk)) {
                                    Logger.debug("Prevented from use[1] " + event.getPlayer().getName());
                                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-use"), event.getPlayer());
                                    event.setCancelled(true);
                                } else {

                                }
                            }
                        } else {
                            if (Towns.hasTown(chunk)) {
                                Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-use"), event.getPlayer());
                                event.setCancelled(true);
                                Logger.debug("Prevented from use[2] " + event.getPlayer().getName());
                            }
                        }
                    } catch (TownNotFoundedException ignored) {
                        if (Towns.hasTown(chunk)) {
                            Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-use"), event.getPlayer());
                            event.setCancelled(true);
                            Logger.debug("Prevented from use[3] " + event.getPlayer().getName());
                        }
                    }
                }
            }

        }
    }

    @EventHandler
    public void SwitchEvent(PlayerInteractEvent event) {
        if (!event.getPlayer().hasPermission("aun.edittowns")) {
            if (event.hasBlock()) {
                ChunkPair chunk = ChunkPair.fromChunk(event.getClickedBlock().getChunk());
                Block block = event.getClickedBlock();
                List<Material> materials = new ArrayList<Material>();

                materials.add(Material.DARK_OAK_DOOR);
                materials.add(Material.ACACIA_DOOR);
                materials.add(Material.BIRCH_DOOR);
                materials.add(Material.IRON_DOOR);
                materials.add(Material.JUNGLE_DOOR);
                materials.add(Material.OAK_DOOR);
                materials.add(Material.SPRUCE_DOOR);

                materials.add(Material.BIRCH_BUTTON);
                materials.add(Material.ACACIA_BUTTON);
                materials.add(Material.DARK_OAK_BUTTON);
                materials.add(Material.JUNGLE_BUTTON);
                materials.add(Material.OAK_BUTTON);
                materials.add(Material.SPRUCE_BUTTON);
                materials.add(Material.STONE_BUTTON);

                materials.add(Material.LEVER);

                materials.add(Material.ACACIA_PRESSURE_PLATE);
                materials.add(Material.BIRCH_PRESSURE_PLATE);
                materials.add(Material.JUNGLE_PRESSURE_PLATE);
                materials.add(Material.OAK_PRESSURE_PLATE);
                materials.add(Material.SPRUCE_PRESSURE_PLATE);
                materials.add(Material.STONE_PRESSURE_PLATE);
                materials.add(Material.DARK_OAK_PRESSURE_PLATE);
                materials.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
                materials.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
                materials.add(Material.OAK_FENCE_GATE);
                materials.add(Material.SPRUCE_FENCE_GATE);
                materials.add(Material.BIRCH_FENCE_GATE);
                materials.add(Material.JUNGLE_FENCE_GATE);

                if (materials.contains(block.getType())) {
                    try {
                        if (Towns.hasMyTown(chunk, Residents.getResident(event.getPlayer()).getTown())) {
                            Town town = Towns.getTown(chunk);
                            if (town != null) {
                                if (!town.canSwitch(Residents.getResident(event.getPlayer()), chunk)) {
                                    Logger.debug("Prevented from switch[1] " + event.getPlayer().getName());
                                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-switch"), event.getPlayer());
                                    event.setCancelled(true);
                                } else {

                                }
                            }
                        } else {
                            if (Towns.hasTown(chunk)) {
                                Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-switch"), event.getPlayer());
                                event.setCancelled(true);
                                Logger.debug("Prevented from switch[2] " + event.getPlayer().getName());
                            }
                        }
                    } catch (TownNotFoundedException ignored) {
                        if (Towns.hasTown(chunk)) {
                            Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-switch"), event.getPlayer());
                            event.setCancelled(true);
                            Logger.debug("Prevented from switch[3] " + event.getPlayer().getName());
                        }
                    }
                }
            }
        }
    }


    @EventHandler
    public void BreakBlock(BlockBreakEvent event) {
        if (PluginCommands.debugHand.contains(event.getPlayer().getName())) {
            String infoMessage = "";
            Town town = Towns.getTown(event.getBlock().getChunk());
            if (town != null) {
                Region region = town.getTownChunks().get(event.getBlock().getChunk());
                infoMessage += "Name: " + region.getRegionName() + "\n" +
                        "TownName: " + region.getTown().getName() + " (" + region.getTown().getChunksCount() + ")";

                if (region instanceof ResidentRegion) {
                    infoMessage = "[!] RESIDENT REGION \nName: " + region.getRegionName() + "\n" +
                            "TownName: " + region.getTown().getName() + " (" + region.getTown().getChunksCount() + ") + \n" + ((ResidentRegion) region).getOwner().getName();
                }
            } else {
                infoMessage = "Unowned";
            }
            if(!AuroraUniverse.containsChunk(ChunkPair.fromChunk(event.getBlock().getChunk())))
            {
                infoMessage = "Empty!";
            }
            event.getPlayer().sendMessage(infoMessage);
            event.setCancelled(true);
        }
        if (!event.getPlayer().hasPermission("aun.edittowns")) {
            try {
                if (Towns.hasMyTown(ChunkPair.fromChunk(event.getBlock().getChunk()), Residents.getResident(event.getPlayer()).getTown())) {
                    Town town = Towns.getTown(event.getBlock().getChunk());
                    if (town != null) {
                        if (!town.canDestroy(Residents.getResident(event.getPlayer()), ChunkPair.fromChunk(event.getBlock().getChunk()))) {
                            Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                            Logger.debug("Prevented from block break[1] " + event.getPlayer().getName());
                            event.setCancelled(true);
                        } else {

                        }
                    }
                } else {
                    if (Towns.hasTown(ChunkPair.fromChunk(event.getBlock().getChunk()))) {
                        Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                        event.setCancelled(true);
                        Logger.debug("Prevented from block break[2] " + event.getPlayer().getName());
                    }
                }
            } catch (TownNotFoundedException ignored) {
                if (Towns.hasTown(ChunkPair.fromChunk(event.getBlock().getChunk()))) {
                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                    event.setCancelled(true);
                    Logger.debug("Prevented from block break[3] " + event.getPlayer().getName());
                }
            }
        }
    }


    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        Block block = event.getBlock();
        ArrayList<BlockIgniteEvent.IgniteCause> causes = new ArrayList<>(Arrays.asList(BlockIgniteEvent.IgniteCause.values()));
        if (causes.contains(event.getCause())) {

            Town town = Towns.getTown(event.getBlock().getChunk());
            if (town != null) {
                if (!town.isFireAllowed(event.getBlock().getChunk())) {
                    event.setCancelled(true);
                    Logger.debug("Prevented fire spread on X:" + event.getBlock().getLocation().getBlockX() + " Y:" + event.getBlock().getLocation().getBlockY() + " Z:" + event.getBlock().getLocation().getBlockZ() + ". From " + event.getCause() + " on " + town.getName());
                }
            }
        }
    }

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        Material id = event.getBlock().getType();
        Town town = Towns.getTown(event.getBlock().getChunk());
        Town toTown = Towns.getTown(event.getToBlock().getChunk());
        if (town == null | toTown != town) {

            if ((id == Material.LAVA | id == Material.WATER) && (toTown != null)) {
                event.setCancelled(true);
            }

        }
    }

    @EventHandler
    public void PlaceBlock(BlockPlaceEvent event) {
        if (!event.getPlayer().hasPermission("aun.edittowns")) {
            try {
                if (Towns.hasMyTown(ChunkPair.fromChunk(event.getBlock().getChunk()), Residents.getResident(event.getPlayer()).getTown())) {
                    Town town = Towns.getTown(event.getBlock().getChunk());
                    if (town != null) {
                        if (!town.canBuild(Residents.getResident(event.getPlayer()), ChunkPair.fromChunk(event.getBlock().getChunk()))) {
                            Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-place"), event.getPlayer());
                            Logger.debug("Prevented from block place[1] " + event.getPlayer().getName());
                            event.setCancelled(true);
                        } else {

                        }
                    }
                } else {
                    if (Towns.hasTown(ChunkPair.fromChunk(event.getBlock().getChunk()))) {
                        Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-place"), event.getPlayer());
                        event.setCancelled(true);
                        Logger.debug("Prevented from block place[2] " + event.getPlayer().getName());
                    }
                }
            } catch (TownNotFoundedException ignored) {
                if (Towns.hasTown(ChunkPair.fromChunk(event.getBlock().getChunk()))) {
                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-place"), event.getPlayer());
                    event.setCancelled(true);
                    Logger.debug("Prevented from block place[3] " + event.getPlayer().getName());
                }
            }
        }
    }

    private boolean canBlockMove(Block block, Block blockTo) {
        Location from = block.getLocation();
        Location to = blockTo.getLocation();

        if (from == to) return true;



            if(Towns.getTown(from.getChunk()) == null && Towns.getTown(to.getChunk()) != null)
            {
                return false;
            }
            else
            {
                return true;
            }

    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {


        if (event.getBlock().getType() != Material.DISPENSER)
            return;



        if (!canBlockMove(event.getBlock(), event.getBlock().getRelative(((Directional) event.getBlock().getBlockData()).getFacing())))
            event.setCancelled(true);
    }


    @EventHandler
    public void pistonEvent(BlockPistonRetractEvent event) {

        Town town = Towns.getTown(event.getBlock().getChunk());
        for (Block block : event.getBlocks()) {




            if(town != null)
            {
               if(Towns.getTown(block.getChunk()) != town)
               {
                   event.setCancelled(true);
               }
            }
            else
            {
                if(Towns.getTown(block.getChunk()) != null)
                {
                    event.setCancelled(true);
                }
            }


        }


    }

    @EventHandler
    public void pistonEvent(BlockPistonExtendEvent event) {


        for (Block block : event.getBlocks()) {


//            if (Towns.getTown(block.getChunk()) != null && Towns.getTown(block.getChunk()) == Towns.getTown(event.getBlock().getRelative(event.getDirection()).getChunk())) {
//                event.setCancelled(true);
//            }

            if(!canBlockMove(block, block.getRelative(event.getDirection())))
            {
                event.setCancelled(true);
            }
        }


    }

    @EventHandler
    public void onPortalCreate(PortalCreateEvent event) {


        if (!event.getEntity().getType().equals(EntityType.PLAYER)) {
            return;
        }

        Player player = (Player) event.getEntity();
        Resident resident = Residents.getResident(player.getName());

        for (BlockState block : event.getBlocks()) {

            //Make decision on whether this is allowed using the PlayerCache and then a cancellable event.
            if (Towns.getTown(block.getChunk()) != null) {
                Town t = Towns.getTown(block.getChunk());


                try {
                    if(resident.getTown() != t)
                    {
                        Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-cantcreateportal"), player);
                        event.setCancelled(true);
                        return;
                    }
                } catch (TownNotFoundedException ignored) {

                }



            }
        }
    }


    @EventHandler
    public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {

        if (!event.getPlayer().hasPermission("aun.edittowns")) {
            try {
                if (Towns.hasMyTown(ChunkPair.fromChunk(event.getBlockClicked().getChunk()), Residents.getResident(event.getPlayer()).getTown())) {
                    Town town = Towns.getTown(event.getBlockClicked().getChunk());
                    if (town != null) {
                        if (!town.canBuild(Residents.getResident(event.getPlayer()), ChunkPair.fromChunk(event.getBlockClicked().getChunk()))) {
                            Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-place"), event.getPlayer());
                            Logger.debug("Prevented from block place[1] " + event.getPlayer().getName());
                            event.setCancelled(true);
                        } else {

                        }
                    }
                } else {
                    if (Towns.hasTown(ChunkPair.fromChunk(event.getBlockClicked().getChunk()))) {
                        Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-place"), event.getPlayer());
                        event.setCancelled(true);
                        Logger.debug("Prevented from block place[2] " + event.getPlayer().getName());
                    }
                }
            } catch (TownNotFoundedException ignored) {
                if (Towns.hasTown(ChunkPair.fromChunk(event.getBlockClicked().getChunk()))) {
                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-place"), event.getPlayer());
                    event.setCancelled(true);
                    Logger.debug("Prevented from block place[3] " + event.getPlayer().getName());
                }
            }
        }

    }

}
