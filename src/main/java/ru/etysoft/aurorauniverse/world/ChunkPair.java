package ru.etysoft.aurorauniverse.world;

import org.bukkit.Chunk;
import org.bukkit.World;

public class ChunkPair {
    private final int x;
    private final int z;
    private final World world;
    public ChunkPair(final int x, final int z, final World world) {
        this.x = x;
        this.z = z;
        this.world = world;
    }
    /**
     * @return the x
     */
    public int getX() {
        return x;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((world == null) ? 0 : world.hashCode());
        result = prime * result + x;
        result = prime * result + z;
        return result;
    }

    public static ChunkPair fromChunk(Chunk chunk)
    {
        return new ChunkPair(chunk.getX(), chunk.getZ(), chunk.getWorld());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ChunkPair other = (ChunkPair) obj;
        if (world == null) {
            if (other.world != null)
                return false;
        } else if (!world.equals(other.world))
            return false;
        if (x != other.x)
            return false;
        if (z != other.z)
            return false;
        return true;
    }
    /**
     * @return the z
     */
    public int getZ() {
        return z;
    }
    /**
     * @return the world
     */
    public World getWorld() {
        return world;
    }
}