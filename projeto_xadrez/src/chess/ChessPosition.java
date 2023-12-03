package chess;

import boardgame.Position;

//Classe para convertermos as posições na matriz em posições do xadrez

public class ChessPosition {
	private char column;
	private int row;
	
	
	public ChessPosition(char column, int row) {
		if (column < 'a' || column > 'h' || row < 1 || row > 8) {
			throw new ChessException("Error instantiating ChessPosition. Valida values are from, a1 to h8");
		}
		this.column = column;
		this.row = row;
	}


	public char getColumn() {
		return column;
	}

	public int getRow() {
		return row;
	}

	protected Position toPosition() {
		return new Position (8 - row, column - 'a' );
	}
	//Metodo para fazermos o 8 (maximo de casas do xadrez) menos a posição que inserimos, podemos calcular em char, pois char é considerado um int já que 'a' = 0, 'b' = 1...
	protected static ChessPosition fromPosition(Position position) {
		return new ChessPosition ((char)('a' - position.getColumn()), 8 - position.getRow());
	}
	// Metodo inverso ao de cima
	
	@Override
	public String toString() {
		return "" + column + row;
	}
}
