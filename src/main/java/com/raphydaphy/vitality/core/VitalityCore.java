package com.raphydaphy.vitality.core;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.Name;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.SortingIndex;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;

@Name(value = "VitalityCore")
@MCVersion(value = "1.10.2")
@TransformerExclusions({ "com.raphydaphy.vitality.core" })
@SortingIndex(100)
public class VitalityCore implements IFMLLoadingPlugin, IFMLCallHook {

	public VitalityCore() {
		System.out.println("Vitality Core Initialized");
	}

	@Override
	public Void call() throws Exception {
		return null;
	}

	@Override
	public String[] getASMTransformerClass() {
		return null;
	}

	@Override
	public String getModContainerClass() {
		return null;
	}

	@Override
	public String getSetupClass() {
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data) {
	}

	@Override
	public String getAccessTransformerClass() {
		return "com.raphydaphy.vitality.core.VitalityTransformer";
	}

}