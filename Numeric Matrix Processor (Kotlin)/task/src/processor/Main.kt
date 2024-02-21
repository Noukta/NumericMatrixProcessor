package processor

import java.util.Scanner
import kotlin.math.pow

object MatrixProcessor {
    private val scanner = Scanner(System.`in`)

    fun read(size: Pair<Int, Int>): MutableList<MutableList<Double>> {
        val matrix: MutableList<MutableList<Double>> = mutableListOf()
        for (i in 0 until size.first) {
            matrix.add(mutableListOf())
            print("> ")
            repeat(size.second) {
                matrix[i].add(scanner.nextDouble())
            }
        }
        return matrix
    }

    fun print(matrix: MutableList<MutableList<Double>>) {
        matrix.forEach {
            println(it.joinToString(" "))
        }
    }

    fun add(
        matrixA: MutableList<MutableList<Double>>,
        matrixB: MutableList<MutableList<Double>>
    ): MutableList<MutableList<Double>>? {
        if (matrixA.size != matrixB.size || matrixA.first().size != matrixB.first().size) {
            return null
        } else {
            val sum: MutableList<MutableList<Double>> = mutableListOf()
            for (i in 0 until matrixA.size) {
                sum.add(mutableListOf())
                for (j in 0 until matrixA.first().size) {
                    sum[i].add(matrixA[i][j] + matrixB[i][j])
                }
            }
            return sum
        }
    }

    fun multiplyByNumber(matrix: MutableList<MutableList<Double>>, number: Double): MutableList<MutableList<Double>> {
        val multiplied: MutableList<MutableList<Double>> = mutableListOf()
        for (i in 0 until matrix.size) {
            multiplied.add(mutableListOf())
            for (j in 0 until matrix.first().size) {
                multiplied[i].add(matrix[i][j] * number)
            }
        }
        return multiplied
    }

    fun multiply(
        matrixA: MutableList<MutableList<Double>>,
        matrixB: MutableList<MutableList<Double>>
    ): MutableList<MutableList<Double>>? {
        if (matrixA.first().size != matrixB.size) {
            return null
        } else {
            val product: MutableList<MutableList<Double>> = mutableListOf()
            for (i in 0 until matrixA.size) {
                product.add(mutableListOf())
                for (k in 0 until matrixB.first().size) {
                    var p = 0.0
                    matrixA[i].forEachIndexed { j, a ->
                        p += a * matrixB[j][k]
                    }
                    product[i].add(p)
                }
            }
            return product
        }
    }

    fun transpose(matrix: MutableList<MutableList<Double>>, transposition: Int): MutableList<MutableList<Double>>? {
        var transpose: MutableList<MutableList<Double>> = mutableListOf()
        when (transposition) {
            1 -> {
                for (i in 0 until matrix.first().size) {
                    transpose.add(mutableListOf())
                    for (j in 0 until matrix.size) {
                        transpose[i].add(matrix[j][i])
                    }
                }
            }

            2 -> {
                for (i in matrix.first().size - 1 downTo 0) {
                    transpose.add(mutableListOf())
                    for (j in matrix.size - 1 downTo 0) {
                        transpose.last().add(matrix[j][i])
                    }
                }
            }

            3 -> {
                for (i in 0 until matrix.size) {
                    transpose.add(matrix[i].reversed().toMutableList())
                }
            }

            4 -> {
                transpose = matrix.reversed().toMutableList()
            }

            else -> return null
        }
        return transpose
    }

    fun determinant(matrix: MutableList<MutableList<Double>>): Double {
        if (matrix.size == 1)
            return matrix[0][0]
        var det = 0.0
        var cofactor = 1
        matrix.first().forEachIndexed { i, e ->
            val subMatrix = subMatrix(matrix, column = i)
            det += cofactor * e * determinant(subMatrix)
            cofactor *= -1
        }
        return det
    }

    private fun subMatrix(
        matrix: MutableList<MutableList<Double>>,
        row: Int = 0,
        column: Int
    ): MutableList<MutableList<Double>> {
        val subMatrix: MutableList<MutableList<Double>> = mutableListOf()
        for (rowIndex in matrix.indices) {
            if (rowIndex != row) {
                val subRow = matrix[rowIndex].filterIndexed { index, _ -> index != column }
                subMatrix.add(subRow.toMutableList())
            }
        }
        return subMatrix
    }

    private fun adjoint(matrix: MutableList<MutableList<Double>>): MutableList<MutableList<Double>>? {
        return transpose(cofactor(matrix), 1)
    }

    private fun cofactor(matrix: MutableList<MutableList<Double>>): MutableList<MutableList<Double>> {
        val cofactor: MutableList<MutableList<Double>> = mutableListOf()
        for (r in matrix.indices) {
            cofactor.add(mutableListOf())
            for (c in matrix[r].indices) {
                cofactor[r].add(determinant(subMatrix(matrix, r, c)) * (-1.0).pow(r+c))
            }
        }
        return cofactor
    }

    fun inverse(matrix: MutableList<MutableList<Double>>): MutableList<MutableList<Double>>? {
        if (matrix.size != matrix.first().size) return null
        val det = determinant(matrix)
        if (det == 0.0) return null
        return adjoint(matrix)?.let { multiplyByNumber(it, 1.0 / det) }
    }
}


fun main() {
    val scanner = Scanner(System.`in`)
    var option: Int
    do {
        print(
            "1. Add matrices\n" +
                    "2. Multiply matrix by a constant\n" +
                    "3. Multiply matrices\n" +
                    "4. Transpose matrix\n" +
                    "5. Calculate a determinant\n" +
                    "6. Inverse matrix\n" +
                    "0. Exit\n" +
                    "Your choice: > "
        )
        option = scanner.nextInt()
        var result: MutableList<MutableList<Double>>? = null
        var det: Double? = null
        when (option) {
            1 -> {
                print("Enter size of first matrix: > ")
                val sizeA = Pair(scanner.nextInt(), scanner.nextInt())
                println("Enter first matrix:")
                val matrixA = MatrixProcessor.read(sizeA)
                print("Enter size of second matrix: > ")
                val sizeB = Pair(scanner.nextInt(), scanner.nextInt())
                println("Enter second matrix:")
                val matrixB = MatrixProcessor.read(sizeB)
                result = MatrixProcessor.add(matrixA, matrixB)
            }

            2 -> {
                print("Enter size of matrix: > ")
                val size = Pair(scanner.nextInt(), scanner.nextInt())
                println("Enter matrix:")
                val matrix = MatrixProcessor.read(size)
                print("Enter constant: > ")
                val number = scanner.nextDouble()
                result = MatrixProcessor.multiplyByNumber(matrix, number)
            }

            3 -> {
                print("Enter size of first matrix: > ")
                val sizeA = Pair(scanner.nextInt(), scanner.nextInt())
                println("Enter first matrix:")
                val matrixA = MatrixProcessor.read(sizeA)
                print("Enter size of second matrix: > ")
                val sizeB = Pair(scanner.nextInt(), scanner.nextInt())
                println("Enter second matrix:")
                val matrixB = MatrixProcessor.read(sizeB)
                result = MatrixProcessor.multiply(matrixA, matrixB)
            }

            4 -> {
                print(
                    "1. Main diagonal\n" +
                            "2. Side diagonal\n" +
                            "3. Vertical line\n" +
                            "4. Horizontal line\n" +
                            "Your choice: > "
                )
                val transposition = scanner.nextInt()
                print("Enter size of matrix: > ")
                val size = Pair(scanner.nextInt(), scanner.nextInt())
                println("Enter matrix:")
                val matrix = MatrixProcessor.read(size)
                result = MatrixProcessor.transpose(matrix, transposition)
            }

            5 -> {
                try {
                    print("Enter size of matrix: > ")
                    val size = Pair(scanner.nextInt(), scanner.nextInt())
                    if (size.first != size.second) throw ArithmeticException("The matrix should be square!")
                    println("Enter matrix:")
                    val matrix = MatrixProcessor.read(size)
                    det = MatrixProcessor.determinant(matrix)
                } catch (e: ArithmeticException) {
                    println(e.message)
                }

            }

            6 -> {
                try {
                    print("Enter size of matrix: > ")
                    val size = Pair(scanner.nextInt(), scanner.nextInt())
                    if (size.first != size.second) throw ArithmeticException("The matrix should be square!")
                    println("Enter matrix:")
                    val matrix = MatrixProcessor.read(size)
                    result = MatrixProcessor.inverse(matrix)
                } catch (e: ArithmeticException) {
                    println(e.message)
                }
            }
        }
        if (option == 5) {
            if (det != null) {
                println("The result is:\n$det")

            } else {
                println("The operation cannot be performed.")
            }
            println()
        } else if (option != 0) {
            if (result != null) {
                println("The result is:")
                MatrixProcessor.print(result)

            } else {
                println(
                    if (option == 6) "This matrix doesn't have an inverse."
                    else "The operation cannot be performed."
                )
            }
            println()
        }
    } while (option != 0)
}
