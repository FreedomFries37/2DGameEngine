package joshuaradin.core

/**
 * @param T the type to convert to
 */
interface IStringConverter<out T> : (List<String>) -> T {

    /**
     * Converts from a string to something else
     *
     * @param str the string
     * @return converted string
     */
    fun convert(strs: List<String>) : T


    override fun invoke(p1: List<String>): T {
        return convert(p1)
    }
}