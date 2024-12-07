package application;

import javafx.application.Application;
import javafx.geometry.Pos;
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
	private TienLenMienNam tienLenMienNam = new TienLenMienNam(3, 0);

	@Override
	public void start(Stage primaryStage) {

		VBox root = new VBox(10);
		root.setStyle("-fx-padding: 25;");
		Scene scene = new Scene(root, 805, 500);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Tiến Lên Miền Nam");
		Label statusLabel1 = new Label("Chọn bài để chơi");
		Label statusLabel2 = new Label("Trên bàn chưa có gì!");
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
					cardButton.setStyle("-fx-padding: 13; -fx-background-color: lightblue;");
//					cardButton.setOnMouseEntered(
//							e -> cardButton.setStyle("-fx-padding: 15; -fx-background-color: pink;"));
//					cardButton.setOnMouseExited(
//							e -> cardButton.setStyle("-fx-padding: 15; -fx-background-color: lightblue;"));

					cardButton.setOnAction(e -> {
						if (tienLenMienNam.getSelectionCard().getCards().contains(card)) {
							tienLenMienNam.deselectCard(card);
							cardButton.setStyle("-fx-padding: 13; -fx-background-color: lightblue;");

						} else {
							tienLenMienNam.setSelectionCard(card);
							cardButton.setStyle("-fx-padding: 10; -fx-background-color: lightgreen;");
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

				if (tienLenMienNam.isTheEnd()) {
					// Khi kết thúc trò chơi, chuyển sang Scene mới chứa nút Restart
					VBox endGameLayout = new VBox(20);
					endGameLayout.setStyle("-fx-padding: 25;");
					endGameLayout.setAlignment(Pos.CENTER);
					String temp = new String("Thứ tự xếp hạng chiến thắng: ");
					tienLenMienNam.rankToString();
					Label winnerLabel = new Label(temp);
					winnerLabel.setFont(Font.font("Arial", 20));
					Label endGameLabel = new Label("Trò chơi kết thúc!");
					endGameLabel.setFont(Font.font("Arial", 30));
					Button restartButton = new Button("Restart");

					restartButton.setOnAction(restartEvent -> {
						tienLenMienNam.resetGame(); // Khởi động lại trò chơi
						statusLabel1.setText("Trò chơi đã được khởi động lại!");
						statusLabel2.setText("Trên bàn chưa có gì!!!");
						updateCardPane.run();
						primaryStage.setScene(scene);
					});
					endGameLayout.getChildren().addAll(winnerLabel, endGameLabel, restartButton);
					Scene endGameScene = new Scene(endGameLayout, 500, 300);
					primaryStage.setScene(endGameScene);
				}

				// Hiển thị text của các lá bài đã chọn
				StringBuilder selectedCardsText = new StringBuilder();
				for (Card card : tienLenMienNam.getSelectionCard().getCards()) {
					selectedCardsText.append(card.toString()).append(", ");
				}
				if (selectedCardsText.length() > 0) {
					selectedCardsText.setLength(selectedCardsText.length() - 2); // Xóa dấu ", " cuối cùng
				}
				statusLabel2.setText("Bài trên bàn:   " + selectedCardsText.toString());
				statusLabel2.setFont(Font.font("Arial", 20));
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

		Button cancelButton = new Button("Cancel");
		cancelButton.setOnAction(e -> {
			if (tienLenMienNam.getSelectionCard().getCards().isEmpty()) {
				statusLabel1.setText("Hãy chọn bài của bạn!");
			} else {
				tienLenMienNam.cancel();
				statusLabel1.setText("Đã bỏ chọn");
			}
			updateCardPane.run();
		});

		root.getChildren().addAll(playerLabel, cardPane, playButton, skipButton, cancelButton, statusLabel1,
				statusLabel2);

		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}