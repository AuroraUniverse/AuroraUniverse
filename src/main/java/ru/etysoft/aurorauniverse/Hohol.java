package ru.etysoft.aurorauniverse;


import net.minecraft.server.v1_16_R2.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;


public class Hohol extends EntityPig {
    public Hohol(Location world) {
        super(EntityTypes.PIG, ((CraftWorld) world.getWorld()).getHandle());
        this.setHealth(100000);
        this.setCustomName(new ChatComponentText("хохол"));
        this.setCustomNameVisible(true);
        this.setSneaking(true);
        this.goalSelector.a(0, new PathfinderGoalAvoidTarget<EntityPlayer>(this, EntityPlayer.class, 15, 22.0D, 22.0D));
        this.goalSelector.a(1, new PathfinderGoalPanic(this, 34.0D));
        this.goalSelector.a(2, new PathfinderGoalRandomStrollLand(this, 20.0D));
        this.setJumping(true);
        this.setAggressive(true);
        this.setBaby(true);
        this.goalSelector.a(3, new PathfinderGoalRandomLookaround(this));
    }
}
