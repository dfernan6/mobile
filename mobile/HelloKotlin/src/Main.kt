fun sayHello(greeting:String, vararg itemsToGreet:String){
    itemsToGreet.forEach { itemsToGreet ->
    println("$greeting $itemsToGreet")
    }
}

fun main() {
    val person = Person()
    person.printInfo()
}