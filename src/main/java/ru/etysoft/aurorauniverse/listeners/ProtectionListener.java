package ru.etysoft.aurorauniverse.listeners;


import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.event.world.PortalCreateEvent;
import org.bukkit.potion.PotionEffect;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.commands.PluginCommands;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.events.InTownBlockBreakEvent;
import ru.etysoft.aurorauniverse.events.InTownBlockPlaceEvent;
import ru.etysoft.aurorauniverse.exceptions.TownNotFoundedException;
import ru.etysoft.aurorauniverse.structures.StructurePatterns;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Permissions;
import ru.etysoft.aurorauniverse.world.*;


import java.util.*;

public class ProtectionListener implements Listener {

    private static HashMap<String, Long> ktList = new HashMap<>();

    public static boolean isInBattle(Player pl) {
        if (!ktList.containsKey(pl.getName())) return false;
        long timeLastInBattle = ktList.get(pl.getName());
        if (System.currentTimeMillis() - timeLastInBattle < AuroraUniverse.getInstance().getConfig().getInt("time-battle")) {
            return true;
        } else {
            return false;
        }
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void enderPearlThrown(PlayerTeleportEvent event) {
        if (event.getCause() != PlayerTeleportEvent.TeleportCause.ENDER_PEARL) return;
        Location to = event.getTo();
        Location from = event.getFrom();

        ChunkPair chunkPair = ChunkPair.fromChunk(event.getTo().getChunk());
        Town town = Towns.getTown(chunkPair);
        if (town != null) {
            if (!town.isPvp(chunkPair) && ProtectionListener.isInBattle(event.getPlayer())) {
                event.setCancelled(true);
            }
        }


    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void EntityPotionEffectEvent(EntityPotionEffectEvent event) {

        Entity entity = event.getEntity();

        PotionEffect effect = event.getNewEffect();
        if (effect != null) {
            if (effect.getType().getName().equals("POISON")) {
                if (event.getCause().name().equals("POTION_SPLASH")) {
                    Chunk chunk = entity.getLocation().getChunk();
                    Town town = Towns.getTown(chunk);

                    if (town != null) {

                        if (!town.isPvp(ChunkPair.fromChunk(chunk))) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }

    }


    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void Death(PlayerDeathEvent deathEvent) {
        ktList.remove(deathEvent.getEntity().getName());
    }

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGHEST)
    public void PvP(EntityDamageByEntityEvent event) {

        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if (Permissions.isAdmin(event.getDamager(), false)) return;
            Resident resident = Residents.getResident(p);
            try {
                ChunkPair chunkPair = ChunkPair.fromChunk(p.getLocation().getChunk());
                if (!Towns.getTown(chunkPair).isPvp(chunkPair)) {

                    event.setCancelled(true);
                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-pvp"), (Player) event.getDamager());
                }
            } catch (Exception e) {
                //can't pass event because null
                if (event.getDamager() instanceof Player) {
                    ktList.put(event.getDamager().getName(), System.currentTimeMillis());
                    ktList.put(p.getName(), System.currentTimeMillis());
                }

            }
        } else if (event.getDamager() instanceof Player) {
            Player p = (Player) event.getDamager();
            if (Permissions.isAdmin(p, false)) return;
            Resident resident = Residents.getResident(p);

            Chunk chunk = event.getEntity().getLocation().getChunk();
            Town entityTown = Towns.getTown(ChunkPair.fromChunk(chunk));

            if (entityTown == null) {
                return;
            }

            Region region = entityTown.getRegion(p.getLocation());

            if (region instanceof ResidentRegion) {

                if (!((ResidentRegion) region).getMembers().contains(p.getName())) {
                    if (!(p.hasPermission("town.region.*") && entityTown.getResidents().contains(Residents.getResident(p.getName())))) {
                        event.setCancelled(true);
                    }
                }
            } else {
                try {
                    if (entityTown != resident.getTown()) {
                        event.setCancelled(true);
                    }
                } catch (TownNotFoundedException e) {
                    event.setCancelled(true);
                }
            }

        } else if (event.getDamager() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getDamager();


            if (arrow.getShooter() instanceof Player) {
                Player p = (Player) arrow.getShooter();
                if (Permissions.isAdmin(p, false)) return;
                Resident resident = Residents.getResident(p);
                Chunk chunk = event.getEntity().getLocation().getChunk();
                Town entityTown = Towns.getTown(ChunkPair.fromChunk(chunk));

                if (entityTown == null) {
                    return;
                }

                Region region = entityTown.getRegion(event.getEntity().getLocation());

                if (region instanceof ResidentRegion) {
                    if (!((ResidentRegion) region).getMembers().contains(p.getName())) {
                        if (!(p.hasPermission("town.region.*") && entityTown.getResidents().contains(Residents.getResident(p.getName())))) {
                            event.setCancelled(true);
                        }
                    }
                } else {
                    try {
                        if (entityTown != resident.getTown()) {
                            event.setCancelled(true);
                        }
                    } catch (TownNotFoundedException e) {
                        event.setCancelled(true);
                    }
                }
            }


        } else {
            Entity entity = event.getEntity();
            ChunkPair chunkPair = ChunkPair.fromChunk(entity.getLocation().getChunk());
            Town t = Towns.getTown(chunkPair);
            if (t != null) {
                if (!t.isPvp(chunkPair)) {
                    event.setCancelled(true);
                }
            }
        }

        if (event.getEntity() instanceof ItemFrame && event.getDamager() instanceof Player) {

            Player player = (Player) event.getDamager();
            if (!player.hasPermission("aun.edittowns")) {
                Location location = event.getEntity().getLocation();

                Town town = Towns.getTown(location.getChunk());

                if (town != null) {
                    Region region = town.getRegion(location);

                    if (region instanceof ResidentRegion) {
                        if (!((ResidentRegion) region).getMembers().contains(player.getName())) {
                            if (!(player.hasPermission("town.region.*") && town.getResidents().contains(Residents.getResident(player.getName())))) {
                                Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-break"), player);
                                Logger.debug("Prevented from block break[100] " + player.getName());
                                event.setCancelled(true);
                            }
                        }
                    } else {
                        Resident resident = Residents.getResident(player.getName());
                        try {
                            if (town != resident.getTown()) {
                                event.setCancelled(true);
                            }
                        } catch (TownNotFoundedException e) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerShearEntity(PlayerShearEntityEvent e) {
        Region region = AuroraUniverse.getTownBlock(ChunkPair.fromChunk(e.getEntity().getLocation().getChunk()));
        if (region != null) {
            Town town = region.getTown();
            if (town != null) {
                Resident resident = Residents.getResident(e.getPlayer());
                if (resident.hasTown()) {
                    try {
                        if (resident.getTown() != town) {
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
            Town town = Towns.getTown(block.getChunk());
            Logger.error("huy");
            if (town == null) {
                finalBlockList.add(block);
            } else {
                if (town.isExplosionEnabled(ChunkPair.fromChunk(block.getChunk()))) {
                    InTownBlockBreakEvent blockBrake = new InTownBlockBreakEvent(town, block);
                    Bukkit.getPluginManager().callEvent(blockBrake);
                    Logger.error("aaaaaaa");

                    if (!blockBrake.isCancelled()) {
                        finalBlockList.add(block);
                        Logger.error("hello!");
                    }
                }
            }
        }
        event.blockList().clear();
        event.blockList().addAll(finalBlockList);

    }

    @EventHandler
    public void onBlockExplode(BlockExplodeEvent event) {

        List<Block> finalBlockList = new ArrayList<>();

        for (Block block : event.blockList().toArray(new Block[event.blockList().size()])) {
            Town town = Towns.getTown(block.getChunk());
            if (town == null) {
                finalBlockList.add(block);
            } else {
                if (town.isExplosionEnabled(ChunkPair.fromChunk(block.getChunk()))) {
                    InTownBlockBreakEvent blockBrake = new InTownBlockBreakEvent(town, block);
                    Bukkit.getPluginManager().callEvent(blockBrake);
                    Logger.error("aaaaaaa");

                    if (!blockBrake.isCancelled()) {
                        finalBlockList.add(block);
                        Logger.error("hello!");
                    }
                }
            }
        }
        event.blockList().clear();
        event.blockList().addAll(finalBlockList);

    }

    @EventHandler
    public void imageOnMapDupePrevent(InventoryClickEvent event) {

        if (event.getCurrentItem() == null) return;
        if (event.getClickedInventory() == null) return;

        if (event.getClickedInventory().getType() == InventoryType.GRINDSTONE && event.getSlotType() == InventoryType.SlotType.RESULT) {
            if (event.getCurrentItem().getType() == Material.FILLED_MAP && AuroraUniverse.getInstance().getConfig().getBoolean("prevent-map-grindstone")) {
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

                    if (Towns.getTown(block.getChunk()) != null) {
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


                materials.add(Material.ITEM_FRAME);
                materials.addAll(MaterialGroups.getContainers());

                if (materials.contains(block.getType())) {
                    try {
                        if (Towns.getTown(event.getClickedBlock().getLocation().getChunk()) != null) {
                            if (Towns.getTown(event.getClickedBlock().getChunk()).getResidentRegion(ChunkPair.fromChunk(event.getClickedBlock().getChunk())) != null) {
                                ResidentRegion residentRegion = Towns.getTown(event.getClickedBlock().getChunk()).getResidentRegion(ChunkPair.fromChunk(event.getClickedBlock().getChunk()));
                                Resident resident = Residents.getResident(event.getPlayer().getName());
                                if ((!residentRegion.getMembers().contains(resident.getName()))) {
                                    if (!resident.getName().equals(residentRegion.getOwnerName())) {
                                        if (!(event.getPlayer().hasPermission("town.region.*") && residentRegion.getTown().getResidents().contains(resident))) {

                                            event.setCancelled(true);
                                            Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("region-event-canceled"), event.getPlayer());
                                            Logger.debug("Prevented from use[1] " + event.getPlayer().getName());
                                        }
                                    }
                                }
                            } else if (Towns.hasMyTown(chunk, Residents.getResident(event.getPlayer()).getTown())) {
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

                materials.addAll(MaterialGroups.Switchable.getDoors());

                materials.addAll(MaterialGroups.Switchable.getTrapdoors());
                materials.addAll(MaterialGroups.Switchable.getGates());
                materials.addAll(MaterialGroups.Switchable.getPlates());

                materials.addAll(MaterialGroups.Switchable.getButtons());

                materials.add(Material.LEVER);
                materials.add(Material.ITEM_FRAME);


                if (materials.contains(block.getType())) {
                    try {
                        if (Towns.getTown(event.getClickedBlock().getLocation().getChunk()) != null) {
                            if (Towns.getTown(event.getClickedBlock().getChunk()).getResidentRegion(ChunkPair.fromChunk(event.getClickedBlock().getChunk())) != null) {
                                ResidentRegion residentRegion = Towns.getTown(event.getClickedBlock().getChunk()).getResidentRegion(ChunkPair.fromChunk(event.getClickedBlock().getChunk()));
                                Resident resident = Residents.getResident(event.getPlayer().getName());
                                if ((!residentRegion.getMembers().contains(resident.getName()))) {
                                    if (!resident.getName().equals(residentRegion.getOwnerName())) {
                                        if (!(event.getPlayer().hasPermission("town.region.*") && residentRegion.getTown().getResidents().contains(resident))) {

                                            event.setCancelled(true);
                                            Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("region-event-canceled"), event.getPlayer());
                                            Logger.debug("Prevented from switch[1] " + event.getPlayer().getName());
                                        }
                                    }
                                }
                            } else if (Towns.hasMyTown(chunk, Residents.getResident(event.getPlayer()).getTown())) {
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
    public void SwitchEvent(PlayerInteractEntityEvent event) {

        // Prevent item rotation
        if (!event.getPlayer().hasPermission("aun.edittowns")) {

            if (event.getRightClicked() instanceof ItemFrame) {

                ChunkPair chunk = ChunkPair.fromChunk(event.getRightClicked().getLocation().getChunk());


                try {
                    if (Towns.getTown(event.getRightClicked().getLocation().getChunk()) != null) {
                        if (Towns.getTown(event.getRightClicked().getLocation().getChunk()).getResidentRegion(ChunkPair.fromChunk(event.getRightClicked().getLocation().getChunk())) != null) {
                            ResidentRegion residentRegion = Towns.getTown(event.getRightClicked().getLocation().getChunk()).getResidentRegion(ChunkPair.fromChunk(event.getRightClicked().getLocation().getChunk()));
                            Resident resident = Residents.getResident(event.getPlayer().getName());
                            if ((!residentRegion.getMembers().contains(resident.getName()))) {
                                if (!resident.getName().equals(residentRegion.getOwnerName())) {
                                    if (!(event.getPlayer().hasPermission("town.region.*") && residentRegion.getTown().getResidents().contains(resident))) {

                                        event.setCancelled(true);
                                        Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("region-event-canceled"), event.getPlayer());
                                        Logger.debug("Prevented from switch[1] " + event.getPlayer().getName());
                                    }
                                }
                            }
                        } else if (Towns.hasMyTown(chunk, Residents.getResident(event.getPlayer()).getTown())) {
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


    @EventHandler
    public void BreakBlock(BlockBreakEvent event) {
        if (PluginCommands.debugHand.contains(event.getPlayer().getName())) {
            StructurePatterns.bufferFrom = event.getBlock().getLocation();
            String infoMessage = "";
            Town town = Towns.getTown(event.getBlock().getChunk());
            if (town != null) {
                Region region = town.getTownChunks().get(event.getBlock().getChunk());
                infoMessage += "Name: " + region.getRegionName() + "\n" +
                        "TownName: " + region.getTown().getName() + " (" + region.getTown().getChunksCount() + ")";

                if (region instanceof ResidentRegion) {
                    infoMessage = "[!] RESIDENT REGION \nName: " + region.getRegionName() + "\n" +
                            "TownName: " + region.getTown().getName() + " (" + region.getTown().getChunksCount() + ") + \n" + ((ResidentRegion) region).getOwnerName();
                }
            } else {
                infoMessage = "Unowned";
            }
            if (!AuroraUniverse.containsChunk(ChunkPair.fromChunk(event.getBlock().getChunk()))) {
                infoMessage = "Empty!";
            }
            event.getPlayer().sendMessage(infoMessage);
            event.setCancelled(true);
        }
        if (!event.getPlayer().hasPermission("aun.edittowns")) {
            Town town = Towns.getTown(event.getBlock().getChunk());
            if (town != null) {
                ResidentRegion residentRegion = town.getResidentRegion(ChunkPair.fromChunk(event.getBlock().getChunk()));
                Resident resident = Residents.getResident(event.getPlayer().getName());
                if (residentRegion != null) {
                    if ((!residentRegion.getMembers().contains(resident.getName()))) {
                        if (!resident.getName().equals(residentRegion.getOwnerName())) {
                            if (!(event.getPlayer().hasPermission("town.region.*") && residentRegion.getTown().getResidents().contains(resident))) {
                                event.setCancelled(true);
                                Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("region-event-canceled"), event.getPlayer());
                                Logger.debug("Prevented from break[1] " + event.getPlayer().getName());
                            }
                        }
                    }
                } else if (town.getResidents().contains(resident)) {
                    if (!town.canDestroy(Residents.getResident(event.getPlayer()), ChunkPair.fromChunk(event.getBlock().getChunk()))) {
                        Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                        Logger.debug("Prevented from block break[1] " + event.getPlayer().getName());
                        event.setCancelled(true);

                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                    event.setCancelled(true);
                    Logger.debug("Prevented from block break[2] " + event.getPlayer().getName());

                }

                if (!event.isCancelled()) {
                    InTownBlockBreakEvent blockBrake = new InTownBlockBreakEvent(town, event.getBlock());
                    Bukkit.getPluginManager().callEvent(blockBrake);

                    if (blockBrake.isCancelled()) {
                        Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-break"), event.getPlayer());
                        event.setCancelled(true);
                        Logger.debug("Prevented from block break " + event.getPlayer().getName() + " by InTownBlockBrakeEvent");
                    }
                }
            }
        } else {
            Town town = Towns.getTown(event.getBlock().getChunk());

            if (town != null) {
                InTownBlockBreakEvent blockBrake = new InTownBlockBreakEvent(town, event.getBlock());
                Bukkit.getPluginManager().callEvent(blockBrake);
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
                if (!town.setFireAllowed(block)) {
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
    public void onStructureGrow(BlockFertilizeEvent event) {
        if (event.getPlayer() != null) {
            if (event.getPlayer().hasPermission("aun.edittowns")) return;
        }
        for (BlockState blockState : event.getBlocks()) {
            if (Towns.hasTown(blockState.getBlock().getLocation())) {
                Resident resident = Residents.getResident(event.getPlayer());
                if (resident == null) return;
                try {
                    if (Towns.getTown(blockState.getBlock().getChunk()) != resident.getTown()) {
                        event.setCancelled(true);
                    }
                } catch (TownNotFoundedException e) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void PlaceBlock(BlockPlaceEvent event) {

        if (!canBlockPlace(event.getPlayer(), event.getBlock())) {
            event.setBuild(false);
            event.setCancelled(true);
        }
    }

    public boolean canBlockPlace(Player player, Block block) {
        boolean result = true;

        if (PluginCommands.debugHand.contains(player.getName())) {
            StructurePatterns.bufferTo = player.getLocation();
            player.sendMessage("Second point is selected!");
            result = false;
        }
        if (!player.hasPermission("aun.edittowns")) {
            Town town = Towns.getTown(block.getChunk());
            if (town != null) {
                Resident resident = Residents.getResident(player.getName());
                if (town.getResidentRegion(ChunkPair.fromChunk(block.getChunk())) != null) {
                    ResidentRegion residentRegion = Towns.getTown(block.getChunk()).getResidentRegion(ChunkPair.fromChunk(block.getChunk()));
                    if ((!residentRegion.getMembers().contains(resident.getName()))) {
                        if (!resident.getName().equals(residentRegion.getOwnerName())) {
                            if (!(player.hasPermission("town.region.*") && residentRegion.getTown().getResidents().contains(resident))) {

                                Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("region-event-canceled"), player);
                                Logger.debug("Prevented from place[1] " + player.getName());
                                result = false;
                            }
                        }
                    }
                } else if (town.getResidents().contains(resident)) {
                    if (!town.canBuild(Residents.getResident(player), ChunkPair.fromChunk(block.getChunk()))) {
                        Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-place"), player);
                        Logger.debug("Prevented from block place[1] " + player.getName());
                        result = false;
                    }
                } else {
                    Messaging.sendPrefixedMessage(AuroraUniverse.getLanguage().getString("town-block-place"), player);
                    Logger.debug("Prevented from block place[2] " + player.getName());
                    result = false;
                }

                if (result) {
                    InTownBlockPlaceEvent blockPlaceEvent = new InTownBlockPlaceEvent(town, block);
                    Bukkit.getPluginManager().callEvent(blockPlaceEvent);

                    if (blockPlaceEvent.isCancelled()) {
                        result = false;
                    }
                }
            }
        } else {
            Town town = Towns.getTown(block.getChunk());

            if (town != null) {
                InTownBlockPlaceEvent blockPlaceEvent = new InTownBlockPlaceEvent(town, block);
                Bukkit.getPluginManager().callEvent(blockPlaceEvent);
            }
        }
        return result;
    }

    private boolean canBlockMove(Block block, Block blockTo) {
        Location from = block.getLocation();
        Location to = blockTo.getLocation();

        if (from == to) return true;


        if (Towns.getTown(from.getChunk()) == null && Towns.getTown(to.getChunk()) != null) {
            return false;
        } else {
            return true;
        }

    }

    @EventHandler
    public void onBlockDispense(BlockDispenseEvent event) {


        if (event.getBlock().getType() != Material.DISPENSER)
            return;


        try {
            //   if (!canBlockMove(event.getBlock(), event.getBlock().getRelative(((CraftDispenser) event.getBlock().getBlockData()).getFacing())))
            //     event.setCancelled(true);
        } catch (Exception e) {
            if (AuroraUniverse.debugmode) {
                e.printStackTrace();
            }
        }

    }


    @EventHandler
    public void pistonEvent(BlockPistonRetractEvent event) {

        Town town = Towns.getTown(event.getBlock().getChunk());
        for (Block block : event.getBlocks()) {


            if (town != null) {
                if (Towns.getTown(block.getChunk()) != town) {
                    event.setCancelled(true);
                }
            } else {
                if (Towns.getTown(block.getChunk()) != null) {
                    event.setCancelled(true);
                }
            }


        }


    }

    @EventHandler
    public void move(PlayerInteractEvent e) {

        if (e.getAction() == Action.RIGHT_CLICK_BLOCK && e.hasBlock()) {


            if (Tag.BEDS.getValues().contains(e.getPlayer().getInventory().getItemInMainHand().getType())) {
                if (!canBlockPlace(e.getPlayer(), e.getClickedBlock())) {
                    e.setCancelled(true);
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

            if (!canBlockMove(block, block.getRelative(event.getDirection()))) {
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
                    if (resident.getTown() != t) {
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
