package com.raphydaphy.vitality.core;

import org.objectweb.asm.tree.ClassNode;

public interface SubClassTransformer {

    public void transformClassNode(ClassNode cn, String transformedClassName, String obfName);

    public String getIdentifier();

    public void addErrorInformation();

    public boolean isTransformRequired(String transformedClassName);

}