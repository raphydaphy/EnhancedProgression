package com.raphydaphy.vitality.render;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.raphydaphy.vitality.init.Reference;
import com.raphydaphy.vitality.item.ItemWand;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.block.model.ItemOverride;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.IModelCustomData;
import net.minecraftforge.client.model.IPerspectiveAwareModel;
import net.minecraftforge.client.model.ItemLayerModel;
import net.minecraftforge.client.model.ModelStateComposition;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelWand implements IModel, IModelCustomData
{
    public static final IModel MODEL = new ModelWand(null,null);
    private final ResourceLocation resourceCore;
    private final ResourceLocation resourceTip;
    private final String core;
    private final String tip;
    protected static final Map<String, String> moduleTransforms = Maps.newHashMap();

    public ModelWand(String coreType, String tipType)
    {
        this.core = coreType;
        this.tip = tipType;
        this.resourceCore = new ResourceLocation("vitality", "items/wand/core" + coreType.toLowerCase());
        this.resourceTip = new ResourceLocation("vitality", "items/wand/tip" + tipType.toLowerCase());
    }

    @Override
    public IModelState getDefaultState()
    {
        return TRSRTransformation.identity();
    }

    @Override
    public Collection<ResourceLocation> getDependencies()
    {
        return ImmutableList.of();
    }

    @Override
    public Collection<ResourceLocation> getTextures()
    {
        ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();
        
        builder.add(new ResourceLocation("vitality", "items/wand/tip_wood"));
        
        builder.add(new ResourceLocation("vitality", "items/wand/core_angelic"));
        builder.add(new ResourceLocation("vitality", "items/wand/core_atmospheric"));
        builder.add(new ResourceLocation("vitality", "items/wand/core_demonic"));
        builder.add(new ResourceLocation("vitality", "items/wand/core_energetic"));
        builder.add(new ResourceLocation("vitality", "items/wand/core_exotic"));

        return builder.build();
    }

    @Override
    public IModel process(ImmutableMap<String, String> customData)
    {
        for (Map.Entry<String, String> entry : customData.entrySet())
        {
            String key = entry.getKey();
            //System.out.printf("customData: %s => %s\n", key, entry.getValue());
            if (key != null && key.startsWith("tr_") == true)
            {
                moduleTransforms.put(key, entry.getValue());
            }
        }

        String core = customData.get("coreType");
        String tip = customData.get("tipType");

        return new ModelWand(core,tip);
    }

    @Override
    public IBakedModel bake(IModelState state, VertexFormat format,
                                    Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter)
    {
        ImmutableMap<TransformType, TRSRTransformation> transformMap = IPerspectiveAwareModel.MapWrapper.getTransforms(state);
        //TRSRTransformation transform = state.apply(Optional.<IModelPart>absent()).or(TRSRTransformation.identity());
        TextureAtlasSprite coreSprite = null;
        ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

        coreSprite = bakedTextureGetter.apply(this.resourceCore);

        if (this.resourceCore != null)
        {
            IBakedModel model = (new ItemLayerModel(ImmutableList.of(this.resourceCore))).bake(state, format, bakedTextureGetter);
            builder.addAll(model.getQuads(null, null, 0));
        }

        if (this.resourceTip != null)
        {
            IModelState stateTmp = this.getTransformedModelState(state, "co");
            IBakedModel model = (new ItemLayerModel(ImmutableList.of(this.resourceTip))).bake(stateTmp, format, bakedTextureGetter);
            builder.addAll(model.getQuads(null, null, 0));
        }

        return new BakedWand(this, builder.build(), coreSprite, format, Maps.immutableEnumMap(transformMap), Maps.<String, IBakedModel>newHashMap());
    }

    private IModelState getTransformedModelState(IModelState state, String module)
    {
        ModuleTransforms mt = new ModuleTransforms(this, "Wand", module);
        TRSRTransformation tr = new TRSRTransformation(new Vector3f(mt.tx, mt.ty, mt.tz), null, new Vector3f(mt.sx, mt.sy, mt.sz), null);
        return new ModelStateComposition(state, TRSRTransformation.blockCenterToCorner(tr));
    }

    protected static class ModuleTransforms
    {
        public final float tx;
        public final float ty;
        public final float tz;
        public final float sx;
        public final float sy;
        public final float sz;

        private ModuleTransforms(ModelWand parent, String tool, String module)
        {
            float tx = 0f, ty = 0f, tz = 0f, sx = 1.02f, sy = 1.02f, sz = 1.6f;
            String id = tool.equals("sword") == true ? "w" : tool.substring(0, 1);

            try
            {
                String str = moduleTransforms.get("tr_tx_" + id + "_" + module);
                if (str != null) tx = Float.valueOf(str);

                str = moduleTransforms.get("tr_ty_" + id + "_" + module);
                if (str != null) ty = Float.valueOf(str);

                str = moduleTransforms.get("tr_tz_" + id + "_" + module);
                if (str != null) tz = Float.valueOf(str);

                str = moduleTransforms.get("tr_sx_" + id + "_" + module);
                if (str != null) sx = Float.valueOf(str);

                str = moduleTransforms.get("tr_sy_" + id + "_" + module);
                if (str != null) sy = Float.valueOf(str);

                str = moduleTransforms.get("tr_sz_" + id + "_" + module);
                if (str != null) sz = Float.valueOf(str);
            }
            catch (NumberFormatException e)
            {
               	System.out.println("something bad happened");
            }

            //System.out.printf("tx: %.2f, ty: %.2f, tz: %.2f, sx: %.2f, sy: %.2f, sz: %.2f\n", tx, ty, tz, sx, sy, sz);
            this.tx = tx;
            this.ty = ty;
            this.tz = tz;
            this.sx = sx;
            this.sy = sy;
            this.sz = sz;
        }
    }

    private static final class BakedWandOverrideHandler extends ItemOverrideList
    {
		public static final BakedWandOverrideHandler INSTANCE = new BakedWandOverrideHandler();

        private BakedWandOverrideHandler()
        {
            super(ImmutableList.<ItemOverride>of());
        }

        @Override
        public IBakedModel handleItemState(IBakedModel originalModelIn, ItemStack stack, World world, EntityLivingBase entity)
        {
        	String coreType = "Unknown";
        	String tipType = "Unknown";
            if (stack.hasTagCompound())
            {
            	coreType = stack.getTagCompound().getString("coreType");
            	tipType = stack.getTagCompound().getString("tipType");
            }
            String key = coreType + tipType;

            BakedWand originalModel = (BakedWand) originalModelIn;

            if (originalModel.cache.containsKey(key) == false)
            {
                ImmutableMap.Builder<String, String> map = ImmutableMap.builder();
                map.put("coreType", coreType);
                map.put("tipType", tipType);

                IModel model = originalModel.parent.process(map.build());

                Function<ResourceLocation, TextureAtlasSprite> textureGetter;
                textureGetter = new Function<ResourceLocation, TextureAtlasSprite>()
                {
                    public TextureAtlasSprite apply(ResourceLocation location)
                    {
                        return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
                    }
                };

                IBakedModel bakedModel = model.bake(new SimpleModelState(originalModel.transforms), originalModel.format, textureGetter);
                originalModel.cache.put(key, bakedModel);

                return bakedModel;
            }

            return originalModel.cache.get(key);
        }
    }

    protected static class BakedWand implements IPerspectiveAwareModel
    {
        private final ModelWand parent;
        private final Map<String, IBakedModel> cache; // contains all the baked models since they'll never change
        private final ImmutableMap<TransformType, TRSRTransformation> transforms;
        private final ImmutableList<BakedQuad> quads;
        private final TextureAtlasSprite particle;
        private final VertexFormat format;

        public BakedWand(ModelWand parent, ImmutableList<BakedQuad> quads, TextureAtlasSprite particle, VertexFormat format,
                ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms, Map<String, IBakedModel> cache)
        {
            this.quads = quads;
            this.particle = particle;
            this.format = format;
            this.parent = parent;
            this.transforms = transforms;
            this.cache = cache;
        }

        @Override
        public ItemOverrideList getOverrides()
        {
            return BakedWandOverrideHandler.INSTANCE;
        }

        @Override
        public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType)
        {
            return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, this.transforms, cameraTransformType);
        }

        @Override
        public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand)
        {
            if(side == null) return quads;
            return ImmutableList.of();
        }

        public boolean isAmbientOcclusion() { return true;  }
        public boolean isGui3d() { return false; }
        public boolean isBuiltInRenderer() { return false; }
        public TextureAtlasSprite getParticleTexture() { return particle; }
        public ItemCameraTransforms getItemCameraTransforms() { return ItemCameraTransforms.DEFAULT; }
    }

    public enum LoaderWand implements ICustomModelLoader
    {
        instance;

        @Override
        public boolean accepts(ResourceLocation modelLocation)
        {
            return modelLocation.getResourceDomain().equals(Reference.MOD_ID) && modelLocation.getResourcePath().contains("generated_model_wand");
        }

        @Override
        public IModel loadModel(ResourceLocation modelLocation) throws IOException
        {
            return MODEL;
        }

        @Override
        public void onResourceManagerReload(IResourceManager resourceManager)
        {
            // no need to clear cache since we create a new model instance
        }
    }
}