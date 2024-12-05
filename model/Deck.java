package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * Lớp Deck là lớp kết tập các lá bài, tức là một tập hợp các lá bài
 * Trong đó ta sẽ có nhưng phương thức để xử lý Deck
 */

public class Deck {
	// Kết tập các lá bài thành 1 Deck
	private List<Card> cards;

	// Constructer
	public Deck() {
		cards = new ArrayList<>();
	}

	// Trộn bộ bài hay các lá bài
	public void shuffleDeck() {
		Collections.shuffle(cards);
	}

	// Sắp xếp bộ bài theo thứ tự từ 3 bích đến 2 cơ :))
	public void sortDeck() {
		for (int i = 0; i < cards.size() - 1; i++) {
			for (int j = i + 1; j < cards.size(); j++) {
				if (cards.get(i).compareCard(cards.get(j))) {
					Card temp = cards.get(i);
					cards.set(i, cards.get(j));
					cards.set(j, temp);
				}
			}
		}
	}

	// Thêm một lá bài vào bộ bài sẵn có
	public void addCard(Card card) {
		cards.add(card);
	}

	// Trả về lá bài nằm ở vị trí index
	public Card getCard(int index) {
		return cards.get(index);
	}

	// Trả về toàn bộ bộ bài
	public List<Card> getCards() {
		return cards;
	}

	// Lấy lá bài ra khỏi bộ bài
	public Card removeCard(int index) {
		return cards.remove(index);
	}

	// Lấy các lá bài ra
	public void removeCards(Deck cardsToRemove) {
		for (Card card : cardsToRemove.getCards()) {
			cards.remove(card); // Xóa từng lá bài khỏi Deck
		}
	}

	// Sao chép một deck
	@Override
	public Deck clone() {
		Deck clonedDeck = new Deck();
		for (Card card : this.getCards()) {
			clonedDeck.addCard(card); // Thêm từng lá bài vào bản sao
		}
		return clonedDeck;
	}

	// Xóa cả bộ bài đi
	public void removeDeck() {
		cards.clear();
	}

	// In thông tin bộ bài
	public void printDeck() {
		for (Card card : cards) {
			System.out.println(card.toString());
		}
	}

	// Trả về số lượng lá bài có trong bộ bài
	public int size() {
		return cards.size();
	}
}
