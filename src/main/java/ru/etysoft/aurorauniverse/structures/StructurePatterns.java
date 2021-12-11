package ru.etysoft.aurorauniverse.structures;

import com.mysql.fabric.xmlrpc.base.Array;
import com.mysql.fabric.xmlrpc.base.Struct;
import org.bukkit.Location;
import org.bukkit.block.banner.Pattern;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FilenameUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import ru.etysoft.aurorauniverse.Logger;
import ru.etysoft.aurorauniverse.world.Resident;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;

public class StructurePatterns {

    public static Location bufferFrom;
    public static Location bufferTo;

    private static final String structureDir = "plugins/AuroraUniverse/structures";

    private static HashMap<String, ArrayList<StructBlock>> patterns = new HashMap<>();

    private static String readFile(File file) throws IOException {

        return FileUtils.readFileToString(file, StandardCharsets.UTF_8);
    }

    public static ArrayList<StructBlock> getBlocksFromPattern(String pattenName) throws PatternNotFoundedException {
        if (!patterns.containsKey(pattenName)) throw new PatternNotFoundedException();
        return patterns.get(pattenName);
    }

    public static void savePattern(String name, JSONArray pattern) {
        try (PrintWriter out = new PrintWriter(structureDir + "/" + name + ".json")) {
            out.println(pattern.toJSONString());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean hasPattern(String name)
    {
        return patterns.containsKey(name);
    }

    public static void loadPatterns()
    {
        Logger.debug("Loading structure patterns...");
        patterns.clear();
        File rootDir = new File(structureDir);
        if(rootDir.exists())
        {
            for(File structPattern : rootDir.listFiles())
            {
                String name = FilenameUtils.removeExtension(structPattern.getName());
                try {
                    ArrayList<StructBlock> structBlocks = new ArrayList<>();
                    JSONParser jsonParser = new JSONParser();
                    JSONArray jsonSchema = (JSONArray) jsonParser.parse(readFile(structPattern));
                    for (int i = 0; i < jsonSchema.size(); i++) {
                        JSONObject jsonObject = (JSONObject) jsonSchema.get(i);
                        StructBlock structBlock = null;
                        try {
                            structBlock = StructBlock.fromJSON(jsonObject);
                            structBlocks.add(structBlock);
                        } catch (StructureWrongCoordsException e) {
                            Logger.error("Error loading block for structure " + name);
                            e.printStackTrace();
                        }

                    }
                    Logger.info("Added structure pattern " + name + "(" + structBlocks.size() + " blocks)");
                    patterns.put(name, structBlocks);
                } catch (IOException | ParseException e) {
                    Logger.error("Error loading structure JSON for " + name);
                    e.printStackTrace();
                }
            }
        }
        else
        {
            try {
                Files.createDirectory(rootDir.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            Logger.warning("Structures folder does not exists!");
        }

    }

}
