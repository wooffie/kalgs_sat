package sat.wooftown.io


import sat.wooftown.util.Clause
import sat.wooftown.util.Formula
import sat.wooftown.util.Variable
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import kotlin.math.abs

/**
 * Класс который отвечает за чтение файлов в формате DIMACS (.cnf)
 * и перевод их в структуры данных представленных в проекте
 * @param input - путь к файлу
 */
class Parser(
    private val input: String,
) {

    constructor(file: File) : this(file.toString())

    /**
     * Функция для возвращения формулы и количества переменных
     * @return входной набор и сколько переменных в нём
     */
    fun parse(): Pair<Formula, Int> {
        val formula = Formula()
        val reader = CNFReader(File(input))


        for (j in 0 until reader.numberOfClauses) {
            val clause = Clause()
            for (dnf in reader.readDNF()) {
                if (dnf == 0) {
                    formula * clause
                } else {
                    // ugly maybe reformat later TODO()
                    if (dnf > 0) {
                        clause + +Variable(abs(dnf) - 1)
                    } else {
                        clause + -Variable(abs(dnf) - 1)
                    }
                }
            }
        }
        reader.close()
        return formula to reader.numberOfVariables
    }


    /**
     * Класс-помощник, наследующий буфферного читателя файла
     */
    private class CNFReader(file: File) : BufferedReader(FileReader(file)) {

        var numberOfVariables: Int
        var numberOfClauses: Int

        /*
        ищем первую строчку выражения для определения сколько у нас строк и переиенных
         */
        init {
            val properties = Regex("p cnf (\\d+) (\\d+)").find(readLine())
                ?: throw IllegalArgumentException("Wrong format for .cnf file")
            numberOfVariables = properties.groupValues[1].toInt()
            numberOfClauses = properties.groupValues[2].toInt()
        }

        /**
         * Прочитать строчку, комментарии не учитываются
         */
        override fun readLine(): String {
            while (true) {
                val nextLine = super.readLine() ?: throw IOException("Wrong format for .cnf file")

                // строчка-коммент или пустая
                if (nextLine.startsWith("c") || nextLine.isEmpty()) {
                    continue
                }
                return nextLine
            }
        }

        /*
            return one clause
            variables can be separated with any number of whitespace characters -> regex for it
         */
        fun readDNF(): List<Int> {
            return readLine().trim().split(Regex("\\s+")).map { it.toInt() }
        }
    }


}

