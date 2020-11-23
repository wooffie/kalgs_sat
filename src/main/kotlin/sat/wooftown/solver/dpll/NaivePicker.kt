package sat.wooftown.solver.dpll

import sat.wooftown.solver.CommonClauses
import sat.wooftown.solver.Picker
import sat.wooftown.util.*
import java.util.*

// TODO() вынести commonclause
class NaivePicker(
    formula: Formula,
    private val size: Int,
) : Picker {

    /*
    Стак по котрому мы будем идти при поиске решения
    Идти будем так 1 -1 2 -2 ...... n -n
    попутно смотря конфликты и если надо возращаться назад
    !!Это будет работать как двоичное дерево!!
    если представить что левый лист это минус а правый плюс, то мы сначала идём по всем левым листам,
    а если есть конфликт, то возращаемся и идём по правому
    https://sun9-33.userapi.com/XT6Tk5CLdiD-hHcvmIexNwofQpcCsjOaTynbIw/tlrCkXCnD8Q.jpg
    */
    private val checkQueue = LinkedList<Literal>()

    // Список наборов переменных в которые входит данный литерал!!! (не переменная)
    // Зачем это нужно будет описано в фунции решателя
    // @see com.wooftown.solver.dpll.NaivePicker.hasConflict
    private val commonClauses = CommonClauses(formula,size)
    // Важное отличие, тут мы можем не хранить нашу formula, нам это не нужно

    // получить множество наборов из массива выше
    fun getCommonClause(literal: Literal): MutableSet<Clause> {
        return commonClauses[literal]
    }

    // Сюда будем записывать решение
    private val solutions = BooleanArray(size)

    // Записываем решение + переходим к следующей переменной
    operator fun set(literal: Literal, isPositive: Boolean) {
        solutions[literal.variable.index] = isPositive
        currentIndex = literal.index + 1
    }

    // просто достаём решение из приватного поля
    operator fun get(literal: Literal) = solutions[literal.index]

    // Получить решение, если оно есть
    override fun getSolution(): Model {
        val result = mutableSetOf<Literal>()
        for (literal in solutions.indices) {
            result.add(Variable(literal).toLiteral(solutions[literal]))
        }
        return Model(result)
    }

    override fun hasConflict(): Pair<Boolean, Clause?> {
        throw NotImplementedError("Naive picker cannot choose problem clause")
    }


    init {
        // добавляем первые листы от *рута* дерева
        checkQueue.add(Literal(0))
        checkQueue.add(Literal(1))
    }

    // указатель где сейчас мы находимся
    private var currentIndex = 0

    // вернутся к листу дереву и начать идти оттуда
    fun backtrack(literal: Literal) {
        currentIndex = literal.index
    }

    // проходили ли мы по этой перменной
    fun notChecked(literal: Literal): Boolean {
        return literal.index >= currentIndex
    }

    // прошли ли мы достаточно чтобы выдать ответ
    fun haveSolution() = size <= currentIndex

    // взять некст лист, если нету, то получается что мы прошли всё дерево и решение не найдено
    fun getNextLiteral(): Literal? =
        if (checkQueue.isEmpty()) {
            null
        } else {
            checkQueue.removeLast()
        }

    // Если у нас не было конфликтов, то идём дальше по дереву
    fun prepareNextLiterals() {
        checkQueue.add(Literal(currentIndex * 2))
        checkQueue.add(Literal(currentIndex * 2 + 1))
    }

}