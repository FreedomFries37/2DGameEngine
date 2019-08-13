package joshuaradin.core

object DefaultConverters {

    class StringConverter : IStringConverter<String> {
        /**
         * Converts from a string to something else
         *
         * @param str the string
         * @return converted string
         */
        override fun convert(str: String): String = str
    }

    class IntConverter : IStringConverter<Int> {
        /**
         * Converts from a string to something else
         *
         * @param str the string
         * @return converted string
         */
        override fun convert(str: String): Int = str.toInt()
    }

    class BooleanConverter : IStringConverter<Boolean> {
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









}