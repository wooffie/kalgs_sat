package sat.wooftown.util

/*
Одна строчка переменных
операторы для удобства работы
 */
/**
 * Набор дизъюнкт состоящий из литералов
 * @param literals - литералы на вход
 */
class Clause(vararg literals: Literal) {

    val literals = mutableSetOf<Literal>()

    init {
        this.literals.addAll(literals)
    }

    /**
     * Добавить литерал к набору дизъюнкт
     */
    operator fun plus(other: Literal): Clause {
        literals.add(other)
        return this
    }

}

