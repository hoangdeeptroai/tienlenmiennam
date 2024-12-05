package model;

/*
 * Lớp Card là lớp cơ bản của chương trình biểu diễn cho các đối tượng lá bài
 * Một lá bài sẽ có hai thuộc tính là Value(giá trị) và Type(chất)
 */

public class Card {
	// Khai báo 2 enum chứa giá trị quân bài và chất
	public enum Value {
		AT, HAI, BA, BON, NAM, SAU, BAY, TAM, CHIN, MUOI, RI, QUY, KA
	}

	public enum Type {
		CO, RO, CHUON, BICH
	}

	// Khai báo hai thuộc tính của một lá bài
	Value value;
	Type type;

	public Card() {
	}

	// constructer
	public Card(Value value, Type type) {
		this.value = value;
		this.type = type;
	}

	// Hai hàm lấy giá trị thực của lá bài để tiện so sánh theo đúng logic luật chơi
	public int getValueValue() {
		switch (value) {
		case AT:
			return 14;
		case HAI:
			return 15;
		case BA:
			return 3;
		case BON:
			return 4;
		case NAM:
			return 5;
		case SAU:
			return 6;
		case BAY:
			return 7;
		case TAM:
			return 8;
		case CHIN:
			return 9;
		case MUOI:
			return 10;
		case RI:
			return 11;
		case QUY:
			return 12;
		case KA:
			return 13;
		default:
			return 0;
		}
	}

	public int getTypeValue() {
		switch (type) {
		case BICH:
			return 1;
		case CHUON:
			return 2;
		case RO:
			return 3;
		case CO:
			return 4;
		default:
			return 0;
		}
	}

//thêm vào cho đẹp:>
	public String getvalue() {
		switch (value) {
		case AT:
			return "A";
		case HAI:
			return "2";
		case BA:
			return "3";
		case BON:
			return "4";
		case NAM:
			return "5";
		case SAU:
			return "6";
		case BAY:
			return "7";
		case TAM:
			return "8";
		case CHIN:
			return "9";
		case MUOI:
			return "10";
		case RI:
			return "J";
		case QUY:
			return "Q";
		case KA:
			return "K";
		default:
			return "\0";
		}
	}

	public String gettype() {
		switch (type) {
		case BICH:
			return "♠";
		case CHUON:
			return "♣";
		case RO:
			return "♦";
		case CO:
			return "♥";
		default:
			return "\0";
		}
	}

	// Hàm trả về một String chứa thông tin lá bài
	public String toString() {
		return getvalue() + " " + gettype();
	}

	// Hàm so sánh giá trị của lá bài
	// Nếu giá trị lá bài hiện tại lớn hơn thì return true
	public boolean compareCard(Card otherCard) {
		if (this.getValueValue() != otherCard.getValueValue()) {
			return this.getValueValue() > otherCard.getValueValue();
		}
		return this.getTypeValue() > otherCard.getTypeValue();
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true; // Kiểm tra nếu là cùng một đối tượng
		if (obj == null || getClass() != obj.getClass())
			return false; // Kiểm tra null hoặc khác lớp

		Card otherCard = (Card) obj; // Downcasting
		return value == otherCard.value && type == otherCard.type; // So sánh cả value và type
	}

	// Các getter và setter
	public Value getValue() {
		return value;
	}

	public void setValue(Value value) {
		this.value = value;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
