package ru.etysoft.aurorauniverse.world;


import org.bukkit.Material;
import ru.etysoft.aurorauniverse.Logger;

import java.util.ArrayList;

public class MaterialGroups {

    public static class Containers
    {
        public static ArrayList<Material> getShulkers()
        {
            ArrayList<Material> materials = new ArrayList<>();
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
            materials.add(Material.PURPLE_SHULKER_BOX);
            materials.add(Material.LIGHT_BLUE_SHULKER_BOX);
            return materials;
        }

        public static ArrayList<Material> getChests()
        {
            ArrayList<Material> materials = new ArrayList<>();
            materials.add(Material.CHEST);
            materials.add(Material.ENDER_CHEST);
            materials.add(Material.TRAPPED_CHEST);
            materials.add(Material.CHEST_MINECART);
            return materials;
        }
    }

    public static class Switchable
    {
        public static ArrayList<Material> getTrapdoors()
        {
            ArrayList<Material> materials = new ArrayList<>();

            // Trapdoors legacy

            materials.add(Material.IRON_TRAPDOOR);
            materials.add(Material.OAK_TRAPDOOR);
            materials.add(Material.SPRUCE_TRAPDOOR);
            materials.add(Material.BIRCH_TRAPDOOR);
            materials.add(Material.JUNGLE_TRAPDOOR);
            materials.add(Material.ACACIA_TRAPDOOR);
            materials.add(Material.DARK_OAK_TRAPDOOR);

            // Doors


            try
            {
                // New trapdoors
                materials.add(Material.CRIMSON_TRAPDOOR);
                materials.add(Material.WARPED_TRAPDOOR);
                materials.add(Material.WARPED_TRAPDOOR);
            }
            catch (Exception e)
            {
                Logger.warning("Not all materials imported.");
            }
            return materials;
        }


        public static ArrayList<Material> getDoors()
        {
            ArrayList<Material> materials = new ArrayList<>();
            materials.add(Material.DARK_OAK_DOOR);
            materials.add(Material.ACACIA_DOOR);
            materials.add(Material.BIRCH_DOOR);
            materials.add(Material.IRON_DOOR);
            materials.add(Material.JUNGLE_DOOR);
            materials.add(Material.OAK_DOOR);
            materials.add(Material.SPRUCE_DOOR);

            try
            {
                // New doors
                materials.add(Material.CRIMSON_DOOR);
                materials.add(Material.WARPED_DOOR);

            }
            catch (Exception e)
            {
                Logger.warning("Not all materials imported.");
            }
            return materials;
        }

        public static ArrayList<Material> getGates()
        {
            ArrayList<Material> materials = new ArrayList<>();
            materials.add(Material.OAK_FENCE_GATE);
            materials.add(Material.SPRUCE_FENCE_GATE);
            materials.add(Material.BIRCH_FENCE_GATE);
            materials.add(Material.JUNGLE_FENCE_GATE);
            materials.add(Material.ACACIA_FENCE_GATE);
            materials.add(Material.DARK_OAK_FENCE_GATE);


            try
            {
                // New doors
                materials.add(Material.CRIMSON_FENCE_GATE);
                materials.add(Material.WARPED_FENCE_GATE);

            }
            catch (Exception e)
            {
                Logger.warning("Not all materials imported.");
            }
            return materials;
        }

        public static ArrayList<Material> getPlates()
        {
            ArrayList<Material> materials = new ArrayList<>();
            materials.add(Material.STONE_PRESSURE_PLATE);
            materials.add(Material.LIGHT_WEIGHTED_PRESSURE_PLATE);
            materials.add(Material.HEAVY_WEIGHTED_PRESSURE_PLATE);
            materials.add(Material.OAK_PRESSURE_PLATE);
            materials.add(Material.SPRUCE_PRESSURE_PLATE);
            materials.add(Material.BIRCH_PRESSURE_PLATE);
            materials.add(Material.JUNGLE_PRESSURE_PLATE);
            materials.add(Material.ACACIA_PRESSURE_PLATE);
            materials.add(Material.DARK_OAK_PRESSURE_PLATE);



            try
            {
                // New doors
                materials.add(Material.POLISHED_BLACKSTONE_PRESSURE_PLATE);
                materials.add(Material.CRIMSON_PRESSURE_PLATE);
                materials.add(Material.WARPED_PRESSURE_PLATE);

            }
            catch (Exception e)
            {
                Logger.warning("Not all materials imported.");
                e.printStackTrace();
            }
            return materials;
        }

        public static ArrayList<Material> getButtons()
        {
            ArrayList<Material> materials = new ArrayList<>();
            materials.add(Material.STONE_BUTTON);
            materials.add(Material.OAK_BUTTON);
            materials.add(Material.SPRUCE_BUTTON);
            materials.add(Material.BIRCH_BUTTON);
            materials.add(Material.JUNGLE_BUTTON);
            materials.add(Material.ACACIA_BUTTON);
            materials.add(Material.DARK_OAK_BUTTON);





            try
            {
                // New doors
                materials.add(Material.POLISHED_BLACKSTONE_BUTTON);
                materials.add(Material.CRIMSON_BUTTON);
                materials.add(Material.WARPED_BUTTON);

            }
            catch (Exception e)
            {
                Logger.warning("Not all materials imported.");
                e.printStackTrace();
            }
            return materials;
        }

    }



    public static ArrayList<Material> getContainers()
    {
        ArrayList<Material> materials = new ArrayList<>();
        materials.addAll(Containers.getShulkers());

        materials.addAll(Containers.getChests());
        materials.add(Material.BLAST_FURNACE);
        materials.add(Material.FURNACE);
        materials.add(Material.SMOKER);
        materials.add(Material.HOPPER);
        materials.add(Material.HOPPER_MINECART);
        materials.add(Material.DISPENSER);
        materials.add(Material.DROPPER);

        materials.add(Material.GRINDSTONE);
        materials.add(Material.ANVIL);
        materials.add(Material.DAMAGED_ANVIL);
        materials.add(Material.CHIPPED_ANVIL);
        materials.add(Material.BREWING_STAND);
        try
        {
            materials.add(Material.BARREL);

        }
        catch (Exception e)
        {
            Logger.error("Are you using outdated minecraft version?");
        }


        return materials;
    }

}
