# Izomod - Bygone Nether
> To advance the ancient history of the nether dimension.

Herein lies my changelog and motivation for developing a mod for Minecraft 1.18.1 aiming to advance the lore behind the nether dimension, as well as provide additional challenges and compelling gameplay in the survival game mode.

## Motivation

Modifications and additions to Nether structure spawning offer wide potential to introduce new lore implications.

I will add a structure unique to the soul sand valley, called catacombs, that spawns an increased number of wither skeletons, as well as harbors wither skulls, to make the spawning of the wither less grindy. This will make the soul sand valley the true home of the wither skeletons, as well as provide a unique challenge to the player: A vertical structure (not unlike the end city) but such that the only feasible entrance (pre-netherite) is at the top of the structure, and the player must fight their way down.

I will add the citadel to the warped forest, indicative of an attempt by enderman (or their ancestors) to settle in the nether. However, the warp corrupted some of its inhabitants, making them immediately hostile to intruders. If the player can overcome these dangerous mobs, precious loot is to be acquired.

I will restrict the default fortress to the nether wastes. These structures demonstrate the attempt of wither skeletons to migrate beyond the soul sand valleys and into the other biomes. However, the lack of souls have left the withered skeletons weak, and in far fewer number. The blaze have found a home in these fortresses, and live symbiotically with the remaining withers. I also redesign the nether fortress to be slightly more stylish.

I will restrict the Bastion to the basalt deltas. The Piglins made use of natural resources in constructing their homes, it is more compelling that such a primitive society could not export large amounts of that material out of the deltas. Piglin troupes venture out into other biomes to collect gold, which they bring back to their bastions. Crusaders also venture out to battle the opposing wither skeletons. Some Piglin miners have acquired ancient debris and netherite, to restock the once-flooded repositories.

Finally, Piglin hunters stay in piglin manors in the crimson forests, where they collect food from the hoglins. They can be found riding on wither skeleton horses for additional mobility.

## Changelog

### Blocks
* Withered Blackstone + variants and Withered Debris added, which naturally generate in catacombs, and can only be broken by netherite-tiered items
* Soul Stone added, which generates in soul sand valleys and is the new base block for summoning the wither.
* Blackstone split into stone and cobbled variants, like overworld stone

### Items
* Added gilded netherite armor, a higher tier than netherite
  * Craft it by placing netherite armor and golden armor in the smithing table
  * The gild will wear off at the speed of gold, yielding the original netherite armor with the durability at the time of crafting. It will then need to be reapplied
* Added the Wither Waltz music disc, which can be found in catacombs

### Mobs
* Unprovoked, neither piglins nor piglin brutes will attack players equipped with Gilded Netherite
  * Pigling Brutes treat players wearing gilded netherite armor like piglins treat players wearing gold
* Added the Wex, vex-like wither mobs that swarm catacombs
* Added the Piglin Prisoner, which spawns as a hostage in catacomb ribs and will pick up any gold-tiered gear the player gives to them
* Added the Piglin Hunter, which spawns in piglin manors, sometimes on wither skeleton horses
* Added the Warped Enderman, a hostile enderman variant that spawns in citadels, whose vines can be sheared
* Added the Wither Skeleton horse, a fire-immune skeleton horse that can be found outside piglin_manors

### Recipes
* Gilded Netherite is craftable in a smithing table (netherite armor + gold armor)
* Recipes concerning blackstone and its cobbled variant now mirror overworld stone and cobblestone

### World
* Bastions now generate only in Basalt Deltas
* Nether Fortresses now generate only in Nether Wastes
* Catacombs now generate in Soul Sand Valleys
* Piglin Manors now generate in Crimson Forests
* Citadels now generate in Warped Forests
* Bastions generate with cobbled blackstone instead of blackstone

### GUI
* Added Advancements to be achieved by exploring Bygone Nether's features
