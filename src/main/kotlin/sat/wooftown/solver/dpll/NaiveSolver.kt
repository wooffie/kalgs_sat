package sat.wooftown.solver.dpll

import sat.wooftown.solver.Solver
import sat.wooftown.util.Formula
import sat.wooftown.util.Literal
import sat.wooftown.util.Model

// DPLL Solver

class NaiveSolver(formulaToNumber: Pair<Formula, Int>) : Solver {

    // Объект который будет отвечать за все данные, а класс солвера отвечает за алгоритмы
    override val picker: NaivePicker = NaivePicker(formulaToNumber.first, formulaToNumber.second)

    // решить примерчик xD
    // Если null - решения нет, и выражение UNSAT
    override fun solve(): Model? {

        var next = picker.getNextLiteral()

        // цикл будет работать пока не перебраны все варианты или найдено решение
        while (next != null) {

            // Добавляем в решение
            picker[next] = next.isPositive

            when {
                // возник конфликт, возвращаемся
                hasConflicts(next) -> picker.backtrack(next)

                // перебрали все варианты/нашли решение - возвращаем
                picker.haveSolution() -> return picker.getSolution()

                // всё ок идём дальше
                else -> picker.prepareNextLiterals()
            }

            next = picker.getNextLiteral()
        }

        return null
    }

    // нахождение конфликтов при выборе переменной
    private fun hasConflicts(picked: Literal): Boolean {
        // тут мы проходим только по выражениям где есть некст литерал с инверсией
        for (clause in  picker.getCommonClause(picked)) { /// !!!!!!!!!!!!!!!
            var flag = false
            // в произведении необходимо чтобы каждый член был положителен
            for (literal in clause.literals) {
                // в сумме необходимо чтоб хоть одна переменная была положительной
                // в условии смотрим проверяли ли мы уже эту переменную и если да,то смотрим подходит ли нам это
                // Если не проверяли то он ещё может принять любое значение и получить true
                if (picker.notChecked(literal) || literal.isPositive == picker[literal]) {
                    flag = true
                    break
                }
            }
            if (!flag) {
                return true
            }
        }
        return false
    }

}