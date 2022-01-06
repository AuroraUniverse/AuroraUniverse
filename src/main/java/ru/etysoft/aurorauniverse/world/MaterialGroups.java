package ru.etysoft.aurorauniverse.world;


import org.bukkit.Material;

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
            materials.add(Material.WARPED_TRAPDOOR);
            materials.add(Material.CRIMSON_TRAPDOOR);
            materials.add(Material.DARK_OAK_TRAPDOOR);
            materials.add(Material.ACACIA_TRAPDOOR);
            materials.add(Material.JUNGLE_TRAPDOOR);
            materials.add(Material.BIRCH_TRAPDOOR);
            materials.add(Material.SPRUCE_TRAPDOOR);
            materials.add(Material.OAK_TRAPDOOR);
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

        materials.add(Material.GRINDSTONE);
        materials.add(Material.ANVIL);
        materials.add(Material.DAMAGED_ANVIL);
        materials.add(Material.CHIPPED_ANVIL);
        materials.add(Material.BREWING_STAND);
        materials.add(Material.BARREL);

        return materials;
    }

}
