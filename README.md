# Simply Skills — NeoForge 1.21.1

A NeoForge port of [Simply Skills 1.7.2](https://github.com/Sweenus/SimplySkills/tree/1.20.1) by Sweenus and Timefall Development, preserving its combat specialisations and Ascendancy skill trees.

## Changes from the original

- **Platform:** Fabric 1.20.1 → NeoForge 1.21.1, with updated dependencies, rendering and animations.
- **Raging Javelin:** default throw interval increased from 8 to 20 ticks; configurable.
- **Righteous Hammers:** duration reduced from 40 to 20 seconds; added first-person visibility and a custom seeking-hammer spin.
- **Agony and Torment:** base duration is 8 seconds, plus 0.05 seconds per Ascendancy point.
- **Fixes:** corrected stack counts/depletion, upgrade priority, enemy targeting, cooldown pausing and minion effects. Arrow Rain volleys are staggered to reduce launch lag.
- **Compatibility:** removed the old Prominence integration and its exclusive content.

Most other changes restore the original behavior on the new platform. Existing configs may retain earlier defaults.

## Requirements

Minecraft **1.21.1**, Java **21**, NeoForge **21.1.248+**, Puffish Skills **0.18.3+**, Puffish Attributes **0.8.3+**, Spell Engine **1.10.2+**, and Spell Power **1.6.0+**. Use the NeoForge versions of dependencies.

Place the mod JAR and its dependencies in your instance's `mods` folder. Main gameplay, progression, item roundtrips, visual smoke checks and modpack world loading have been verified at the scopes recorded below.

Original code and assets are credited to Sweenus and Timefall Development. See [LICENSE](LICENSE).

## Review and testing

The port has been submitted for review. See the [review documentation](review/README.md), [AI usage disclosure](AI_USAGE.md), [recorded testing evidence](review/verification.md) and [remaining release checks](review/release-review.md). Progression limits, post-removal ability smoke checks, build storage/restoration and focused curse isolation checks passed. Projectile visual smoke checks are user-confirmed. Standalone packaged-server startup and focused two-player checks passed. Multiplayer testing found and fixed a shared Rapidfire shot counter; post-restart traces confirmed independent counters.

Core/class summaries and detailed Ascendancy, shared-mechanic, visual and server testing are consolidated in the verification document. The latest development-server checks used Java 21, NeoForge 21.1.248 and Spell Engine 1.10.5; the Gradle build passed. Local worlds, configs and debugger state do not transfer through Git.
