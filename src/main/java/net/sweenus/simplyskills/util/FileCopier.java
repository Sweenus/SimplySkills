package net.sweenus.simplyskills.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.Identifier;
import net.sweenus.simplyskills.SimplySkills;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class FileCopier {
    public static void copyFileToConfigDirectory(String fileName) {
        String configDirectory = FabricLoader.getInstance().getConfigDir().toString(); // Config directory path
        System.out.println("config directory path is: "+configDirectory);
        //System.out.println("Possible path is: "+FileCopier.class.getClassLoader().getResourceAsStream("/src/main/java/net/sweenus/simplyskills/").toString());

        try {
            // Get the input stream of the file within the .jar WHERE THE HECK IS THE FILE
            InputStream inputStream = FileCopier.class.getResourceAsStream("/src/main/java/net/sweenus/simplyskills/" + fileName);
            System.out.println("trying to copy: "+inputStream);

            // Create the target file in the config directory
            File targetFile = new File(configDirectory, fileName);

            // Copy the file from the input stream to the target file
            Files.copy(inputStream, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            System.out.println("File copied successfully!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}