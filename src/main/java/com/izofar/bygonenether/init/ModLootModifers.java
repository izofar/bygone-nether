package com.izofar.bygonenether.init;

import com.izofar.bygonenether.BygoneNetherMod;
import com.izofar.bygonenether.loot.ReplaceItemLootModifier;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModLootModifers {

    public static final DeferredRegister<GlobalLootModifierSerializer<?>> LOOT_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.LOOT_MODIFIER_SERIALIZERS.get(), BygoneNetherMod.MODID);

    public static final RegistryObject<GlobalLootModifierSerializer<?>> REPLACE_ITEM = LOOT_MODIFIER_SERIALIZERS.register("replace_item", ReplaceItemLootModifier.Serializer::new);

    public static void register(IEventBus bus) { LOOT_MODIFIER_SERIALIZERS.register(bus); }

}
