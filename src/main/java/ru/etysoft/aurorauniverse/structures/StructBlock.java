package ru.etysoft.aurorauniverse.structures;


import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.material.Directional;
import org.json.simple.JSONObject;
import ru.etysoft.aurorauniverse.Logger;

public class StructBlock {

    private int x;
    private int y;
    private int z;
    private String blockData;
    private Material material;

    private static final String LOCAL_X = "LOCAL_X";
    private static final String LOCAL_Y = "LOCAL_Y";
    private static final String LOCAL_Z = "LOCAL_Z";
    private static final String MATERIAL = "MATERIAL";
    private static final String BLOCK_DATA = "BLOCK_DATA";

    public StructBlock(int x, int y, int z, Block block) throws StructureWrongCoordsException {
        if (x < 0 | y < 0 | z < 0) {
            throw new StructureWrongCoordsException();
        }

        this.x = x;
        this.y = y;
        this.z = z;
        this.material = Material.getMaterial(block.getType().name());



       try {


            blockData =  block.getBlockData().getAsString();
            Logger.debug(blockData);
        }
       catch (Exception e)
       {
          // Logger.debug(block.getType().name() + " sheesh " + block.getBlockData());
       }







    }

    public StructBlock()
    {}


    public static StructBlock fromJSON(JSONObject jsonObject) throws StructureWrongCoordsException {
        int x = Math.toIntExact((long) jsonObject.get(LOCAL_X));
        int y = Math.toIntExact((long) jsonObject.get(LOCAL_Y));
        int z = Math.toIntExact((long) jsonObject.get(LOCAL_Z));
        String face;
        StructBlock structBlock = new StructBlock();;
        try {
            face = (String) jsonObject.get(BLOCK_DATA);

            structBlock.setBlockData(face);
        }
        catch (Exception e)
        {
            Logger.warning("Facing not loaded");

        }

        String materialName = (String) jsonObject.get(MATERIAL);



        structBlock.setMaterial(Material.getMaterial(materialName));
        structBlock.setX(x);
        structBlock.setY(y);
        structBlock.setZ(z);



        return structBlock;
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MATERIAL, material.name());
        jsonObject.put(LOCAL_X, x);
        jsonObject.put(LOCAL_Y, y);
        jsonObject.put(LOCAL_Z, z);
        jsonObject.put(BLOCK_DATA, blockData);


        return jsonObject;
    }


    public String getBlockData() {
        return blockData;
    }

    public void setBlockData(String blockData) {
        this.blockData = blockData;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setZ(int z) {
        this.z = z;
    }


    public void setMaterial(Material material) {
        this.material = material;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Material getMaterial() {
        return material;
    }
}
