package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;

public abstract class ChessPiece extends Piece{
	private Color color;

	public ChessPiece(Board board, Color color) {
		super(board);
		this.color = color;
	}

	public Color getColor() {
		return color;
	}

	public ChessPosition getChessPosition() {
		return ChessPosition.fromPosition(position);
	}
	//Metodo para converter da posição em matriz para a posição que o usuario insere no sistema
	
	protected boolean isThereOpponentPiece(Position position) {
		ChessPiece p = (ChessPiece)getBoard().piece(position);
		return p != null && p.getColor() != color;
		//Ele verifica se a posição é diferente de nulo e verifica se a peça em que está movendo é diferente da que está na posição
	}
	//Metodo para saber se a posição indicada pelo usuario contem uma peça inimiga
	
	
	
}
