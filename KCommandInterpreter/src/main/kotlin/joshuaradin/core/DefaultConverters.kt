package joshuaradin.core

object DefaultConverters {

    interface ISingleStringConverter<T> : IStringConverter<T> {
        /**
         * Converts from a string to something else
         *
         * @param str the string
         * @return converted string
         */
        override fun convert(strs: List<String>): T {
            return convert(strs.first())
        }

        fun convert(str: String): T
    }

    class StringConverter : ISingleStringConverter<String> {
        /**
         * Converts from a string to something else
         *
         * @param str the string
         * @return converted string
         */
        override fun convert(str: String): String = str
    }

    class IntConverter : ISingleStringConverter<Int> {
        /**
         * Converts from a string to something else
         *
         * @param str the string
         * @return converted string
         */
        override fun convert(str: String): Int = str.toInt()
    }

    class BooleanConverter : ISingleStringConverter<Boolean> {
        /**
         * Converts from a string to something else
         *
         * @param str the string
         * @return converted string
         */
        override fun convert(str: String): Boolean {
            return str.toBoolean()
        }
    }

    class DefaultCommandConverter : IMultiParameterConverter<String?> {
        override fun convert(strs: Set<ParameterValue<*>>): String? {
            return null
        }
    }









}