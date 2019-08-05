package joshuaradin.gameengine2d.editor.commandline

class Command

class Interpreter {

    /*
    private val argToOptionSetup = mutableMapOf<String, OptionSetup>()
    private val shortcuts = mutableMapOf<String, OptionSetup>()

     */

    companion object {
        private fun String.tokenize() : Array<out String> {
            val output = mutableListOf<String>()
            var current = ""
            val splitRegex = Regex("\\s")
            var quoteStart: Char? = null
            var inQuote = false

            val charArray = this.toCharArray()
            for (character in charArray) {
                if(!inQuote && splitRegex.matches("" + character)) {

                    if(current.isNotEmpty()) {
                        output += current
                        current = ""
                    }

                }else if (character == '"' || character == '\'') {
                    if (inQuote) {
                        if (character == quoteStart) {
                            inQuote = false
                        }
                    } else {
                        inQuote = true
                        quoteStart = character
                    }
                }else{
                    current += character
                }


            }

            return output.toTypedArray()
        }
    }



}
