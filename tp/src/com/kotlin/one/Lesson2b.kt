// Exercise 1 — Immutable List
// Create an immutable list of 5 integers and return it.
fun ex1CreateImmutableList(): List<Int> {
    return listOf(1, 2, 3, 4, 5)
}

// Exercise 2 — Mutable List
// Create a mutable list of 3 strings, then add one more element to it. Return the final list.
fun ex2CreateMutableList(): MutableList<String> {
    val list = mutableListOf("apple", "banana", "cherry")
    list.add("date")
    return list
}

// Exercise 3 — Filter Even
// Create a list of numbers from 1 to 10. Filter only the even numbers and return the result.
fun ex3FilterEvenNumbers(): List<Int> {
    return (1..10).filter { it % 2 == 0 }
}

// Exercise 4 — Filter and Map
// Given a list of ages: 1) Keep only ages >= 18, 2) Transform each into "Adult: X", 3) Return the list.
fun ex4FilterAndMapAges(): List<String> {
    val ages = listOf(12, 25, 17, 30, 15, 42)
    return ages.filter { it >= 18 }.map { "Adult: $it" }
}

// Exercise 5 — Flatten Nested Lists
// Create a nested list such as [[1, 2], [3, 4], [5]] and flatten it. Return the flattened list.
fun ex5FlattenList(): List<Int> {
    val nested = listOf(listOf(1, 2), listOf(3, 4), listOf(5))
    return nested.flatten()
}

// Exercise 6 — FlatMap
// Create a list of phrases. Use flatMap and split(" ") to extract and return all words.
fun ex6FlatMapWords(): List<String> {
    val phrases = listOf("Kotlin is fun", "I love lists")
    return phrases.flatMap { it.split(" ") }
}

// Exercise 7 — Eager Processing
// Create a list from 1..1_000_000. Filter divisible by 3, map to squares, take first 5. Measure time.
fun ex7EagerProcessing(): List<Int> {
    val start = System.currentTimeMillis()
    val result = (1..1_000_000)
        .filter { it % 3 == 0 }
        .map { it * it }
        .take(5)
    val end = System.currentTimeMillis()
    println("Eager time: ${end - start} ms")
    return result
}

// Exercise 8 — Lazy Processing
// Repeat Exercise 7 but use .asSequence() for lazy evaluation. Return the first 5 results. Measure time.
fun ex8LazyProcessing(): List<Int> {
    val start = System.currentTimeMillis()
    val result = (1..1_000_000)
        .asSequence()
        .filter { it % 3 == 0 }
        .map { it * it }
        .take(5)
        .toList()
    val end = System.currentTimeMillis()
    println("Lazy time: ${end - start} ms")
    return result
}

// Exercise 9 — Chain multiple operations
// Given a list of names, filter those starting with 'A', convert to uppercase, sort alphabetically, return.
fun ex9FilterAndSortNames(): List<String> {
    val names = listOf("Alice", "Bob", "Anna", "Charlie", "Aaron", "Diana")
    return names.filter { it.startsWith("A") }.map { it.uppercase() }.sorted()
}

fun verify(name: String, block: () -> Boolean) {
    try {
        check(block()) { "❌ Test failed: $name" }
        println("✅ $name")
    } catch (e: Throwable) {
        println("❌ $name → ${e.message}")
    }
}

fun main() {
    println("🔍 Running List Processing Tests...\n")

    verify("ex1 - Immutable list of 5 ints") { ex1CreateImmutableList().size == 5 }
    verify("ex2 - Mutable list has 4 elements") { ex2CreateMutableList().size == 4 }
    verify("ex3 - Even numbers from 1..10") { ex3FilterEvenNumbers() == listOf(2, 4, 6, 8, 10) }
    verify("ex4 - Filter and map ages") { ex4FilterAndMapAges() == listOf("Adult: 25", "Adult: 30", "Adult: 42") }
    verify("ex5 - Flatten nested lists") { ex5FlattenList() == listOf(1, 2, 3, 4, 5) }
    verify("ex6 - FlatMap words") { ex6FlatMapWords() == listOf("Kotlin", "is", "fun", "I", "love", "lists") }
    verify("ex7 - Eager processing first 5") { ex7EagerProcessing().size == 5 }
    verify("ex8 - Lazy processing first 5") { ex8LazyProcessing().size == 5 }
    verify("ex9 - Filter and sort names") { ex9FilterAndSortNames() == listOf("AARON", "ALICE", "ANNA") }

    println("\n🎯 All list processing exercises done!")
}
