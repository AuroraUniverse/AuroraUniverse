package ru.etysoft.aurorauniverse.world;



import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Warning;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import ru.etysoft.aurorauniverse.AuroraUniverse;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.data.Messages;
import ru.etysoft.aurorauniverse.data.Nations;
import ru.etysoft.aurorauniverse.data.Residents;
import ru.etysoft.aurorauniverse.data.Towns;
import ru.etysoft.aurorauniverse.economy.Bank;
import ru.etysoft.aurorauniverse.events.PreTownDeleteEvent;
import ru.etysoft.aurorauniverse.events.TownDeleteEvent;
import ru.etysoft.aurorauniverse.events.TownRenameEvent;
import ru.etysoft.aurorauniverse.exceptions.*;
import ru.etysoft.aurorauniverse.permissions.AuroraPermissions;
import ru.etysoft.aurorauniverse.permissions.Group;
import ru.etysoft.aurorauniverse.placeholders.PlaceholderFormatter;
import ru.etysoft.aurorauniverse.structures.Structure;
import ru.etysoft.aurorauniverse.structures.StructureBuildException;
import ru.etysoft.aurorauniverse.utils.AuroraLanguage;
import ru.etysoft.aurorauniverse.utils.Messaging;
import ru.etysoft.aurorauniverse.utils.Numbers;

import javax.annotation.Nullable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/*
       ⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠋⣵⣶⣬⣉⡻⠿⠿⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿
       ⣿⣿⣿⣿⣿⣿⣿⠿⠿⠛⣃⣸⣿⣿⣿⣿⣿⣿⣷⣦⢸⣿⣿⣿⣿⣿⣿⣿⣿
       ⣿⣿⣿⣿⣿⣿⢡⣶⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣦⣭⣙⠿⣿⣿⣿⣿⣿
       ⣿⣿⣿⣿⡿⠿⠘⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣧⢸⣿⣿⣿⣿
       ⣿⣿⣿⠋⣴⣾⣿⣿⣿⡟⠁⠄⠙⣿⣿⣿⣿⠁⠄⠈⣿⣿⣿⣿⣈⠛⢿⣿⣿
       ⣿⣿⣇⢸⣿⣿⣿⣿⣿⣿⣦⣤⣾⣿⣿⣿⣿⣦⣤⣴⣿⣿⣿⣿⣿⣷⡄⢿⣿
       ⣿⠟⣋⣠⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⢸⣿
       ⢁⣾⣿⣿⣿⣿⣿⡉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⠉⣹⣿⣿⣿⣦⠙
       ⣾⣿⣿⣿⣿⣿⣿⣿⣦⣄⣤⣶⣿⣿⣿⣿⣿⣿⣷⣦⣄⣤⣾⣿⣿⣿⣿⣿⣧
       ⠘⢿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⠏
       ⣷⣦⣙⠛⠿⢿⣿⣿⡿⠿⠿⠟⢛⣛⣛⡛⠻⠿⠿⠿⣿⣿⣿⣿⠿⠟⢛⣡⣾
        */
public class Town {

    public String name;

    public Chunk homeblock;
    public Location townSpawnPoint;

    public float bank;
    private boolean pvp = false;
    private boolean mobs = false;
    private boolean explosion = false;
    private boolean forcePvp = false;
    private boolean forceExplosions = false;

    private ArrayList<OutpostRegion> outPosts = new ArrayList<>();
    private boolean fire = false;
    private double resTax = 0;
    private int bonusChunks = 0;

    private HashMap<String, Double> townChunkTaxMultipliers = new HashMap<String, Double>();

    private Resident mayor;
    private Map<ChunkPair, Region> townChunks = new ConcurrentHashMap<>();
    private ArrayList<Resident> residents = new ArrayList<>();

    private ArrayList<String> embargoList = new ArrayList<>();
    private ChunkPair mainChunk = null;
    private Bank townBank;
    private String nationName;
    private String id;
    private Structure auctionStructure;
    private ArrayList<String> transactionHistory = new ArrayList<>();

    private ArrayList<Resident> invitedResidents = new ArrayList<>();

    private double auctionTax = 0;

    // Permission type -> list of groups with permission
    private Set<String> buildGroups = new HashSet<String>();
    private Set<String> destroyGroups = new HashSet<String>();
    private Set<String> useGroups = new HashSet<String>();
    private Set<String> switchGroups = new HashSet<String>();

    public static final class JsonKeys {
        public static final String NAME = "NAME";
        public static final String MAYOR = "MAYOR";
        public static final String PVP = "PVP";
        public static final String FIRE = "FIRE";
        public static final String MOBS = "MOBS";
        public static final String EXPLOSION = "EXPLOSION";

        public static final String BANK = "BANK";
        public static final String BONUS_CHUNKS = "BONUS_CHUNKS";
        public static final String RESIDENTS = "RESIDENTS";
        public static final String RESIDENT_TAX = "RES_TAX";
        public static final String NATION_NAME = "NATION_NAME";
        public static final String EMBARGO_LIST = "EMBARGO_LIST";

        public static final String AUCTION_TAX = "AUCTION_TAX";

        public static final String REGIONS = "REGIONS";
        public static final String IS_OUTPOST = "IS_OUTPOST";
        public static final String X = "X";
        public static final String Z = "Z";
        public static final String WORLD = "WORLD";
        public static final String REGION = "REGION";

        public static final String SPAWN_X = "SPAWN_X";
        public static final String SPAWN_Y = "SPAWN_Y";
        public static final String ID = "ID";
        public static final String SPAWN_YAW = "SPAWN_YAW";
        public static final String SPAWN_PITCH = "SPAWN_PITCH";
        public static final String SPAWN_Z = "SPAWN_Z";
        public static final String SPAWN_DIR_X = "SPAWN_DX";
        public static final String SPAWN_DIR_Y = "SPAWN_DY";
        public static final String SPAWN_DIR_Z = "SPAWN_DX";
        public static final String SPAWN_WORLD = "SPAWN_WORLD";


        public static final String MAIN_CHUNK = "MAIN_CHUNK";
        public static final String AUCTION_STRUCT = "AUCTION_STRUCT";


        public static final String BUILD_GROUPS = "BUILD_GROUPS";
        public static final String DESTROY_GROUPS = "DESTROY_GROUPS";
        public static final String USE_GROUPS = "USE_GROUPS";
        public static final String SWITCH_GROUPS = "SWITCH_GROUPS";
    }

    @Warning
    public Town() {
    }

    public ArrayList<OutpostRegion> getOutPosts() {
        return outPosts;
    }

    public int getMaxChunks() {
        return bonusChunks + ((AuroraUniverse.getInstance().getConfig().getInt("chunk-per-resident")) * residents.size());
    }

    public void setBonusChunks(int bonusChunks) {
        this.bonusChunks = bonusChunks;
    }

    public int getBonusChunks() {
        return bonusChunks;
    }

    public double getAuctionTax() {
        return auctionTax;
    }

    public void setAuctionTax(double auctionTax) {
        this.auctionTax = auctionTax;
    }

    public boolean isExplosionEnabled() {
        if(forceExplosions) return true;
        return explosion;
    }

    public boolean isExplosionEnabled(ChunkPair chunk)
    {
        if (forceExplosions) return true;
        Region region = townChunks.get(chunk);
        if (region instanceof ResidentRegion) {
            return ((ResidentRegion) region).isExplosions();
        }
        return explosion;
    }


    public void addEmbargoTown(Town town)
    {
        if(!embargoList.contains(town.getId()))
        {
            embargoList.add(town.getId());
        }
    }

    public void removeEmbargoTown(Town town)
    {
        if(embargoList.contains(town.getId()))
        {
            embargoList.remove(town.getId());
        }
    }


    public void setExplosionEnabled(boolean explosion) {
        this.explosion = explosion;
    }

    public ArrayList<Resident> getInvitedResidents() {
        return invitedResidents;
    }

    public double getResTax() {
        return Numbers.round(resTax);
    }

    public ArrayList<String> getTransactionHistory() {
        return transactionHistory;
    }

    public void setResTax(double resTax) {
        if(resTax < 0) return;
        this.resTax = resTax;
    }

    public ArrayList<Town> getEmbargoList() {

        ArrayList<Town> towns = new ArrayList<>();
        for(String id : new ArrayList<>(embargoList))
        {
            try {
                Town town = Towns.getTownById(id);
                if(town == null)
                {
                    embargoList.remove(id);
                }
                else {
                    towns.add(town);
                }
            } catch (TownNotFoundedException e) {
                embargoList.remove(id);
            }
        }

        return towns;
    }


    public boolean hasEmbargoForTown(Town town)
    {
        return embargoList.contains(town.getId());
    }



    public double getNewChunkPrice() {
        float defPrice = AuroraUniverse.getInstance().getConfig().getLong("town-chunk-price");
        double modifier = AuroraUniverse.getInstance().getConfig().getDouble("town-new-chunk-tax-mod");
        modifier = Math.pow(modifier, getChunksCount() - 1);
        Logger.debug("Chunk mod: " + modifier);
        double finalPrice;
        finalPrice = Math.round(modifier * defPrice * 100);
        finalPrice = finalPrice / 100;
        return finalPrice;
    }

    public double getNewOutpostPrice() {
        float defPrice = AuroraUniverse.getInstance().getConfig().getLong("town-outpost-price");
        double modifier = AuroraUniverse.getInstance().getConfig().getDouble("town-outpost-mod-price");
        modifier = Math.pow(modifier, getOutPosts().size());
        Logger.debug("Outpost mod: " + modifier);
        double finalPrice;
        finalPrice = Math.round(modifier * defPrice * 100);
        finalPrice = finalPrice / 100;
        return finalPrice;
    }

    public boolean isOutpost(ChunkPair ch) {
        if (hasChunk(ch)) {
            return townChunks.get(ch) instanceof OutpostRegion;
        }
        return false;
    }

    public void setForcePvp(boolean forcePvp) {
        this.forcePvp = forcePvp;
    }

    public boolean isForcePvp() {
        return forcePvp;
    }

    public void setMobs(boolean mobs) {
        this.mobs = mobs;
    }

    public boolean isMobs() {
        return mobs;
    }

    public boolean isMobs(Region region)
    {
        if (region instanceof ResidentRegion)
        {
            return ((ResidentRegion) region).isMobs();
        }
        else
        {
            return mobs;
        }
    }

    public void setFire(boolean fire) {
        this.fire = fire;
    }

    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }

    public void setMayor(Resident newMayor) {

        if (mayor != null) {
            mayor.setPermissionGroup("resident");
            AuroraPermissions.setPermissions(mayor.getName(), AuroraPermissions.getGroup("resident"));

        }

        newMayor.setPermissionGroup("mayor");
        AuroraPermissions.setPermissions(newMayor.getName(), AuroraPermissions.getGroup("mayor"));


        this.mayor = newMayor;
    }

    private void setId(String id) {
        this.id = id;
    }

    public void setNationName(String nationName) {
        this.nationName = nationName;
    }


    public static Town loadTownFromJSON(JSONObject jsonObject) throws TownException {


        /**
         * Firstly, initialize the town
         */

        String townName = (String) jsonObject.get(JsonKeys.NAME);
        double resTax = (double) jsonObject.get(JsonKeys.RESIDENT_TAX);
        String nation = (String) jsonObject.get(JsonKeys.NATION_NAME);

        Resident mayorResident = Resident.fromJSON((JSONObject) jsonObject.get(JsonKeys.MAYOR));
        ChunkPair mainChunk = getChunkFromInfo((JSONObject) jsonObject.get(JsonKeys.MAIN_CHUNK));

        Town town = Towns.loadTown(townName, mayorResident, mainChunk);
        Logger.debug("Loaded town " + townName);
        town.setId((String) jsonObject.get(JsonKeys.ID));
        town.setResTax(resTax);
        town.setNationName(nation);
        town.getBank().setBalance((double) jsonObject.get(JsonKeys.BANK));

        try {

            /**
             * Secondary, initialize the town residents
             */

            JSONArray residentsArray = (JSONArray) jsonObject.get(JsonKeys.RESIDENTS);

            for (int i = 0; i < residentsArray.size(); i++) {
                JSONObject residentObj = (JSONObject) residentsArray.get(i);
                Resident resident = Resident.fromJSON(residentObj);
                if (resident != null) {
                    town.addResident(resident);
                    Residents.loadResident(resident);
                    Logger.debug(townName + " > Loaded resident " + resident.getName());
                }

            }

            /**
             * Now, load regions and chunks
             */

            JSONArray chunkInfos = (JSONArray) jsonObject.get(JsonKeys.REGIONS);

            for (int i = 0; i < chunkInfos.size(); i++) {
                JSONObject chunkInfo = (JSONObject) chunkInfos.get(i);

                ChunkPair chunk = getChunkFromInfo(chunkInfo);

                JSONObject regionObj = (JSONObject) chunkInfo.get(JsonKeys.REGION);
                boolean isOutpost = (boolean) chunkInfo.get(JsonKeys.IS_OUTPOST);

                Region region;

                if (regionObj.containsKey(ResidentRegion.JsonKeys.MEMBERS)) {
                    region = ResidentRegion.fromJSON(regionObj);
                } else {
                    if (isOutpost) {
                        region = OutpostRegion.fromJSON(regionObj);
                        town.getOutPosts().add((OutpostRegion) region);
                    } else {
                        region = Region.fromJSON(regionObj);
                    }
                }
                if (region != null) {
                    town.townChunks.put(chunk, region);
                    AuroraUniverse.addTownBlock(chunk, region);
                    Logger.debug(townName + " > Loaded chunk " + chunk.getX() + ", " + chunk.getZ());
                } else {
                    Logger.error(townName + " > Chunk " + chunk.getX() + ", " + chunk.getZ() + " is ignored");
                }


            }
            JSONArray jsonArrayEmbargo = (JSONArray) jsonObject.get(JsonKeys.EMBARGO_LIST);
            ArrayList<String> embargoList = new ArrayList<>();

            for (int i = 0; i < jsonArrayEmbargo.size(); i++) {
                String embargoId = (String) jsonArrayEmbargo.get(i);

                embargoList.add(embargoId);

            }

            town.setEmbargoList(embargoList);

            String spawnWorld = (String) jsonObject.get(JsonKeys.SPAWN_WORLD);
            double spawnX = (double) jsonObject.get(JsonKeys.SPAWN_X);
            double spawnY = (double) jsonObject.get(JsonKeys.SPAWN_Y);
            double spawnZ = (double) jsonObject.get(JsonKeys.SPAWN_Z);

            double spawnDirX = (double) jsonObject.get(JsonKeys.SPAWN_DIR_X);
            double spawnYaw = (double) jsonObject.get(JsonKeys.SPAWN_YAW);
            double spawnPitch = (double) jsonObject.get(JsonKeys.SPAWN_PITCH);
            double spawnDirY = (double) jsonObject.get(JsonKeys.SPAWN_DIR_Y);
            double spawnDirZ = (double) jsonObject.get(JsonKeys.SPAWN_DIR_Z);

            Location spawnLocation = new Location(Bukkit.getWorld(spawnWorld), spawnX, spawnY, spawnZ);

            spawnLocation.setYaw(-Float.parseFloat(String.valueOf(spawnYaw)));
            spawnLocation.setPitch(Float.parseFloat(String.valueOf(spawnPitch)));

            town.setFire((boolean) jsonObject.get(JsonKeys.FIRE));
            town.setPvP((boolean) jsonObject.get(JsonKeys.PVP));
            town.setMobs((boolean) jsonObject.get(JsonKeys.MOBS));
            town.setBonusChunks((int) ((long) jsonObject.get(JsonKeys.BONUS_CHUNKS)));
            town.setExplosionEnabled((boolean) jsonObject.get(JsonKeys.EXPLOSION));



            // spawnLocation.setDirection(new Vector(spawnDirX,spawnDirY,spawnDirZ));


            town.setBuildGroups(getList((JSONArray) jsonObject.get(JsonKeys.BUILD_GROUPS)));
            town.setDestroyGroups(getList((JSONArray) jsonObject.get(JsonKeys.DESTROY_GROUPS)));
            town.setSwitchGroups(getList((JSONArray) jsonObject.get(JsonKeys.SWITCH_GROUPS)));
            town.setUseGroups(getList((JSONArray) jsonObject.get(JsonKeys.USE_GROUPS)));

            if (jsonObject.containsKey(JsonKeys.AUCTION_STRUCT)) {
                town.setAuctionStructure(Structure.fromJSON((JSONObject) jsonObject.get(JsonKeys.AUCTION_STRUCT)));
            }


            town.setSpawn(spawnLocation);

            if (jsonObject.containsKey(JsonKeys.AUCTION_TAX))
            {
                double auctionTax = Double.parseDouble(jsonObject.get(JsonKeys.AUCTION_TAX).toString());
                town.setAuctionTax(auctionTax);
            }
            else
            {
                town.setAuctionTax(0);
            }






        } catch (Exception e) {
            Logger.error("Error loading town " + townName);
            e.printStackTrace();
        }

        return town;
    }


    public void setEmbargoList(ArrayList<String> embargoList) {
        this.embargoList = embargoList;
    }

    public Structure getAuctionStructure() {
        return auctionStructure;
    }

    public void setAuctionStructure(Structure auctionStructure) {
        this.auctionStructure = auctionStructure;
    }

    private static JSONArray getJsonArrayFromList(Set<String> list) {
        JSONArray jsonArrayToApply = new JSONArray();
        for (String string : list) {
            jsonArrayToApply.add(string);
        }
        return jsonArrayToApply;
    }

    private static Set<String> getList(JSONArray jsonArray) {
        Set<String> arrayList = new HashSet<>();

        for (int i = 0; i < jsonArray.size(); i++) {
            String string = (String) jsonArray.get(i);
            arrayList.add(string);
        }
        return arrayList;
    }

    public String getNationName() {
        return nationName;
    }

    public Nation getNation() {
        return Nations.getNation(nationName);
    }

    private static ChunkPair getChunkFromInfo(JSONObject chunkInfo) {
        long x = (long) chunkInfo.get(JsonKeys.X);
        long z = (long) chunkInfo.get(JsonKeys.Z);
        String world = (String) chunkInfo.get(JsonKeys.WORLD);


        ChunkPair chunk = new ChunkPair((int) x, (int) z, Bukkit.getWorld(world));

        return chunk;
    }

    public void setTownChunkTaxMultiplier(String pluginName, double multiplier) {
        if (townChunkTaxMultipliers.containsKey(pluginName)) {
            townChunkTaxMultipliers.remove(pluginName);
            townChunkTaxMultipliers.put(pluginName, multiplier);
        } else {
            townChunkTaxMultipliers.put(pluginName, multiplier);
        }
    }

    public void setBuildGroups(Set<String> buildGroups) {
        this.buildGroups = buildGroups;
    }

    public void setDestroyGroups(Set<String> destroyGroups) {
        this.destroyGroups = destroyGroups;
    }

    public void setUseGroups(Set<String> useGroups) {
        this.useGroups = useGroups;
    }

    public void setSwitchGroups(Set<String> switchGroups) {
        this.switchGroups = switchGroups;
    }


    public void sendMessage(String message) {
        for (Resident player : getResidents()) {
            if (Bukkit.getPlayer(player.getName()) != null) {
                Bukkit.getPlayer(player.getName()).sendMessage(PlaceholderFormatter.process(message, Bukkit.getPlayer(player.getName())));
            }
        }
    }

    public boolean isTownPvp() {

        return pvp;
    }

    public boolean isPvp(ChunkPair chunk) {
        if (forcePvp) return true;
        Region region = townChunks.get(chunk);
        if (region instanceof ResidentRegion) {
            return ((ResidentRegion) region).isPvp();
        }
        return pvp;
    }


    public boolean isTownFire() {
        return fire;
    }

    public String getId() {
        return id;
    }

    public JSONObject toJSON() {

        JSONObject townJsonObject = new JSONObject();
        townJsonObject.put(JsonKeys.NAME, name);
        townJsonObject.put(JsonKeys.MAYOR, mayor.toJson());
        townJsonObject.put(JsonKeys.PVP, pvp);
        townJsonObject.put(JsonKeys.FIRE, fire);
        townJsonObject.put(JsonKeys.MOBS, mobs);
        townJsonObject.put(JsonKeys.EXPLOSION, explosion);

        townJsonObject.put(JsonKeys.SPAWN_X, townSpawnPoint.getX());
        townJsonObject.put(JsonKeys.SPAWN_Y, townSpawnPoint.getY());
        townJsonObject.put(JsonKeys.SPAWN_Z, townSpawnPoint.getZ());

        townJsonObject.put(JsonKeys.SPAWN_DIR_X, townSpawnPoint.getDirection().getX());
        townJsonObject.put(JsonKeys.SPAWN_YAW, townSpawnPoint.getYaw());
        townJsonObject.put(JsonKeys.SPAWN_PITCH, townSpawnPoint.getPitch());
        townJsonObject.put(JsonKeys.SPAWN_DIR_Y, townSpawnPoint.getDirection().getY());
        townJsonObject.put(JsonKeys.SPAWN_DIR_Z, townSpawnPoint.getDirection().getZ());
        townJsonObject.put(JsonKeys.SPAWN_WORLD, townSpawnPoint.getWorld().getName());
        townJsonObject.put(JsonKeys.RESIDENT_TAX, getResTax());
        townJsonObject.put(JsonKeys.NATION_NAME, nationName);
        townJsonObject.put(JsonKeys.ID, id);
        townJsonObject.put(JsonKeys.BONUS_CHUNKS, bonusChunks);
        if (auctionStructure != null) {
            townJsonObject.put(JsonKeys.AUCTION_STRUCT, auctionStructure.toJson());
        }

        townJsonObject.put(JsonKeys.BANK, townBank.getBalance());
        townJsonObject.put(JsonKeys.AUCTION_TAX, this.auctionTax);

        JSONArray jsonArrayResidents = new JSONArray();

        for (Resident resident : residents) {
            jsonArrayResidents.add(resident.toJson());
        }

        if (!townChunks.containsKey(mainChunk)) {
            Logger.error("Main chunk not in chunklist!");
        }

        townJsonObject.put(JsonKeys.RESIDENTS, jsonArrayResidents);

        JSONArray jsonArrayEmbargo = new JSONArray();

        for(String id : embargoList)
        {
            jsonArrayEmbargo.add(id);
        }

        townJsonObject.put(JsonKeys.EMBARGO_LIST, embargoList);


        JSONArray regions = new JSONArray();

        for (ChunkPair chunk : townChunks.keySet()) {
            JSONObject chunkInfo = new JSONObject();

            Region region = townChunks.get(chunk);


            if (region instanceof OutpostRegion) {
                chunkInfo.put(JsonKeys.IS_OUTPOST, true);
            } else {
                chunkInfo.put(JsonKeys.IS_OUTPOST, false);
            }
            chunkInfo.put(JsonKeys.REGION, region.toJson());

            chunkInfo.put(JsonKeys.X, chunk.getX());
            chunkInfo.put(JsonKeys.Z, chunk.getZ());
            chunkInfo.put(JsonKeys.WORLD, chunk.getWorld().getName());

            regions.add(chunkInfo);

            if (mainChunk.equals(chunk)) {
                townJsonObject.put(JsonKeys.MAIN_CHUNK, chunkInfo);
            }


        }

        townJsonObject.put(JsonKeys.REGIONS, regions);

        townJsonObject.put(JsonKeys.USE_GROUPS, getJsonArrayFromList(useGroups));
        townJsonObject.put(JsonKeys.BUILD_GROUPS, getJsonArrayFromList(buildGroups));
        townJsonObject.put(JsonKeys.SWITCH_GROUPS, getJsonArrayFromList(switchGroups));
        townJsonObject.put(JsonKeys.DESTROY_GROUPS, getJsonArrayFromList(destroyGroups));

        return townJsonObject;
    }

    public Set<String> getDestroyGroups() {
        return destroyGroups;
    }

    public Set<String> getBuildGroups() {
        return buildGroups;
    }

    public Set<String> getUseGroups() {
        return useGroups;
    }

    public Set<String> getSwitchGroups() {
        return switchGroups;
    }

    public void createAuction(Location location, int numberOfSelected, Runnable onBuildFinished, Runnable onFailCreation) throws WorldNotFoundedException, AuctionPlaceException, StructureBuildException {
        Structure structure = new Structure(location.getChunk().getX() * 16, location.getBlockY(), location.getChunk().getZ() * 16,
                AuroraUniverse.getInstance().getConfig().getString("auction-structures." + numberOfSelected), location.getWorld().getName());

        if (!structure.isInSingleChunk())
            throw new AuctionPlaceException(AuroraUniverse.getLanguage().getString("auction-error-chunk"));
        if (!structure.isSpaceClear())
            throw new AuctionPlaceException(AuroraUniverse.getLanguage().getString("auction-error-place"));
        if (!hasChunk(ChunkPair.fromChunk(location.getChunk())))
            throw new AuctionPlaceException(AuroraUniverse.getLanguage().getString("auction-error-place"));


        if (auctionStructure != null) {
            if (auctionStructure.isFullBuilt() && AuroraUniverse.getInstance().isAuctionStructureEnabled()) {
                auctionStructure.destroy(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if(AuroraUniverse.getInstance().isAuctionStructureEnabled()){
                                structure.build(onBuildFinished);
                            }

                            auctionStructure = structure;
                        } catch (WorldNotFoundedException e) {
                            e.printStackTrace();
                        } catch (StructureBuildException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        onFailCreation.run();
                        auctionStructure = null;
                    }
                }, false);
            } else {

                Logger.debug("Auction is not full built!");

                onFailCreation.run();

            }
        } else
        {
            if(AuroraUniverse.getInstance().isAuctionStructureEnabled()) {
                structure.build(onBuildFinished);
            }
            auctionStructure = structure;
        }







    }


    public ResidentRegion getResidentRegion(ChunkPair chunk) {
        Region region = townChunks.get(chunk);
        if (region instanceof ResidentRegion) {
            return (ResidentRegion) region;
        } else {
            return null;
        }
    }


    public void createPlayerRegion(ChunkPair originalLocation, Resident resident) throws RegionException {
//        if (residents.contains(resident)) {
            if (townChunks.containsKey(originalLocation)) {
                if (originalLocation != mainChunk) {
                    townChunks.remove(originalLocation);

                    townChunks.put(originalLocation, new ResidentRegion(this, resident.getName()));
                    AuroraUniverse.removeTownBlock(originalLocation);
                    AuroraUniverse.addTownBlock(originalLocation, new ResidentRegion(this, resident.getName()));
                } else {
                    throw new RegionException("Unable to remove town's main chunk");
                }
            } else {
                throw new RegionException("Chunk don't belong to town");
            }

//        } else {
//            throw new RegionException("Resident don't belong to town");
//        }
    }

    public void resetRegion(ChunkPair originalLocation) throws RegionException {

        if (hasChunk(originalLocation)) {
            Region region = townChunks.get(originalLocation);
            if (originalLocation != mainChunk && region instanceof ResidentRegion) {
                townChunks.remove(originalLocation);
                townChunks.put(originalLocation, new Region(this));
                AuroraUniverse.removeTownBlock(originalLocation);
                AuroraUniverse.addTownBlock(originalLocation, new Region(this));
            } else {
                throw new RegionException("Unable to reset non-resident region");
            }
        } else {
            throw new RegionException("Chunk don't belong to town");
        }


    }

    public ArrayList<Resident> getResidents() {
        return residents;
    }

    public void setFireAllowed(boolean enableFireSpreading) {
        fire = enableFireSpreading;
    }

    public boolean setFireAllowed(Block block) {
        Region region = this.getRegion(block.getLocation());
        if (region instanceof ResidentRegion) {
            return ((ResidentRegion) region).isFire();
        }
        return fire;
    }

    public boolean isFire()
    {
        return fire;
    }
    public Bank getBank() {
        return townBank;
    }

    public void depositBank(double d) {
        townBank.deposit(d);
    }
    public void depositBank(double d, Resident resident) {
        if(d > AuroraUniverse.getInstance().getConfig().getInt("big-amount-starts-from")) {
            String deposited = AuroraLanguage.getColorString("gui.trans-history-deposit")
                    .replace("%s", resident.getName()).replace("%d", String.valueOf(d));

            if (transactionHistory.size() > 5) {
                ArrayList<String> temp = new ArrayList<>();
                int i = 0;
                int start = transactionHistory.size() - 5;
                for (String info : transactionHistory) {
                    if (i > start) {
                        temp.add(info);
                    }
                    i++;
                }
                transactionHistory = temp;

            }
            transactionHistory.add(deposited);
        }
        townBank.deposit(d);
    }

    public boolean withdrawBank(double d) {
        return townBank.withdraw(d);
    }
    public boolean withdrawBank(double d, Resident resident) {
        String withdrawn = AuroraLanguage.getColorString("gui.trans-history-withdraw")
                .replace("%s", resident.getName()).replace("%d", String.valueOf(d));
        if(d > AuroraUniverse.getInstance().getConfig().getInt("big-amount-starts-from")) {

            if (transactionHistory.size() > 5) {
                ArrayList<String> temp = new ArrayList<>();
                int i = 0;
                int start = transactionHistory.size() - 5;
                for (String info : transactionHistory) {
                    if (i > start) {
                        temp.add(info);
                    }
                    i++;
                }
                transactionHistory = temp;

            }
            transactionHistory.add(withdrawn);
        }
        return townBank.withdraw(d);
    }


    public Town(String name2, Resident mayor, ChunkPair homeblock) throws TownException {
        if (!mayor.hasTown()) {

            if (!AuroraUniverse.townList.containsKey(name2)) {
                if (!name2.equals("") && !name2.contains("\\")) {
                    if (!Towns.hasTown(homeblock)) {
                        name = name2;

                        id = System.currentTimeMillis() + homeblock.toString() + name;
                        this.mayor = mayor;
                        addResident(this.mayor);
                        this.mayor.setTown(name);
                        Group mayorPermissions = AuroraPermissions.getGroup("mayor");

                        toggleBuild(mayorPermissions);
                        toggleDestroy(mayorPermissions);
                        toggleUse(mayorPermissions);
                        toggleSwitch(mayorPermissions);

                        Group residentPermissions = AuroraPermissions.getGroup("resident");
                        toggleBuild(residentPermissions);
                        toggleDestroy(residentPermissions);
                        toggleUse(residentPermissions);
                        toggleSwitch(residentPermissions);
                        Region homeRegion = new Region(this);
                        townChunks.put(homeblock, homeRegion);
                        mainChunk = homeblock;
                        AuroraUniverse.addTownBlock(homeblock, homeRegion);
                        townBank = new Bank("aun.town." + name, 0, mayor.getName());
                        AuroraUniverse.getInstance().getEconomy().addBank(townBank);

                        Logger.log("Town " + name + " created by " + mayor.getName());
                    } else {
                        throw new TownException(AuroraUniverse.getLanguage().getString("e1"));
                    }

                } else {
                    throw new TownException(AuroraUniverse.getLanguage().getString("e2"));
                }
            } else {
                throw new TownException(AuroraUniverse.getLanguage().getString("e3").replace("%s", name2));
            }
        } else {
            throw new TownException(AuroraUniverse.getLanguage().getString("e4"));
        }
    }

    public Region getRegion(Location location) {

        ChunkPair chunkPair = ChunkPair.fromChunk(location.getChunk());
        if (hasChunk(chunkPair)) {
            return townChunks.get(chunkPair);
        } else {
            return null;
        }
    }

    public int getChunksCount() {
        return townChunks.size();
    }

    public String getMembersList() {
        String res = "";
        for (Resident resident : residents) {
            if (res.equals("")) {
                res = resident.getName();
            } else {
                res = res + ", " + resident.getName();
            }
        }
        return res;
    }

    public int getMembersCount() {
        return residents.size();
    }

    public ChunkPair getMainChunk() {
        return mainChunk;
    }


    public boolean isMayor(Resident r) {
        if (mayor.getName().equals(r.getName())) {
            return true;
        } else {
            return false;
        }
    }

    public void setPvP(boolean pvp2) {
        pvp = pvp2;
    }

    public boolean addResident(Resident resident) {
        if (!resident.hasTown()) {
            residents.add(resident);
            if (resident.getPermissionGroupName().equals("newbies")) {
                AuroraPermissions.setPermissions(resident.getName(), AuroraPermissions.getGroup("resident"));
            }
            resident.setTown(name);
            Logger.log("Added resident  " + resident.getName() + " to " + getName());
            return true;
        }
        return false;
    }

    public String getName() {
        return name;
    }


    public boolean removeResident(Resident resident) {
        if (residents.contains(resident)) {
            if (!isMayor(resident)) {
                residents.remove(resident);
                resident.setPermissionGroup("newbies");
                resident.setTown(null);
                resident.setTownRank("");
                AuroraPermissions.setPermissions(resident.getName(), AuroraPermissions.getGroup("newbies"));
                Logger.log("Kicked resident  " + resident.getName() + " to " + getName());
                return true;
            }

        }
        return false;
    }

    public boolean toggleBuild(Group group) {
        if (buildGroups.contains(group.getName())) {
            buildGroups.remove(group.getName());
            Logger.debug("Group " + group.getName() + " now can't build in " + getName());
            return false;
        } else {
            buildGroups.add(group.getName());
            Logger.debug("Group " + group.getName() + " now can build in " + getName());
            return true;
        }
    }

    public boolean toggleDestroy(Group group) {
        if (destroyGroups.contains(group.getName())) {
            destroyGroups.remove(group.getName());
            Logger.debug("Group " + group.getName() + " now can't destroy in " + getName());
            return false;
        } else {
            destroyGroups.add(group.getName());
            Logger.debug("Group " + group.getName() + " now can destroy in " + getName());
            return true;
        }
    }

    public boolean toggleUse(Group group) {
        if (useGroups.contains(group.getName())) {
            useGroups.remove(group.getName());
            Logger.debug("Group " + group.getName() + " now can't use in " + getName());
            return false;
        } else {
            useGroups.add(group.getName());
            Logger.debug("Group " + group.getName() + " now can use in " + getName());
            return true;
        }
    }

    public boolean toggleSwitch(Group group) {
        if (switchGroups.contains(group.getName())) {
            switchGroups.remove(group.getName());
            Logger.debug("Group " + group.getName() + " now can't switch in " + getName());
            return false;
        } else {
            switchGroups.add(group.getName());
            Logger.debug("Group " + group.getName() + " now can switch in " + getName());
            return true;
        }
    }

    public boolean canSwitch(Resident resident, ChunkPair chunk) {
        if (!isResident(resident)) return false;
        ResidentRegion residentRegion = getResidentRegion(chunk);
        if (residentRegion != null) {
            return residentRegion.canEdit(resident);
        }
        return switchGroups.contains(resident.getPermissionGroupName());
    }

    public boolean canUse(Resident resident, ChunkPair chunk) {
        if (!isResident(resident)) return false;
        ResidentRegion residentRegion = getResidentRegion(chunk);
        if (residentRegion != null) {
            return residentRegion.canEdit(resident);
        }
        return useGroups.contains(resident.getPermissionGroupName());
    }

    public boolean canDestroy(Resident resident, ChunkPair chunk) {
        if (!isResident(resident)) return false;
        ResidentRegion residentRegion = getResidentRegion(chunk);
        if (residentRegion != null) {
            return residentRegion.canEdit(resident);
        }
        return destroyGroups.contains(resident.getPermissionGroupName());
    }

    public boolean canBuild(Resident resident, ChunkPair chunk) {
        if (!isResident(resident)) return false;
        ResidentRegion residentRegion = getResidentRegion(chunk);
        if (residentRegion != null) {
            return residentRegion.canEdit(resident);
        }
        return buildGroups.contains(resident.getPermissionGroupName());
    }

    public boolean isResident(Resident resident) {
        return residents.contains(resident);
    }


    public void setForceExplosions(boolean forceExplosions) {
        this.forceExplosions = forceExplosions;
    }

    public boolean isConnected(Chunk chunk, @Nullable Player player) {
        AtomicBoolean connected = new AtomicBoolean(false);
        AuroraUniverse.getTownBlocks().forEach((chunk1, region) -> {
            int m = AuroraUniverse.getMinTownsDistance();
            for (int x = -1; x < 2; x++) {
                for (int z = -1; z < 2; z++) {
                    if (chunk1.getX() + x == chunk.getX() && chunk1.getZ() + z == chunk.getZ()) {
                        if (region.getTown().getName().equals(name)) //если это наш чанк(приват рядом со своим городом
                        {
                            //Проверяем есть ли диагональ
                            if (chunk1.getX() == chunk.getX() + m && chunk1.getZ() == chunk.getZ() + m) //++
                            {
                            } else if (chunk1.getX() == chunk.getX() - m && chunk1.getZ() == chunk.getZ() + m) // -+
                            {
                            } else if (chunk1.getX() == chunk.getX() + m && chunk1.getZ() == chunk.getZ() - m) //+-
                            {
                            } else if (chunk1.getX() == chunk.getX() - m && chunk1.getZ() == chunk.getZ() - m) //--
                            {
                            } else {
                                connected.set(true);
                            }
                        } else {

                            if(player != null)
                            {
                                Messaging.sendPrefixedMessage(Messages.claimTooClose(), player);
                            }

                        }
                    } else {
                        if (!region.getTown().getName().equals(name) && player != null) {
                            Messaging.sendPrefixedMessage(Messages.claimTooFar(), player);
                        }

                    }
                }
            }


            // chunk 2 is either adjacent to or has the same coordinates as chunk1


        });

        return connected.get();
    }

    public boolean delete() {
        PreTownDeleteEvent preTownDeleteEvent = new PreTownDeleteEvent(this);
        AuroraUniverse.callEvent(preTownDeleteEvent);

        if (!preTownDeleteEvent.isCancelled()) {
            for (Resident r :
                    residents) {
                r.setTown(null);
                r.setLastwild(true);
                r.setPermissionGroup("newbies");
            }

            if (hasNation()) {
                if(getNation().getCapital() == this)
                {
                    getNation().delete();
                }
            }

            AuroraUniverse.townList.remove(name);
            townChunks.forEach((chunk, region) -> {
                AuroraUniverse.removeTownBlock(chunk);
            });
            if (getNation() != null) {
                if (getNation().getCapital() == this) {
                    getNation().delete();
                } else {
                    getNation().removeTown(this);
                }

            }
            TownDeleteEvent townDeleteEvent = new TownDeleteEvent(this);
            AuroraUniverse.callEvent(townDeleteEvent);
            Logger.log("Town " + name + " deleted");
            return true;
        } else {
            Logger.log("Town " + name + " deletion was canceled");
            Logger.warning("Town deletion was canceled by third-party plugin");
            return false;
        }


    }

    public boolean hasNation() {
        return getNation() != null;
    }


    public Region claimChunk(ChunkPair chunk, Player player) throws TownException {
        boolean claimed = false;
        boolean success = false;
        boolean far = false;
        boolean isOutpost = true;
        if (townChunks.size() < getMaxChunks() && !townChunks.containsKey(chunk)) {
            for (ChunkPair chunk1 : AuroraUniverse.getTownBlocks().keySet()) {
                Region region = AuroraUniverse.getTownBlock(chunk1);
                int m = AuroraUniverse.getMinTownsDistance();
                if (!chunk1.equals(chunk) && !(region instanceof OutpostRegion)) // есть ли чанк, который мы хотим заприватить в городах
                {
                    //такого чанка нет


                    //другой чанк города лежит не более чем в 1 блоке рядом
                    for (int x = -1; x < 2; x++) {
                        for (int z = -1; z < 2; z++) {
                            if (chunk1.getX() + x == chunk.getX() && chunk1.getZ() + z == chunk.getZ()) {
                                if (region.getTown().getName().equals(name)) //если это наш чанк(приват рядом со своим городом
                                {
                                    //Проверяем есть ли диагональ

                                    if (chunk1.getX() == chunk.getX() + m && chunk1.getZ() == chunk.getZ() + m) //++
                                    {

                                        //Diagonal
                                    } else if (chunk1.getX() == chunk.getX() - m && chunk1.getZ() == chunk.getZ() + m) // -+
                                    {

                                        //Diagonal
                                    } else if (chunk1.getX() == chunk.getX() + m && chunk1.getZ() == chunk.getZ() - m) //+-
                                    {

                                        //Diagonal
                                    } else if (chunk1.getX() == chunk.getX() - m && chunk1.getZ() == chunk.getZ() - m) //--
                                    {

                                        //Diagonal
                                    } else {
                                        isOutpost = false;

                                        success = true;

                                    }


                                } else {

                                    claimed = true;
                                    // TODO: too close to another town
                                }
                            } else {
                                if (!region.getTown().getName().equals(name)) {
                                    // far.set(true);
                                }
                                // TODO: too far from town
                            }
                        }
                    }
                }


                // chunk 2 is either adjacent to or has the same coordinates as chunk1


            }
            Region toClaim = null;

            if (success && !claimed) {
                toClaim = new Region(this);
                townChunks.put(chunk, toClaim);
                Logger.info("Claimed chunk: " + chunk + " for town " + name);
                Logger.log("Claimed chunk: " + chunk + " for town " + name);
                AuroraUniverse.addTownBlock(chunk, toClaim);
            } else {
                if (!success && !claimed) {
                    if (outPosts.size() < AuroraUniverse.getMaxOutposts()) {
                        toClaim = new OutpostRegion(this, player.getLocation());
                        outPosts.add((OutpostRegion) toClaim);
                        AuroraUniverse.addTownBlock(chunk, toClaim);
                        townChunks.put(chunk, toClaim);
                    }
                }
            }

            return toClaim;
        }
        return null;
    }

    public boolean hasAuction()
    {

        if(!AuroraUniverse.getInstance().isAuctionEnabled()) return false;

        if(auctionStructure != null)
        {
            if(!AuroraUniverse.getInstance().isAuctionStructureEnabled()) return true;
            try {
                return auctionStructure.isFullBuilt();
            } catch (WorldNotFoundedException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public boolean unclaimChunk(ChunkPair chunk) {
        if(auctionStructure != null)
        {
            if(AuroraUniverse.getInstance().isAuctionStructureEnabled()) {
                if (chunk.equals(auctionStructure.getStartChunk())) {
                    return false;
                }
            }
        }


        if (townChunks.containsKey(chunk) && !mainChunk.equals(chunk)) {
            if (townChunks.get(chunk) instanceof OutpostRegion) {
                outPosts.remove((OutpostRegion) townChunks.get(chunk));
            }
            townChunks.remove(chunk);
            AuroraUniverse.removeTownBlock(chunk);
            return true;
        } else {
            return false;
        }
    }

    public void teleportToTownSpawn(Player pl) {
        pl.teleport(townSpawnPoint);
        Towns.handleChunkChange(pl, ChunkPair.fromChunk(pl.getLocation().getChunk()));
    }

    public void teleportToOutpost(Player pl, int outpostNum) {
        pl.teleport(outPosts.get(outpostNum).getSpawnLocation());
        Towns.handleChunkChange(pl, ChunkPair.fromChunk(pl.getLocation().getChunk()));
    }

    public boolean hasChunk(Location lc) {

        ChunkPair chunkPair = ChunkPair.fromChunk(lc.getChunk());

        return hasChunk(chunkPair);
    }

    public boolean hasChunk(ChunkPair chunkPair) {

        if (townChunks.containsKey(chunkPair)) {
            return true;
        } else {
            return false;
        }
    }


    public void setSpawn(Location location) throws TownException {
        if (hasChunk(location)) {
            townSpawnPoint = location;
            mainChunk = ChunkPair.fromChunk(location.getChunk());
            if ((getTownChunks().get(ChunkPair.fromChunk(location.getChunk())) instanceof ResidentRegion)) {
                throw new TownException(AuroraUniverse.getLanguage().getString("e5"));
            }


            // До лучших времён
//            Bukkit.getServer().getScheduler().runTaskTimer(AuroraUniverse.getInstance(), new Runnable() {
//                @Override
//                public void run() {
//                    Hohol hohol = new Hohol(location);
//                    WorldServer worldServer = ((CraftWorld) location.getWorld()).getHandle();
//                    worldServer.addEntity(hohol);
//                    hohol.setPosition(location.getX(), location.getY(), location.getZ());
//                }
//            }, 20L, 1L);

        } else {
            throw new TownException(AuroraUniverse.getLanguage().getString("e5"));
        }


    }

    public double getTownChunkTax()
    {
        return AuroraUniverse.getInstance().getConfig().getDouble("town-chunk-tax");
    }

    public double getTownTax() {
        double townChunkTax = getTownChunkTax();

        double multiplier = 1;
        for (double d: townChunkTaxMultipliers.values()) {
            multiplier *= d;
        }

        return Numbers.round(townChunkTax * (double) getChunksCount() * multiplier);
    }

    public void rename(String newName) {
        if(AuroraUniverse.getTownList().containsKey(newName)) return;
        TownRenameEvent townRenameEvent = new TownRenameEvent(newName, name);
        Bukkit.getPluginManager().callEvent(townRenameEvent);
        AuroraUniverse.getTownList().remove(name);
        AuroraUniverse.getTownList().put(newName, this);
        name = newName;


        for (Resident resident : residents) {
            resident.setTown(name);
        }
    }


    public Map<ChunkPair, Region> getTownChunks() {
        return townChunks;
    }

    public Resident getMayor() {
        return mayor;
    }
}
