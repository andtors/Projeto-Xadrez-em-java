package boardgame;


//Classe destinada apenas ao campo de xadrez
public class Board {
	
	private int rows;
	private int columns;
	private Piece[][] pieces;
	
	
	public Board(int rows, int columns) {
		if (rows < 1|| columns < 1) {
			throw new BoardException("Error creating board: there must be at least 1 row and 1 column");
		}
		// Se houver menos de 1 rows e columns jogo não irá ser executado
		this.rows = rows;
		this.columns = columns;
		pieces = new Piece[rows][columns];
	}


	
	
	public int getRows() {
		return rows;
	}

	public int getColumns() {
		return columns;
	}

	public Piece piece(int row, int column) {
		if (!positionExists(row, column)) {
			throw new BoardException ("Position not on the board");
		}
		return pieces [row][column];
		// Metodo para salvarmos a posição da peça no board quando formos fazer um movimento, se não existir a posição irá trazer mensagem de erro
	}
	
	public Piece piece (Position position) {
		if (!positionExists(position)) {
			throw new BoardException ("Position not on the board");
		}
		return pieces[position.getRow()][position.getColumn()];
		// Metodo para trazer a linha e coluna em que a peça se encontra
	}
	
	public void placePiece(Piece piece, Position position) {
		if (thereIsAPiece(position)) {
			throw new BoardException ("There is already a piece on position " + position);
		}
		pieces[position.getRow()][position.getColumn()] = piece;
		// Está puxando o valor das rows e columns que está na matriz e salvando no objeto piece e salvando no position
		piece.position = position;
	}
	
	public Piece removePiece(Position position) {
		if (!positionExists(position)) {
			throw new BoardException ("Position not on the board");
		}
		if (piece(position) == null) {
			return null;
		}
		Piece aux = piece(position);
		aux.position = null;
		pieces[position.getRow()][position.getColumn()] = null;
		return aux;
		//Metodo para retirar a peça de uma posição
	}
	
	
	private boolean positionExists(int row, int column) {
		return row >= 0 && row < rows && column >= 0 && column < columns;
		//Método para sabermos se a posição que está sendo inserida existe no campo
	}
		
	public boolean positionExists(Position position) {
		return positionExists(position.getRow(), position.getColumn());
		// retornando os valores da posição no metodo positionExists para usos futuros para testar exceptions
	}
	
	public boolean thereIsAPiece (Position position) {
		if (!positionExists(position)) {
			throw new BoardException ("Position not on the board");
		}
		return piece(position) != null;
		
		// método para saber se há uma peça na posição, se houver irá aparecer mensagem de erro, porém se a posição for nula, não dará erro
		
		
		
	}
}
