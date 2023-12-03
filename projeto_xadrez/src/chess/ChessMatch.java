package chess;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.King;
import chess.pieces.Rook;

public class ChessMatch {
	
	private int turn;
	private Color currentPlayer;
	private Board board;
	private boolean check;
	private boolean checkMate;
	
	private List<Piece> piecesOnTheBoard = new ArrayList<>();
	private List<Piece> capturedPieces = new ArrayList<>();
	
	public ChessMatch() {
		board = new Board(8, 8);
		// Criado o board com rows e columns de 8 x 8 
		
		turn = 1;
		currentPlayer = Color.WHITE;
		initialSetup();
		// programa irá executar o metodo inicial que começa na linha 29
	}
	
	public int getTurn(){
		return turn;
	}
	
	public Color getCurrentPlayer() {
		return currentPlayer;
	}
	
	
	public boolean getCheck() {
		return check;
	}
	
	public boolean getCheckMate() {
		return checkMate;
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
	
	public boolean [][] possibleMoves(ChessPosition sourcePosition){
		Position position = sourcePosition.toPosition();
		validateSourcePosition(position);
		return board.piece(position).possibleMoves();
	}
	//Metodo para que quando o usuario selecionar a peça, apareça os movimentos disponiveis no tabuleiro
	
	public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
		Position source = sourcePosition.toPosition();
		Position target = targetPosition.toPosition();
		//O usuario irá inputar no console a posição da peça que ele quer mover, para onde ela irá, com isso iremos converter elas para a matriz
		validateSourcePosition(source);
		//com isso ele irá validar se essa posição existe, se existe ela irá validar e dar ok, se não, lançara uma exceção
		validateTargetPosition(source, target);
		Piece capturedPiece = makeMove(source, target);
		
		if (testCheck(currentPlayer)) {
			undoMove(source, target, capturedPiece);
			throw new ChessException ("You can't put yourself in check");
		}
		
		check = (testCheck(opponent(currentPlayer))) ? true : false;
		
		//Checagem pra verificar se o oponente se colocou em check
		
		if(testCheckMate(opponent(currentPlayer))){
			checkMate = true;
		}
		else {
			nextTurn();
		}
		
		return (ChessPiece)capturedPiece;
	}
	
	private Piece makeMove(Position source, Position target) {
		Piece p = board.removePiece(source);
		// Retiramos a peça que estava na posição de origem
		Piece capturedPiece = board.removePiece(target);
		// Removemos a possivel peça que vamos capturar
		board.placePiece(p, target);
		//por fim executamos o metodo de colocar a peça
		
		//e salvamos a peça que foi capturada
		
		if (capturedPiece != null) {
			piecesOnTheBoard.remove(capturedPiece);
			capturedPieces.add(capturedPiece);
		}
		//Fazemos o if para retirar uma peça do campo e colocar ela dentro da listas das capturadas para posteriormente imprimir no Ui quais estão capturadas
		return capturedPiece;
	}
	
	private void undoMove(Position source, Position target, Piece capturedPiece) {
		Piece p  = board.removePiece(target);
		board.placePiece(p, source);
		
		if(capturedPiece != null) {
			board.placePiece(capturedPiece, target);
			capturedPieces.remove(capturedPiece);
			piecesOnTheBoard.add(capturedPiece);
		}
	}
	//Metodo para refazer toda a alteração que fizemos da peça ao longo do jogo
		
	private void validateSourcePosition(Position position) {
		if (!board.thereIsAPiece(position))/*caso não haja (!) uma posição na peça em que o usuario deseja mover, irá dar o seguinte erro:*/ {
			throw new ChessException("There is no piece on source of position");
		}
		if(currentPlayer != ((ChessPiece)board.piece(position)).getColor()) {
			//Foi necessario fazer um downcast por que:Color é da classe  ChessPiece, porém por sua vez ChessPiece é da Piece que é uma classe muito generica
			throw new ChessException ("The chosen piece is not yours");
		}
		
		if(!board.piece(position).isThereAnyPossibleMove())/*Caso não haja (!) movimento possivel para a peça fazer irá aparecer este erro*/ {
			throw new ChessException("There is no possible moves for the chosen piece");
		}
	}
	
	private void validateTargetPosition(Position source, Position target) {
		if(!board.piece(source).possibleMove(target)) {
		//Se para a peça de origem não é um movimento de origem, então não é possivel mover para lá
			throw new ChessException("The chosen piece can't move to target position");
		}
	}
	
	private void nextTurn(){
		turn++;
		currentPlayer = (currentPlayer == Color.WHITE) ? Color.BLACK : Color.WHITE;
		// metodo para ir alternando conforme os turnos de um jogador para o outro
	}
	
	private Color opponent (Color color) {
		return (color == Color.WHITE) ? Color.BLACK : Color.WHITE;
	}
	//Esse metodo serve para o programa pegar qual o oponente no turno atual
	
	private ChessPiece king(Color color) {
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == color).collect(Collectors.toList());
		for(Piece p : list) {
			if (p instanceof King) {
				return (ChessPiece)p;
			}
		}
		throw new IllegalStateException("There is no " + color + " king on the board");
	}
	//Capturamos as posições do Rei, pois com ele vamos fazer a lógica se é ou não check mate
	
	private boolean testCheck(Color color) {
		Position kingPosition = king(color).getChessPosition().toPosition();
		List<Piece> opponentPieces = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent (color)).collect(Collectors.toList());
		for (Piece p : opponentPieces) {
			boolean [][] mat = p.possibleMoves();
			if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
				return true;
			}
		}
		return false;
	}
	
	//Nesse metodo o programa ele esgota as opções de movimento das peças, se por algum acaso houver alguma peça do oponente que pode ter como posição o rei, será check-mate
	
	
	private boolean testCheckMate(Color color) {
		if (!testCheck(color)) {
			return false;
		}
		List<Piece> list = piecesOnTheBoard.stream().filter(x -> ((ChessPiece)x).getColor() == opponent (color)).collect(Collectors.toList());
		
		for (Piece p : list) {
			boolean [][] mat = p.possibleMoves();
			for (int i=0; i<board.getRows();i++) {
				for (int j=0; j<board.getColumns();j++) {
					if (mat[i][j]) {
						Position source =((ChessPiece)p).getChessPosition().toPosition();
						Position target = new Position(i, j);
						Piece capturedPiece = makeMove(source, target);
						boolean testCheck = testCheck(color);
						undoMove(source,target, capturedPiece);
						if(!testCheck) {
							return false;
						}
					}
			}
		}
		
	}
		return true;
} 
		
	private void placeNewPiece(char column, int row, ChessPiece piece) {
		board.placePiece(piece, new ChessPosition(column, row).toPosition());
		piecesOnTheBoard.add(piece);
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
