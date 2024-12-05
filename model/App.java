package model;

public class App {
	public static void main(String[] args) {
		try {
			TienLenMienNam game = new TienLenMienNam(4);
			game.newGame();
			System.out.println("Khởi tạo thành công!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
