package al132.alchemistry.datagen;

import al132.alchemistry.Ref;
import al132.alchemistry.blocks.combiner.CombinerRecipe;
import al132.alchemistry.blocks.combiner.CombinerRegistry;
import al132.alchemistry.blocks.dissolver.DissolverRecipe;
import al132.alchemistry.blocks.dissolver.DissolverRegistry;
import al132.alchemistry.blocks.dissolver.DissolverTagData;
import al132.alchemistry.datagen.recipe.CombinerRecipeBuilder;
import al132.alchemistry.datagen.recipe.DissolverRecipeBuilder;
import al132.alchemistry.datagen.recipe.FissionRecipeBuilder;
import al132.alchemistry.misc.ProbabilityGroup;
import al132.alchemistry.misc.ProbabilitySet;
import al132.chemlib.chemistry.CompoundRegistry;
import al132.chemlib.chemistry.ElementRegistry;
import al132.chemlib.items.CompoundItem;
import al132.chemlib.items.ElementItem;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.RecipeProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeTagHandler;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ForgeItemTagsProvider;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static al132.alchemistry.utils.StackUtils.toStack;
import static com.google.common.collect.Lists.newArrayList;

public class Recipes extends RecipeProvider {

    public static List<DissolverTagData> metalTagData = new ArrayList<>();
    public static List<String> metals = new ArrayList<>();
    public static final String heathenSpelling = "aluminium";
    private Consumer<IFinishedRecipe> consumer;


    public Recipes(DataGenerator generatorIn) {
        super(generatorIn);
    }

    @Override
    protected void registerRecipes(Consumer<IFinishedRecipe> consumer) {
        this.consumer = consumer;
        initMetals();
        registerFissionRecipes();
        registerCombinerRecipes();
        registerDissolverRecipes();
    }

    private void combiner(Item output, List<ItemStack> input) {
        combiner(output, 1, input);
    }

    private void combiner(Item output, int outputCount, List<ItemStack> input) {
        CombinerRecipeBuilder.recipe(new ItemStack(output, outputCount),
                input.stream()
                        .map(x -> {
                            if (x == null) return ItemStack.EMPTY;
                            else return x;
                        }).collect(Collectors.toList())).build(consumer);
    }

    public static ProbabilitySet.Builder set() {
        return new ProbabilitySet.Builder();
    }

    public void dissolver(String tag, ProbabilitySet set) {
        dissolver(Ingredient.fromTag(ForgeTagHandler.createOptionalTag(ForgeRegistries.ITEMS, new ResourceLocation(tag))),
                set, tag.replace(":", "."));
    }

    public void dissolver(Ingredient input, ProbabilitySet set, String name) {
        DissolverRecipeBuilder.recipe(input, set, name).build(consumer);
    }

    public void dissolver(Ingredient input, ProbabilitySet set, String name, boolean reversible) {
        DissolverRecipeBuilder.recipe(input, set, name).build(consumer);
        if (reversible) {
            CombinerRecipeBuilder.recipe(input.getMatchingStacks()[0], set.getSet().get(0).getOutputs());
        }
    }

    public void dissolver(Item input, ProbabilitySet set) {
        dissolver(Ingredient.fromItems(input), set, input.getRegistryName().getPath(), false);
    }

    public void dissolver(Item input, ProbabilitySet set, boolean reversible) {
        dissolver(Ingredient.fromItems(input), set, input.getRegistryName().getPath(), reversible);
    }


    public void dissolver(ItemStack input, ProbabilitySet set) {
        dissolver(Ingredient.fromStacks(input), set, input.getItem().getRegistryName().getPath(), false);
    }

    public void dissolver(ItemStack input, ProbabilitySet set, boolean reversible) {
        dissolver(Ingredient.fromStacks(input), set, input.getItem().getRegistryName().getPath(), reversible);
    }

    private void registerFissionRecipes() {
        for (int i = 2; i <= 118; i++) {
            FissionRecipeBuilder.recipe(i).build(consumer);
        }
    }

    private void registerDissolverRecipes() {
        dissolver("minecraft:logs",
                set().addGroup(1.0, toStack("cellulose")).build());

        dissolver("minecraft:planks",
                set().relative(false).addGroup(25.0, toStack("cellulose")).build());

        dissolver("minecraft:leaves",
                set().relative(false).addGroup(5, toStack("cellulose")).build());

        dissolver(Items.COBBLESTONE,
                set().addGroup(700, ItemStack.EMPTY)
                        .addGroup(2, toStack("aluminum"))
                        .addGroup(4, toStack("iron"))
                        .addGroup(1.5, toStack("gold"))
                        .addGroup(10, toStack("silicon_dioxide"))
                        .addGroup(1, toStack("dysprosium"))
                        .addGroup(1.5, toStack("zirconium"))
                        .addGroup(1, toStack("nickel"))
                        .addGroup(1, toStack("gallium"))
                        .addGroup(1, toStack("tungsten"))
                        .build());

        dissolver(Items.MOSSY_COBBLESTONE,
                set().addGroup(697, ItemStack.EMPTY)
                        .addGroup(2, toStack("aluminum"))
                        .addGroup(4, toStack("iron"))
                        .addGroup(1.5, toStack("gold"))
                        .addGroup(10, toStack("silicon_dioxide"))
                        .addGroup(1, toStack("dysprosium"))
                        .addGroup(1.5, toStack("zirconium"))
                        .addGroup(1, toStack("nickel"))
                        .addGroup(1, toStack("gallium"))
                        .addGroup(1, toStack("tungsten"))
                        .addGroup(3, toStack("cellulose"))
                        .build());

        newArrayList(Items.GRANITE, Items.POLISHED_GRANITE).forEach(item -> {
            dissolver(item,
                    set().addGroup(5, toStack("aluminum_oxide"))
                            .addGroup(2, toStack("iron"))
                            .addGroup(2, toStack("potassium_chloride"))
                            .addGroup(10, toStack("silicon_dioxide"))
                            .addGroup(1, toStack("technetium"))
                            .addGroup(1.5, toStack("manganese"))
                            .addGroup(1.5, toStack("radium"))
                            .build());
        });

        newArrayList(Items.DIORITE, Items.POLISHED_DIORITE).forEach(item -> {
            dissolver(item,
                    set().addGroup(4, toStack("aluminum_oxide"))
                            .addGroup(2, toStack("iron"))
                            .addGroup(4, toStack("potassium_chloride"))
                            .addGroup(10, toStack("silicon_dioxide"))
                            .addGroup(1.5, toStack("indium"))
                            .addGroup(2, toStack("manganese"))
                            .addGroup(2, toStack("osmium"))
                            .addGroup(3, toStack("tin"))
                            .build());
        });

        dissolver(Items.MAGMA_BLOCK,
                set().rolls(2)
                        .addGroup(10, toStack("manganese", 2))
                        .addGroup(5, toStack("aluminum_oxide"))
                        .addGroup(20, toStack("magnesium_oxide"))
                        .addGroup(2, toStack("potassium_chloride"))
                        .addGroup(10, toStack("silicon_dioxide", 2))
                        .addGroup(20, toStack("sulfur", 2))
                        .addGroup(10, toStack("iron_oxide"))
                        .addGroup(8, toStack("lead", 2))
                        .addGroup(4, toStack("fluorine"))
                        .addGroup(4, toStack("bromine"))
                        .build());

        newArrayList(Items.ANDESITE, Items.POLISHED_ANDESITE).forEach(item -> {
            dissolver(item,
                    set().addGroup(4, toStack("aluminum_oxide"))
                            .addGroup(3, toStack("iron"))
                            .addGroup(4, toStack("potassium_chloride"))
                            .addGroup(10, toStack("silicon_dioxide"))
                            .addGroup(2, toStack("platinum"))
                            .addGroup(4, toStack("calcium"))
                            .build());
        });

        for (Item item : newArrayList(Items.STONE, Items.SMOOTH_STONE, Items.STONE_BRICK_SLAB, Items.STONE_BRICKS, Items.CRACKED_STONE_BRICKS,
                Items.CHISELED_STONE_BRICKS)) {
            int rolls = 1;
            if (item == Items.STONE_BRICKS || item == Items.CRACKED_STONE_BRICKS || item == Items.CHISELED_STONE_BRICKS)
                rolls = 4;
            else if (item == Items.STONE_BRICK_SLAB) rolls = 2;
            dissolver(item,
                    set().rolls(rolls)
                            .addGroup(20, ItemStack.EMPTY)
                            .addGroup(2, toStack("aluminum"))
                            .addGroup(4, toStack("iron"))
                            .addGroup(1.5, toStack("gold"))
                            .addGroup(20, toStack("silicon_dioxide"))
                            .addGroup(.5, toStack("dysprosium"))
                            .addGroup(1.25, toStack("zirconium"))
                            .addGroup(1, toStack("tungsten"))
                            .addGroup(1, toStack("nickel"))
                            .addGroup(1, toStack("gallium"))
                            .build());
        }

        dissolver("forge:chests/wooden",
                set().addGroup(1.0, toStack("cellulose", 2)).build());

        dissolver(Items.CRAFTING_TABLE,
                set().addGroup(1.0, toStack("cellulose")).build());

        dissolver(Items.COBWEB,
                set().addGroup(1.0, toStack("protein", 2)).build());

        for (Item item : newArrayList(Items.TALL_GRASS, Items.SEAGRASS, Items.GRASS, Items.DEAD_BUSH)) {
            dissolver(item,
                    set().relative(false)
                            .addGroup(25, toStack("cellulose"))
                            .build());
        }

        dissolver(Items.FLINT,
                set().addGroup(1.0, toStack("silicon_dioxide", 3))
                        .build());

        dissolver(Items.COCOA_BEANS,
                set().relative(false)
                        .addGroup(100, toStack("caffeine"))
                        .addGroup(50, toStack("cellulose"))
                        .build());

        dissolver(Items.APPLE,
                set().addGroup(1.0,
                        toStack("sucrose"),
                        toStack("cellulose"))
                        .build());

        dissolver(Items.COAL_ORE,
                set().addGroup(1.0,
                        toStack("carbon", 32),
                        toStack("sulfur", 8))
                        .build());

        dissolver(Items.COAL_BLOCK,
                set().addGroup(1.0, toStack("carbon", 9 * 8)).build());

        dissolver(Items.COAL,
                set().addGroup(1.0, toStack("carbon", 8)).build());

        dissolver(Items.CHARCOAL,
                set().addGroup(1.0, toStack("carbon", 8)).build());

        dissolver("minecraft:wooden_slabs",
                set().relative(false)
                        .addGroup(12.0, toStack("cellulose")).build());

        dissolver("forge:slimeballs",
                set().addGroup(1,
                        toStack("protein", 2),
                        toStack("sucrose", 2))
                        .build());

        dissolver(Items.SLIME_BLOCK,
                set().addGroup(1,
                        toStack("protein", 2 * 9),
                        toStack("sucrose", 2 * 9))
                        .build());

        dissolver(Ref.condensedMilk,
                set().addGroup(1,
                        toStack("calcium", 4),
                        toStack("protein", 2),
                        toStack("sucrose"))
                        .build());

        dissolver(Items.STICK,
                set().relative(false).addGroup(10, toStack("cellulose")).build());

        dissolver(Items.ENDER_PEARL,
                set().addGroup(1,
                        toStack("silicon", 16),
                        toStack("mercury", 16),
                        toStack("neodymium", 16))
                        .build(), true);


        dissolver(Items.WHEAT_SEEDS,
                set().relative(false)
                        .addGroup(10, toStack("cellulose"))
                        .build());

        dissolver(Items.NETHERRACK,
                set().addGroup(15, ItemStack.EMPTY)
                        .addGroup(2, toStack("zinc_oxide"))
                        .addGroup(1, toStack("gold"))
                        .addGroup(1, toStack("phosphorus"))
                        .addGroup(3, toStack("sulfur"))
                        .addGroup(1, toStack("germanium"))
                        .addGroup(4, toStack("silicon"))
                        .build());

        dissolver(Items.CRIMSON_NYLIUM,
                set().addGroup(10, ItemStack.EMPTY)
                        .addGroup(2, toStack("cellulose"))
                        .addGroup(4, toStack("zinc_oxide"))
                        .addGroup(1, toStack("gold"))
                        .addGroup(1, toStack("phosphorus"))
                        .addGroup(3, toStack("sulfur"))
                        .addGroup(3, toStack("germanium"))
                        .addGroup(5, toStack("silicon"))
                        .addGroup(1, toStack("selenium"))
                        .build());

        dissolver(Items.WARPED_NYLIUM,
                set().addGroup(10, ItemStack.EMPTY)
                        .addGroup(2, toStack("cellulose"))
                        .addGroup(4, toStack("zinc_oxide"))
                        .addGroup(1, toStack("gold"))
                        .addGroup(1, toStack("phosphorus"))
                        .addGroup(3, toStack("sulfur"))
                        .addGroup(1, toStack("mercury"))
                        .addGroup(5, toStack("silicon"))
                        .addGroup(1, toStack("neodymium"))
                        .build());

        for (Item item : newArrayList(Items.NETHER_BRICK, Items.NETHER_BRICKS, Items.CRACKED_NETHER_BRICKS, Items.CHISELED_NETHER_BRICKS,
                Items.RED_NETHER_BRICKS)) {
            int rolls = 1;
            if (item != Items.NETHER_BRICK) rolls = 4;
            dissolver(item,
                    set().rolls(rolls)
                            .addGroup(5, ItemStack.EMPTY)
                            .addGroup(2, toStack("zinc_oxide"))
                            .addGroup(1, toStack("gold"))
                            .addGroup(1, toStack("phosphorus"))
                            .addGroup(4, toStack("sulfur"))
                            .addGroup(1, toStack("germanium"))
                            .addGroup(4, toStack("silicon"))
                            .build());
        }

        dissolver(Items.SPIDER_EYE,
                set().addGroup(1,
                        toStack("beta_carotene", 2),
                        toStack("protein", 2))
                        .build());

        dissolver(Items.IRON_HORSE_ARMOR,
                set().addGroup(1, toStack("iron", 64)).build());

        dissolver(Items.GOLDEN_HORSE_ARMOR,
                set().addGroup(1, toStack("gold", 64)).build());

        dissolver(Items.DIAMOND_HORSE_ARMOR,
                set().addGroup(1, toStack("carbon", 4 * (64 * 8))).build());


        dissolver("minecraft:wool",
                set().addGroup(1, toStack("protein"), toStack("triglyceride")).build());

        dissolver("minecraft:carpets",
                set().relative(false)
                        .addGroup((2.0 / 3.0) * 100, toStack("protein"), toStack("triglyceride")).build());

        dissolver(Items.ANVIL,
                set().addGroup(1, toStack("iron", (144 * 3) + (16 * 4))).build());

        dissolver(Items.IRON_DOOR,
                set().addGroup(1, toStack("iron", 32)).build());

        dissolver(Items.IRON_TRAPDOOR,
                set().addGroup(1, toStack("iron", 64)).build());

        for (Item item : newArrayList(Items.EMERALD, Items.EMERALD_ORE, Items.EMERALD_BLOCK)) {
            int multiplier = 1;
            boolean reversible = false;
            if (item == Items.EMERALD) reversible = true;
            else if (item == Items.EMERALD_ORE) multiplier = 2;
            else if (item == Items.EMERALD_BLOCK) multiplier = 9;
            dissolver(item,
                    set().addGroup(1,
                            toStack("beryl", 8 * multiplier),
                            toStack("chromium", 8 * multiplier),
                            toStack("vanadium", 4 * multiplier)
                    ).build(), reversible);
        }

        for (Item item : newArrayList(Items.DIAMOND, Items.DIAMOND_ORE, Items.DIAMOND_BLOCK)) {
            int multiplier = 1;
            if (item == Items.DIAMOND_ORE) multiplier = 2;
            else if (item == Items.DIAMOND_BLOCK) multiplier = 9;
            dissolver(item,
                    set().addGroup(1, toStack("carbon", 64 * 8 * multiplier)).build());
        }

        for (Item item : newArrayList(Items.END_STONE, Items.END_STONE_BRICKS)) {
            dissolver(item,
                    set().addGroup(50, toStack("mercury"))
                            .addGroup(5, toStack("neodymium"))
                            .addGroup(250, toStack("silicon_dioxide"))
                            .addGroup(50, toStack("lithium"))
                            .addGroup(2, toStack("thorium"))
                            .build());
        }

        for (Item item : newArrayList(Items.SNOW_BLOCK, Items.ICE)) {
            dissolver(item,
                    set().addGroup(1, toStack("water", 16)).build());
        }

        dissolver(Items.PACKED_ICE, set().addGroup(1, toStack("water", 16 * 9)).build());
        dissolver(Items.BLUE_ICE, set().addGroup(1, toStack("water", 16 * 9 * 4)).build());
        dissolver(Items.SNOWBALL, set().addGroup(1, toStack("water", 4)).build());
        dissolver(Items.SNOW, set().addGroup(1, toStack("water", 4)).build());


        for (Item item : newArrayList(Items.PRISMARINE_SHARD, Items.PRISMARINE, Items.PRISMARINE_BRICKS)) {
            int multiplier = 1;
            if (item == Items.PRISMARINE) multiplier = 4;
            else if (item == Items.PRISMARINE_BRICKS) multiplier = 9;
            dissolver(item, set().addGroup(1,
                    toStack("beryl", 4 * multiplier),
                    toStack("niobium", 3 * multiplier),
                    toStack("selenium", multiplier)).build());
        }
        dissolver(Items.PRISMARINE_CRYSTALS, set().addGroup(1,
                toStack("silicon_dioxide", 4),
                toStack("phosphorus", 3),
                toStack("selenium", 1)).build());

        dissolver(Items.DARK_PRISMARINE, set().addGroup(1,
                toStack("beryl", 32),
                toStack("niobium", 24),
                toStack("selenium", 8),
                toStack("titanium_dioxide", 4)).build());

        dissolver("minecraft:music_discs",
                set().addGroup(1,
                        toStack("polyvinyl_chloride", 64),
                        toStack("lead", 16),
                        toStack("cadmium", 16))
                        .build());

        dissolver(Items.JUKEBOX,
                set().addGroup(1,
                        toStack("carbon", 64 * 8),
                        toStack("cellulose", 2))
                        .build());

        for (Item item : newArrayList(Items.DIRT, Items.COARSE_DIRT, Items.PODZOL)) {

            dissolver(item,
                    set().addGroup(30, toStack("water"))
                            .addGroup(50, toStack("silicon_dioxide"))
                            .addGroup(10, toStack("cellulose"))
                            .addGroup(10, toStack("kaolinite"))
                            .build());
        }
        dissolver(Items.MYCELIUM,
                set().addGroup(30, toStack("water"))
                        .addGroup(50, toStack("silicon_dioxide"))
                        .addGroup(10, toStack("cellulose"))
                        .addGroup(10, toStack("kaolinite"))
                        .addGroup(30, toStack("psilocybin"))
                        .build());

        dissolver(Items.GRASS_BLOCK,
                set().addGroup(30, toStack("water"))
                        .addGroup(50, toStack("silicon_dioxide"))
                        .addGroup(10, toStack("cellulose"))
                        .addGroup(10, toStack("kaolinite"))
                        .build());

        dissolver("forge:glass",
                set().addGroup(1, toStack("silicon_dioxide", 4)).build());

        dissolver("minecraft:saplings",
                set().relative(false)
                        .addGroup(25, toStack("cellulose", 1)).build());

        dissolver(Items.VINE,
                set().relative(false)
                        .addGroup(25, toStack("cellulose", 1)).build());

        dissolver(Items.PAPER,
                set().addGroup(1, toStack("cellulose")).build());

        dissolver(Items.LILY_PAD,
                set().relative(false)
                        .addGroup(25, toStack("cellulose", 1)).build());

        for (Item item : newArrayList(Items.PUMPKIN, Items.CARVED_PUMPKIN)) {
            dissolver(item,
                    set().relative(false)
                            .addGroup(50, toStack("cucurbitacin", 1)).build());
        }
        dissolver(Items.QUARTZ,
                set().addGroup(1,
                        toStack("barium", 16),
                        toStack("silicon_dioxide", 32))
                        .build(), true);

        for (Item item : newArrayList(Items.NETHER_QUARTZ_ORE, Items.QUARTZ_SLAB, Items.SMOOTH_QUARTZ_SLAB)) {
            dissolver(item,
                    set().addGroup(1,
                            toStack("barium", 16 * 2),
                            toStack("silicon_dioxide", 32 * 2))
                            .build());
        }
        dissolver("forge:storage_blocks/quartz",
                set().addGroup(1,
                        toStack("barium", 16 * 4),
                        toStack("silicon_dioxide", 32 * 4))
                        .build());

        for (Item item : newArrayList(Items.CHISELED_QUARTZ_BLOCK, Items.SMOOTH_QUARTZ, Items.QUARTZ_PILLAR, Items.QUARTZ_BRICKS)) {
            dissolver(item,
                    set().addGroup(1,
                            toStack("barium", 16 * 4),
                            toStack("silicon_dioxide", 32 * 4))
                            .build());
        }

        dissolver(Items.BROWN_MUSHROOM,
                set().addGroup(1,
                        toStack("cellulose"),
                        toStack("psilocybin"))
                        .build(), true);

        dissolver(Items.RED_MUSHROOM,
                set().addGroup(1,
                        toStack("psilocybin"),
                        toStack("cellulose"))
                        .build(), true);

        dissolver(Items.CRIMSON_FUNGUS,
                set().addGroup(1,
                        toStack("psilocybin"),
                        toStack("cellulose"),
                        toStack("selenium"))
                        .build(), true);


        dissolver(Items.WARPED_FUNGUS,
                set().addGroup(1,
                        toStack("psilocybin"),
                        toStack("cellulose"),
                        toStack("mercury"))
                        .build(), true);

        dissolver(Items.CRIMSON_ROOTS,
                set().addGroup(1,
                        toStack("cellulose"),
                        toStack("mercury_sulfide"))
                        .build(), true);

        dissolver(Items.WARPED_ROOTS,
                set().addGroup(1,
                        toStack("cellulose"),
                        toStack("copper_chloride"))
                        .build(), true);

        dissolver(Items.NETHER_SPROUTS,
                set().addGroup(1,
                        toStack("cellulose"),
                        toStack("copper_chloride"))
                        .build(), true);

        dissolver(Items.TWISTING_VINES,
                set().addGroup(1,
                        toStack("cellulose"),
                        toStack("copper_chloride"))
                        .build(), true);

        dissolver(Items.WEEPING_VINES,
                set().addGroup(1,
                        toStack("cellulose"),
                        toStack("mercury_sulfide"))
                        .build(), true);


        for (Item item : newArrayList(Items.SOUL_SAND, Items.SOUL_SOIL)) {
            dissolver(item,
                    set().addGroup(1,
                            toStack("thulium"),
                            toStack("silicon_dioxide", 4))
                            .build(), true);
        }

        dissolver(Items.SUGAR_CANE,
                set().addGroup(1, toStack("sucrose")).build());

        dissolver(Items.SAND,
                set().relative(false)
                        .addGroup(100, toStack("silicon_dioxide", 4))
                        .addGroup(1, toStack("gold"))
                        .build());

        for (Item item : newArrayList(Items.SANDSTONE, Items.CHISELED_SANDSTONE, Items.CUT_SANDSTONE)) {
            dissolver(item,
                    set().rolls(4)
                            .relative(false)
                            .addGroup(100, toStack("silicon_dioxide", 4))
                            .addGroup(1, toStack("gold"))
                            .build());
        }

        dissolver(Items.RED_SAND,
                set().relative(false)
                        .addGroup(100, toStack("silicon_dioxide", 4))
                        .addGroup(10, toStack("iron_oxide"))
                        .build());

        for (Item item : newArrayList(Items.RED_SANDSTONE, Items.CUT_RED_SANDSTONE, Items.SMOOTH_RED_SANDSTONE)) {
            dissolver(item,
                    set().rolls(4)
                            .relative(false)
                            .addGroup(100, toStack("silicon_dioxide", 4))
                            .addGroup(10, toStack("iron_oxide"))
                            .build());
        }

        dissolver(Items.SUGAR,
                set().addGroup(1, toStack("sucrose")).build());

        dissolver(Items.GUNPOWDER,
                set().addGroup(1,
                        toStack("potassium_nitrate", 2),
                        toStack("sulfur", 8),
                        toStack("carbon", 8))
                        .build(), true);

        dissolver(Items.BLAZE_POWDER,
                set().addGroup(1,
                        toStack("germanium", 8),
                        toStack("carbon", 8),
                        toStack("sulfur", 8))
                        .build(), true);

        dissolver(Items.NETHER_WART,
                set().addGroup(1,
                        toStack("cellulose"),
                        toStack("germanium", 4),
                        toStack("selenium", 4))
                        .build(), true);

        dissolver(Items.NETHER_WART_BLOCK,
                set().addGroup(1,
                        toStack("cellulose", 9),
                        toStack("germanium", 4 * 9),
                        toStack("selenium", 4 * 9))
                        .build());

        dissolver(Items.WARPED_WART_BLOCK,
                set().addGroup(1,
                        toStack("cellulose", 9),
                        toStack("neodymium", 4),
                        toStack("mercury", 4))
                        .build());


        dissolver(Items.GLOWSTONE_DUST,
                set().addGroup(1, toStack("phosphorus", 4)).build(), true);

        dissolver(Items.GLOWSTONE,
                set().addGroup(1, toStack("phosphorus", 16)).build());

        dissolver(Items.IRON_BARS,
                set().addGroup(1, toStack("iron", 6)).build());

        for (Item item : newArrayList(Items.BASALT, Items.POLISHED_BASALT, Items.BLACKSTONE,
                Items.CHISELED_POLISHED_BLACKSTONE, Items.CRACKED_POLISHED_BLACKSTONE_BRICKS)) {
            dissolver(item,
                    set().addGroup(49, toStack("silicon_dioxide"))
                            .addGroup(5, toStack("sodium_oxide"))
                            .addGroup(2, toStack("potassium_oxide"))
                            .addGroup(2, toStack("titanium_oxide"))
                            .addGroup(7, toStack("iron_ii_oxide"))
                            .addGroup(15, toStack("aluminum_oxide"))
                            .addGroup(10, toStack("calcium_oxide"))
                            .addGroup(10, toStack("magnesium_oxide"))
                            .build());
        }

        dissolver(Items.POLISHED_BLACKSTONE,
                set().addGroup(29, toStack("silicon_dioxide"))
                        .addGroup(20, toStack("gold", 8))
                        .addGroup(5, toStack("sodium_oxide"))
                        .addGroup(2, toStack("potassium_oxide"))
                        .addGroup(2, toStack("titanium_oxide"))
                        .addGroup(7, toStack("iron_ii_oxide"))
                        .addGroup(15, toStack("aluminum_oxide"))
                        .addGroup(10, toStack("calcium_oxide"))
                        .addGroup(10, toStack("magnesium_oxide"))
                        .build());

        for (Item item : newArrayList(Items.LAPIS_LAZULI, Items.LAPIS_BLOCK, Items.LAPIS_ORE)) {
            int multiplier = 1;
            boolean reversible = false;
            if (item == Items.LAPIS_BLOCK) multiplier = 9;
            else if (item == Items.LAPIS_ORE) multiplier = 4;
            else reversible = true;
            dissolver(item,
                    set().addGroup(1,
                            toStack("sodium", 6 * multiplier),
                            toStack("calcium", 2 * multiplier),
                            toStack("aluminum", 6 * multiplier),
                            toStack("silicon", 6 * multiplier),
                            toStack("oxygen", 24 * multiplier),
                            toStack("sulfur", 2 * multiplier))
                            .build(), reversible);
        }

        dissolver(Items.STRING, set().relative(false).addGroup(50, toStack("protein")).build());

        for (Item item : newArrayList(Items.WHEAT, Items.HAY_BLOCK)) {
            int rolls = 1;
            if (item == Items.HAY_BLOCK) rolls = 9;
            dissolver(item,
                    set().relative(false)
                            .rolls(rolls)
                            .addGroup(5, toStack("starch"))
                            .addGroup(25, toStack("cellulose"))
                            .build());
        }

        dissolver(Items.MELON,
                set().relative(false)
                        .addGroup(50, toStack("cucurbitacin"))
                        .addGroup(1, toStack("water", 4), toStack("sucrose", 2))
                        .build());

        dissolver(Items.CACTUS,
                set().addGroup(1, toStack("cellulose"), toStack("mescaline"))
                        .build(), true);

        //Forge & Minecraft, why no terracotta tag?
        for (Item item : newArrayList(Items.TERRACOTTA, Items.BLACK_GLAZED_TERRACOTTA, Items.BLACK_TERRACOTTA,
                Items.BLUE_GLAZED_TERRACOTTA, Items.BLUE_TERRACOTTA, Items.BROWN_GLAZED_TERRACOTTA, Items.BROWN_TERRACOTTA,
                Items.CYAN_GLAZED_TERRACOTTA, Items.CYAN_TERRACOTTA, Items.GRAY_GLAZED_TERRACOTTA, Items.GRAY_TERRACOTTA,
                Items.GREEN_GLAZED_TERRACOTTA, Items.GREEN_TERRACOTTA, Items.LIGHT_BLUE_GLAZED_TERRACOTTA, Items.LIGHT_BLUE_TERRACOTTA,
                Items.LIGHT_GRAY_GLAZED_TERRACOTTA, Items.LIGHT_GRAY_TERRACOTTA, Items.LIME_GLAZED_TERRACOTTA, Items.LIME_TERRACOTTA,
                Items.MAGENTA_GLAZED_TERRACOTTA, Items.MAGENTA_TERRACOTTA, Items.ORANGE_GLAZED_TERRACOTTA, Items.ORANGE_TERRACOTTA,
                Items.PINK_TERRACOTTA, Items.PINK_GLAZED_TERRACOTTA, Items.YELLOW_TERRACOTTA, Items.YELLOW_GLAZED_TERRACOTTA,
                Items.PURPLE_TERRACOTTA, Items.PURPLE_GLAZED_TERRACOTTA, Items.RED_TERRACOTTA, Items.RED_GLAZED_TERRACOTTA)) {
            dissolver(item, set().addGroup(1, toStack("kaolinite", 4)).build());
        }

        //Forge & Minecraft, why no concrete tag?
        for (Item item : newArrayList(Items.BLACK_CONCRETE_POWDER, Items.BLUE_CONCRETE_POWDER, Items.CYAN_CONCRETE_POWDER,
                Items.BROWN_CONCRETE_POWDER, Items.GRAY_CONCRETE_POWDER, Items.GREEN_CONCRETE_POWDER, Items.LIGHT_BLUE_CONCRETE_POWDER,
                Items.LIGHT_GRAY_CONCRETE_POWDER, Items.LIME_CONCRETE_POWDER, Items.MAGENTA_CONCRETE_POWDER,
                Items.ORANGE_CONCRETE_POWDER, Items.PINK_CONCRETE_POWDER, Items.PURPLE_CONCRETE_POWDER,
                Items.RED_CONCRETE_POWDER, Items.WHITE_CONCRETE_POWDER, Items.YELLOW_CONCRETE_POWDER)) {
            dissolver(item, set().addGroup(1, toStack("silicon_dioxide", 2)).build());
        }

        for (Item item : newArrayList(Items.BLACK_CONCRETE, Items.BLUE_CONCRETE, Items.CYAN_CONCRETE,
                Items.BROWN_CONCRETE, Items.GRAY_CONCRETE, Items.GREEN_CONCRETE, Items.LIGHT_BLUE_CONCRETE,
                Items.LIGHT_GRAY_CONCRETE, Items.LIME_CONCRETE, Items.MAGENTA_CONCRETE,
                Items.ORANGE_CONCRETE, Items.PINK_CONCRETE, Items.PURPLE_CONCRETE,
                Items.RED_CONCRETE, Items.WHITE_CONCRETE, Items.YELLOW_CONCRETE)) {
            dissolver(item, set().addGroup(1, toStack("silicon_dioxide", 2), toStack("water", 4)).build());
        }

        dissolver(Items.GRAVEL,
                set().addGroup(1, toStack("silicon_dioxide")).build());


        for (Item item : newArrayList(Items.DEAD_BRAIN_CORAL, Items.DEAD_BRAIN_CORAL_BLOCK, Items.DEAD_BRAIN_CORAL_FAN,
                Items.DEAD_BUBBLE_CORAL, Items.DEAD_BUBBLE_CORAL_BLOCK, Items.DEAD_BUBBLE_CORAL_FAN,
                Items.DEAD_FIRE_CORAL, Items.DEAD_FIRE_CORAL_BLOCK, Items.DEAD_FIRE_CORAL_FAN,
                Items.DEAD_HORN_CORAL, Items.DEAD_HORN_CORAL_BLOCK, Items.DEAD_HORN_CORAL_FAN,
                Items.DEAD_TUBE_CORAL, Items.DEAD_TUBE_CORAL_BLOCK, Items.DEAD_TUBE_CORAL_FAN)) {
            int quantity = 4;
            if (item == Items.DEAD_BRAIN_CORAL_BLOCK || item == Items.DEAD_BUBBLE_CORAL_BLOCK || item == Items.DEAD_FIRE_CORAL_BLOCK
                    || item == Items.DEAD_HORN_CORAL_BLOCK || item == Items.DEAD_TUBE_CORAL_BLOCK) {
                quantity = 16;
            }
            dissolver(item, set().addGroup(1, toStack("calcium_carbonate", quantity)).build());
        }

        for (Item item : newArrayList(Items.BRAIN_CORAL, Items.BRAIN_CORAL_BLOCK, Items.BRAIN_CORAL_FAN)) {
            int multiplier = 4;
            if (item == Items.BRAIN_CORAL_BLOCK) multiplier = 16;
            dissolver(item, set().addGroup(1, toStack("calcium_carbonate", multiplier),
                    toStack("arsenic_sulfide", (multiplier / 2))).build());
        }
        for (Item item : newArrayList(Items.BUBBLE_CORAL, Items.BUBBLE_CORAL_BLOCK, Items.BUBBLE_CORAL_FAN)) {
            int multiplier = 4;
            if (item == Items.BUBBLE_CORAL_BLOCK) multiplier = 16;
            dissolver(item, set().addGroup(1, toStack("calcium_carbonate", multiplier),
                    toStack("han_purple", (multiplier / 2))).build());
        }

        for (Item item : newArrayList(Items.FIRE_CORAL, Items.FIRE_CORAL_BLOCK, Items.FIRE_CORAL_FAN)) {
            int multiplier = 4;
            if (item == Items.FIRE_CORAL_BLOCK) multiplier = 16;
            dissolver(item, set().addGroup(1, toStack("calcium_carbonate", multiplier),
                    toStack("cobalt_nitrate", (multiplier / 2))).build());
        }
        for (Item item : newArrayList(Items.HORN_CORAL, Items.HORN_CORAL_BLOCK, Items.HORN_CORAL_FAN)) {
            int multiplier = 4;
            if (item == Items.HORN_CORAL_BLOCK) multiplier = 16;
            dissolver(item, set().addGroup(1, toStack("calcium_carbonate", multiplier),
                    toStack("lead_iodide", (multiplier / 2))).build());
        }
        for (Item item : newArrayList(Items.TUBE_CORAL, Items.TUBE_CORAL_BLOCK, Items.TUBE_CORAL_FAN)) {
            int multiplier = 4;
            if (item == Items.TUBE_CORAL_BLOCK) multiplier = 16;
            dissolver(item, set().addGroup(1, toStack("calcium_carbonate", multiplier),
                    toStack("cobalt_aluminate", (multiplier / 2))).build());
        }

        dissolver(Items.KELP, set().relative(false)
                .addGroup(100, toStack("cellulose"))
                .addGroup(17, toStack("sodium_carbonate", 2))
                .addGroup(5, toStack("potassium_iodide", 2)).build());

        dissolver(Items.DRIED_KELP, set()
                .addGroup(1, toStack("sodium_carbonate"),
                        toStack("potassium_iodide")).build());

        dissolver(Items.DRIED_KELP_BLOCK, set()
                .addGroup(1, toStack("sodium_carbonate", 9),
                        toStack("potassium_iodide", 9)).build());

        dissolver(Items.SEA_PICKLE, set()
                .addGroup(1, toStack("cellulose"),
                        toStack("cadmium_sulfide", 2),
                        toStack("chromium_oxide", 2)).build());

        for (Item item : newArrayList(Items.POTATO, Items.BAKED_POTATO)) {
            dissolver(item,
                    set().relative(false)
                            .addGroup(10, toStack("starch"))
                            .addGroup(25, toStack("potassium", 5))
                            .build());
        }

        dissolver(Items.REDSTONE,
                set().addGroup(1,
                        toStack("iron_oxide"),
                        toStack("strontium_carbonate"))
                        .build(), true);

        dissolver(Items.REDSTONE_ORE,
                set().addGroup(1,
                        toStack("iron_oxide", 4),
                        toStack("strontium_carbonate", 4))
                        .build());

        dissolver(Items.REDSTONE_BLOCK,
                set().addGroup(1,
                        toStack("iron_oxide", 9),
                        toStack("strontium_carbonate", 9))
                        .build());

        for (Item item : newArrayList(Items.BEEF, Items.PORKCHOP, Items.MUTTON, Items.COOKED_PORKCHOP,
                Items.COOKED_BEEF, Items.COOKED_MUTTON, Items.CHICKEN, Items.COOKED_CHICKEN, Items.RABBIT, Items.COOKED_RABBIT)) {
            dissolver(item,
                    set().addGroup(1, toStack("protein", 4)).build());
        }

        for (Item item : newArrayList(Items.COD, Items.COOKED_COD, Items.TROPICAL_FISH)) {
            dissolver(item,
                    set().addGroup(1,
                            toStack("protein", 4),
                            toStack("selenium", 2))
                            .build());
        }

        for (Item item : newArrayList(Items.SALMON, Items.COOKED_SALMON)) {
            dissolver(item,
                    set().addGroup(1,
                            toStack("protein", 4),
                            toStack("selenium", 4))
                            .build());
        }

        dissolver(Items.PUFFERFISH,
                set().addGroup(1,
                        toStack("protein", 4),
                        toStack("potassium_cyanide", 4))
                        .build());

        dissolver(Items.SPONGE,
                set().addGroup(1,
                        toStack("kaolinite", 8),
                        toStack("calcium_carbonate", 8))
                        .build(), true);

        dissolver(Items.LEATHER,
                set().addGroup(1, toStack("protein", 3)).build());

        dissolver(Items.ROTTEN_FLESH,
                set().addGroup(1, toStack("protein", 3)).build());

        dissolver(Items.FEATHER,
                set().addGroup(1, toStack("protein", 2)).build());

        dissolver(Items.BONE_MEAL,
                set().relative(false).addGroup(50, toStack("hydroxylapatite")).build());

        dissolver(Items.BONE_BLOCK,
                set().rolls(9).relative(false).addGroup(50, toStack("hydroxylapatite")).build());

        dissolver(Items.EGG,
                set().addGroup(1,
                        toStack("calcium_carbonate", 8),
                        toStack("protein", 2))
                        .build(), true);

        dissolver(Ref.mineralSalt,
                set()
                        .addGroup(60, toStack("sodium_chloride"))
                        .addGroup(5, toStack("lithium"))
                        .addGroup(10, toStack("potassium_chloride"))
                        .addGroup(10, toStack("magnesium"))
                        .addGroup(5, toStack("iron"))
                        .addGroup(4, toStack("copper"))
                        .addGroup(2, toStack("zinc"))
                        .build());


        dissolver(Items.CARROT,
                set().relative(false)
                        .addGroup(20, toStack("beta_carotene")).build());

        dissolver("forge:dyes/red",
                set().addGroup(1, toStack("mercury_sulfide", 4)).build());

        dissolver("forge:dyes/pink",
                set().addGroup(1, toStack("arsenic_sulfide", 4)).build());

        dissolver("forge:dyes/green",
                set().addGroup(1, toStack("nickel_chloride", 4)).build());


        dissolver("forge:dyes/lime",
                set().addGroup(1,
                        toStack("cadmium_sulfide", 2),
                        toStack("chromium_oxide", 2))
                        .build());

        dissolver("forge:dyes/purple",
                set().addGroup(1, toStack("potassium_permanganate", 4)).build());

        dissolver("forge:dyes/yellow",
                set().addGroup(1, toStack("lead_iodide", 4)).build());

        dissolver("forge:dyes/orange",
                set().addGroup(1, toStack("potassium_dichromate", 4)).build());

        dissolver("forge:dyes/black",
                set().addGroup(1, toStack("titanium_oxide", 4)).build());

        dissolver("forge:dyes/gray",
                set().addGroup(1, toStack("barium_sulfate", 4)).build());

        dissolver("forge:dyes/magenta",
                set().addGroup(1, toStack("han_purple", 4)).build());

        dissolver("forge:dyes/light_blue",
                set().addGroup(1,
                        toStack("cobalt_aluminate", 2),
                        toStack("antimony_trioxide", 2))
                        .build());

        dissolver("forge:dyes/blue",
                set().addGroup(1,
                        toStack("cobalt_aluminate", 4)).build());

        dissolver("forge:dyes/light_gray",
                set().addGroup(1, toStack("magnesium_sulfate", 4)).build());

        dissolver("forge:dyes/cyan",
                set().addGroup(1, toStack("copper_chloride", 4)).build());

        dissolver(Items.WITHER_SKELETON_SKULL,
                set().addGroup(1,
                        toStack("hydroxylapatite", 8),
                        toStack("mendelevium", 32))
                        .build());

        dissolver(Items.SKELETON_SKULL,
                set().addGroup(1, toStack("hydroxylapatite", 8)).build());

        dissolver(Items.BELL,
                set().addGroup(1, toStack("gold", 64)).build());

        dissolver(Items.SHROOMLIGHT,
                set().addGroup(1, toStack("phosphorus", 16),
                        toStack("psilocybin"), toStack("cellulose")).build());

        dissolver(Items.HONEYCOMB,
                set().addGroup(1, toStack("sucrose", 3)).build());
        dissolver(Items.HONEY_BOTTLE,
                set().addGroup(1, toStack("sucrose", 3)).build());
        dissolver(Items.HONEY_BLOCK,
                set().addGroup(1, toStack("sucrose", 12)).build());
        dissolver(Items.HONEYCOMB_BLOCK,
                set().addGroup(1, toStack("sucrose", 12)).build());

        dissolver(Items.TURTLE_EGG,
                set().addGroup(1, toStack("protein", 4), toStack("calcium_carbonate", 8))
                        .build());

        dissolver(Items.SCUTE,
                set().addGroup(1, toStack("protein", 2)).build());

        dissolver(Items.NETHERITE_SCRAP,
                set().addGroup(1, toStack("tungsten", 16)).build());
        dissolver(Items.ANCIENT_DEBRIS,
                set().addGroup(1, toStack("tungsten", 16)).build());

        dissolver(Items.GOLDEN_APPLE,
                set().addGroup(1, toStack("gold", 8 * 16),
                        toStack("cellulose"),
                        toStack("sucrose")).build());

        dissolver(Items.CHORUS_FLOWER,
                set().relative(false)
                        .addGroup(100, toStack("cellulose"))
                        .addGroup(50, toStack("lutetium"))
                        .build());

        dissolver(Items.CHORUS_FRUIT,
                set().relative(false)
                        .addGroup(100, toStack("cellulose"))
                        .addGroup(50, toStack("lutetium"))
                        .build());

        dissolver(Items.CHORUS_PLANT,
                set().relative(false)
                        .addGroup(100, toStack("cellulose"))
                        .addGroup(50, toStack("lutetium"))
                        .build());

        dissolver(Items.POPPED_CHORUS_FRUIT,
                set().relative(false)
                        .addGroup(100, toStack("cellulose"))
                        .addGroup(50, toStack("lutetium"))
                        .build());


        newArrayList(Items.PURPUR_BLOCK, Items.PURPUR_PILLAR).forEach(item -> {
            dissolver(item,
                    set().relative(false)
                            .addGroup(100, toStack("silicon_dioxide", 4))
                            .addGroup(50, toStack("lutetium"))
                            .build());
        });

        for (Item item : newArrayList(Items.CLAY_BALL, Items.BRICK)) {
            dissolver(item,
                    set().addGroup(1, toStack("kaolinite", 1)).build());
        }
        for (Item item : newArrayList(Items.CLAY, Items.BRICKS)) {
            dissolver(item,
                    set().addGroup(1, toStack("kaolinite", 4)).build());
        }
        dissolver(Items.BRICK_SLAB, set().addGroup(1, toStack("kaolinite", 2)).build());

        dissolver(Items.BEETROOT,
                set().relative(false)
                        .addGroup(100, toStack("sucrose"))
                        .addGroup(50, toStack("iron_oxide"))
                        .build());

        dissolver(Items.BONE,
                set().relative(false)
                        .addGroup(50, toStack("hydroxylapatite", 3))
                        .build(), true);

        dissolver(Items.OBSIDIAN,
                set().addGroup(1,
                        toStack("magnesium_oxide", 8),
                        toStack("potassium_chloride", 8),
                        toStack("aluminum_oxide", 8),
                        toStack("silicon_dioxide", 24))
                        .build(), true);

        dissolver(Items.BAMBOO, set().addGroup(1, toStack("cellulose")).build());

        dissolver(Items.RABBIT_FOOT, set().addGroup(1, toStack("protein", 2)).build());

        dissolver(Items.RABBIT_HIDE, set().addGroup(1, toStack("protein", 2)).build());

        dissolver(Items.SHULKER_SHELL, set()
                .addGroup(1, toStack("calcium_carbonate", 8), toStack("lutetium", 8)).build());

        dissolver(Items.DRAGON_BREATH, set().addGroup(1, toStack("xenon", 8),
                toStack("xenon", 8),
                toStack("radon", 8),
                toStack("oganesson", 8)).build());

        dissolver(Items.GHAST_TEAR, set().addGroup(1, toStack("polonium", 16)).build());

        dissolver(Items.NAUTILUS_SHELL, set().addGroup(1, toStack("calcium_carbonate", 16)).build());

        dissolver(Items.PHANTOM_MEMBRANE, set().addGroup(1, toStack("cerium", 8)).build());

        dissolver(Items.SWEET_BERRIES, set().addGroup(1, toStack("cellulose"), toStack("sucrose")).build());

        for (CompoundItem compound : CompoundRegistry.compounds) {
            List<ItemStack> stacks = new ArrayList<>();
            compound.getComponentStacks().forEach(x -> stacks.add(x));
            ProbabilityGroup group = new ProbabilityGroup(stacks, 1.0);
            dissolver(compound,
                    set().addGroup(group)//(new ItemStack[0])).build())
                            .build());
        }


        for (ElementItem element : ElementRegistry.elements.values()) {
            Item ingot = ForgeRegistries.ITEMS.getValue(
                    new ResourceLocation("chemlib", "ingot_" + element.getChemicalName()));
            if (ingot != Items.AIR) {
                dissolver("forge:ingots/" + element.getChemicalName(), set().addGroup(1, new ItemStack(element, 16)).build());

            }
        }
        dissolver(Items.IRON_INGOT, set().addGroup(1, toStack("iron", 16)).build());
        dissolver(Items.IRON_NUGGET, set().addGroup(1, toStack("iron", 1)).build());
        dissolver(Items.IRON_BLOCK, set().addGroup(1, toStack("iron", 16 * 9)).build());
        dissolver(Items.IRON_ORE, set().addGroup(1, toStack("iron", 32)).build());
        dissolver(Items.CHAIN, set().addGroup(1, toStack("iron", 18)).build());

        dissolver(Items.GOLD_INGOT, set().addGroup(1, toStack("gold", 16)).build());
        dissolver(Items.GOLD_NUGGET, set().addGroup(1, toStack("gold", 1)).build());
        dissolver(Items.GOLD_BLOCK, set().addGroup(1, toStack("gold", 16 * 9)).build());
        dissolver(Items.GOLD_ORE, set().addGroup(1, toStack("gold", 32)).build());
        dissolver(Items.NETHER_GOLD_ORE, set().addGroup(1, toStack("gold", 32)).build());

        dissolver(Items.INK_SAC, set().addGroup(1, toStack("titanium_oxide", 4)).build());


    }

    private void initMetals() {
        //metals.add(heathenSpelling);
        Set<Integer> nonMetals = Sets.newHashSet(1, 2, 6, 7, 8, 9, 10, 15, 16, 17, 18, 35, 36, 53, 54, 80, 86);
        metals.addAll(ElementRegistry.elements.values().stream()
                .filter(it -> !nonMetals.contains(it.atomicNumber))
                .map(it -> it.internalName).collect(Collectors.toList()));

        metalTagData.add(new DissolverTagData("ingots", 16, metals));
        metalTagData.add(new DissolverTagData("ores", 32, metals));
        metalTagData.add(new DissolverTagData("dusts", 16, metals));
        metalTagData.add(new DissolverTagData("storage_blocks", 144, metals));
        metalTagData.add(new DissolverTagData("nuggets", 1, metals));
        metalTagData.add(new DissolverTagData("plates", 16, metals));
    }

    private void registerCombinerRecipes() {
        combiner(Items.EMERALD, newArrayList(
                toStack("beryl", 8),
                toStack("chromium", 8),
                toStack("vanadium", 4)));
        combiner(Items.LAPIS_LAZULI, newArrayList(
                toStack("beryl", 8),
                toStack("chromium", 8),
                toStack("vanadium", 4)));
        combiner(Items.BAMBOO, newArrayList(
                null, null, null,
                null, null, null,
                null, null, toStack("cellulose")));

        combiner(Items.SLIME_BALL, newArrayList(toStack("protein", 2), toStack("sucrose", 2)));
        combiner(Items.NETHER_WART, newArrayList(
                toStack("cellulose"),
                toStack("germanium", 4),
                toStack("selenium", 4)));
        combiner(Items.RED_DYE, newArrayList(toStack("mercury_sulfide", 4)));
        combiner(Items.PINK_DYE, newArrayList(toStack("arsenic_sulfide", 4)));
        combiner(Items.GREEN_DYE, newArrayList(toStack("nickel_chloride", 4)));
        combiner(Items.LIME_DYE, newArrayList(toStack("cadmium_sulfide", 2), toStack("chromium_oxide", 2)));
        combiner(Items.PURPLE_DYE, newArrayList(toStack("potassium_permanganate", 4)));
        combiner(Items.YELLOW_DYE, newArrayList(toStack("lead_iodide", 4)));
        combiner(Items.ORANGE_DYE, newArrayList(toStack("potassium_dichromate", 4)));
        combiner(Items.BLACK_DYE, newArrayList(toStack("titanium_oxide", 4)));
        combiner(Items.GRAY_DYE, newArrayList(toStack("barium_sulfate", 4)));
        combiner(Items.MAGENTA_DYE, newArrayList(toStack("han_purple", 4)));
        combiner(Items.LIGHT_BLUE_DYE, newArrayList(toStack("cobalt_aluminate", 2), toStack("antimony_trioxide", 2)));
        combiner(Items.LIGHT_GRAY_DYE, newArrayList(toStack("magnesium_sulfate", 4)));
        combiner(Items.CYAN_DYE, newArrayList(toStack("copper_chloride", 4)));

        combiner(Items.CHARCOAL, newArrayList(null, null, toStack("carbon", 8)));
        combiner(Items.COAL, newArrayList(null, toStack("carbon", 8)));

        combiner(Items.GLOWSTONE, newArrayList(null, toStack("phosphorus", 16)));
        combiner(Items.GLOWSTONE_DUST, newArrayList(toStack("phosphorus", 4)));

        combiner(Items.DIAMOND,
                newArrayList(
                        toStack("carbon", 64), toStack("carbon", 64), toStack("carbon", 64),
                        toStack("carbon", 64), null, toStack("carbon", 64),
                        toStack("carbon", 64), toStack("carbon", 64), toStack("carbon", 64)));

        combiner(Items.SAND, newArrayList(null, null, null, null, null, null, null, null, toStack("silicon_dioxide", 4)));
        combiner(Items.COBBLESTONE, 2, newArrayList(toStack("silicon_dioxide")));
        combiner(Items.STONE, newArrayList(null, toStack("silicon_dioxide")));
        combiner(Items.CLAY, newArrayList(null, toStack("kaolinite", 4)));
        combiner(Items.DIRT, 4, newArrayList(toStack("water"), toStack("cellulose"), toStack("kaolinite")));
        combiner(Items.MYCELIUM, 4, newArrayList(null, null, null, null, null, toStack("psilocybin"),
                toStack("water"), toStack("cellulose"), toStack("kaolinite")));
        combiner(Items.FEATHER, newArrayList(null, null, null, null, null, toStack("protein", 2)));
        combiner(Items.SPIDER_EYE, newArrayList(null, toStack("beta_carotene", 2), toStack("protein", 2)));
        combiner(Items.GRASS_BLOCK, 4, newArrayList(null, null, null,
                toStack("water"), toStack("cellulose"), toStack("kaolinite")));
        combiner(Items.GRAVEL, newArrayList(null, null, toStack("silicon_dioxide")));
        combiner(Items.WATER_BUCKET, newArrayList(null, null, null,
                null, toStack("water", 16), null,
                null, toStack(Items.BUCKET), null));
        combiner(Items.MILK_BUCKET, newArrayList(null, toStack("calcium", 4), null,
                toStack("protein", 2), toStack("water", 16), toStack("sucrose"),
                null, toStack(Items.BUCKET), null));
        combiner(Items.REDSTONE_BLOCK, newArrayList(null, null, null,
                toStack("iron_oxide", 9), toStack("strontium_carbonate", 9)));
        combiner(Items.REDSTONE, newArrayList(toStack("iron_oxide"), toStack("strontium_carbonate")));
        combiner(Items.STRING, 4, newArrayList(null, toStack("protein", 2)));
        combiner(Items.WHITE_WOOL, newArrayList(null, null, null,
                null, null, null,
                toStack("protein"), toStack("triglyceride")));
        combiner(Items.CARROT, newArrayList(null, null, null,
                toStack("cellulose"), toStack("beta_carotene")));
        combiner(Items.SUGAR_CANE, newArrayList(null, null, null,
                toStack("cellulose"), toStack("sucrose")));
        combiner(Items.SUGAR, newArrayList(toStack("sucrose")));
        combiner(Items.EGG, newArrayList(toStack("calcium_carbonate", 8), toStack("protein", 2)));
        combiner(Items.GRANITE, newArrayList(null, null, null, toStack("silicon_dioxide")));
        combiner(Items.DIORITE, newArrayList(null, null, null, null, toStack("silicon_dioxide")));
        combiner(Items.ANDESITE, newArrayList(null, null, null, null, null, toStack("silicon_dioxide")));
        combiner(Items.FLINT, newArrayList(null, null, null,
                null, null, null,
                null, toStack("silicon_dioxide", 3)));
        combiner(Items.POTATO, newArrayList(toStack("starch"), toStack("potassium", 4)));
        combiner(Items.APPLE, newArrayList(null, toStack("cellulose"), null,
                null, toStack("sucrose")));
        combiner(Items.WHEAT_SEEDS, newArrayList(null, toStack("triglyceride"), null,
                toStack("sucrose")));
        combiner(Items.PUMPKIN_SEEDS, newArrayList(null, toStack("triglyceride"), null,
                null, toStack("sucrose")));
        combiner(Items.MELON_SEEDS, newArrayList(null, toStack("triglyceride"), null,
                null, null, toStack("sucrose")));
        combiner(Items.BEETROOT_SEEDS, newArrayList(null, toStack("triglyceride"), null,
                null, null, null,
                toStack("sucrose"), toStack("iron_oxide")));
        combiner(Items.BEETROOT, newArrayList(null, toStack("sucrose"), toStack("iron_oxide")));

        combiner(Items.OAK_SAPLING, newArrayList(toStack("oxygen"), toStack("cellulose", 2)));
        combiner(Items.SPRUCE_SAPLING, newArrayList(null, toStack("oxygen"), toStack("cellulose", 2)));
        combiner(Items.JUNGLE_SAPLING, newArrayList(null, null, toStack("oxygen"), toStack("cellulose", 2)));
        combiner(Items.ACACIA_SAPLING, newArrayList(null, null, null, toStack("oxygen"), toStack("cellulose", 2)));
        combiner(Items.DARK_OAK_SAPLING, newArrayList(null, null, null, null, toStack("oxygen"), toStack("cellulose", 2)));
        combiner(Items.BIRCH_SAPLING, newArrayList(null, null, null, null, null, toStack("oxygen"), toStack("cellulose", 2)));

        combiner(Items.OAK_LOG, newArrayList(toStack("cellulose")));
        combiner(Items.SPRUCE_LOG, newArrayList(null, toStack("cellulose")));
        combiner(Items.JUNGLE_LOG, newArrayList(null, null, toStack("cellulose")));
        combiner(Items.ACACIA_LOG, newArrayList(null, null, null, toStack("cellulose")));
        combiner(Items.DARK_OAK_LOG, newArrayList(null, null, null, null, toStack("cellulose")));
        combiner(Items.BIRCH_LOG, newArrayList(null, null, null, null, null, toStack("cellulose")));

        combiner(Items.SNOWBALL, newArrayList(null, null, null,
                null, null, null,
                toStack("water", 4)));

        combiner(Items.SNOW, newArrayList(null, null, null,
                null, null, null,
                null, toStack("water", 16)));

        combiner(Items.ICE, newArrayList(null, null, null,
                null, null, null,
                null, null, toStack("water", 16)));

        combiner(Items.BONE_MEAL, 3, newArrayList(null, null, toStack("hydroxylapatite", 2)));
        combiner(Items.LEATHER, newArrayList(null, null, null,
                null, toStack("protein", 3)));
        combiner(Items.ROTTEN_FLESH, newArrayList(null, null, null,
                null, null, null,
                null, toStack("protein", 3)));
        combiner(Items.NETHER_STAR, newArrayList(toStack("lutetium", 64), toStack("hydrogen", 64), toStack("titanium", 64),
                toStack("hydrogen", 64), toStack("hydrogen", 64), toStack("hydrogen", 64),
                toStack("dysprosium", 64), toStack("hydrogen", 64), toStack("mendelevium", 64)));
        combiner(Items.CACTUS, newArrayList(toStack("cellulose"), toStack("mescaline")));

        /*
        for (DissolverRecipe recipe : DissolverRegistry.getRecipes(world)) {
            if (recipe.reversible) {
                List<ItemStack> outputs = recipe.outputs.toStackList().stream().map(ItemStack::copy).collect(Collectors.toList());
                combiner(recipe.getIngredients().get(0).getMatchingStacks()[0].getItem(), outputs);//.add(new CombinerRecipe(recipe.getInput(), outputs));
            }
        }*/

        Map<CompoundItem, List<ItemStack>> overrides = new HashMap<>();
        overrides.put(CompoundRegistry.getByName("triglyceride").get(),
                newArrayList(null, null, toStack("oxygen", 2),
                        null, toStack("hydrogen", 32), null,
                        toStack("carbon", 18)));
        overrides.put(CompoundRegistry.getByName("cucurbitacin").get(),
                newArrayList(null, null, null,
                        null, toStack("hydrogen", 44), null,
                        toStack("carbon", 32), null, toStack("oxygen", 8)));
        overrides.put(CompoundRegistry.getByName("acetic_acid").get(),
                newArrayList(toStack("carbon", 2), null, toStack("hydrogen", 4),
                        null, null, toStack("oxygen", 2)));
        overrides.put(CompoundRegistry.getByName("carbon_monoxide").get(),
                newArrayList(null, toStack("oxygen"), null,
                        null, null, null,
                        toStack("carbon")));
        overrides.put(CompoundRegistry.getByName("carbon_dioxide").get(),
                newArrayList(null, toStack("oxygen", 2), null,
                        null, null, null,
                        null, toStack("carbon")));
        overrides.put(CompoundRegistry.getByName("carbonate").get(),
                newArrayList(null, toStack("oxygen", 3), null,
                        null, null, null,
                        null, null, toStack("carbon")));
        for (CompoundItem compound : CompoundRegistry.compounds) {
            List<ItemStack> inputs = new ArrayList<>();

            if (overrides.containsKey(compound)) {
                inputs = overrides.get(compound).stream().map(x -> {
                    if (x == null) return ItemStack.EMPTY;
                    else return x;
                }).collect(Collectors.toList());
            } else {
                for (int i = 0; i < compound.shiftedSlots; i++) {
                    inputs.add(ItemStack.EMPTY);
                }
                for (ItemStack component : compound.getComponentStacks()) {
                    inputs.add(component.copy());
                }
            }
            combiner(compound, inputs);
            //combinerRecipes.add(new CombinerRecipe(Ingredient.fromStacks(new ItemStack(compound)), inputs));
        }

        for (ElementItem element : ElementRegistry.elements.values()) {
            Item ingot = ForgeRegistries.ITEMS.getValue
                    (new ResourceLocation("chemlib", "ingot_" + element.getChemicalName()));
            if (ingot != Items.AIR) {
                combiner(ingot, newArrayList(toStack(element.getItem(), 16)));
            }
        }
        combiner(Items.IRON_INGOT, newArrayList(toStack("iron", 16)));
        combiner(Items.GOLD_INGOT, newArrayList(toStack("gold", 16)));


    /*
        for (String metal : metals) {
            String tag = "forge:ingots/" + metal;
            //if (!tag.getAllElements().isEmpty()) {

            Ingredient ing = TagUtils.tagIngredient(tag);// Ingredient.fromTag(ItemTags.createOptional(new ResourceLocation(tag)));
            //Arrays.stream(ing.getMatchingStacks()).forEach(System.out::println);
            combiner(toStack("chemlib:ingot_" + metal).getItem(), newArrayList(toStack(metal, 16)));
            //combiner(ing.getMatchingStacks()[0].getItem(), newArrayList(toStack(metal, 16)));
            //combinerRecipes.add(new CombinerRecipe(ing, newArrayList(toStack(metal, 16))));
            //combinerRecipes.add(new CombinerRecipe(new ItemStack(tag.getAllElements().iterator().next()),
            //        newArrayList(toStack(metal, 16))));
            //}
        }
*/
    }

}