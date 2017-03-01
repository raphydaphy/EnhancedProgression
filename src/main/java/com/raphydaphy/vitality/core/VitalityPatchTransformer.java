package com.raphydaphy.vitality.core;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.tree.ClassNode;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

public class VitalityPatchTransformer implements SubClassTransformer {

    private static final String PATCH_PACKAGE = "com.raphydaphy.vitality.core.patches";

    private static ClassPatch currentPatch = null;

    private Map<String, List<ClassPatch>> availablePatches = new HashMap<>();

    public VitalityPatchTransformer() throws IOException {
    	System.out.println("Vitality is loading patches..");
        int loaded = loadClassPatches();
        System.out.println("Vitality Transformer Initialized! Successfully loaded " + loaded + " patches.");
    }

    private int loadClassPatches() throws IOException {
        ImmutableSet<ClassPath.ClassInfo> classes =
                ClassPath.from(Thread.currentThread().getContextClassLoader()).getTopLevelClassesRecursive(PATCH_PACKAGE);
        List<Class> patchClasses = new LinkedList<>();
        for (ClassPath.ClassInfo info : classes) {
            if(info.getName().startsWith(PATCH_PACKAGE)) {
                patchClasses.add(info.load());
            }
        }
        int load = 0;
        for (Class patchClass : patchClasses) {
            if (ClassPatch.class.isAssignableFrom(patchClass) && !Modifier.isAbstract(patchClass.getModifiers())) {
                try {
                    ClassPatch patch = (ClassPatch) patchClass.newInstance();
                    if(!availablePatches.containsKey(patch.getClassName())) {
                        availablePatches.put(patch.getClassName(), new LinkedList<>());
                    }
                    availablePatches.get(patch.getClassName()).add(patch);
                    load++;
                } catch (Exception exc) {
                    throw new IllegalStateException("Could not load ClassPatch: " + patchClass.getSimpleName(), exc);
                }
            }
        }
        return load;
    }

    @Override
    public void transformClassNode(ClassNode cn, String transformedClassName, String obfName) {
        if(!availablePatches.isEmpty()) {
            List<ClassPatch> patches = availablePatches.get(transformedClassName);
            if(patches != null && !patches.isEmpty()) {
            	System.out.println("Vitality is transforming " + obfName + " : " + transformedClassName + " with " + patches.size() + " patches!");
                try {
                    for (ClassPatch patch : patches) {
                        currentPatch = patch;
                        patch.transform(cn);
                        System.out.println("Vitality Transformer applied patch" + patch.getClass().getSimpleName().toUpperCase());
                        currentPatch = null;
                    }
                } catch (Exception exc) {
                    throw new ASMTransformationException("Applying ClassPatches failed (ClassName: " + obfName + " - " + transformedClassName + ") - Rethrowing exception!", exc);
                }
            }
            availablePatches.remove(transformedClassName);
        }
    }

    @Override
    public String getIdentifier() {
        return "Patch based transformer";
    }

    @Override
    public void addErrorInformation() {
        if(currentPatch != null) {
        	System.out.println("Vitality was in active patch mode for: " + currentPatch.getClass().getSimpleName());
        }
    }

    @Override
    public boolean isTransformRequired(String transformedClassName) {
        return availablePatches.containsKey(transformedClassName);
    }

}