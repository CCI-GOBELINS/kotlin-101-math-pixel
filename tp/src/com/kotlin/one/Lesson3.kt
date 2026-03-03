package com.android.one

// Battle Arena (prototype console)
// Two players each create a team of 3 characters from 4 types (Warrior, Magus, Colossus, Dwarf).
// Each character has a unique name, a type (unique per team), HP and a weapon.
// Players take turns: choose a character, then attack an enemy or heal an ally (Magus only).
// A character at 0 HP dies. When all characters of a team are dead, the other player wins.
// At the end: display winner, number of turns, and status of each character.
//
// Concepts: Class, Inheritance, Interface, Abstract class, Polymorphism, Encapsulation, Composition.
//
// Types:
// | Type     | HP         | Weapon power |
// | Warrior  | Medium     | Medium       |
// | Magus    | High       | Low (healer) |
// | Colossus | Very high  | Medium       |
// | Dwarf    | Low        | Very high    |

// Weapon class (composition)
data class Weapon(val name: String, val power: Int)

// Interfaces
interface Attacker {
    fun attack(target: Character)
}

interface Healer {
    fun heal(target: Character)
}

// Abstract character class
abstract class Character(
    val name: String,
    val type: String,
    private var hp: Int,
    val weapon: Weapon
) {
    fun getHp(): Int = hp

    fun isAlive(): Boolean = hp > 0

    fun takeDamage(amount: Int) {
        hp = maxOf(0, hp - amount)
    }

    fun receiveHeal(amount: Int) {
        hp += amount
    }

    abstract fun action(allies: List<Character>, enemies: List<Character>)

    override fun toString(): String = "$name ($type) - HP: $hp - Weapon: ${weapon.name} (${weapon.power})"
}

// Warrior: balanced attacker
class Warrior(name: String) : Character(name, "Warrior", 100, Weapon("Sword", 25)), Attacker {
    override fun attack(target: Character) {
        println("$name attacks ${target.name} with ${weapon.name} for ${weapon.power} damage!")
        target.takeDamage(weapon.power)
    }

    override fun action(allies: List<Character>, enemies: List<Character>) {
        val target = chooseEnemy(enemies)
        if (target != null) attack(target)
    }
}

// Magus: can heal allies, lower attack
class Magus(name: String) : Character(name, "Magus", 120, Weapon("Staff", 15)), Attacker, Healer {
    override fun attack(target: Character) {
        println("$name casts a spell on ${target.name} for ${weapon.power} damage!")
        target.takeDamage(weapon.power)
    }

    override fun heal(target: Character) {
        val healAmount = 20
        println("$name heals ${target.name} for $healAmount HP!")
        target.receiveHeal(healAmount)
    }

    override fun action(allies: List<Character>, enemies: List<Character>) {
        println("Choose action for $name: 1) Attack  2) Heal")
        val choice = readln().trim()
        if (choice == "2") {
            val target = chooseAlly(allies)
            if (target != null) heal(target)
        } else {
            val target = chooseEnemy(enemies)
            if (target != null) attack(target)
        }
    }
}

// Colossus: very resistant, medium attack
class Colossus(name: String) : Character(name, "Colossus", 180, Weapon("Hammer", 20)), Attacker {
    override fun attack(target: Character) {
        println("$name smashes ${target.name} with ${weapon.name} for ${weapon.power} damage!")
        target.takeDamage(weapon.power)
    }

    override fun action(allies: List<Character>, enemies: List<Character>) {
        val target = chooseEnemy(enemies)
        if (target != null) attack(target)
    }
}

// Dwarf: fragile but very strong
class Dwarf(name: String) : Character(name, "Dwarf", 70, Weapon("Axe", 35)), Attacker {
    override fun attack(target: Character) {
        println("$name strikes ${target.name} with ${weapon.name} for ${weapon.power} damage!")
        target.takeDamage(weapon.power)
    }

    override fun action(allies: List<Character>, enemies: List<Character>) {
        val target = chooseEnemy(enemies)
        if (target != null) attack(target)
    }
}

// Player class
class Player(val playerName: String) {
    val team = mutableListOf<Character>()

    fun allDead(): Boolean = team.none { it.isAlive() }

    fun aliveCharacters(): List<Character> = team.filter { it.isAlive() }
}

// Helper to choose an enemy target
fun chooseEnemy(enemies: List<Character>): Character? {
    val alive = enemies.filter { it.isAlive() }
    if (alive.isEmpty()) return null
    println("Choose a target:")
    alive.forEachIndexed { i, c -> println("  ${i + 1}) $c") }
    val idx = readln().trim().toIntOrNull()?.minus(1) ?: 0
    return alive.getOrElse(idx) { alive[0] }
}

// Helper to choose an ally target
fun chooseAlly(allies: List<Character>): Character? {
    val alive = allies.filter { it.isAlive() }
    if (alive.isEmpty()) return null
    println("Choose an ally to heal:")
    alive.forEachIndexed { i, c -> println("  ${i + 1}) $c") }
    val idx = readln().trim().toIntOrNull()?.minus(1) ?: 0
    return alive.getOrElse(idx) { alive[0] }
}

// Create a character by type
fun createCharacter(type: String, name: String): Character {
    return when (type.lowercase()) {
        "warrior" -> Warrior(name)
        "magus" -> Magus(name)
        "colossus" -> Colossus(name)
        "dwarf" -> Dwarf(name)
        else -> throw IllegalArgumentException("Unknown type: $type")
    }
}

// Build a team for a player
fun buildTeam(player: Player, usedNames: MutableSet<String>) {
    val usedTypes = mutableSetOf<String>()
    val availableTypes = listOf("Warrior", "Magus", "Colossus", "Dwarf")

    println("\n--- ${player.playerName}, create your team of 3 characters ---")
    for (i in 1..3) {
        println("\nCharacter #$i")
        println("Available types: ${availableTypes.filter { it.lowercase() !in usedTypes }}")

        var type: String
        while (true) {
            print("Enter type: ")
            type = readln().trim()
            if (type.lowercase() in usedTypes) {
                println("Type already used in your team. Choose another.")
            } else if (type.lowercase() !in availableTypes.map { it.lowercase() }) {
                println("Invalid type. Choose from: $availableTypes")
            } else break
        }
        usedTypes.add(type.lowercase())

        var name: String
        while (true) {
            print("Enter name: ")
            name = readln().trim()
            if (name.lowercase() in usedNames.map { it.lowercase() }) {
                println("Name already taken. Choose another.")
            } else break
        }
        usedNames.add(name)

        player.team.add(createCharacter(type, name))
        println("Created: ${player.team.last()}")
    }
}

fun main() {
    println("⚔️  Welcome to Battle Arena! ⚔️\n")

    val player1 = Player("Player 1")
    val player2 = Player("Player 2")
    val usedNames = mutableSetOf<String>()

    // Team creation
    buildTeam(player1, usedNames)
    buildTeam(player2, usedNames)

    // Combat
    println("\n--- ⚔️  COMBAT BEGINS ⚔️  ---\n")
    var turn = 0
    var currentPlayer = player1
    var opponent = player2

    while (!player1.allDead() && !player2.allDead()) {
        turn++
        println("\n=== TURN $turn — ${currentPlayer.playerName}'s turn ===")

        val alive = currentPlayer.aliveCharacters()
        println("Choose a character:")
        alive.forEachIndexed { i, c -> println("  ${i + 1}) $c") }
        val idx = readln().trim().toIntOrNull()?.minus(1) ?: 0
        val chosen = alive.getOrElse(idx) { alive[0] }

        chosen.action(currentPlayer.team, opponent.team)

        // Check for dead characters
        opponent.team.filter { !it.isAlive() }.forEach {
            println("💀 ${it.name} has been defeated!")
        }

        // Swap turns
        val temp = currentPlayer
        currentPlayer = opponent
        opponent = temp
    }

    // End of game
    val winner = if (player1.allDead()) player2 else player1
    println("\n=== 🏆 GAME OVER ===")
    println("Winner: ${winner.playerName}!")
    println("Total turns: $turn")
    println("\nFinal status:")
    println("-- ${player1.playerName} --")
    player1.team.forEach { println("  $it ${if (it.isAlive()) "✅" else "💀"}") }
    println("-- ${player2.playerName} --")
    player2.team.forEach { println("  $it ${if (it.isAlive()) "✅" else "💀"}") }
}
