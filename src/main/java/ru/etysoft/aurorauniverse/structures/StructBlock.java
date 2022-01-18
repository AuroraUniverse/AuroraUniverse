package ru.etysoft.aurorauniverse.structures;


import org.bukkit.Chunk;
import org.bukkit.Material;
import org.json.simple.JSONObject;

public class StructBlock {

    private int x;
    private int y;
    private int z;
    private Material material;

    private static final String LOCAL_X = "LOCAL_X";
    private static final String LOCAL_Y = "LOCAL_Y";
    private static final String LOCAL_Z = "LOCAL_Z";
    private static final String MATERIAL = "MATERIAL";

    public StructBlock(int x, int y, int z, String material) throws StructureWrongCoordsException {
        if (x < 0 | y < 0 | z < 0) {
            throw new StructureWrongCoordsException();
        }

        this.x = x;
        this.y = y;
        this.z = z;
        this.material = Material.getMaterial(material);
    }


    public static StructBlock fromJSON(JSONObject jsonObject) throws StructureWrongCoordsException {
        int x = Math.toIntExact((long) jsonObject.get(LOCAL_X));
        int y = Math.toIntExact((long) jsonObject.get(LOCAL_Y));
        int z = Math.toIntExact((long) jsonObject.get(LOCAL_Z));
        String materialName = (String) jsonObject.get(MATERIAL);
        return new StructBlock(x, y, z, materialName);
    }

    public JSONObject toJson() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(MATERIAL, material.name());
        jsonObject.put(LOCAL_X, x);
        jsonObject.put(LOCAL_Y, y);
        jsonObject.put(LOCAL_Z, z);
        return jsonObject;
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
