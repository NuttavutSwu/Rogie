# Three Kingdoms Deck Rogue (Android Prototype)

A beginner-friendly Kotlin + XML Android Studio prototype of a roguelike deck-building card game inspired by the Three Kingdoms era.

## Included Features

- Character selection at game start (with faction-linked heroes)
- Turn-based battle loop with energy, HP, block, and hand cards
- RecyclerView card hand UI
- Card types and effects (Attack, Defense, Skill, Strategy)
- Famous generals as high-impact cards:
  - Guan Yu
  - Zhang Fei
  - Zhuge Liang
  - Cao Cao
  - Lu Bu (rare)
- Roguelike progression:
  - Random encounters
  - Boss every 5th battle (Lu Bu, Dong Zhuo, Yuan Shao)
  - Post-battle reward: choose 1 of 3 cards
  - Extra random reward (gold or heal)
  - Optional relic chance: Imperial Seal (+1 max energy)
- Permadeath run reset when HP reaches 0
- Persistent meta progression:
  - Character unlocks after boss kills (e.g. Lu Bu, Cao Cao)
  - Skill points earned on death from run performance
  - Character-specific skill trees with permanent upgrades
  - Character selection screen (unlocked only)
  - Skill tree upgrade screen
  - Death summary screen with earned points
  - Slight enemy scaling based on upgrade strength

## Architecture

- MVVM-style UI state handling:
  - `BattleViewModel` for battle screen state
- Core classes:
  - `Card`
  - `Player`
  - `Enemy`
  - `DeckManager`
  - `BattleManager`
- Run/session data:
  - `GameSession`

## Project Structure

- `app/src/main/java/com/rogie/threekingdoms/model` - game data models
- `app/src/main/java/com/rogie/threekingdoms/game` - core game logic
- `app/src/main/java/com/rogie/threekingdoms/ui` - activities, adapter, view model
- `app/src/main/res/layout` - XML UI layouts
- `app/src/main/res/drawable/bg_scroll.xml` - card/scroll visual style

## Run

1. Open project in Android Studio.
2. Let Gradle sync.
3. Run on an emulator or Android device (API 24+).
