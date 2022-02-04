# Izomod - Bygone Nether
> To advance the ancient history of the nether dimension.

Herein lies the my changelog and motivation for developing a mod for Minecraft 1.18.1 aiming to advance the lore behind the nether dimension, as well as provide additional challenges and compelling gameplay in the surivial game mode.

## Motivation

Modifications and additions to Nether structure spawning offer wide potential to introduce new lore implications.

I will add a structure unique to the soul sand valley, called wither forts, that spawns an increased number of wither skeletons, as well as harbors wither skulls, to make the spawning of the wither less grindy. This will make the soul sand valley the true home of the wither skeletons, as will as provide a unique challenge to the player: A vertical structure (not unlike the end city) but such that the only feasible entrance (pre-netherite) is at the top of the structure, and the player must fight their way down.

I will add the citadel to the warped forest, indicative of an attempt by enderman (or their ancestors) to settle in the nether. However, the warp corrupted some of its inhabitants, making them immediately hostile to intruders. If the player can overcome these dangerous mobs, precious loot is to be acquired.

I will restrict the default fortress to the nether wastes. These structures demonstrate the attempt of wither skeletons to migrate beyond the soul sand valleys and into the other biomes. However, the lack of souls have left the withered skeletons weak, and in far fewer number. The blaze have found a home in these fortresses, and live symbiotically with the remaining withers. I also redesign the nether fortress to be slightly more stylish.

I will restrict the Bastion to the basalt deltas. The Piglins made use of natural resources in constructing their homes, it is more compelling that such a primitive society could not export large amounts of that material out of the deltas. Piglin troupes venture out into other biomes to collect gold, which they bring back to their bastions. Crusaders also venture out to battle the opposing wither skeletons. Some Piglin miners have acquired ancient debris and netherite, to restock the once-flooded repositories.

Finally, Piglin hunters stay in piglin manors in the crimson forests, where they collect food from the hoglins.

## Changelog

### Blocks
* Withered Blackstone + variants and Withered Debris added, which naturally generate in wither forts, and can only be broken by netherite-tiered items
* Soul Stone added, which [TODO] generates in soul sand valleys
* Blackstone split into stone and cobbled variants, like overworld stone

### Items
* Added gilded netherite armor, a higher tier than netherite
    * Craft it by placing netherite armor and golden armor in the smithing table
    * The gild will wear off at the speed of gold, yielding the original netherite armor with the durability at the time of crafting. It will then need to be reapplied

### Mobs
* Unprovoked, neither piglins nor piglin brutes will attack players equipped with Gilded Netherite
    * Pigling Brutes treat players wearing gilded netherite armor like piglins treat players wearing gold
* Added the Wex, vex-like wither mobs that swarm wither forts
* Added the Piglin Prisoner, which spawns as hostage in wither fort ribs and will pick up any gold-tiered gear the player drops to them
* Added the Warped Enderman, a hostile enderman variant that spawns in citadels

### Recipes
* Gilded Netherite is craftable in a smithing table (netherite armor + gold armor)

### Loot
* [NONE]

### World
* Bastions now generate only in Basalt Deltas
* Nether Fortresses now generate only in Nether Wastes
* Wither Forts now generate in Soul Sand Valleys
* Citadels now generate in Warped Forests
* Bastions generate with cobbled blackstone instead of blackstone

### GUI
* [None]

## Todo
* Add piglin manors to crimson forests
* Create necessary processors and features to create nether fortress columns
* Rename Wither Forts