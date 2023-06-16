package net.sweenus.simplyskills.util;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class FileGen {

    public static void copyFiles(String fileSource, String fileDest, Boolean overwrite) {

        File source = new File("fileSource");
        File dest = new File("fileDest");

        if (dest.exists() && !overwrite) return;

        try {
            FileUtils.copyDirectory(source, dest);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void generateSkillFiles() {

        copyFiles("data/simplyskills/puffish_skills/categories/combat/experience.json", "config/puffish_skills/categories/combat/experience.json", true);

    }

    public static void checkFileDirectory() {
        File directory = new File("./");
        System.out.println(directory.getAbsolutePath());
    }


}
