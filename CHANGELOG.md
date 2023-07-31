# Bygone Nether
> To advance the ancient history of the nether dimension.

Herein lies my changelog and motivation for developing a mod for Minecraft aiming to advance the lore behind the nether dimension, as well as provide additional challenges and compelling gameplay in the survival game mode.

<hr/>

## 🧠 Motivation

Modifications and additions to Nether structure spawning offer wide potential to introduce new lore implications. The additions should backlight the Piglin vs. Wither war, the ancient activities of these species, and expand upon the workings of their civilizations.

The gameplay should include additional and unique challenges which offer unique but rewarding loot. There should be additional purpose to explore each vanilla nether biome.

The new mobs should provide novel combat and cooperation. The designs should be deliberate and thorough to make the world feel lived in, and should reinforce the intended lore and magic system. The treasures to be gained from these endeavors should be upgrades to current vanilla offerings.

The structures should be as procedural as possible so that few many spawns play the same. The loot should be balanced enough to encourage exploration, but to not break the game's progresion.

<hr/>

## 📖 Current Features

### Blocks
* Added **Withered Blackstone** (and variants), **Withered Debris**, **Withered Basalt**, **Withered Coal**, and **Withered Quartz**, which naturally generate in catacombs, and can only be broken by at least netherite tier
* Added **Soul Stone**, which generates in soul sand valleys and is an additional base block for summoning the wither.
* Added **Warped Nether Bricks** (and variants), which generates as part of Citadel generation
* Blackstone split into stone and cobbled variants, like overworld stone
* Added a **Netherite Bell**, which generated in Piglin Manors

### Items
* Added **Gilded Netherite Armor**, a higher tier than netherite
* Added the **Wither Waltz Music Disc**, which can be found in catacombs
* Added the **Warped Ender Pearl**, which does not damage the player, and grants its user potions effects dependent on the circumstances of its collision
* Added the **Gilded Netherite Shield**, which can not be disabled by an axe
* Added spawn eggs for new mobs

### Mobs
* Added the **Wex**, vex-like wither mobs that swarm catacombs
* Added the **Piglin Prisoner**, which spawns as a hostage in catacomb ribs and can be rescued for a unique reward
* Added the **Piglin Hunter**, which spawns in piglin manors, sometimes on wither skeleton horses
* Added the **Warped Enderman**, a hostile enderman variant that spawns in citadels, whose vines can be sheared
* Added the **Wither Skeleton horse**, a fire-immune skeleton horse that rides over lava and can be found outside piglin manors
* Added the **Corpor**, a brutish wither skeleton, that is slow yet deals significant damage. Spawns in catacombs
* Added the **Wither Skeleton Knight**, an armored wither skeleton, whose armor can be broken before defeat. Spawns in catacombs
* Added the **Wraither**, a speedy and posessed wither skeleton, whose inhabiting wex will escape upon near death. Spawns in catacombs
* Unprovoked, neither piglins nor piglin brutes will attack players equipped with Gilded Netherite

### World
* Added the **Catacomb** which generates in Soul Sand Valleys
* Added the **Piglin Manor** which generates in Crimson Forests
* Added the **Citadel** which generates in Warped Forests
* Bastions now also generate in Basalt Deltas

### Recipes
* Gilded Netherite armor is craftable in a smithing table (netherite armor + corresponding gold armor)
* Recipes concerning blackstone and its cobbled variant now mirror overworld stone and cobblestone

### GUI
* Added Advancements to be achieved by exploring Bygone Nether's features

<hr/>

## 📦 CHANGELOG
### v1.3.1
#### Feature
* Ported to 1.20.1
* Added compatibility with Create Mod
  * Crushed Withered Debris that can be turned into netherite scrap variants
  * Crushing, mixing, and splashing recipes
* Added compatibility with Quark Mod
  * Vertical slabs for each slab variant
* Added compatibility with Infernal Expansion:
  * `infernalexp:soul_stone` now prioritized over `bygonenether:soul_stone`
* Added biome compatibility for updated mods:
  * Cinderscapes Reforged
  * Better Nether Reforged
* Nerfed Ancient Debris generation in Catacombs
* Added Turkish Localization
* [1.20.1] Wex model and texture now mirror 1.20.x+ Vex and Allay
#### Bug
* Fixed crash when paired with Amplified Nether
* Removed erroneous fortress datapack files
* Fixed blackstone and soul stone blob errors
* [1.18.2] Fixed missing netherite bell item texture
* [1.20.1] Removed gilded netherite armor, ported behavior to gold-trimmed armor
<hr/>

### v1.3.0
#### Feature
* Ported to 1.19.4 and 1.20
* Added Gilded Netherite Shield, obtained by rescuring Piglin Prisoners
* Added Netherite Bell, which generates in Piglin Manors
* Added Piglin Prisoner rescue behavior
* Piglin Hunters now spawn with shields
* Added new Citadel variant
* Added new Piglin Manor variant
* Nerfed Citadel loot
* Added new advancement for rescuing Piglin Prisoners
* Wither Skeleton Horses now ride over lava
* Removed Bygone Fortress (to be included in its own mod/datapack)
* Added new creative mode tab for Bygone Nether additions
* Added Spanish Localization
#### Bug
* Fixed Soul Stone and Cobbled Blackstone blobs not spawning
* Removed unnecessary Bastion modifications in datapacks
<hr/>

### v1.2.2
#### Bug
* Fixed RandomSource Bug with Warped Endermen
* Fixed crafting recipes for cobbled blackstone
* Fixed advancement localization to include modid to prevent conflicts
* Fixed sound localization to be more consistent with vanilla
* Added music disc tag to wither waltz
* Withered Debris can now be broken by tools higher than netherite from other mods
* Piglin prisoners now defend players from hoglins
* Fixed structures overlapping
* Fixed advancement localization to include modid to prevent conflicts
#### Feature
* Added Shield AI to Wither Skeleton Knight
<hr/>

### v1.2.1
#### Bug
* Corpors now wield stone axes
* Wither Skeleton Knights now wield shields
* Fixed Wraithers acquiring stone swords when dispossessed
* Fixed withered block variants not mineable by pickaxe
* Fixed withered and warped blocks not dropping on break
* Fixed recipes for chiseled blocks that overwrote chiseled deepslate recipe
#### Feature
* Added Russian localization
* Other QOL improvements and optimizations
* Added 1.19.x version
<hr/>

### v1.2
#### Feature
* Soul stone is no longer the only base block for constructing the wither
* Cracked nether bricks and blackstone slabs have been retextured
* Added withered blackstone stair and slab variants
* Added withered basalt, withered coal, and withered quartz, found in catacomb generation
* Added warped enderpearls, which don't damage the player, and grants situationally protective potion effects. Dropped by warped endermen and found in citadel chests
* Added the Corpor, a brutish wither skeleton, that is slow yet deals significant damage. Spawns in catacombs
* Added the Wither Skeleton Knight, an armored wither skeleton, whose armor can be broken before defeat. Spawns in catacombs
* Added the Wraither, a speedy and possessed wither skeleton, whose inhabiting wex will escape upon near death. Spawns in catacombs
* Added stonecutting and crafting recipes for blackstone stair and slab variants
* Structures now spawn in biomes from Oh the Biomes You'll Go for 1.18.2+
* Adjusted generation of structures to be more common
* Added new pieces to fortress generation
* Overhauled catacomb design
<hr/>

### v1.1.2
#### Bug
* Fixed basalt columns generating inside bastion remnants
* Added smelting recipe turning cobbled blackstone into blackstone
* Fixed sound subtitles and localization
<hr/>

### v1.1.1
#### Bug
* Fixed catacombs, citadels, piglin manors, and bastion remnants not generating when both Biomes O'Plenty and Incendium aren't loaded
<hr/>

### v1.1.0
#### Feature
* Soul stone is now the only base block for constructing the wither, as demonstrated in catacomb ribs
* Music disc: “wither waltz” by izofar, added to the game, which generates in catacomb loot
* Added the wither skeleton horse: a fire-immune, maxed-out variant of the skeleton horse
* Piglin hunters can now spawn on wither skeleton horses in front of piglin manors
* Piglin Prisoners admire gold items, and now pledge fealty to any player who gives them gold ingots, in addition to receiving potion buffs
* Piglin prisoners now dance when broken out of prison
* Warped endermen now spawn wither varying degrees of warp, and can now be sheared into normal enderman
* Added custom sounds for warped endermen and the wex
* Modified blackstone recipes to mirror overworld stone
* Added stonecutting and crafting recipes for warped netherbrick blocks
* Structures now spawn in biomes from other mods! Added integration for:
    * Better Nether Reforged (1.16.5)
    * Cinderscapes Reforged (1.16.5)
    * Infernal Expansion (1.16.5)
    * Incendium (1.18.2)
    * Amplified Nether (1.18.1+)
    * Biomes O’Plenty (1.X)
* Adjusted generation of structures to be more common
* Added new pieces to fortress generation
* Added downstairs features to piglin manors
* Polished generation and surrounding terrain
* Added Advancements to be achieved by exploring Bygone Nether’s features
* <hr/>