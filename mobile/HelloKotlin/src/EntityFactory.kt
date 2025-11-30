import java.util.*

enum class EntityType {
    HELP, EASY, MEDIUM, HARD;

    fun getFormattedName() =
        name.lowercase().replaceFirstChar { it.uppercase() }
}

object EntityFactory {
    fun create(type: EntityType): Entity {
        val id = UUID.randomUUID().toString()
        val name = when (type) {
            EntityType.HELP -> type.getFormattedName()
            EntityType.EASY -> type.name
            EntityType.MEDIUM -> type.getFormattedName()
            EntityType.HARD -> "Hard"
        }
        return when (type) {
            EntityType.EASY -> Entity.Easy(id, name)
            EntityType.MEDIUM -> Entity.Medium(id, name)
            EntityType.HARD -> Entity.Hard(id, name, multiplier = 2f)
            EntityType.HELP -> Entity.Help
        }
    }
}

sealed class Entity {
    object Help : Entity() {
        val name = "Help"
    }
    data class Easy(val id: String, val name: String) : Entity()
    data class Medium(val id: String, val name: String) : Entity()
    data class Hard(val id: String, val name: String, val multiplier: Float) : Entity()
}

// Extension function
fun Entity.Medium.printInfo() {
    println("Medium class: $id")
}

// Extension property
val Entity.Medium.info: String
    get() = "some info"

fun main() {
    val entity1 = Entity.Easy(id = "id", name = "name")
    val entity2 = EntityFactory.create(EntityType.MEDIUM)

    if (entity2 is Entity.Medium) {
        entity2.printInfo()
        println(entity2.info)
    }
}
