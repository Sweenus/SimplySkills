# Verification evidence

This is the central testing record for the port. The core/class summaries below were moved from the porting notes with their recorded claims preserved. They are earlier completion summaries, not newly recovered per-node traces or a fresh test of the current revision. Entries describing a correction alone do not establish an independent runtime pass.

Detailed entries distinguish debugger measurements, user observations, build/static checks and unverified cases. Later changes may need focused retests even where an earlier tree summary says complete. Worlds, configs and debugger state are local to the recorded session; references to a running Client do not describe this computer's current state.

- [Core tree](#core-tree)
- [Class trees](#class-trees)
- [Ascendancy](#ascendancy)
- [Visuals and casting](#visuals-and-casting)
- [Shared mechanics](#shared-mechanics)
- [Server and packaged checks](#server-and-packaged-checks)
- [Remaining checks](#remaining-checks)

## Core tree

- Restored skill visibility and icons in the Puffish Skills interface.
- Restored Swordfall, Havensmith, Judgment, and related overhead projectile/model behavior.
- Verified the main-tree passive statistics and combat triggers in-game.
- **Progression regression: PASS.** Zero points blocked the first unlock; the Initiate prerequisite and exclusive roots behaved correctly. The server rejected a second specialisation while Wizard remained unlocked, despite optimistic command feedback. With administrator-prefilled nodes, runtime snapshots confirmed 40 spent / Ascendancy locked, 41 / locked, then 42 / unlocked. An additional UI unlock attempt left 42 spent and zero available, despite 104 total granted points. This verifies the boundary and guard paths; the prefill was test setup.

## Class trees

### Berserker

- Corrected Berserker Challenge so one nearby enemy is sufficient.
- Restored Regeneration and Resistance granted by Rampage's Charge upgrade.

### Berserker and Spellblade completion

- Verified the complete Berserker and Spellblade trees, including their signature upgrades.

### Spellblade

- Corrected Weapon Expert's inverted Spellforged roll so its `chance = 5` value acts as 5%.
- Corrected Elemental Surge Renewal to match its original description: a 15% roll that adds 3 seconds.

### Cleric

- Verified Cleric healing, sharing, cleansing, resistance, barrier, aura, and Undying interactions.

### Crusader

- Verified Crusader defensive, taunt, mark, hammer, consecration, and related upgrade behavior.

### Ranger

- Fixed Raging Javelin targeting and throwing behavior, then reduced its repeated throw interval.
- Verified Ranger Reveal, Tamer, Bonded, Trained, Incognito, and the complete Disengage branch.
- Verified the complete Arrow Rain branch, including its Elemental enhancement, five-wave rain, Minefield, and Elemental Artillery.
- Verified the complete Elemental Arrows branch, including all attunements, quantity and radius tiers, isolated and grouped targeting, and Renewal.
- Confirmed the staggered Arrow Rain removes the observed launch lag while keeping the original arrow count, elemental chances, and player-to-rain projectile path.

### Rogue

- Verified the complete Rogue tree, including Stealth requirements, rear-angle detection, Smoke Bomb, Shadow Veil, Evasion, Fan of Blades, Bladestorm, Siphoning Strikes, and their upgrades.
- Confirmed successful Evasion rolls cancel damage without starting the hurt animation, while failed rolls still behave as normal hits.
- Verified Fan of Blades and Siphoning Strikes start with the displayed stack counts, Bladestorm gains one stack per enemy hit, and Fan of Blades Renewal adds two stacks per kill.

### Wizard

- Verified the complete Wizard tree with Wizards RPG Series installed, including spell-power scaling, every signature branch, isolated and combined upgrades, projectile behavior, effect counts, renewal chances, and ability cooldown pausing.
- Confirmed the corrected combined Wizard tiers select Greater ++, 52 Static Discharge leaps, a 15% Speed chance, and exactly six Frost Volley shots.

### Necromancer

- Verified the complete Necromancer tree, including minion attributes and limits, Wraith effects, harmful-effect transfers, defensive effects, death triggers, auras, life siphoning, resurrection chances, and Greater Dreadglare traits.
- Retested the Necromancer corrections: Winterborn stacks, Fortification armor/toughness, normal Shadow Aura damage, Endless Servitude's 20% base chance, and Greater Dreadglare Might levels. Combustion regression confirmed one explosion for both aura-triggered and direct deaths after fixing recursive death-handler calls.

## Ascendancy

All thirteen Ascendancy abilities have completed their main checks at the recorded scopes. Normal casting animations and no-target visual checks are user-confirmed; the subsequently removed Prominence integration is no longer part of this port. See [release review](release-review.md) for remaining work. Skyward Sunder movement/animations and recovery were user-confirmed at 30 points; runtime showed 45 ticks, a roughly seven-block rise, and return to ground. Upward strike and final slam each dealt 11.232 at multiplier 1.8 (another cast dealt 10.8 as attributes changed). Fixed Death Mark to trigger at >=30 and Might-to-Barrier to use actual stacks. Post-restart probes confirmed Might III -> Barrier III with matching 1128-tick duration, and two charge hits at exactly 30 points each applied Death Mark for 60 ticks. No probe errors; completed probes removed. Midair damage, lower-point comparison, and nine-stack Barrier cap were not separately verified. Latest code passed IntelliJ compilation and Java 21 Gradle build. Post-removal smoke checks are recorded below.

- Ascendancy: Bone Armor, Righteous Hammers, and Cyclonic Cleave main checks are complete at the recorded scope. Cleave's 30-point damage scaling, whirlwind animation, and pull against an AI-enabled zombie are confirmed. Do not repeat these without a relevant change or failure.
- **Magic Circle checks complete:** visible circle, immobilization/recovery, healing gesture, 241 ticks at 1 point and 270 ticks at 30 points, and +30% power in all six schools passed. Corrected the Echo bonus to require owning Wizard Spell Echo. Post-fix runtime checks confirmed 15% with the passive locked inside the circle, and 15% outside / 25% inside with it unlocked. Builds passed; probes removed. Arcane Slash checks are also complete at the scope below.
- Arcane Slash main checks complete at the accepted scope. Arcane Slash was tested at 30 spent Ascendancy points. Verified repaired animation/orientation, upgraded projectile branch, automatic repeats, 3 Attunement stacks per use and a hard 20-stack cap. Three aligned targets received sequential hits (initially 2.106432 damage each, confirmed by health loss). User accepted the practical larger-group piercing check; larger visual size was tentative. Exact damage coefficient/power correlation and the 10-target cutoff were not separately verified. Builds passed.
- Agony bonus-damage and 30-point healing checks passed for the same-player caster: four paired traces applied 0.7175 bonus damage and restored 0.1025 health per hit (12.933332 to 13.343332). The two-player Agony ally-hit check below verifies healing of a different attacking player. Preserve 10% of Healing spell power per trigger and +1 tick per point. Earlier 201/230-tick duration traces used the former 200-tick baseline; the current user-requested 160-tick baseline and remaining-time observation are recorded in the effect audit below.
- Torment main 30-point checks are complete: curse and Taunt applied for 190 ticks (9.5 seconds); six redirected 3.0-damage hits reduced the zombie health (20 to 17.06 on the first, eventually 0), while player health remained 8.975332 through the sequence. No breakpoint errors. Applied damage is subject to target mitigation. Reuse prior shared Taunt coverage; lower-point comparison and post-expiration hit behavior were not separately checked here.
- Chainbreaker main 30-point mechanics verified after the fix: Weakness II and Slowness II were fully removed, Night Vision retained its exact 1006-tick duration, and Might IV, Marksmanship IV and Undying applied for 120 ticks (6 seconds). Health stayed 14.84; no breakpoint errors. The fix removes complete non-beneficial effects from a snapshot, resolving the previous one-level decrement. Shared helper unchanged; reuse earlier Undying survival coverage. Added the requested spell_engine:one_handed_shout_release gesture once per activation for caster and tracking players. Animation asset name checked, Gradle build passed, and user confirmed the shout animation in-game. Chainbreaker checks complete at this scope.
- Ghostwalk main checks complete at 30 spent points: starts at 60 ticks (3 seconds) with Soulshock IV. Eight paired hits captured; raw damage 8.7125, first zombie health 14 to 5.4269 after mitigation. First heal raised player health 12.965332 to 17.321583 (+4.356251, half raw damage); next hit reached the 20-health cap. Expiration reached the gravity-reset statement. User confirmed Wither damage stopped during Ghostwalk, resumed afterward, and normal movement returned. Wither was present in the start trace; damage cancellation itself was not captured by the mixin probe. Invulnerability/recovery acceptance is based on user observation. Two-player Ghostwalk protection isolation subsequently passed (see multiplayer checks below); lower-point scaling was not separately tested.
- Rapidfire gameplay and projectile orientation checks complete at the recorded scope. Bow trace verified at 30 spent points: 150-tick duration (7.5 seconds), 23 projectile-dispatch calls, and one Marksmanship stack per third dispatch, reaching VII. No breakpoint errors. Crossbow trace also verified at 30 points using archers:netherite_rapid_crossbow: 150 ticks and 23 projectile-dispatch calls, reaching Marksmanship VIII. The previous bow cast left the arrow counter at 2, so the first crossbow arrow completed the next group of three; counter persistence across casts is confirmed. No breakpoint errors. User confirmed normal weapon use returns. Warden damage verified: entity 2523 health fell from 16.497253 to 7.7335033 between two arrow damage events, confirming 8.76375 health lost from the first hit. The second incoming amount was 9.640125 with Marksmanship I; final health/death was not captured. No probe errors. User confirmed the character animation and walking during Rapidfire, then reported the arrow projectile flying sideways. Changed rapidfire_projectile model orientation from ALONG_MOTION to TOWARDS_MOTION, following the verified Arcane Slash correction. JSON parsing and Gradle build passed; user confirmed the corrected arrow direction after a full restart. Launch velocity remains 8.9. Inspection of the installed Spell Engine damage calculation confirmed projectile speed does not multiply Rapidfire impact damage. Projectile definition uses 0.9 times physical ranged power before other modifiers/mitigation. Normal crossbow comparison captured two arrow events with no active shooter effects: each incoming amount was 10.25, and Warden health 490.775 to 480.525 confirmed 10.25 actual damage for the first. Tested Rapidfire baseline 8.76375 is 14.5% lower per arrow than this normal shot; this comparison is not a DPS measurement. Multiplayer counter isolation exposed a shared-counter bug; the fix and post-restart verification are recorded below. Lower-point behavior was not separately tested.
- Weaker weapon comparison: archers:rapid_crossbow Rapidfire baseline 7.8412495 versus netherite 8.76375 (10.5% lower); Marksmanship I 8.625375 and VII 13.330124. All 23 hits reached Warden 3015; health 470.275 to 224.05983 confirms approximately 246.215 total burst damage. Five subsequent normal shots with no active effects registered 9.225, 8.2, 7.175, 9.225 and 8.2 (mean 8.405); successive health readings confirm the first four. Normal-shot variation was observed, not diagnosed; the earlier netherite normal sample contained only two 10.25 hits. No breakpoint errors. Weapon-dependent Rapidfire damage is verified; this is not a controlled normal-fire DPS comparison.
- Righteous Shield at 30 points: passive gain every 400 ticks; three base casts hit three zombies each with two ricochets and 0.96278244 damage per hit. Upgraded projectile hit six distinct zombies with five ricochets. Scoped collision fixes preserve velocity and damage. Exact 5/10/15-stack casts select the matching tier and leave Aegis=null; a six-stack cast consumes five and retains one. Healing at 11 stacks raised a tamed wolf from 5 to 6.706625 health and left one stack. Final tier reduced zombie health from 20 to 14.336106 and applied Death Mark (153 ticks remaining). Six melee-block traces retained 18 stacks at the cap; a later user check confirmed three blocks grant three stacks below cap. Shield-specific depletion avoids recreating amplifier -1. Main checks passed; final-tier projectile was briefly visible but its larger-size comparison remains deferred.
- Cataclysm main checks complete: user confirmed Fire/Frost visuals, channel animation, and movement recovery. At 30 points: 70 ticks, four dispatches every 17 ticks at distances 5/10/15/20, granting Spellforged I-IV with 60-tick refreshes. At 29 points: three dispatches every 18 ticks at distances 10/15/20 and final Spellforged=null after clearing it beforehand. Frost 7.584 > Fire 1.304 selected comets; Fire 10.432 > Frost 1.264 selected meteors. Falling-impact stacks and sequential Warden health confirmed Frost hits of 17.693604 and 14.423317 and a Fire hit of 39.038227. Later incoming amounts were captured without final health. Exact coefficient/dropoff correlation and frequency cap were not separately verified; no Cataclysm source changes were needed.

## Shared mechanics

### Shared kill callbacks

- Rechecked the repaired kill callbacks: Bloodthirsty restores 25% maximum health, Elemental Arrows Renewal follows its 35% roll and adds one stack, and Fan of Blades Renewal adds two stacks up to 20.

### Scope of the effect audit

- Registry now contains 65 effects: 63 unconditional and two requiring Paladins. The 65 normal effects map to completed core/class/Ascendancy checks; this mapping is not a new per-effect runtime pass.
- Removed eight Prominence-only effects: Focus, Titans Grip and six Melodies. Focused two-player checks subsequently passed at the scopes recorded below.
- Golden Aegis below-cap gain passed by user observation: three melee blocks gave three stacks after clearing the effect. Final-tier shield was briefly visible, but the size comparison is deferred.
- Agony uses 160 + Ascendancy points ticks, at user request: 8 seconds plus 0.05 seconds per point. EN/FR/RU tooltips say 8 seconds. A Warden query returned 165 remaining ticks after casting, consistent with 190 initial ticks at 30 points after command-entry time. Exact initial duration and tooltip display were not captured; user elected to move on.
- Agony and Torment produced no visible activation without a target. Exact retry cooldowns were not measured.
- Anoint Weapon worked on the first press after rejoining, then expired and recast normally. Earlier intermittent first-use trouble did not reproduce; this does not rule out every intermittent failure or verify all expiration callbacks.
- Added buff particles were removed from all 15 call sites and the shared helper. User confirmed their absence on Anoint Weapon after restart.

Further runtime checks should name a concrete unresolved behavior and expected result. Reuse completed shared-mechanic evidence rather than repeating generic tree tests.
Preparation regression: user reported dashing with Shadowstrike off. Runtime captured the Shadowstrike branch (range 8), with velocity changing from stationary to approximately (-7.74, 0, -0.47). The reference incorrectly used kp8uei8ppni71b5x (Preparation itself); corrected it to m2fqj7bmtsh33fvs (Shadowstrike), verified against rogue/skills.json. IntelliJ compilation and Gradle build passed. Post-restart runtime verified both branches: OFF reported shadowstrike=false, no dash call and unchanged velocity (0, -0.0784, 0); ON reported true, reached the range-8 dash and ended with velocity approximately (-7.728, 0, 0.020). Both casts applied Stealth and Speed III for 80 ticks; no probe errors. User reported the result good. Earlier Rogue completion did not validate this distinction. All three agent probes removed; no source logging added.

Earlier quick-switch tests used the now-removed unlock bypass. Use normal progression or administrator skill commands for future setup. Worlds/configs do not transfer through Git.

See [release review](release-review.md) for remaining review work and release gates. The evidence here remains scoped to the builds and observations recorded; later refactors need their own validation.

### Refactor regression

After rebuilding and restarting the dedicated server, RCON applied two Barrier stacks to the tagged test husk. Two one-damage attempts changed amplifier 1 to 0, then removed the effect; health remained 16.6. A third attempt without Barrier succeeded and reduced health to 15.6. This exercises the refactored single-stack helper through the normal damage hook, including final depletion. User confirmed Arcane Bolt icon and casting work normally on the restarted client. After Bone Armor was unlocked via RCON, user confirmed its Ascendancy-slot icon, armor activation and cooldown. Both HUD slots and the single-stack consumption/depletion checks passed at this scope.

### Multi-stack depletion fix

Pre-fix Exhaustion decay removed five from five but left one stack, confirmed by the next helper entry. The shared guard now removes the effect when `stacksRemoved >= currentAmplifier + 1`; positive remainders keep the existing replacement behavior. IntelliJ compilation and Java 21 Gradle build passed.

Post-restart debugger checks passed with no probe errors:

- Exact decay: 5 minus 5 produced `effect=null`, twice.
- Partial decay: 7 minus 5 left two stacks, preserving the 1182-tick duration.
- Excess decay: about one second later, 2 minus 5 produced `effect=null`.
- Aegis partial consumption: caller reported 40 stacks and cost 35; five remained with the same 1194-tick duration, then natural decay cleared them.
- Aegis exact boundary: caller reported 35 stacks and cost 35; paired helper traces recorded 1190 ticks before removal and `effect=null` immediately afterward.

All agent probes were removed; user-owned breakpoints retained. Client remains running locally. These checks verify stack consumption/depletion; no new measurement of Divine Protection strength or multiplayer behavior was made.

### Curse/taunt target isolation — focused checks passed

Pre-fix Client trace: both tagged NoAI husks carried command-applied Agony; husk B also had ordinary MobEffectInstance Taunt without a source. Initially both effect registries reported no retained target. Player 56 cast Torment at 30 points on husk A (1296). Immediately afterward AgonyEffect and TauntedEffect for husk B (1316) both reported retainedTarget=56 despite B still carrying an ordinary, source-less Taunt instance. A carried the expected SimplyStatusEffectInstance Taunt. No probe errors. This proves shared target leakage at the targeting branch; actual pursuit was not observed because the husks had NoAI. TormentEffect contains the same copied logic, but its own cross-mob leakage was not separately reproduced.

Taunt now resolves its target locally per affected entity. Agony and Torment are marker effects with their existing combat hooks; only Taunt forces targeting. IntelliJ compilation and Java 21 Gradle build passed. Post-restart isolation passed on fresh husks: A (350) had SimplyStatusEffectInstance Taunt with source=12 and mobTarget=12; interleaved B (348) had ordinary MobEffectInstance Taunt with source=-1 and mobTarget=-1, both before and after A was taunted. No probe errors. Older restored husks also initially showed no source/target, but were replaced before the seeded test; this does not complete restored-source isolation. Torment combat regression passed: a command-generated 3.0 mob_attack attributed to husk A (350) reached the sourced Torment hook; paired traces showed mob health 20.0 to 17.06 and player health 24.1 at both points. Execution reached return true, whose caller cancels the original player hit. No probe errors. Agony combat regression passed across four paired triggers on husk 350: bonus damage reduced health by 0.8384 each time (first 17.06 to 16.2216); each hook healed the player by approximately 0.208 (first 7.7666664 to 7.9746666). Player health decreased between later triggers for an unmeasured reason, but the paired hook traces isolate each healing gain. No probe errors. Completed agent probes removed. Follow-up curse-only and restored source-less isolation checks passed on the development server. At 30 points, Torment applied 190-tick sourced Taunt to A; four samples showed A targeting its caster while Agony-only B and Torment-only C had no Taunt and no mob target. A command-applied source-less Taunt was then saved on B and survived a full server restart with the same UUID. After another sourced cast on A, paired samples showed A targeting its caster and restored B retaining ordinary MobEffectInstance, source=-1 and target=null. These NoAI checks verify target state, not pursuit or preservation of an actual caster reference across reload. Two-player Torment damage-redirection ownership subsequently passed; simultaneous 30-point Taunt AI targeting was not separately measured. Test mobs and agent probes were removed; temporary Ascendancy allocations were restored.

### Nearest eligible curse target — focused checks passed

Pre-fix Agony selected owned wolf 714 (tag target_friend) at squared distance 4.0, then reached return false, twice. User setup placed a NoAI husk six blocks away; that husk was not inspected by the old selection trace because selection stopped at the wolf. No probe errors. Both Agony and Torment now filter eligible living candidates using the existing friendly-fire helper before choosing the nearest. Compilation and Java 21 Gradle build passed; post-restart Agony selected husk 19 (target_enemy) at squared distance 36.0 and applied 190 ticks while the user maintained the closer-wolf setup; no probe errors. Torment also selected husk 19 at squared distance 33.867743037072074 and applied 190 ticks; no probe errors. During the friendly-only test, an initial Torment cast hit leftover husk 16 (curse_b) at squared distance 79.3057. Later Torment and Agony casts each reached return false with target=none; no probe errors. User setup retained the friendly wolf; the failure probes establish no eligible selection, not a complete entity census. Both success and no-eligible-target paths are verified at this scope. All agent probes removed; exact retry cooldowns were not measured. Agent probes removed before edits. Local test entities are tagged target_friend and target_enemy; Agony is selected. Torment's pre-fix failure was not separately reproduced.

## Visuals and casting

- Animation work: fixed the shared release-animation callback and moved 12 custom animations into `player_animations`; added Spell Engine's two-handed ground-release gesture to Summoning Ritual. User visually confirmed Cyclonic Cleave, Wizard Arcane Bolt, and Necromancer Summoning Ritual. The last code build passed. Normal animations were subsequently user-confirmed; removed Prominence animations are no longer applicable.
- **Casting-animation coverage:** reviewed all 23 class signatures and 13 Ascendancy activation paths, including effect-driven gestures. Added release gestures to 13 class abilities that lacked an activation gesture (Disengage and Sacred Onslaught intentionally use their movement alone at user request) (the subsequently removed Prominence branches are excluded). The shared server-only helper sends one gesture per successful direct activation; existing spell/effect sequences remain in place. Agony/Torment gestures are user-confirmed after restart, with no animation playback errors found in the client log. Bone Armor and Righteous Hammers gestures are user-confirmed. Selected dependency assets/internal names, IntelliJ compilation and Java 21 Gradle build passed. Normal class gestures are user-confirmed visually; removed Prominence gestures are no longer applicable. No cast delay, movement lock, damage, cooldown or duration changes were added.
- Git transfers source/assets and these review notes, but not `run` worlds/configs or IDE debugger state. Pull `main`, use Java 21, run `gradlew.bat build`, then launch the IntelliJ `Client` configuration. Use a full client restart for resource changes. Recreate test-world unlocks if needed; do not assume the old world is present.
- HUD alignment: moved both ability frames one pixel right so their centers match the icons, cooldown overlays, and key labels. IntelliJ compilation passed and the user confirmed the alignment looks good after restarting.
- Animation-name repair: IDE console captured repeated null-animation exceptions from Spell Engine playback. Player Animator registers internal JSON names, and Arcane Slash requested arcane_slash_alt while its file declared one_handed_slash_horizontal_right. Corrected five custom internal names to match their resource IDs (arcane_slash, arcane_slash_alt, ground_cleave, shield_throw, upward_slash). All 12 custom files parsed and all 97 spell animation references resolved against internal names; Gradle build passed. After restart, the registry probe confirmed the requested slash name is present and the old name absent. No null-animation playback exceptions were found in the new IDE console; the remaining observed null-pointer messages were startup recipe-category warnings. User confirmed the visuals look good. Removed the completed registry probe; the earlier filename-only validation was insufficient. Later, at reviewer request, the original internal names were restored and the spell definitions were changed to reference them instead; this needs an in-game recheck.
- Startup performance: user reported world-entry lag. Current IDE log records a 4136 ms server backlog after joining and 2132 ms during the first shield cast. First flight probe events had a 6.5-second gap; later casts completed promptly. Debugger overhead is a possible contributor, not a proven cause. User reports the issue is no longer occurring after probe removal; causality is not established. No performance code change made. Revisit only if it recurs.
- Debugger handoff: all agent probes removed; user-owned probes retained. Current Client was launched normally for visual testing. Worlds/configs and skill selections are local only; do not assume the previous home setup is active here.

### Original projectile orientation audit

Compared all 25 remaining ALONG_MOTION spell definitions with [original Simply Skills 1.20.1 spell data](https://github.com/Sweenus/SimplySkills/tree/1.20.1/src/main/resources/data/simplyskills/spells), and inspected the cached Spell Engine 0.15.6 and 1.10.2 renderer bytecode. The old ProjectileModel constructor defaults to TOWARDS_MOTION. Of those 25 originals, only passive_throw explicitly selects ALONG_MOTION; the other 24 omit orientation. Both renderer versions add a 90-degree turn for ALONG_MOTION (the rotation order differs).

Restored TOWARDS_MOTION in 23 definitions: Cataclysm comet/meteor, Divine Intervention, all fire meteor and ice comet variants, lightning balls/lesser, bow snipe, homing dagger, Havensmith's Call, four righteous shields, both sacred orbs and lesser soul bolt. Kept passive_throw's original orientation and the user-approved custom seeking-hammer spin. The four Arcane Bolt variants were already corrected. Only orientation fields changed; spin, model transforms, flight and damage settings were preserved.

Validation: all spell JSON parses, orientation-only diffs checked, IntelliJ build and Java 21 Gradle build passed. **Projectile visual smoke check: PASS (user-confirmed).** Arcane Bolt was confirmed in the external modpack; Ice Comet and Sacred Orb were observed in the development client. Rapidfire was additionally inspected after the user initially saw only particles: debugger traces confirmed its baked arrow model and draw call, the user saw a stationary diagnostic arrow, and subsequently confirmed the flying model at its original speed. Temporary speed overrides and agent probes were removed. This is an overall gameplay visual assessment, not a per-variant or per-angle measurement.

### Casting and regression evidence

Buff activation particle preview removed at user request from all 15 activations, including the shared sender helper. Existing ability effects and casting gestures remain; Sacred Onslaught still uses its rush alone. Gradle build passed. After restart, user confirmed Anoint Weapon has no added particles. Shared helper and all 15 call sites are removed; other activations were not individually rechecked. Continue effect/regression checks.

Bone Armor and Righteous Hammers casting gestures passed user visual confirmation after restart. Both code builds passed previously; the client log inspected during that check contained no animation playback errors. Reuse their completed gameplay checks unless a new failure appears.

Agony and Torment now send spell_engine:one_handed_projectile_release once in their successful server activation branches, after applying the curse to a valid target. Existing effects, smoke, sounds, targeting and failed-cast behavior are unchanged. Shared activation/effect paths were checked for duplicate gestures, and the installed asset internal name was verified. IntelliJ compilation and Java 21 Gradle build passed. User confirmed both successful-cast gestures after full Client restart; no animation playback errors were found in the current client log. Agony and Torment with no nearby target: user reported nothing happened for each, confirming no visible activation; exact retry cooldowns were not measured. Other normal class gestures are now user-confirmed; the checklist below records the selected gestures.

Casting-gesture visual checklist:

| Class | Abilities and gestures |
| --- | --- |
| Berserker | Rampage (with or without Charge), Bloodthirsty and Berserking: one-handed shout. User prefers the same shout during the body tackle; conditional pointing gesture removed. Compilation/build passed; user reports all normal animations look good. |
| Rogue | Evasion and Preparation: outward area release; Siphoning Strikes: crossed weapons. Preparation node-reference fix verified after restart: Shadowstrike off leaves velocity unchanged; on reaches the dash branch. User confirmed the Rogue gestures and pose recovery, including Evasion and Siphoning Strikes; no animation playback errors found in the current client log. |
| Ranger | Disengage: leap only, hand gesture removed at user request (user-confirmed in the overall animation check); Elemental Arrows: healing/enchantment release; Arrow Rain: upward archery release. User confirms both tested; visual checks complete at this scope. |
| Spellblade | Elemental Surge: area release; Elemental Impact: forward release; Spellweaver: crossed weapons. User confirmed all three gestures and pose recovery. |
| Cleric | Anoint Weapon: healing/enchantment release; user-confirmed in the overall animation check. |
| Crusader | Sacred Onslaught: rush only, hand gesture removed at user request; Consecration: two-handed ground release. User-confirmed in the overall animation check. |
| Ascendancy | Agony and Torment: forward release, visually confirmed after restart |

Existing coverage retained: Wizard Meteor Shower/Ice Comet/Static Discharge/Arcane Bolt; Cleric Divine Intervention/Sacred Orb; Crusader Heavensmith's Call; Necromancer Summoning Ritual; all other Ascendancy gestures, including delayed Rapidfire/Skyward Sunder sequences. Reuse prior visual evidence where recorded.

## Server and packaged checks

See also [compatibility-removal and packaged smoke checks](#prominence-integration-removal) and [packaged Arcane Bolt testing](#arcane-bolt-packaged-orientation-regression).

### Dedicated development server

- Startup, login, skill-tree display and icons passed. Anoint Weapon animation, buff and cooldown were user-confirmed.
- Arcane Bolt was visible to the caster; tagged husk health changed from 20 to 16.6 at Arcane Spell Power 1.0, matching the 3.4 coefficient. Other-player Arcane Bolt animation and projectile visibility subsequently passed by user observation.
- This used the IDE runtime, not the packaged JAR. Local world: `run/dedicated-test/dedicated-test-world`; server binds `127.0.0.1:25565`. The unauthenticated IntelliJ `Dev` player needs local `online-mode=false`.
- RCON is verified on `127.0.0.1:25575`. Password and `Send-LocalCommand.ps1` are under ignored `run/dedicated-test`; they do not transfer through Git. Run the helper with `powershell -NoProfile -ExecutionPolicy Bypass -File`.
- With default restrictions, lock the previous specialisation category before unlocking another. Locking only its active node leaves the one-specialisation guard in force. Puffish command success text alone does not prove the category unlocked.

### Packaged server and multiplayer checks

**Focused multiplayer smoke checks: PASS.** The standalone NeoForge 21.1.248 server loaded the built Simply Skills JAR and data pack; Dev and Dev2 joined from separate clients on the same PC. The server ran at its normal 20 TPS target. This localhost setup verifies two client sessions, not external-network connectivity.

- **Visibility:** the user confirmed that Dev2 saw Dev's Arcane Bolt casting animation and projectile.
- **Divine Intervention:** Dev2's health rose from 12 to 12.74807 with natural regeneration disabled; the user confirmed the recipient buff and wing effect.
- **Friendly fire:** Cyclonic Cleave left allied Dev2 at 12.74807 health while the test husk fell from 20 to 16.11311.
- **Consecration:** sampled caster health rose from 17.5 to 20, ally health from 10.24807 to 14.523073, and enemy health fell from 20 to 2.900002. Dev2 received Might V and Spellforged III. Tests used the two ally-buff upgrades.
- **Torment ownership:** both players cursed separate husks. Command-generated mob attacks against each husk's own caster left player health unchanged and damaged the husks. Cross-caster attacks then reduced each player's health by 2.5 while husk health stayed unchanged. This verifies source-specific redirection; it does not separately measure simultaneous upgraded Taunt pursuit.
- **Agony ally hit:** Dev applied Agony at 30 points; Dev2's Arcane Bolt reduced the cursed husk from 20 to 16.5 and healed Dev2 from 16.423075 to 16.523075. Dev remained at 20. This verifies healing of a different attacker using an upgraded caster's curse.
- **Ghostwalk isolation:** with Dev's Ghostwalk active, a controlled two-damage hit left Dev at 20 while Dev2 fell from 16.523075 to 14.523075 without Ghostwalk. After expiration, the same hit reduced Dev to 18.
- **Rapidfire:** both players' individual casts produced Marksmanship without applying effects to the idle player. Target health samples confirmed projectile hits; the user subsequently confirmed the arrow model in flight. Counter isolation required the fix below.

### Rapidfire per-player counter fix

The effect singleton held one arrow counter for every player. Server debugger traces reproduced the problem during overlapping two-second effect activations: Dev's first projectile logged count 1 and Dev2's first logged count 2 on the same effect object. One player could therefore advance the other's three-shot Marksmanship threshold.

The counter now belongs to each live player via weak player keys, preserving progress between that player's casts without retaining disconnected players indefinitely. Projectile speed, damage, duration, thresholds and cooldown code are unchanged. Java 21 Gradle build passed. After a full debugger-server restart, the same two-player activation independently logged 1, 2, 3, then 1, 2, 3 for both players, with stored counts matching each player and no probe errors. The focused regression used command-applied Rapidfire effects; normal keybind casting had already been exercised by both players. Agent probes were removed. The rebuilt JAR then passed standalone server startup, including successful loading of the Simply Skills data pack.
### Prominence integration removal

Removed at user request: Prominence ability classes and Bard bridge, corruption/tree overrides, exclusive combat/movement hooks, eight effect registrations/classes, MiscConfig and its translations, compatibility icons, two exclusive spells and eldritch hammer models/texture. Removed Immersive Melodies from Gradle dependencies because only the removed Bard bridge used it. Preserved normal category IDs, ability formulas, Bone Armor armor/toughness modifiers (including its existing modifier identifier), 70-second cooldown, and standard item/tree behavior. Other integrations remain.

Validation: IntelliJ compilation and Java 21 Gradle build passed; all retained JSON resources parsed; source/build search found no Prominence/dependency references; JAR inspection found none of the removed classes or asset files. Registry count is 65. A clean task could not remove dependency JARs locked by the running Client; subsequent build recompiled Java and resources successfully. Leftover empty compatibility folders were subsequently deleted individually using exact paths. A forced JAR rebuild passed, and both filesystem checks and JAR entry inspection confirmed the compatibility directories are absent. The user installed the rebuilt JAR in their external modpack, entered a world and confirmed the skill menus/trees and icons looked correct. This verifies packaged startup/world loading and menu display by user observation; no external logs were inspected, and Ascendancy unlock eligibility was not separately established. Follow-up ability and item smoke checks passed in the dedicated development runtime; packaged dedicated-server startup and two-player login subsequently passed.

### Post-removal ability and item checks

- **Bone Armor: PASS.** At one Ascendancy point, runtime captured amplifier 3, duration 800 ticks and armor 4; the user confirmed the HUD, activation and cooldown.
- **Chainbreaker: PASS.** At one point, paired traces showed Weakness II and Slowness II removed, Night Vision retaining its exact duration, and Might I / Marksmanship I applied for 120 ticks. The user confirmed the shout animation and cooldown.
- **Cyclonic Cleave: PASS.** One-point activation lasted 40 ticks; two hits reduced a tagged husk from 20 to 16.835377 health. The effect expired, and the user confirmed the spin, cooldown and movement recovery.
- **Skill Chronicle: PASS.** A stored build recorded its owner, three categories and five skills; storage cleared the allocation, restoration unlocked all five saved skills and emptied the item data. A separate XP roundtrip restored XP 150, five XP-derived points and all five saved main-tree skills after storage had cleared XP and allocation to zero. Command-granted extra points are not stored in the XP template.
- **Malevolent Manuscript: PASS.** The user confirmed repeated successful use; runtime traced entry and completion of respecialisation during this session.
- **Gracious Manuscript: functional PASS.** Runtime reached the held-item bypass for all eight classes; sampled main/Wizard/Ascendancy totals reflected the 99-point grants. The user accepted the result. A transient use-time lag spike was observed with the debugger active; its cause was not established.

These checks used Java 21, NeoForge 21.1.248 and Spell Engine 1.10.5 on the dedicated development server. Gradle build passed. Shared mechanics retain their earlier coverage; these are focused regressions after compatibility removal.

### Arcane Bolt packaged orientation regression

During the packaged class-cast test following the Arcane Bolt unlock instructions, the user reported a sideways projectile model. All four definitions (base, greater, lesser and expanding) still used ALONG_MOTION with the shared arcane_projectile model. Changed only their orientation to TOWARDS_MOTION, matching previously verified Arcane Slash/Rapidfire fixes. JSON parsing, Gradle build and IntelliJ build passed. New JAR is in build/libs with the same filename. The user subsequently confirmed Arcane Bolt now looks correct in the external modpack; individual variants and flight angles were not specified. No projectile velocity, damage or upgrade formulas changed.

## Remaining checks

See [release gates and lower-priority work](release-review.md#release-gates-and-lower-priority-work) for the release checklist, and the explicit limits in each entry above. The planned focused release smoke checks are complete at the scopes above, including packaged startup and two-player interactions. Rapidfire counter isolation was fixed and verified after restart. Optional deeper coverage and lower-priority code review remain separate from these completed checks.
