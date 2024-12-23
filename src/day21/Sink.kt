package day21

class Sink : ButtonRequester {
  override fun request(buttonList: List<Char>, position: Position?) =
      Pair(buttonList.size.toLong(), Position(0, 0))
}
