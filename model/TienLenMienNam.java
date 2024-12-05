package model;

import java.util.ArrayList;
import java.util.List;

import model.Player.PlayerState;

/*
 * Lớp tiến lên miền nam chứa tất cả các thành phần của trò chơi tiến lên
 * Bao gồm danh sách người chơi
 * Các logic của game, vòng chơi, luật chơi ...
 */

public class TienLenMienNam {
	// Khai báo hai enum chứa kiểu chơi bài
	// NULL là kiểu chơi không xác định hay không hợp lệ
	public enum PlayType {
		COC, DOI, BA, TU, SANH, DOI_THONG, NULL;
	}

	// Khai báo và khai báo
	private List<Player> player;
	private Deck motherPack;
	private int numOfPlayer;
	private int numOfAIPlayer;
	private Deck lastPlay;
	private Deck selectionCard;
	private int round;
	private int nowPlayer;
	private Card minCard = new Card();// đã thêm-là lá bài bé nhất, 4 players -> là 3 bích
	private int key = 1;// đã thêm-biến key thể hiện được ván bài đầu tiên, điều này giúp cho logic của
						// lần đánh đầu tiên hoạt động tốt hơn...

	// constructer
	// Khi khởi tạo constructer ta đồng thời khởi tạo một bộ bài tây 52 lá
	public TienLenMienNam(int numOfPlayer) {
		player = new ArrayList<>();
		selectionCard = new Deck();
		this.numOfPlayer = numOfPlayer;
		this.motherPack = new Deck();
		this.newMotherPack();
		this.dealCard();
		this.informationPlayer();
		this.lastPlay = new Deck();
		this.round = 0;
		this.nowPlayer = 0;
		this.newGame();
	}

	// Tạo bộ bài tây 52 lá và trộn bài
	public void newMotherPack() {
		if (motherPack != null) {
			motherPack.removeDeck();
		}
		for (Card.Value value : Card.Value.values()) {
			for (Card.Type type : Card.Type.values()) {
				motherPack.addCard(new Card(value, type));
			}
		}
		motherPack.shuffleDeck();
//		motherPack.printDeck();
	}

	// Chia bài cho từng người chơi và sắp xếp bộ bài cho từng người
	public void dealCard() {
		player.clear();
		for (int i = 0; i < numOfPlayer; i++) {
			Player playerTemp = new Player();
			for (int j = 0; j < 13; j++) {
				playerTemp.addCard(motherPack.removeCard(0));
			}
			playerTemp.sortDeck();
			player.add(playerTemp);
		}
	}

	public void newGame() {
		if (round == 0) {
			this.newFirstRound();
		}
		round++;
	}

	public void newFirstRound() {
		for (int i = 1; i < numOfPlayer; i++) {
			if (!player.get(i).getCard(0).compareCard(player.get(nowPlayer).getCard(0))) {
				this.nowPlayer = i;
			}
		}
		minCard = player.get(nowPlayer).getCard(0);
		this.newRound();
	}

	// băt đầu 1 round mới, những người chơi bỏ luotj sẽ được đưa về trạng thái
	// trong vòng và tiếp tục chơi
	public void newRound() {
		for (Player playerTemp : player) {
			if (playerTemp.getPlayerState() != PlayerState.HET_BAI) {
				playerTemp.setPlayerState(PlayerState.TRONG_VONG);
			}
		}
		lastPlay.getCards().clear();
	}

	public void playGame() {
		if (!isTheEnd()) {
			if (player.get(nowPlayer).getPlayerState() == PlayerState.TRONG_VONG) {
				selectionCard.sortDeck();
				if (isHit()) {
					lastPlay = selectionCard.clone();
					// selectionCard.removeDeck();
					player.get(nowPlayer).removeCards(lastPlay);
					if (player.get(nowPlayer).getCards().isEmpty()) {
						player.get(nowPlayer).setPlayerState(PlayerState.HET_BAI);
					}
				}
				nextPlayer();
			}
		} else {
			// in ra người chiến thắng....
			resetGame();
			return;
		}
	}

	// đã sửa lại cho đúng logic
	/*
	 * hàm skip sẽ bỏ lượt nhưng đồng thời cũng check xem có bỏ lượt được không?
	 * những trường hợp không thể skip: người có minCard(first round), khi mọi người
	 * đều skip và mình là người bắt đầu round mới,
	 */
	public boolean skip() {
		int activePlayers = 0;
		for (Player playerTemp : player) {
			if (playerTemp.getPlayerState() == PlayerState.TRONG_VONG) {
				activePlayers++;
			}
		}
		// nếu bài trên bàn là rỗng thì không thể skip
		if (lastPlay.getCards().isEmpty()) {
			selectionCard.removeDeck();// dùng để fix bug 6
			return false;
		}
		// nếu round còn 2 người mà skip thì tạo round mà người tiếp theo sẽ mở bát
		if (activePlayers == 2) {
			newRound();
			selectionCard.removeDeck();
			nextPlayer();
			return true;
		}
		// nếu không phải vòng đầu tiên thì có thể bỏ lượt
		if (key != 1) {
			player.get(nowPlayer).setPlayerState(PlayerState.BO_LUOT);
			selectionCard.removeDeck();
			nextPlayer();
			return true;
		}
		return false;
	}

	public void nextPlayer() {
		do {
			nowPlayer = (nowPlayer + 1) % numOfPlayer;
		} while (player.get(nowPlayer).getPlayerState() != PlayerState.TRONG_VONG);
	}

	public boolean isHit() {
		selectionCard.sortDeck();// đã thêm
		PlayType selectionCardType = checkTypePlay(selectionCard);
		PlayType lastPlayType = checkTypePlay(lastPlay);
//		//nếu 
//		if (lastPlay.getCards() == null) {
//			return true;
//		}

		if (key == 1 && player.get(nowPlayer).getCard(0).equals(minCard) && !selectionCard.getCard(0).equals(minCard)) {
			return false;
		} // đã thêm
		if (selectionCard.size() > 0) {
			int a = selectionCard.size() - 1;
			int b = lastPlay.size() - 1;
			if (lastPlayType == PlayType.NULL) {// đã sửa ở đây
				if (selectionCardType == PlayType.NULL) {
					return false;
				}
				key = 0;
				return true;
			} else if (selectionCardType == lastPlayType) {
				if (selectionCardType == PlayType.SANH) {
					if (a == b && selectionCard.getCard(a).compareCard(lastPlay.getCard(b))) {
						return true;
					}
				} else if (selectionCardType == PlayType.DOI_THONG) {
					if (a > b) {
						return true;
					} else if (a == b && selectionCard.getCard(a).compareCard(lastPlay.getCard(b))) {
						return true;
					} else {
						return false;
					}
				} else if (selectionCard.getCard(a).compareCard(lastPlay.getCard(b))) {
					return true;
				}
			} else {
				if (selectionCardType == PlayType.TU && lastPlayType == PlayType.DOI_THONG && b + 1 == 6) {
					return true;
				} else if (lastPlayType == PlayType.TU && selectionCardType == PlayType.DOI_THONG && b + 1 >= 8) {
					return true;
				} else if (lastPlay.getCard(b).getValueValue() == 15) {
					if (b == 0) {
						if (selectionCardType == PlayType.TU || selectionCardType == PlayType.DOI_THONG) {
							return true;
						}
					} else if (b == 1) {
						if (selectionCardType == PlayType.TU || (selectionCardType == PlayType.DOI_THONG && a > 6)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	public boolean isTheEnd() {
		int activePlayers = 0;
		for (Player playerTemp : player) {
			if (playerTemp.getPlayerState() == PlayerState.TRONG_VONG) {
				activePlayers++;
			}
		}
		return activePlayers <= 1;
	}

	// In thông tin người chơi
	public void informationPlayer() {
		for (Player playerTemp : player) {
			playerTemp.printDeck();
			System.out.println("===========================");
		}
	}

	// Check các lá bài người chơi lấy ra thuộc kiểu chơi nào
	public PlayType checkTypePlay(Deck deck) {
		if (deck.size() == 1) {
			return PlayType.COC;
		} else if (deck.size() == 2) {
			if (checkDoi(deck.getCard(0), deck.getCard(1))) {
				return PlayType.DOI;
			}
		} else if (deck.size() == 3) {
			if (checkDoi(deck.getCard(0), deck.getCard(1))) {
				if (checkDoi(deck.getCard(0), deck.getCard(2))) {
					return PlayType.BA;
				}
			} else {
				return checkSanh(deck);
			}
		} else if (deck.size() == 4) {
			if (checkDoi(deck.getCard(0), deck.getCard(1))) {
				if (checkDoi(deck.getCard(0), deck.getCard(2))) {
					if (checkDoi(deck.getCard(0), deck.getCard(3))) {
						return PlayType.TU;
					}
				}
			} else {
				return checkSanh(deck);
			}
		} else if (deck.size() == 0) {
			return PlayType.NULL;
		} else if (deck.size() % 2 == 0) {
			// deck.sortDeck();
			for (int i = 0; i < deck.size(); i = i + 2) {
				if (!checkDoi(deck.getCard(i), deck.getCard(i + 1))) {
					return PlayType.NULL;
				}
			}
			Deck temp = new Deck();
			for (int i = 0; i < deck.size(); i = i + 2) {
				temp.addCard(deck.getCard(i));
			}
			if (checkSanh(temp) == PlayType.SANH) {
				return PlayType.DOI_THONG;
			}
		} else {
			return checkSanh(deck);
		}
		return PlayType.NULL;
	}

	// Check xem có phải đôi
	public boolean checkDoi(Card card1, Card card2) {
		return card1.getValueValue() == card2.getValueValue();
	}

	// Check xem có là sảnh
	public PlayType checkSanh(Deck deck) {
		deck.sortDeck();
		if (deck.size() == 0 || deck.getCard(deck.size() - 1).getValueValue() == 15)
			return PlayType.NULL;
		if (deck.size() < 3) {
			return PlayType.NULL;
		}
		for (int i = 0; i < deck.size() - 1; i++) {
			if ((deck.getCard(i + 1).getValueValue() - deck.getCard(i).getValueValue()) != 1) {
				return PlayType.NULL;
			}
		}
		return PlayType.SANH;
	}

	public void resetGame() {
		selectionCard.removeDeck();
		lastPlay.removeDeck();
		newMotherPack();
	}

	public Player getNowPlayer() {
		return player.get(nowPlayer);
	}

	public Deck getSelectionCard() {
		return selectionCard;
	}

	public void deselectCard(Card card) {
		selectionCard.getCards().remove(card);
	}

	public void setSelectionCard(Card card) {
		if (!selectionCard.getCards().contains(card) && player.get(nowPlayer).getCards().contains(card)) {
			selectionCard.addCard(card);
		}
	}

	public int getNowPlayerNum() {
		return nowPlayer;
	}
}