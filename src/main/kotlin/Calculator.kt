package calculator

import java.math.BigInteger

val LATIN = Regex("[a-zA-Z]+")
val NUMBER = Regex("-?\\d+")
val PLUS = Regex("[+]+")
val MINUS = Regex("[-]+")
val MULTIPLY = Regex("[*]")
val DIVIDE = Regex("[/]")

fun doMath(a: BigInteger, b: BigInteger, exp: String): BigInteger {
    return when {
        exp.matches(PLUS) -> a + b
        exp.matches(MINUS) -> a - b
        exp.matches(MULTIPLY) -> a * b
        exp.matches(DIVIDE) -> if (b == BigInteger.ZERO) throw Exception("Division by zero") else a / b
        else -> throw Exception("Something went wrong")
    }
}

fun calculator(line: MutableList<String>) {

    try {

        val postfix = mutableListOf<String>()
        val stack = mutableListOf<String>()

        line.forEach {

            when {
                it.matches(NUMBER) -> {
                    postfix.add(it)
                }
                stack.isEmpty() || stack.last() == "(" -> {
                    stack.add(it)

                }
                it == "*" || it == "/" -> {
                    when {
                        stack.last() == "+" || stack.last() == "-" -> stack.add(it)
                        stack.last() == "*" || stack.last() == "/" -> {
                            while (stack.last() != "+" || stack.last() == "-") {
                                postfix.add(stack.removeAt(stack.lastIndex))
                                if (stack.isEmpty() || stack.last() != "(") break
                            }
                            stack.add(it)
                        }
                    }

                }
                it == "+" || it == "-" -> {
                    while (stack.last() != "(") {
                        postfix.add(stack.removeAt(stack.lastIndex))
                        if (stack.isEmpty()) break
                    }
                    stack.add(it)

                }

                it == "(" -> {
                    stack.add(it)

                }
                it == ")" -> {
                    while (stack.last() != "(") {
                        postfix.add(stack.removeAt(stack.lastIndex))
                    }
                    stack.removeAt(stack.lastIndex)
                }
            }
        }
        postfix.addAll(stack.reversed())
        stack.clear()

        var result = mutableListOf<String>()

        postfix.forEach {
            when {
                it.matches(NUMBER) -> {
                    result.add(it)
                }
                else -> {
                    val a = result[result.lastIndex - 1]
                    val b = result.last()
                    result = result.dropLast(2).toMutableList()
                    result.add(doMath(a.toBigInteger(), b.toBigInteger(), it).toString())
                }
            }
        }
        println(result[0])
    } catch (e: Exception) {
        invalidExpression()
    }
}