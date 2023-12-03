package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	private Board board;
	
	public ChessMatch() {
		board = new Board(8, 8);
		// Criado o board com rows e columns de 8 x 8 
		initialSetup();
		// programa irá executar o metodo inicial que começa na linha 29
	}
	
	public ChessPiece[][] getPieces(){
		ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];
		for (int i=0; i<board.getRows();i++) {
			for (int j=0; j<board.getColumns();j++) {
				mat[i][j] = (ChessPiece) board.piece(i, j);
			}
		}
		return mat;
		//Metodo para percorrermos o campo de xadrez
		
	}
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		//O usuario irá inputar no console a posição da peça que ele quer mover, para onde ela irá, com isso iremos converter elas para a matriz
		validateSourcePosition(source);
		//com isso ele irá validar se essa posição existe, se existe ela irá validar e dar ok, se não, lançara uma exceção
		Piece capturedPiece = makeMove(source, target);
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		// Retiramos a peça que estava na posição de origem
		Piece capturedPiece = board.removePiece(target);
		// Removemos a possivel peça que vamos capturar
		board.placePiece(p, target);
		//por fim executamos o metodo de colocar a peça
		return capturedPiece;
		//e salvamos a peça que foi capturada
	}
	
		
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position))/*caso não haja (!) uma posição na peça em que o usuario deseja mover, irá dar o seguinte erro:*/ {
			throw new ChessException("There is no piece on source of position");
		}
	}
	
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
	}
	
	private void initialSetup() {
		placeNewPiece('c', 1, new Rook(board, Color.WHITE));
		placeNewPiece('c', 2, new Rook(board, Color.WHITE));
		placeNewPiece('d', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 2, new Rook(board, Color.WHITE));
		placeNewPiece('e', 1, new Rook(board, Color.WHITE));
		placeNewPiece('d', 1, new King(board, Color.WHITE));
		
		placeNewPiece('c', 7, new Rook(board, Color.BLACK));
		placeNewPiece('c', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 7, new Rook(board, Color.BLACK));
		placeNewPiece('e', 8, new Rook(board, Color.BLACK));
		placeNewPiece('d', 8, new King(board, Color.BLACK));
		
	
		
		
	}
	//Definindo onde cada peça irá aparecee inicialmente junto a cor dela
}
