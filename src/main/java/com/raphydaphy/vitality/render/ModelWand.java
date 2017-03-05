package com.raphydaphy.vitality.render;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
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
import com.raphydaphy.vitality.api.wand.WandEnums.CoreType;
import com.raphydaphy.vitality.api.wand.WandEnums.TipType;
import com.raphydaphy.vitality.api.wand.WandHelper;
import com.raphydaphy.vitality.init.Reference;

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
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class ModelWand implements IModel, IModelCustomData {
	public static final IModel MODEL = new ModelWand(CoreType.DEMONIC, TipType.WOODEN);
	private final ResourceLocation resourceCore;
	private final ResourceLocation resourceTip;

	public ModelWand(CoreType coreType, TipType tipType) {
		System.out.println(coreType);
		System.out.println(
				new ResourceLocation("vitality", "items/wand/core_" + coreType.getName().toLowerCase()).toString());
		this.resourceCore = new ResourceLocation("vitality", "items/wand/core_" + coreType.getName().toLowerCase());
		this.resourceTip = new ResourceLocation("vitality", "items/wand/tip_" + tipType.getName().toLowerCase());
	}

	@Override
	public IModelState getDefaultState() {
		return TRSRTransformation.identity();
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return ImmutableList.of();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		ImmutableSet.Builder<ResourceLocation> builder = ImmutableSet.builder();

		builder.add(new ResourceLocation("vitality", "items/wand/tip_wooden"));

		builder.add(new ResourceLocation("vitality", "items/wand/core_angelic"));
		builder.add(new ResourceLocation("vitality", "items/wand/core_atmospheric"));
		builder.add(new ResourceLocation("vitality", "items/wand/core_demonic"));
		builder.add(new ResourceLocation("vitality", "items/wand/core_energetic"));
		builder.add(new ResourceLocation("vitality", "items/wand/core_exotic"));

		return builder.build();
	}

	@Override
	public IModel process(ImmutableMap<String, String> customData) {
		for (Map.Entry<String, String> entry : customData.entrySet()) {
			String key = entry.getKey();
			System.out.printf("customData: %s => %s\n", key, entry.getValue());
		}

		System.out.println(customData.size());

		CoreType core = CoreType.ANGELIC;
		TipType tip = TipType.WOODEN;
		try {
			core = CoreType.valueOf(customData.get(WandHelper.CORE_TYPE));

		} catch (Exception e) {

		}
		try {
			tip = TipType.valueOf(customData.get(WandHelper.TIP_TYPE));
		} catch (Exception e) {

		}
		System.out.println("Core: " + core + " Tip:" + tip);
		return new ModelWand(core, tip);
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		ImmutableMap<TransformType, TRSRTransformation> transformMap = IPerspectiveAwareModel.MapWrapper
				.getTransforms(state);
		// TRSRTransformation transform =
		// state.apply(Optional.<IModelPart>absent()).or(TRSRTransformation.identity());
		TextureAtlasSprite coreSprite = null;
		ImmutableList.Builder<BakedQuad> builder = ImmutableList.builder();

		coreSprite = bakedTextureGetter.apply(this.resourceCore);

		if (this.resourceCore != null) {
			IBakedModel model = (new ItemLayerModel(ImmutableList.of(this.resourceCore))).bake(state, format,
					bakedTextureGetter);
			builder.addAll(model.getQuads(null, null, 0));
		}

		if (this.resourceTip != null) {
			IBakedModel model = (new ItemLayerModel(ImmutableList.of(this.resourceTip))).bake(state, format,
					bakedTextureGetter);
			builder.addAll(model.getQuads(null, null, 0));
		}

		return new BakedWand(this, builder.build(), coreSprite, format, Maps.immutableEnumMap(transformMap),
				Maps.<String, IBakedModel>newHashMap());
	}

	private static final class BakedWandOverrideHandler extends ItemOverrideList {
		public static final BakedWandOverrideHandler INSTANCE = new BakedWandOverrideHandler();

		private BakedWandOverrideHandler() {
			super(ImmutableList.<ItemOverride>of());
		}

		@Override
		public IBakedModel handleItemState(IBakedModel originalModelIn, ItemStack stack, World world,
				EntityLivingBase entity) {
			CoreType coreType = null;
			TipType tipType = null;
			if (stack.hasTagCompound()) {
				SimpleEntry<CoreType, TipType> pair = WandHelper.getUsefulInfo(stack);
				coreType = pair.getKey();
				tipType = pair.getValue();
			}
			if (coreType == null) {
				coreType = CoreType.ANGELIC;
			}
			if (tipType == null) {
				tipType = TipType.WOODEN;
			}

			String key = coreType.getName() + tipType.getName();

			BakedWand originalModel = (BakedWand) originalModelIn;

			if (originalModel.cache.containsKey(key) == false) {
				ImmutableMap.Builder<String, String> map = ImmutableMap.builder();
				map.put(WandHelper.CORE_TYPE, coreType.getName());
				map.put(WandHelper.TIP_TYPE, tipType.getName());

				IModel model = originalModel.parent.process(map.build());

				Function<ResourceLocation, TextureAtlasSprite> textureGetter;
				textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
					public TextureAtlasSprite apply(ResourceLocation location) {
						return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
					}
				};

				IBakedModel bakedModel = model.bake(new SimpleModelState(originalModel.transforms),
						originalModel.format, textureGetter);
				originalModel.cache.put(key, bakedModel);

				return bakedModel;
			}

			return originalModel.cache.get(key);
		}
	}

	protected static class BakedWand implements IPerspectiveAwareModel {
		private final ModelWand parent;
		private final Map<String, IBakedModel> cache; // contains all the baked
														// models since they'll
														// never change
		private final ImmutableMap<TransformType, TRSRTransformation> transforms;
		private final ImmutableList<BakedQuad> quads;
		private final TextureAtlasSprite particle;
		private final VertexFormat format;

		public BakedWand(ModelWand parent, ImmutableList<BakedQuad> quads, TextureAtlasSprite particle,
				VertexFormat format, ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> transforms,
				Map<String, IBakedModel> cache) {
			this.quads = quads;
			this.particle = particle;
			this.format = format;
			this.parent = parent;
			this.transforms = transforms;
			this.cache = cache;
		}

		@Override
		public ItemOverrideList getOverrides() {
			return BakedWandOverrideHandler.INSTANCE;
		}

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(TransformType cameraTransformType) {
			if (cameraTransformType == TransformType.FIRST_PERSON_RIGHT_HAND) {
				TRSRTransformation tr = new TRSRTransformation(new Vector3f(-0.25f, 0.6f, 0),
						TRSRTransformation.quatFromXYZ(new Vector3f(0, 0, 49.9f)), new Vector3f(0.90f, 0.90f, 0.90f),
						TRSRTransformation.quatFromXYZ(new Vector3f(0, 0, 49.9f)));
				Matrix4f mat = null;
				if (tr != null && !tr.equals(TRSRTransformation.identity()))
					mat = TRSRTransformation.blockCornerToCenter(tr).getMatrix();
				return Pair.of(this, mat);
			} else if (cameraTransformType == TransformType.FIRST_PERSON_LEFT_HAND) {
				TRSRTransformation tr = new TRSRTransformation(new Vector3f(0.5f, 0f, 0),
						TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 0, 22)),
						new Vector3f(0.90f, 0.90f, 0.90f),
						TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 0, 22)));
				Matrix4f mat = null;
				if (tr != null && !tr.equals(TRSRTransformation.identity()))
					mat = TRSRTransformation.blockCornerToCenter(tr).getMatrix();
				return Pair.of(this, mat);
			} else if (cameraTransformType == TransformType.THIRD_PERSON_RIGHT_HAND) {
				// Vector3f(left/right, forwards,back, up/down)
				TRSRTransformation tr = new TRSRTransformation(new Vector3f(-0.05f, 0.8f, 1.2f),
						TRSRTransformation.quatFromXYZDegrees(new Vector3f(340f, 230, 310f)),
						new Vector3f(0.85f, 0.85f, 0.85f),
						TRSRTransformation.quatFromXYZDegrees(new Vector3f(340f, 200, 280f)));
				Matrix4f mat = null;
				if (tr != null && !tr.equals(TRSRTransformation.identity()))
					mat = TRSRTransformation.blockCornerToCenter(tr).getMatrix();
				return Pair.of(this, mat);
			} else if (cameraTransformType == TransformType.THIRD_PERSON_LEFT_HAND) {
				// Vector3f(left/right, forwards,back, up/down)
				// quatFromXYZDegrees(?, ?, tilt left/right)
				// TRSRTransformation tr = new TRSRTransformation(new
				// Vector3f(0f,0f,0f), TRSRTransformation.quatFromXYZDegrees(new
				// Vector3f(0f,0,0f)), new Vector3f(0.85f,0.85f,0.85f),
				// TRSRTransformation.quatFromXYZDegrees(new
				// Vector3f(0f,0,0f)));
				// Matrix4f mat = null;
				// if(tr != null && !tr.equals(TRSRTransformation.identity()))
				// mat = TRSRTransformation.blockCornerToCenter(tr).getMatrix();
				// return Pair.of(this, mat);
			} else if (cameraTransformType == TransformType.GROUND) {
				TRSRTransformation tr = new TRSRTransformation(new Vector3f(0, 0, 0f),
						TRSRTransformation.quatFromXYZ(new Vector3f(0, 0, 0)), new Vector3f(0.85f, 0.85f, 0.85f),
						TRSRTransformation.quatFromXYZ(new Vector3f(0, 0, 0)));
				Matrix4f mat = null;
				if (tr != null && !tr.equals(TRSRTransformation.identity()))
					mat = TRSRTransformation.blockCornerToCenter(tr).getMatrix();
				return Pair.of(this, mat);
			}
			return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, this.transforms, cameraTransformType);
		}

		@Override
		public List<BakedQuad> getQuads(IBlockState state, EnumFacing side, long rand) {
			if (side == null)
				return quads;
			return ImmutableList.of();
		}

		public boolean isAmbientOcclusion() {
			return true;
		}

		public boolean isGui3d() {
			return false;
		}

		public boolean isBuiltInRenderer() {
			return false;
		}

		public TextureAtlasSprite getParticleTexture() {
			return particle;
		}

		public ItemCameraTransforms getItemCameraTransforms() {
			return ItemCameraTransforms.DEFAULT;
		}
	}

	public enum LoaderWand implements ICustomModelLoader {
		instance;

		@Override
		public boolean accepts(ResourceLocation modelLocation) {
			return modelLocation.getResourceDomain().equals(Reference.MOD_ID)
					&& modelLocation.getResourcePath().contains("generated_model_wand");
		}

		@Override
		public IModel loadModel(ResourceLocation modelLocation) throws IOException {
			return MODEL;
		}

		@Override
		public void onResourceManagerReload(IResourceManager resourceManager) {
			// no need to clear cache since we create a new model instance
		}
	}
}