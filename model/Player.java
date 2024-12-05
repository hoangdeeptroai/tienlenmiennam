package model;

/*
 * Lớp Player đại diện cho người chơi
 * Mỗi người chơi sẽ sở hữu các lá bài tức sở hữu một Deck
 * Do đó ta sẽ cho lớp Player kế thừa lớp Deck
 * Cùng với đó mỗi Player sẽ có thêm các thuộc tính như tên, trạng thái trong vòng ....
 */

public class Player extends Deck {
	/*
	 * Trạng thái trong vòng chơi của người chơi sẽ có 3 trạng thái Bỏ lượt là người
	 * chơi đã bỏ qua vòng chơi và không còn quyền tham gia Trong vòng là người chơi
	 * đang trong vòng Và hết bài là người chơi đã đánh hết bài trong tay và về
	 */
	public enum PlayerState {
		BO_LUOT, TRONG_VONG, HET_BAI
	}

	// Các thuộc tính của người chơi như Trạng thái, tên, STT, và số dư
	private PlayerState playerState;
	private String playerName;
	private double playerBallance;
	private int playerNum;

	// constructer
	public Player() {
	}

	// tiếp tục là constructer
	public Player(String playerName, double playerBallance, int playerNum) {
		this.playerState = PlayerState.TRONG_VONG;
		this.playerName = playerName;
		this.playerBallance = playerBallance;
		this.playerNum = playerNum;
	}

	// Check trạng thái của người chơi, nếu đang trong vòng chơi sẽ trả về true
	public Boolean checkState() {
		switch (playerState) {
		case TRONG_VONG:
			return true;
		default:
			return false;
		}
	}

	// Người chơi sẽ có thao tác bỏ lượt
	public void skip() {
		playerState = PlayerState.BO_LUOT;
	}

	// Phương thức trả về trạng thái người chơi
	public PlayerState getPlayerState() {
		return playerState;
	}

	// Phương thức trả về tên người chơi
	public String getPlayerName() {
		return playerName;
	}

	public double getPlayerBallance() {
		return playerBallance;
	}

	public int getPlayerNum() {
		return playerNum;
	}

	// Phương thức đặt lại trạng thái
	public void setPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}
}
