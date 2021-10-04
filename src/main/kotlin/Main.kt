package calculator

import kotlin.system.exitProcess

fun invalidExpression() {
    println("Invalid expression")
    main()
}

fun invalidAssignment() {
    println("Invalid assignment")
    main()
}

val variables = mutableMapOf<String, String>()
val varRegex = mutableListOf<String>()

fun main() {
    variables.forEach { varRegex.add(it.key) }

    while (true) {
        var input = readLine()!!
        when {
            input.isEmpty() -> continue
            input == "/help" ->
                println("The program calculates the sum of numbers")
            input == "/exit" -> {
                println("Bye!")
                exitProcess(0)
            }
            input.matches(Regex("[-+]?\\d+")) -> println(input.filter { it != '+' })

            input.matches(Regex("/.*")) -> println("Unknown command")

            input.contains('=') -> {
                if (input.count { it == '=' } != 1) println("Invalid assignment")
                input = input.filter { it != ' ' }
                val (key, value) = input.split('=')
                when {
                    !key.matches(LATIN) -> println("Invalid identifier")
                    value.matches(LATIN) -> if (value in variables.keys) {
                        variables[key] = variables.getValue(value)
                    } else {
                        invalidAssignment()
                    }
                    else -> if (value.matches(NUMBER)) {
                        variables[key] = value
                    } else {
                        invalidAssignment()
                    }
                }
                main()
            }

            input.matches(LATIN) -> println(if (input in varRegex) variables[input] else "Unknown variable")

            else -> {
                val line = input.split(" ").filter { it != "" }.toMutableList()
                line.filter { it.contains(Regex("[()]+")) }.forEach { element ->

                    val indexOfElement = line.indexOf(element)
                    line.removeAt(indexOfElement)

                    val chars = mutableListOf<String>()
                    val digits = mutableListOf<String>()

                    element.toCharArray().map {
                        if (it.toString().matches(Regex("[()]"))) {
                            chars.add(it.toString())
                        } else {
                            digits.add(it.toString())
                        }}
                    val number = digits.joinToString("")

                    if (element[0].isDigit() || element[0] == '-') {
                        line.addAll(indexOfElement, chars)
                        line.add(indexOfElement, number)
                    } else {
                        line.add(indexOfElement, number)
                        line.addAll(indexOfElement, chars)
                    }
                }
                line.forEachIndexed { index, it ->
                    when {
                        variables.keys.contains(it) -> line[index] = variables.getValue(it)
                        it.matches(PLUS) || (it.matches(MINUS) && it.count { it == '-' } % 2 == 0 ) -> line[index] = "+"
                        it.matches(MINUS) -> line[index] = "-"
                    } }

                calculator(line)
            }
        }
    }
}