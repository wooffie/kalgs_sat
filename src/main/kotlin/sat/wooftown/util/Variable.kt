package sat.wooftown.util


/*
Переменная, определяется индексом
операторы для удобства работы
 */

/**
 * Переменная, хранит в себе целочисленный индекс, который берётся из .CNF
 * @param index - "название" переменной
 */

class Variable(
    val index: Int,
) {

    /**
     * Возращает литерал данной переменной
     * @param isPositive - стоит ли перед литералом минус (отрицание)
     * @return literal - литерал
     */
    fun toLiteral(isPositive: Boolean) = Literal(index * 2 + if (isPositive) 0 else 1)

    /**
     * Оператор для записи переменной как положительный литерал
     * @return - литерал со знаком "+"
     */
    operator fun unaryPlus() = Literal(index * 2)

    /**
     * Оператор для записи переменной как отрицательный литерал
     * @return - литерал со знаком "-"
     */
    operator fun unaryMinus() = Literal(index * 2 + 1)


    override fun equals(other: Any?): Boolean = other is Variable && index == other.index

    override fun hashCode(): Int {
        return index
    }

    override fun toString(): String {
        return (index + 1).toString()
    }
}

