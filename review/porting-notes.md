# Porting notes

## Main porting work

- Migrated the project to NeoForge 1.21.1 and Java 21.
- Updated the Gradle setup, mod metadata, registries, networking, mixins, events, attributes, effects, entities, and resource data for current APIs.
- Updated the active dependency set for Puffish Skills, Puffish Attributes, Spell Engine, Spell Power, RPG Series integrations, and supporting APIs.
- Updated skill definitions and resource schemas used by Puffish Skills and Spell Engine.
- Custom area spells use Spell Engine's friendly-target rules.
- Restored custom effect, projectile, HUD, model, and particle rendering affected by the port.
- Replaced legacy UUID-style attribute modifier names with stable readable resource IDs.
- Added a login migration that removes obsolete attribute modifiers left in existing test worlds.

## Differences from the original

- Removed inherited Prominence compatibility at user request for this NeoForge port, including its exclusive effects/assets and Immersive Melodies dependency. Standard Simply Skills behavior uses the former non-Prominence branches.

The items in "Compatibility work" change the implementation but are meant to keep the original result. The other sections clearly separate a deliberate balance change from corrections where the original Java disagrees with its own skill text or values.

Loader and API changes:

- The mod now uses NeoForge 21.1 instead of Fabric.
- Minecraft was updated from 1.20.1 to 1.21.1, and Java was updated from 17 to 21.
- Puffish Skills, Spell Engine, item data, attributes, damage, particles, models, networking, and mixins were updated for their new APIs.
- The dependencies were replaced with their NeoForge 1.21.1 versions.
- Effect modifiers now have readable IDs instead of UUID-style names. Old `simplyskills:modifier_*` entries are removed when a player logs in.

Compatibility work:

- Skill definitions, spells, effects, projectiles, particles, and models were updated because the old data does not work correctly with the new APIs.
- Swordfall, Judgment, Havensmith's Call, and Righteous Hammers were adjusted to appear and move like they did originally.
- Projectile orientation restores the original TOWARDS_MOTION default, including elemental arrows, Fan of Blades, Arcane Bolt and 23 definitions found in the remaining-orientation audit. Spell Engine 0.15.6 defaults to TOWARDS_MOTION; the port had explicitly selected ALONG_MOTION, adding a 90-degree rotation. Original passive_throw explicitly uses ALONG_MOTION and retains it. Seeking Righteous Hammers retain their separately approved custom spin. This changes model orientation only; projectile trajectories and combat values are unchanged.
- Skill icons, custom effect hooks, targeting, and projectile spawning were updated to restore their original behavior on NeoForge.
- Player-kill callbacks now use NeoForge's living-death event, restoring Bloodthirsty healing and the Ranger and Rogue Renewal upgrades after the original callback target was removed in 1.21.1.
- Evasion now uses NeoForge's incoming-damage event and respects Minecraft's normal hurt immunity before rolling again.
- Ability cooldown reduction treats Spell Power haste 1.0 as the baseline, applying reduction only to the bonus above it. Bone Armor's normal-haste cooldown is verified at 70 seconds.
- Expiration follow-up hooks run after the current effect iteration to prevent duplicate callbacks when they add effects. Bone Armor follow-ups and Undying were rechecked; broader lifecycle regression testing remains pending.
- Rage damage and receive-hit stack gain are applied after Minecraft accepts a hit, preventing repeated contact from bypassing normal hurt immunity, adding extra stacks, or causing continuous knockback.

Deliberate balance change:

- Raging Javelin now throws every 20 ticks by default instead of every 8 ticks. The interval can be changed in the config.
- Ascendancy Righteous Hammers lasts 20 seconds instead of the original 40 seconds, at the user's request. Its base cooldown remains 60 seconds.

Requested visual addition:

- Righteous Hammers now has a Simply Skills first-person world-render hook, reusing the existing orbiting models. The user verified first-person visibility with five orbiting hammers. This does not change its damage or targeting.
- Seeking Righteous Hammer projectiles use a centered forward end-over-end spin in a vertical plane, accepted by the user during hammer testing. Orbiting hammer orientation is unchanged.
- Custom player animations load from `player_animations`, and direct spell delivery sends the configured release gesture. Summoning Ritual also plays a Spell Engine two-handed casting gesture. Cleave, Arcane Bolt, and Summoning Ritual were visually confirmed.

Performance adjustment:

- Arrow Rain keeps the same radius, density, volley count, and elemental chances. Elemental projectiles launch together from the player when the skill triggers, while the visible normal rain is released in volleys four ticks apart.
- Elemental Artillery only creates a homing projectile after finding a valid enemy. Invalid homing projectiles are removed instead of remaining until their range expires.

Confirmed original code corrections:

- Agony and Torment: the [original 1.20.1 implementation](https://github.com/Sweenus/SimplySkills/blob/1.20.1/src/main/java/net/sweenus/simplyskills/abilities/AscendancyAbilities.java) selects the nearest living entity before checking friendly fire, so a rejected friendly can prevent a cast. The port now filters eligible targets first to match the nearest-enemy description. This is a correction to inherited behavior, not a loader compatibility repair. Both curses passed farther-enemy selection and no-eligible-target checks after restart; existing friendly-fire rules remain in use.

- Challenge: the original checks for more than one enemy, while its text grants Haste for each nearby enemy. The port allows one enemy to grant the first stack.
- Rampage Charge: the original starts the charge but does not grant Regeneration or Resistance. Its text explicitly promises both effects, so the port adds them.
- Weapon Expert: the original sets `chance` to `5` but checks whether a random value is greater than `5`, which succeeds 94% of the time. The port reverses that comparison so the value acts as a 5% chance. The skill text only says "occasionally" and does not give a number.
- Elemental Surge Renewal: the original Java has no chance roll and adds the literal value `3` to a duration measured in ticks. This is 3 ticks, or 0.15 seconds, and is not caused by the port. The original skill text says 15% and 3 seconds, so the port uses a 15% roll and adds 60 ticks.
- Arrow Rain tiers: the original checks tier 1 before tiers 2 and 3, so the higher radius and volley values are never selected after their prerequisite is unlocked. The port checks the highest tier first, matching the `+1`, `+2`, and `+3` skill text.
- Elemental Arrows tiers: the original has the same radius-order problem and adds all three quantity bonuses together. The port uses only the highest unlocked tier, matching the radius and quantity values shown in the skill text.
- Opportunistic Mastery: the original applies poison without checking for Stealth even though every tier says it requires Stealth. The port performs that check.
- Fan of Blades and Siphoning Strikes: the original passes the configured stack count directly as an effect amplifier, displaying one extra stack. The port converts the count to the correct zero-based amplifier.
- Wizard upgrade tiers: the original checks the lower Ice Comet, Leap, Speed, and Meteoric Wrath Renewal upgrades first, preventing higher unlocked tiers from taking effect. The port checks the highest tier first.
- Frost Volley and Static Discharge Leap: the original passes their configured counts directly as effect amplifiers, producing one extra volley or leap. The port converts the count to the correct zero-based amplifier.
- Meteoric Wrath Renewal: the original comparison makes each configured chance one percent higher, and its default config produces 10%, 25%, and 40% despite the text promising 10%, 30%, and 50%. The port uses the exact chances shown in the skill text.
- Ability cooldowns: the original uses wall-clock time, so cooldowns expire while a single-player world is paused. The port advances cooldowns with active client ticks instead.
- Bladestorm: the original uses a separate random roll after each Fan of Blades pulse. Its text grants one stack for each enemy hit, so the port increments once for each valid target hit and keeps the 20-stack cap.
- Shadow Veil: the original schedules Resistance using the Regeneration frequency. The port uses the Resistance frequency from its config.
- Exploitation: the original raw yaw comparison fails when rotations cross the `-180/180` boundary. The port uses the wrapped angular difference while keeping the original 32-degree rear arc.
- Evasion no longer blocks `/kill` or void damage, and Rage no longer allows very large damage values to overflow into invalid player health.
- Winterborn now converts Frost spell power into the displayed number of Soulshock stacks without adding an extra level.
- Necrotic Fortification now grants exactly half of the player's Armor and Armor Toughness, or the full values to a Greater Dreadglare, without hidden base points.
- Shadow Aura now deals its stated normal damage and stops ticking after its minion dies. Shadow Combust also blocks recursive calls during its lethal self-damage, preventing the death handler from producing a second explosion.
- Endless Servitude now starts at the stated 20% chance and still gains 5% for each harmful effect, up to 60%.
- Greater Dreadglare only gains Might when it has harmful effects, with one Might level for each harmful effect.

The original code and text used for this comparison are available in [AbilityLogic](https://github.com/Sweenus/SimplySkills/blob/1.20.1/src/main/java/net/sweenus/simplyskills/abilities/AbilityLogic.java), [AbilityEffects](https://github.com/Sweenus/SimplySkills/blob/1.20.1/src/main/java/net/sweenus/simplyskills/abilities/AbilityEffects.java), [BerserkerAbilities](https://github.com/Sweenus/SimplySkills/blob/1.20.1/src/main/java/net/sweenus/simplyskills/abilities/BerserkerAbilities.java), [SpellbladeAbilities](https://github.com/Sweenus/SimplySkills/blob/1.20.1/src/main/java/net/sweenus/simplyskills/abilities/SpellbladeAbilities.java), [RangerAbilities](https://github.com/Sweenus/SimplySkills/blob/1.20.1/src/main/java/net/sweenus/simplyskills/abilities/RangerAbilities.java), [RogueAbilities](https://github.com/Sweenus/SimplySkills/blob/1.20.1/src/main/java/net/sweenus/simplyskills/abilities/RogueAbilities.java), [WizardAbilities](https://github.com/Sweenus/SimplySkills/blob/1.20.1/src/main/java/net/sweenus/simplyskills/abilities/WizardAbilities.java), [StaticChargeEffect](https://github.com/Sweenus/SimplySkills/blob/1.20.1/src/main/java/net/sweenus/simplyskills/effects/StaticChargeEffect.java), [StealthEffect](https://github.com/Sweenus/SimplySkills/blob/1.20.1/src/main/java/net/sweenus/simplyskills/effects/StealthEffect.java), and [the original English skill text](https://github.com/Sweenus/SimplySkills/blob/1.20.1/src/main/resources/assets/simplyskills/lang/en_us.json).

Review and testing support:

- Removed the port-only click-to-lock testing hook. The upstream `removeUnlockRestrictions` config option is kept as it was upstream (default false); normal path/exclusivity rules apply and the existing Gracious Manuscript exception remains.
- Test traces are kept in the IntelliJ debugger. They are not included in the mod.

## In-game verification

Testing records are consolidated in [verification.md](verification.md), including [core-tree checks](verification.md#core-tree), [class-tree summaries](verification.md#class-trees), [Ascendancy evidence](verification.md#ascendancy) and [server/packaged checks](verification.md#server-and-packaged-checks). See that document for observed results and their limits, and [release review](release-review.md) for remaining work.

## Known review notes

- Ascendancy main checks are complete at the recorded scopes; final effect/animation audits and regression remain.
- Earlier intermittent first-use input/cooldown behavior did not reproduce in the latest user rejoin/first-press check with Anoint Weapon, consistent with an earlier successful isolated rejoin. No input-handling change made; this does not rule out an intermittent failure. Reproduce before changing input handling.
- Lightning Ball rolls a 5% discharge chance per nearby entity every five ticks. Its original text only describes this as periodic.
- Registered-effect review reuses completed tree/shared-mechanic runtime evidence and tests specific uncovered mechanics; the registry mapping itself is not a new runtime pass. Prominence-only effects and integration were subsequently removed at user request.
- Main tree testing is complete. Remaining regression and release work is tracked in [release review](release-review.md) and [verification evidence](verification.md).
- The development client can report missing Spell Engine conventional tags and Simply Swords recipes for optional mods that are not installed. These warnings do not stop the client and are not produced by Simply Skills logic.
