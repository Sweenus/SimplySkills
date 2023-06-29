package net.sweenus.simplyskills.util;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SimplySkillsPreInit implements PreLaunchEntrypoint {

    @Override
    public void onPreLaunch() {
        String fileName = "example.txt";
        FileCopier.copyFileToConfigDirectory(fileName);
    }


}
