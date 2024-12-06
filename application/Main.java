package application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import model.Card;
import model.TienLenMienNam;

public class Main extends Application {
	private TienLenMienNam tienLenMienNam = new TienLenMienNam(3);

	@Override
	public void start(Stage primaryStage) {
		VBox root = new VBox(10);
		root.setStyle("-fx-padding: 25;");
		Label statusLabel1 = new Label("Chọn bài để chơi");
		Label statusLabel2 = new Label("Trên bàn đang có");
		Label playerLabel = new Label();

		FlowPane cardPane = new FlowPane(); // Dùng để hiển thị các nút
		cardPane.setHgap(10);
		cardPane.setVgap(10);

		Runnable updateCardPane = () -> {
			cardPane.getChildren().clear();

			// Đặt text cho playerLabel
			playerLabel.setText("Người chơi số " + (tienLenMienNam.getNowPlayerNum() + 1) + " - Số bài còn lại: "
					+ tienLenMienNam.getNowPlayer().size());
			// Tăng kích thước font chữ
			playerLabel.setFont(Font.font("Arial", 30)); // Font "Arial" và kích thước font 20

			if (tienLenMienNam.getNowPlayer() != null && tienLenMienNam.getNowPlayer().getCards() != null) {
				for (Card card : tienLenMienNam.getNowPlayer().getCards()) {
					Button cardButton = new Button(card.toString());
					cardButton.setStyle("-fx-padding: 10; -fx-background-color: lightgray;");
					cardButton.setOnAction(e -> {
						if (tienLenMienNam.getSelectionCard().getCards().contains(card)) {
							tienLenMienNam.deselectCard(card);
							cardButton.setStyle("-fx-background-color: lightgray;");
						} else {
							tienLenMienNam.setSelectionCard(card);
							cardButton.setStyle("-fx-background-color: lightgreen;");
						}
					});
					cardPane.getChildren().add(cardButton);
				}
			} else {
				System.err.println("Không thể cập nhật cardPane: Player hoặc Cards bị null.");
			}
		};

		updateCardPane.run();

		Button playButton = new Button("Hit");
		playButton.setOnAction(e -> {
			if (tienLenMienNam.isHit()) {
				tienLenMienNam.playGame(); // Đánh bài
				statusLabel1.setText("Đánh bài ");
				// Hiển thị text của các lá bài
				StringBuilder selectedCardsText = new StringBuilder();
				for (Card card : tienLenMienNam.getSelectionCard().getCards()) {
					selectedCardsText.append(card.toString()).append(", ");
				}
				if (selectedCardsText.length() > 0) {
					selectedCardsText.setLength(selectedCardsText.length() - 2); // Xóa dấu ", " cuối cùng
				}
				statusLabel2.setText("Bài trên bàn: " + selectedCardsText.toString());
				tienLenMienNam.getSelectionCard().removeDeck();
				updateCardPane.run(); // Cập nhật giao diện bài
			} else {
				statusLabel1.setText("Bài không hợp lệ! Vui lòng chọn lại.");
			}
		});

		Button skipButton = new Button("Skip");
		skipButton.setOnAction(e -> {

			if (tienLenMienNam.skip()) {
				statusLabel1.setText("Bỏ lượt ");
			} else {
				statusLabel1.setText("Không thể skip!!!");
			}
			updateCardPane.run();
		});

		root.getChildren().addAll(playerLabel, cardPane, playButton, skipButton, statusLabel1, statusLabel2);

		Scene scene = new Scene(root, 805, 500);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Tiến Lên Miền Nam");
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
