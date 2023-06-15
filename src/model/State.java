package model;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;


public class State {
	private int[][] cell; // ma trận trạng thái
	public int steps; // số ô đã đánh

        private List<JButton> list;
	
	public State(){
            this.steps = 0;
            this.cell = new int[Value.SIZE][Value.SIZE];
            for (int i=0; i<Value.SIZE; i++) {
                for(int j=0; j<Value.SIZE; j++) 
                    cell[i][j] = 0;
            }
	}
	
	public State(int[][] cell){
            this.steps = 0;
            this.cell = new int[Value.SIZE][Value.SIZE];
            for (int i=0; i<Value.SIZE; i++) {
		for(int j=0; j<Value.SIZE; j++) {
                    this.cell[i][j] = cell[i][j];
                    if(cell[i][j] != 0) {
			this.steps++;
                    }
		}
            }		
	}
	public int[][] getState(){
		return this.cell;
	}
        public List<JButton> getlist(){
            return list;
        }
        public int getStep(){
            return steps;
        }
        public void updatestate(int x, int y){
		cell[x][y]=0;
	}
	public void update(int x, int y, int player) {
		this.cell[x][y] = player;
		this.steps++;
	}
	
	
	
	public void setState(int[][] cell) {
		for (int i=0; i<Value.SIZE; i++) {
			for(int j=0; j<Value.SIZE; j++) this.cell[i][j] = cell[i][j];
		}
	}
	
	public void printState() {
		for (int i=0; i<Value.SIZE; i++) {
			for(int j=0; j<Value.SIZE; j++) {
				if(cell[i][j] == Value.AI_VALUE) System.out.print("- ");
				else if(cell[i][j] == Value.USER_VALUE) System.out.print("+ ");
				else System.out.print(cell[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	
	/**
	 * Kiểm tra người thắng cuộc
	 * @return true: nếu player thắng, false nếu ngược lại
	 */
    public int checkWinner(JButton[][] button,int user) {
	int[] lineX = {1, 1, 0, 1};  // |các đường cần kiểm tra(ngang, dọc, chéo xuống trái, chéo xuống phải)
	int[] lineY = {0, 1, 1, -1}; // |để tìm người thắng
        list = new ArrayList<>();
	for (int x = 0; x < Value.SIZE; x++) {
            for (int y = 0; y < Value.SIZE; y++) {
                if(cell[x][y] == user) { // Nếu ô này đã được player chọn => kiểm tra			
                    for (int i = 0; i < 4; i++) { // kiểm tra 4 đường
                        list = new ArrayList<>();
			int count = 1; // count = 5 => player chiến thắng
                        list.add(button[x][y]);
			for(int j = 1; j <= 4; j++) { // kiểm tra 4 ô tiếp theo
                            int vtx = x + lineX[i]*j; // vị trí x của ô tiếp theo cần check
                            int vty = y + lineY[i]*j; // vị trí y của ô tiếp theo cần check
                            // vtx hoặc vty < 0 hoặc > Value.SIZE, hoặc ô này != ô đầu => khỏi ktra
                            if(vtx < 0 || vty < 0 || vtx >= Value.SIZE || vty >= Value.SIZE) break;
                            if(cell[vtx][vty] == user){
                                list.add(button[vtx][vty]);
                                count++;
                            }
                            else break;
			}
			if(count == 5){
                            return 1;
                        } // Player thắng
                    }
		}
            }
	}
	return 0; // Không ai thắng cả
    }

	/**
	 * Kiểm tra một ô đã có ai đánh chưa
	 * @param x
	 * @param y
	 * @return
	 */
	public boolean isClickable(int x, int y) {
		if(x >= 0 && x < Value.SIZE && y >= 0 && y < Value.SIZE)
			if(cell[x][y] == 0) return true;
		return false;
	}
	
	/**
	 * Hết ô để đánh
	 * @return true: hết, false: chưa hết
	 */
	public boolean isOver() {
		if(this.steps == Value.SIZE*Value.SIZE) return true;
		else return false;
	}
}
