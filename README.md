# cobblemon-warp

Fabric Minecraft **1.21.1** addon that adds a **Warp Move** option to Cobblemon's Pokémon interaction wheel.

## Selected versions

| Component | Version |
|---|---|
| Minecraft | `1.21.1` |
| Java | `21` |
| Gradle Wrapper | `8.10.2` |
| Fabric Loom | `1.9-SNAPSHOT` |
| Yarn mappings | `1.21.1+build.3` |
| Fabric Loader | `0.16.10` |
| Fabric API | `0.116.5+1.21.1` |
| Architectury API (Fabric) | `13.0.6` |
| Cobblemon (CurseMaven project `687131`) | file id `8976513` (pinned) |

> Cobblemon currently does not publish a canonical Maven coordinate on Maven Central/Fabric Maven for all builds, so this project pins the CurseMaven artifact by file id.

## Feature summary

- Hooks Cobblemon's public `POKEMON_INTERACTION_GUI_CREATION` event.
- Adds a **Warp Move** interaction-wheel button for the currently selected, active party Pokémon (using the exact Pokémon UUID provided by the wheel event).
- Opens a custom screen titled:
  - `"[Pokémon Name] - Teleport Channeling"`
- Provides integer-only X/Y/Z coordinate text fields + **Confirm Warp** button with friendly validation errors.
- Sends a versioned C2S payload with Pokémon UUID + target coordinates.
- Server validates:
  - protocol version
  - Pokémon exists
  - Pokémon belongs to sender party and is in same dimension
  - proximity to player
  - build/height bounds and safe destination checks
- Teleports player and Pokémon server-side only.
- Plays portal/witch particles and enderman/conduit sounds at source + destination for both entities (server broadcast).

## Build

```bash
./gradlew clean build
```

Output artifact:

- `build/libs/cobblemon-warp-0.1.0.jar`

## Install

1. Install Java 21.
2. Install Fabric Loader for Minecraft 1.21.1.
3. Put in `mods/`:
   - `cobblemon-warp-0.1.0.jar`
   - matching Cobblemon Fabric jar for 1.21.1 (the pinned build above)
   - Fabric API
4. Launch Minecraft.

## Sinytra Connector / NeoForge notes

This mod is implemented with Fabric APIs and Cobblemon's interaction-wheel API. It is intended to run through **Sinytra Connector** where connector compatibility allows.

Known caveats:

- Cobblemon + Fabric networking/event internals are version-sensitive; Connector behavior can vary by exact Cobblemon/Fabric/NeoForge build combination.
- If interaction-wheel callbacks fail under Connector, verify matching versions first and test on pure Fabric to isolate Connector-specific behavior.

## Troubleshooting

- If **Warp Move** does not appear:
  - ensure you're Shift + Right-Clicking an active party Pokémon
  - verify Cobblemon/Fabric API versions match this README
- If warp does nothing:
  - destination may be invalid/unsafe/out of bounds
  - Pokémon may no longer be in range or in your active party
- If dependency resolution fails:
  - check availability of the pinned Cobblemon CurseMaven file id
