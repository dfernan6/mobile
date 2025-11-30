// Demonstrates functional programming style in Kotlin

fun main() {
    val numbers = listOf(1, 2, 3, 4, 5)

    // map
    val doubled = numbers.map { it * 2 }
    println("Doubled: $doubled")

    // filter
    val evens = numbers.filter { it % 2 == 0 }
    println("Even numbers: $evens")

    // reduce
    val sum = numbers.reduce { acc, n -> acc + n }
    println("Sum: $sum")

    // forEach
    numbers.forEach { println("Number: $it") }

    // chaining
    val result = numbers
        .map { it * 3 }
        .filter { it > 5 }
        .reduce { acc, n -> acc + n }
    println("Chained result: $result")
}
