package ru.etysoft.aurorauniverse.structures;

import org.bukkit.*;
import org.bukkit.block.Block;
import org.json.simple.JSONObject;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.exceptions.WorldNotFoundedException;
import ru.etysoft.aurorauniverse.world.ChunkPair;

import java.util.ArrayList;
import java.util.List;

public class Structure {

    private int maxX;
    private int maxY;
    private int maxZ;


    private int startX;
    private int startY;
    private int startZ;

    private String patternName;
    private String world;
    private List<StructBlock> structBlockList = new ArrayList<>();

    private static final String START_X = "START_X";
    private static final String START_Y = "START_Y";
    private static final String START_Z = "START_Z";
    private static final String PATTERN = "STRUCT_PATTERN_NAME";
    private static final String WORLD = "WORLD";

    public Structure(int startX, int startY, int startZ, String patterName, String world) {
        this.startX = startX;
        this.startY = startY;
        this.startZ = startZ;
        this.patternName = patterName;
        this.world = world;
        calculateSize();
        try {
            structBlockList = StructurePatterns.getBlocksFromPattern(patterName);
        } catch (PatternNotFoundedException e) {
            Logger.error("Error loading structure: pattern with name " + patterName + " does not exists!");
            e.printStackTrace();
        }
    }


    public ChunkPair getStartChunk()
    {
        return ChunkPair.fromChunk((new Location(Bukkit.getWorld(world), startX, startY, startZ)).getChunk());
    }

    public boolean isInSingleChunk()
    {
        int x = 0;
        int z = 0;
        boolean isFirst = true;
        for(StructBlock structBlock : structBlockList)
        {
            Location location = new Location(Bukkit.getWorld(world), startX + structBlock.getX(), startY + structBlock.getY(), startZ + structBlock.getZ());
            if(isFirst)
            {
                isFirst = false;
            }
            else {
                if(x != location.getChunk().getX()) return false;
                if(z != location.getChunk().getZ()) return false;
            }
                x = location.getChunk().getX();
                z = location.getChunk().getZ();

        }
        return true;
    }

    public static Structure fromJSON(JSONObject jsonObject) throws PatternNotFoundedException, WorldNotFoundedException {
        String pattern = (String) jsonObject.get(PATTERN);
        String world = (String) jsonObject.get(WORLD);
        int startX = Math.toIntExact((long) jsonObject.get(START_X));
        int startY = Math.toIntExact((long) jsonObject.get(START_Y));
        int startZ = Math.toIntExact((long) jsonObject.get(START_Z));

        if(Bukkit.getServer().getWorld(world) == null) throw new WorldNotFoundedException();

        if(StructurePatterns.hasPattern(pattern))
        {
            return new Structure(startX, startY, startZ, pattern, world);
        }
        else
        {
            throw new PatternNotFoundedException();
        }

    }

    public JSONObject toJson()
    {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(START_X, startX);
        jsonObject.put(START_Y, startY);
        jsonObject.put(START_Z, startZ);
        jsonObject.put(PATTERN, patternName);
        jsonObject.put(WORLD, world);
        return jsonObject;
    }

    public boolean isSpaceClear() throws WorldNotFoundedException {
        World world = Bukkit.getWorld(this.world);
        if(world == null) throw new WorldNotFoundedException();
        for (StructBlock structBlock : structBlockList)
        {
            int x = startX + structBlock.getX();
            int y = startY + structBlock.getY();
            int z = startZ + structBlock.getZ();
            if(world.getBlockAt(x, y, z).getType() != Material.AIR && world.getBlockAt(x, y, z).getType() != Material.GRASS)
            {
                return false;
            }
        }
        return true;
    }
    boolean isDestroying;
    public void destroy(Runnable onBuildFinished, Runnable onDestroyPrevent, boolean force) throws WorldNotFoundedException, StructureBuildException {
        if(!isFullBuilt() && !force) return;

        isDestroying = true;


        Thread buildingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                for(StructBlock block : structBlockList) {
                    if(!isDestroying) return;
                    Bukkit.getServer().getScheduler().runTask(AuroraUniverse.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            Location loc = new Location(Bukkit.getServer().getWorld(world),block.getX() + startX, block.getY() + startY, block.getZ() + startZ);
                            loc.getChunk().load();
                            if(Bukkit.getWorld(world).getBlockAt(loc).getType() == block.getMaterial() | force)
                            {
                                Bukkit.getServer().getWorld(world).getBlockAt(loc).setType(Material.AIR);
                            }
                            else
                            {
                                Logger.debug("Failed " + Bukkit.getWorld(world).getBlockAt(loc).getType().name() + "; " + block.getMaterial().name());
                                onDestroyPrevent.run();
                                isDestroying = false;
                            }


                        }
                    });
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Bukkit.getServer().getScheduler().runTask(AuroraUniverse.getInstance(), onBuildFinished);

            }
        });
        buildingThread.start();


    }

    public void build(Runnable onBuildFinished) throws WorldNotFoundedException, StructureBuildException {
        if(!isSpaceClear()) throw new StructureBuildException();
        if(isFullBuilt()) return;

        Thread buildingThread = new Thread(new Runnable() {
            @Override
            public void run() {

                for(StructBlock block : structBlockList) {
                    Bukkit.getServer().getScheduler().runTask(AuroraUniverse.getInstance(), new Runnable() {
                        @Override
                        public void run() {
                            Location loc = new Location(Bukkit.getServer().getWorld(world),block.getX() + startX, block.getY() + startY, block.getZ() + startZ);
                            loc.getChunk().load();
                            Bukkit.getServer().getWorld(world).getBlockAt(loc).setType(block.getMaterial());
                        }
                    });
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                Bukkit.getServer().getScheduler().runTask(AuroraUniverse.getInstance(), onBuildFinished);

            }
        });
        buildingThread.start();


    }



    public boolean isFullBuilt() throws WorldNotFoundedException {
        World world = Bukkit.getWorld(this.world);
        if(world == null) throw new WorldNotFoundedException();
        for (StructBlock structBlock : structBlockList)
        {
            int x = startX + structBlock.getX();
            int y = startY + structBlock.getY();
            int z = startZ + structBlock.getZ();
            if(!world.getBlockAt(x, y, z).getType().name().equals(structBlock.getMaterial().name()))
            {
                Logger.debug("Missmatch " + world.getBlockAt(x, y, z).getType().name() + " : " + structBlock.getMaterial().name());
                return false;
            }
        }
        return true;
    }

    public void addBlock(int localX, int localY, int localZ, String materialName) throws StructureWrongCoordsException {
        structBlockList.add(new StructBlock(localX, localY, localZ, materialName));
    }

    public String getPatternName() {
        return patternName;
    }

    private void calculateSize()
    {

        for(StructBlock structBlock : structBlockList)
        {
            if(maxX < structBlock.getX())
            {
                maxX = structBlock.getX();
            }

            if(maxY < structBlock.getY())
            {
                maxY = structBlock.getY();
            }

            if(maxZ < structBlock.getZ())
            {
                maxZ = structBlock.getZ();
            }
        }
    }

}
