package sat.wooftown.solver.cdcl

import sat.wooftown.solver.CommonClauses
import sat.wooftown.solver.Picker
import sat.wooftown.util.*


class CDCLPicker(
    formula: Formula,
    private val size: Int,
) : Picker {

    private val commonClauses = CommonClauses(formula,size)

    // Список который показывает откуда следует назначение данного литера, null если было выбрано на рандом,
    // и показывает clause если по правилу чистых переменных
    private val parentClauses: MutableList<Clause?> = MutableList(size * 2) { null }

    // не сдалал мапу, потому что будем обрашаться по индексам
    private val solutions: MutableList<Literal?> = MutableList(size * 2) { null }

    // переделать ответ в map
    private fun valuesToMap(): Map<Variable, Boolean> {
        val assignments = mutableMapOf<Variable, Boolean>()
        for (it in 0 until currentIndex) {
            assignments[solutions[it]!!.variable] = solutions[it]!!.isPositive
        }
        return assignments
    }


    override fun getSolution(): Model {
        return Model(solutions.filterNotNull().toSet())
    }

    // уровни решения
    private val decisionLevels = MutableList(size * 2) { -1 }

    // текущий уровень мышления
    private var currentLevel = -1

    // слудующий индекс по которому мы будем брать переменную
    private var nextIndex = 0

    // индекс у переменной выбранной на данном этапе решения
    private var currentIndex = 0

    // итерируемся в цикле пока не чекнем все литералы
    fun canIterate() = currentIndex < size


    // Если в наших ответах два раза повторится переменная то надо бектрекнуться
    fun duplicated(): Boolean {
        val literal = solutions[currentIndex]!!

        for (it in currentIndex - 1 downTo 0) {
            val other = solutions[it]!!
            if (other.index == literal.index) {
                return true
            }
        }
        currentIndex++
        return false
    }


    // Проверка на конфликт
    override fun hasConflict(): Pair<Boolean, Clause?> {
        // литерал который сейчас определили
        val literal = solutions[currentIndex - 1]!!

        for (clause in commonClauses[literal]) {
            // если у нас нету неопределённых литералов в наборе и он неверен, то вернём неверный clause
            if (!analyze(clause)) {
                return true to clause
            }
        }

        return false to null
    }

    // смотрим Clause, true если всё ок
// false - есть конфликт
    private fun analyze(clause: Clause): Boolean {

        val assignments = valuesToMap()

        var uncheckedCount = 0
        var lastLiteral: Literal? = null

        for (literal in clause.literals) {
            // один литерал положителен и у нас всё круто
            if (assignments[literal.variable] == literal.isPositive) {
                return true
            }
            // смотрим сколько неопредленных литералов
            if (assignments[literal.variable] == null) {
                uncheckedCount++
                lastLiteral = literal
            }
        }
        // если все определены, то это провал и есть конфликт
        if (uncheckedCount == 0) {
            return false
        }
        // Если всего один литерал не определён, то можно просто исключить его по правилу чистой переменной
        if (uncheckedCount == 1 && lastLiteral != null) {
            addToValues(lastLiteral, clause)
        }

        return true
    }


    // найти неопределённый литерал
    fun getNextLiteral() {

        if (currentIndex >= nextIndex) {

            val assignments = valuesToMap()

            for (it in 0 until size) {
                val variable = Variable(it)

                if (assignments[variable] == null) {
                    addToValues(+variable)
                    return
                }
            }
            return
            //throw NoSuchElementException("No next elements") // ????
        }

    }


    // без clause - взятый через getNextLiteral()
// с clause - правилом чистой переменной
    private fun addToValues(literal: Literal, parent: Clause? = null) {

        // если уже добавлено
        solutions.subList(0, nextIndex).forEach {
            if (it == literal) {
                return
            }
        }

        solutions[nextIndex] = literal
        parentClauses[nextIndex] = parent
        // выходим на следующий уровень, если установка литерала не следует из какого-либо Clause
        // иначе остаёмся на том же уровне
        if (parent == null) {
            currentLevel++
        }
        decisionLevels[nextIndex] = currentLevel
        nextIndex++
    }


    // если без набора, то  это из-за того что взяли плохо и получили конфликт
// Если набор есть, то из него у нас получился конфликт
// true - всё ок, false- нельзя вернуться и ответа нету
    fun backtrack(conflictedClause: Clause? = null): Boolean {

        // Ищем откуда будет искать место бектрека
        var backtrackingIndex = 0
        while (decisionLevels[backtrackingIndex] != decisionLevels[currentIndex - 1]) {
            backtrackingIndex++
        }

        // мы в самом вверху импликационного графа и нам некуда подниматься
        if (decisionLevels[backtrackingIndex] == -1) {
            return false
        }

        // Если мы бектрекаемся по конфликтующему набору
        if (conflictedClause != null) {
            var backtrackTo = backtrackingIndex - 1

            while (backtrackTo >= 0) {

                for (literal in conflictedClause.literals) {
                    if (solutions[backtrackTo] == literal) {

                        backtrackingIndex = currentIndex - 1

                        while (decisionLevels[backtrackingIndex] != decisionLevels[backtrackTo]) {
                            backtrackingIndex--
                        }

                        backtrackingIndex++
                        break
                    }
                }
                backtrackTo--
            }
        }

        // меняем знак переменной
        solutions[backtrackingIndex] = solutions[backtrackingIndex]!!.inversion

        nextIndex = backtrackingIndex + 1

        currentIndex = backtrackingIndex

        // уменьшаем уровень решения
        if (backtrackingIndex == 0) {
            decisionLevels[backtrackingIndex] = -1
        } else {
            decisionLevels[backtrackingIndex] = decisionLevels[backtrackingIndex - 1]
        }

        currentLevel = decisionLevels[backtrackingIndex]


        return true
    }


    // Если у нас произошёл конфликт, то надо добавить к набору всех дизъюнт новый
// он будет состоять из самого конфликтного набора и набора из которого последовало присвоение переменной
    fun learn(conflictedClause: Clause?): Clause? {
        val literalParent = parentClauses[currentIndex - 1]
        val conflictedVariable = solutions[currentIndex - 1]!!.variable

        if (conflictedClause != null && literalParent != null) {

            val result = Clause()

            // откуда последовало чистая переменная и набор с конфликтом
            // если мы их соеденим то исключим установку по правилу !!!
            // раз по правилу чистой переменной она оказалось неверной, значит среди переменных в выражении откуда
            // последовало этого есть true и мы можем смерджить это
            (conflictedClause.literals + literalParent.literals)
                .filter { it.variable != conflictedVariable }
                .forEach {
                    result + it
                }

            commonClauses.updateClauses(result)


            return result
        }

        return null
    }


}