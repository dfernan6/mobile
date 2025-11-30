// Demonstrates higher-order functions in Kotlin

fun calculate(x: Int, y: Int, operation: (Int, Int) -> Int): Int {
    return operation(x, y)
}

fun sum(a: Int, b: Int) = a + b
fun multiply(a: Int, b: Int) = a * b

fun main() {
    val result1 = calculate(5, 3, ::sum)
    val result2 = calculate(5, 3, ::multiply)

    println("Sum result: $result1")
    println("Multiply result: $result2")

    // Using lambda directly
    val result3 = calculate(10, 2) { a, b -> a - b }
    println("Lambda subtraction result: $result3")
}
