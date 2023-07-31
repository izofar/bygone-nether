package com.izofar.bygonenether.loot;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.function.UnaryOperator;

public class ReplaceItemLootModifier extends LootModifier {

    private final Item target;
    private final Item replacement;
    private final String modDependency;
    private final UnaryOperator<ItemStack> conversion;

    protected ReplaceItemLootModifier(LootItemCondition[] conditions, Item target, Item replacement, String modDependency) {
        super(conditions);
        this.target = target;
        this.replacement = replacement;
        this.modDependency = modDependency;
        conversion = itemStack -> itemStack.getItem() == target ? new ItemStack(this.replacement, itemStack.getCount()) : itemStack;
    }

    @Nonnull
    @Override
    protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context) {
        if (ModList.get().isLoaded(this.modDependency)) {
            generatedLoot.replaceAll(this.conversion);
        }
        return generatedLoot;
    }

    public static class Serializer extends GlobalLootModifierSerializer<ReplaceItemLootModifier> {

        @Override
        public ReplaceItemLootModifier read(ResourceLocation name, JsonObject object, LootItemCondition[] conditions) {
            Item target = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "target")));
            Item replacement = ForgeRegistries.ITEMS.getValue(new ResourceLocation(GsonHelper.getAsString(object, "replacement")));
            String modDependency = GsonHelper.getAsString(object, "mod_dependency");
            return new ReplaceItemLootModifier(conditions, target, replacement, modDependency);
        }

        @Override
        public JsonObject write(ReplaceItemLootModifier instance) {
            JsonObject json = makeConditions(instance.conditions);
            json.addProperty("target", ForgeRegistries.ITEMS.getKey(instance.target).toString());
            json.addProperty("replacement", ForgeRegistries.ITEMS.getKey(instance.replacement).toString());
            json.addProperty("mod_dependency", instance.modDependency);
            return json;
        }
    }

}
