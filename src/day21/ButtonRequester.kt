package day21

interface ButtonRequester {
    fun request(buttonList: List<Char>, position: Position? = null): Pair<List<Char>, Position>
}