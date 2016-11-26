package com.raphydaphy.enhancedprogression.init;

import net.minecraftforge.common.config.Config;

@Config(modid = Reference.MOD_ID, name = Reference.MOD_ID + "/" + Reference.MOD_ID)
public class ConfigHandler 
{

    public static WandSettings wandSettings = new WandSettings();
    public static Crafting crafting = new Crafting();
    public static World world = new World();

    public static class Crafting 
    {
        @Config.Comment({"Allow crafting bronze from three copper ingots and one tin.", "Default: true"})
        public boolean enableBronzeCrafting = true;
        
        @Config.Comment({"Enable Vial Extraction Spell Crafting.", "Default: true"})
        public boolean enableVitalExtraction = true;
        
        @Config.Comment({"Enable Enhanced Extraction Spell Crafting.", "Default: true"})
        public boolean enableEnhancedExtraction = true;
        
        @Config.Comment({"Enable Magic Lantern Spell Crafting.", "Default: true"})
        public boolean enableMagicLantern = true;
        
        @Config.Comment({"Enable Contained Explosion Spell Crafting.", "Default: true"})
        public boolean enableContainedExplosion = true;
        
        @Config.Comment({"Enable Radiant Fireball Spell Crafting.", "Default: true"})
        public boolean enableRadiantFireball = true;
        
        @Config.Comment({"Enable Cryptic Transmutation Spell Crafting.", "Default: true"})
        public boolean enableCrypticTransmutation = true;
        
        @Config.Comment({"Enable Rapidfire Spell Crafting.", "Default: true"})
        public boolean enableRapidfire = true;
        
        @Config.Comment({"Enable Hunger Spell Crafting.", "Default: true"})
        public boolean enableHunger = true;
    }

    public static class WandSettings {
        @Config.Comment({"How much essence the basic wand should store when full.", "Default: 1000"})
        public int basicWandStorage = 1000;
        
        @Config.Comment({"Should the basic wand be able to break when extracting essence.", "Default: true"})
        public boolean canBasicWandBreak = true;
        
        @Config.Comment({"How much essence the advanced wand should store when full.", "Default: 10000"})
        public int advancedWandStorage = 10000;
    }

    public static class World {
        @Config.Comment({"Enable copper generation.", "Default: true"})
        public boolean enableCopperGen = true;
        
        @Config.Comment({"Enable tin generation.", "Default: true"})
        public boolean enableTinGen = true;
    }
}