package day12

fun main() {
    val field: List<List<Char?>> = getField()
    val cellStates: List<List<CellState>> =
        field.indices.map { field[0].indices.map { CellState(false, mutableSetOf()) } }
    val result = field.indices.flatMap { i ->
        field[i].indices.mapNotNull { j ->
            if (cellStates[i][j].visited) {
                null
            } else {
                findAreaAndSides(field, cellStates, Pair(i, j))
            }
        }
    }.sumOf { it.area * it.perimeter }
    println(result)
}

private fun findAreaAndSides(
    field: List<List<Char?>>, cellStates: List<List<CellState>>, p: Position
): AreaAndPerimeter {
    var result = AreaAndPerimeter(1, 0)
    val cellState = cellStates[p.first][p.second]

    val v = field[p.first][p.second]
    cellState.visited = true

    for (d in Direction.entries) {
        val q = p + d
        if (!inField(field, q)) {
            result = result.addPerimeter(visitSide(field, cellStates, p, d))
            continue
        }
        val w = field[q.first][q.second]
        if (w != v) {
            result = result.addPerimeter(visitSide(field, cellStates, p, d))
            continue
        }
        if (cellStates[q.first][q.second].visited) {
            continue
        }
        result += findAreaAndSides(field, cellStates, q)
    }
    return result
}

fun visitSide(field: List<List<Char?>>, cellStates: List<List<CellState>>, p: Position, d: Direction): Int {
    val cellState = cellStates[p.first][p.second]
    if (d !in cellState.visitedSides) {
        markSideAsVisited(field, cellStates, p, d)
        return 1
    } else {
        return 0
    }
}

fun markSideAsVisited(field: List<List<Char?>>, cellStates: List<List<CellState>>, p: Position, d: Direction) {
    val d1 = Direction.entries[(d.ordinal - 1).mod(Direction.entries.size)]
    val d2 = Direction.entries[(d.ordinal + 1).mod(Direction.entries.size)]
    markHalfSideAsVisited(field, cellStates, p, d, d1)
    markHalfSideAsVisited(field, cellStates, p, d, d2)
}

private fun markHalfSideAsVisited(
    field: List<List<Char?>>,
    cellStates: List<List<CellState>>,
    p: Position,
    side: Direction,
    walkDirection: Direction
) {
    val v = field[p.first][p.second]
    var q = p
    while (inField(field, q) && field[q.first][q.second] == v) {
        val r = q + side
        if (inField(field, r) && field[r.first][r.second] == v) {
            break
        }
        cellStates[q.first][q.second].visitedSides += side
        q += walkDirection
    }
}

data class CellState(var visited: Boolean, val visitedSides: MutableSet<Direction>)