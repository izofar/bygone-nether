package com.izofar.bygonenether.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ReplaceItemLootModifier extends LootModifier {

    public static final Supplier<Codec<ReplaceItemLootModifier>> CODEC = Suppliers.memoize(() -> RecordCodecBuilder.create(
            inst -> codecStart(inst).and(
                    inst.group(
                        ForgeRegistries.ITEMS.getCodec().fieldOf("target").forGetter(m -> m.target),
                        ForgeRegistries.ITEMS.getCodec().fieldOf("replacement").forGetter(m -> m.replacement),
                        Codec.STRING.fieldOf("mod_dependency").forGetter(m -> m.modDependency)
                    )
            ).apply(inst, ReplaceItemLootModifier::new)));

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
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        if (ModList.get().isLoaded(this.modDependency)) {
            generatedLoot.replaceAll(this.conversion);
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC.get();
    }
}