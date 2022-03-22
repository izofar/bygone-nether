package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.entity.PiglinHunterEntity;
import com.izofar.bygonenether.entity.PiglinPrisonerEntity;
import com.izofar.bygonenether.entity.WarpedEnderManEntity;
import com.izofar.bygonenether.entity.WexEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public abstract class ModEntityTypes {

	public static final DeferredRegister<EntityType<?>> MOD_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, BygoneNetherMod.MODID);

	public static final RegistryObject<EntityType<WexEntity>> WEX = MOD_ENTITY_TYPES.register("wex", () -> EntityType.Builder.of(WexEntity::new, EntityClassification.MONSTER).fireImmune().sized(0.4F, 0.8F).clientTrackingRange(8).build(new ResourceLocation(BygoneNetherMod.MODID, "wex").toString()));
	public static final RegistryObject<EntityType<WarpedEnderManEntity>> WARPED_ENDERMAN = MOD_ENTITY_TYPES.register("warped_enderman", () -> EntityType.Builder.of(WarpedEnderManEntity::new, EntityClassification.MONSTER).fireImmune().sized(0.6F, 2.9F).clientTrackingRange(8).build(new ResourceLocation(BygoneNetherMod.MODID, "warped_enderman").toString()));
	
	public static final RegistryObject<EntityType<PiglinPrisonerEntity>> PIGLIN_PRISONER = MOD_ENTITY_TYPES.register("piglin_prisoner", () -> EntityType.Builder.of(PiglinPrisonerEntity::new, EntityClassification.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(new ResourceLocation(BygoneNetherMod.MODID, "piglin_prisoner").toString()));
	public static final RegistryObject<EntityType<PiglinHunterEntity>> PIGLIN_HUNTER = MOD_ENTITY_TYPES.register("piglin_hunter", () -> EntityType.Builder.of(PiglinHunterEntity::new, EntityClassification.MONSTER).sized(0.6F, 1.95F).clientTrackingRange(8).build(new ResourceLocation(BygoneNetherMod.MODID, "piglin_hunter").toString()));

	public static void register(IEventBus eventBus) { MOD_ENTITY_TYPES.register(eventBus); }

}