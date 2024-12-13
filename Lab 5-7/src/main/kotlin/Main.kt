import com.sun.jdi.IntegerType
import java.io.File

fun printTree(tree: List<ParsingTreeRow>, level: Int) {
    for (i in tree.size - 1 downTo 0) {
        val children = tree.filter { it.parent == i }.sortedBy { it.rightSibling == -1 }
        for (j in 0 until tree.size - i + 1)
            print("   ")
        for (child in children)
            print("${child.info}    ")
        println()



    }
}

fun main() {
    val g = Grammar("src/main/resources/g2.in")
    println("Nonterminals: ${g.nonTerminals}")
    println("Terminals: ${g.terminals}")
    println("Starting symbol: ${g.startingSymbol}")
    println("Production set: ${g.productionSet}")
    println(if (g.checkCFG()) "It is a cfg" else "It is not a cfg")

    val lr = LR(g)
    val canonicalCollection = lr.canonicalCollection()
    println("States")
    for (i in canonicalCollection.states.indices)
        println("$i: ${canonicalCollection.states[i]}")
    println("State transitions")
    for (pair in canonicalCollection.adjacencyList)
        println("${pair.key} -> ${pair.value}")

    println(g.getEnrichedGrammar().productionSet.getOrderedProductions())
    println(lr.getParsingTable())


    try {
        val input = File("src/main/resources/input.txt").readLines()
            .flatMap { it.split("\\s+".toRegex()) }
            .filter { it.isNotBlank() }

//        val parseTree = lr.parse(listOf("a", "b", "b", "c")).sortedBy { it.index }
        val parseTree = lr.parse(input).sortedBy { it.index }
        for (row in parseTree)
            println("Row ${row.index}: symbol ${row.info}, parent: ${row.parent}, rightSibling: ${row.rightSibling}")
        println("        ${parseTree.last().info}  ")
        printTree(parseTree, 0)
    } catch (e: Exception) {
        println(e.message)
    }
}
