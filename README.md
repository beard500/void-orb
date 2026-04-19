# void-orb

A throwable custom item for a Fabric 1.21.11 Minecraft server. Looks like a
deep-black planet wrapped in a glowing Saturn-style ring — a "void orb."

**Key property:** this is a *server-side-only* mod. Players joining from a
vanilla Minecraft client don't install anything. The custom 3D model is
delivered automatically via a resource pack shipped by the server, and the
item polymorphs to a regular ender pearl for the client's view of the world.

## How it works

- **Item registration (server side)**: a real `void_orb:void_orb` item with
  its own custom behavior (throws a `VoidOrbEntity` on right-click).
- **Client view (via Polymer)**: the server rewrites every packet containing
  the void_orb to look like an ender pearl carrying an `item_model`
  component that points at `void_orb:void_orb`. Combined with the auto-shipped
  resource pack, the client renders our 3D model in inventory and in-hand.
- **Projectile in flight**: `VoidOrbEntity extends ThrownItemEntity implements
  PolymerEntity` — the server runs custom impact behavior, but the client is
  told the flying entity is a vanilla `EntityType.ENDER_PEARL`, so it renders
  the normal ender pearl sprite during flight (no client mod needed).
- **On impact**: purple portal + reverse-portal particles, amethyst-break
  sound, entity discarded. No teleport in v1; no damage.

See `src/main/java/com/voidorb/` for the three source files
(`VoidOrbMod`, `VoidOrbItem`, `VoidOrbEntity`).

## Server install

Requires a Fabric 1.21.11 server with Fabric Loader 0.19.2 or newer.

1. Download the three required mod JARs (all server-side):
   - [Fabric API 0.141.3+1.21.11](https://modrinth.com/mod/fabric-api/version/0.141.3+1.21.11)
   - [Polymer 0.15.2+1.21.11](https://modrinth.com/mod/polymer/version/0.15.2+1.21.11) — use the `polymer-bundled` JAR
   - `void-orb-0.1.0.jar` (this mod) — from `./gradlew build` or the
     Build workflow's uploaded artifact on GitHub Actions

2. Drop all three JARs into your server's `mods/` folder.

3. Restart the server. The log should show:

   ```
   [void_orb] registered item, entity, and resource pack
   ```

4. The server will now auto-send a resource pack to joining players.
   They'll see a "Server Resource Pack" prompt on join — they need to
   accept it to see the void_orb's 3D model (otherwise it renders as
   a plain ender pearl).

## In-game test

Give yourself one:

```
/give @s void_orb:void_orb
```

Checklist:
- [ ] Item appears in inventory as a black sphere with a glowing purple ring
- [ ] Held in hand (first/third person), the orb tilts toward the camera
- [ ] Right-click throws it — in flight it renders as a vanilla ender pearl
- [ ] On impact: purple portal particles, amethyst-break sound
- [ ] The orb does **not** teleport you (that's intentional for v1)

## Build from source

Requires Java 21 and Gradle 8.9+ installed locally (or just use the CI
artifact from GitHub Actions).

```
git clone https://github.com/beard500/void-orb.git
cd void-orb
python3 scripts/gen_textures.py    # regenerate PNGs from scripts (optional — checked in)
gradle build                        # or ./gradlew build if you generated the wrapper
```

Output: `build/libs/void-orb-0.1.0.jar`.

## Editing the model

The geometry is at `src/main/resources/assets/void_orb/models/item/void_orb.json`.
You can open that JSON directly in Blockbench (drop it on the start screen).
Edit, export over the same path, rebuild. See `blockbench/README.md`.

## Stack (all verified against primary sources)

| Component     | Version              |
| ------------- | -------------------- |
| Minecraft     | 1.21.11              |
| Fabric Loader | 0.19.2               |
| Loom          | 1.16-SNAPSHOT        |
| Fabric API    | 0.141.3+1.21.11      |
| Yarn mappings | 1.21.11+build.4      |
| Polymer       | 0.15.2+1.21.11       |
| Java          | 21                   |

1.21.11 is the last Minecraft release with Yarn mapping support.
Future mod updates should plan to migrate to Mojang mappings.

## License

MIT.
