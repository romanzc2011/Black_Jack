package game;

import java.awt.geom.RoundRectangle2D;

// Name: Roman Campbell
// Project: Black_Jack
// A basic game of blackjack, 21 makes a winner, anything above is a bust

import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application 
{
	private static ArrayList<Cards> cardPlayerHand;
	private static ArrayList<Cards> cardDealerHand;
	private static GameButton dealButton;
	private static Cards card;
	private static Integer cardOne;
	private static Integer cardTwo;
	private static ScoreChecker scoreChecker;
	private static HBox playerHand;
	private static HBox dealerHand;
	private static Label playerWinningsLabel;
	private static Label playerBetLabel;
	private static Pane winContainer;
	private static Pane loseContainer;
	private TextInputDialog prompt;
	private ImageView blankView;
	private static Cards blankCard;
	private DeckCards cardDeck;
	private Integer playerScore = 0;
	private Integer dealerScore = 0;
	private Integer playerWins 	= 0;
	private Integer playerLoses = 0;
	private NumberFormat currencyFormat;
	private Double betAmount = 0.0;
	private Double betWinnings = 0.0;
	private String betFormatted;
	private String betForLabel;
	private Integer dealerCardCount = 0;
	private Integer playerCardCount = 0;
	private Integer firstRound = 0;
	private static Label winLabel;
	private static Label loseLabel;
	private boolean firstTwoRoundsOver = false;
	private boolean loop = true;
	private static boolean canHit;
	private static boolean canDeal;
	private boolean canBet = false;
	private boolean gameStatus = false;
	private boolean playerStood = false;
	private boolean isBlankCardAdded = false;
	private static Integer overallRound = 0;
	
	public void start(Stage primaryStage) throws FileNotFoundException
	{
		// Instantiate card deck
		cardDeck 						= new DeckCards();
		card							= new Cards();
		cardDealerHand 					= new ArrayList<Cards>();
		cardPlayerHand 					= new ArrayList<Cards>();
		blankCard 						= new Cards();
		blankView 						= blankCard.displayBlankCardView();
		scoreChecker 					= new ScoreChecker();
		prompt 							= new TextInputDialog();
		
		prompt.setTitle("Black Jack");
		prompt.setHeaderText("Enter Y or N");
		prompt.setContentText("Play again?");
		
		// ###############################################################################
		// BUTTONS
		GameButton quitButton 		= new GameButton("Quit", "20", 50, 100, "quit", cardDeck);
		dealButton 					= new GameButton("Deal", "20", 50, 100, "deal", cardDeck);
		GameButton hitButton 		= new GameButton("Hit Me", "20", 50, 100, "hit", cardDeck);
		GameButton standButton 		= new GameButton("Stand", "20", 50, 100, "hit", cardDeck);
		GameButton shuffleButton 	= new GameButton("Shuffle", "20", 50, 100, "shuffle", cardDeck);
		
		// ###############################################################################
		// BORDER PANE CONTROLS
		
		BorderPane root = new BorderPane();
		root.setPadding(new Insets(10,50,50,50));
		
		// ###############################################################################
		// TOP LEFT HBOX - DEALER HAND
		dealerHand = new HBox();
		
		Label dealerHandLabel = new Label("Dealer Hand:");
		dealerHandLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		
		dealerHand.setPadding(new Insets(10,10,10,10));
		dealerHand.setSpacing(20);
		
		// ###############################################################################
		
		
		// ###############################################################################
		// BOTTOM LEFT HBOX - PLAYER HAND
		playerHand 					= new HBox();
		
		Label playerHandLabel 		= new Label("Player Hand:");
		playerBetLabel 				= new Label("Current Bet:");
		playerWinningsLabel 		= new Label("Total Winnings:");
		
		playerWinningsLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		playerHandLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		playerBetLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		
		playerHand.setPadding(new Insets(10,10,10,10));
		playerHand.setSpacing(20);
		
		
		// ###############################################################################
		// TOP LEFT HBOX
		
		HBox topLButtons = new HBox();
		topLButtons.setPadding(new Insets(10,10,10,10));
		topLButtons.setSpacing(20);
		topLButtons.getChildren().addAll(
										shuffleButton.getButton(),
										dealButton.getButton());
		
		// ###############################################################################
		// BOTTOM LEFT HBOX
		
		HBox bottomLButtons = new HBox();
		bottomLButtons.setPadding(new Insets(10,10,10,10));
		bottomLButtons.setSpacing(20);
		bottomLButtons.getChildren().addAll(quitButton.getButton(),  
											hitButton.getButton(),
											standButton.getButton()
											);
		
		// ###############################################################################
		// TOP CENTER HBOX
		winLabel 	= new Label("Wins: " + playerWins);
		loseLabel 	= new Label("Loses: " + playerLoses);
		
		winLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		loseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		
		// Red background to loser and Light green background to winner
		winContainer 	= new StackPane();
		loseContainer 	= new StackPane();
		
		// Set background with fill
		winContainer.setBackground(new Background(new BackgroundFill(Color.LIGHTGREEN,null,null)));
		loseContainer.setBackground(new Background(new BackgroundFill(Color.LIGHTCORAL,null,null)));
		
		// Create appropriate size
		winContainer.setPrefSize(200, 50);
		loseContainer.setPrefSize(200, 50);
		
		// Add labels to containers
		winContainer.getChildren().add(winLabel);
		loseContainer.getChildren().add(loseLabel);
		
		// Round corners
		winContainer.setStyle("-fx-background-radius: 20; "
							+ "-fx-background-color: lightgreen;");
		
		loseContainer.setStyle("-fx-background-radius: 20;"
							+ "-fx-background-color: lightcoral;");
		
		// Center the labels
		StackPane.setAlignment(winLabel, Pos.CENTER);
		StackPane.setAlignment(loseLabel, Pos.CENTER);
		
		// Setup backgrounds for flash animation on wins and loses
		BackgroundFill winFill = new BackgroundFill(Color.LIGHTGREEN, null, null);
		BackgroundFill loseFill = new BackgroundFill(Color.LIGHTCORAL, null, null);
		
		
				

		VBox middleRightVBox = new VBox();
		middleRightVBox.setPadding(new Insets(10,10,10,10));
		middleRightVBox.setSpacing(20);
		
		middleRightVBox.getChildren().addAll(winContainer,loseContainer);
		
		root.setRight(middleRightVBox);
		BorderPane.setAlignment(middleRightVBox, javafx.geometry.Pos.TOP_CENTER);
		
		// ###############################################################################
		// TOP LEFT VBOX
		VBox topLeftVBox = new VBox();
		topLeftVBox.getChildren().add(dealerHandLabel);
		topLeftVBox.getChildren().add(dealerHand);
		topLeftVBox.getChildren().add(topLButtons);
		
		root.setTop(topLeftVBox);
		BorderPane.setAlignment(topLeftVBox, javafx.geometry.Pos.TOP_LEFT);
		
		// ###############################################################################
		// BOTTOM LEFT VBOX
		VBox bottomLeftVBox = new VBox();
		bottomLeftVBox.getChildren().add(playerBetLabel);
		bottomLeftVBox.getChildren().add(playerHandLabel);
		bottomLeftVBox.getChildren().add(playerWinningsLabel);
		bottomLeftVBox.getChildren().add(playerHand);
		bottomLeftVBox.getChildren().add(bottomLButtons);
		
		root.setBottom(bottomLeftVBox);
		BorderPane.setAlignment(bottomLeftVBox, javafx.geometry.Pos.BOTTOM_LEFT);
		
		// Set BorderPane background color #################################################################
		root.setStyle("-fx-background-color: green");
		
		Scene scene = new Scene(root,1000,1500);
		
		
		// ###############################################################################
		// ###############################################################################
		// BUTTON ACTIONS
		// ###############################################################################
		// ###############################################################################
		
		
		// SHUFFLE
		shuffleButton.getButton().setOnAction(new EventHandler<ActionEvent>() 
		{

			public void handle(ActionEvent event)
			{
				cardDeck.shuffleDeck();
			}
			
		});
		
		// STAND
		standButton.getButton().setOnAction(new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event)
			{
				
				// method not used here as program was crashing after 2 rounds
				cardDeck.clearGameCards();
				cardPlayerHand.clear();
				cardDealerHand.clear();
				
				try {
					
					card = cardDeck.getCards(1).get(0);
					cardDealerHand.add(card);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if (card.getCardValue() != null) 
				{
					dealerScore += card.getCardValue();
				}
				System.out.println(dealerScore);
				// Remove cards from deck that are used
				cardDeck.removeCard(1);
				
				// DISPLAY BLANK CARD AT END OF HAND OF DEALER
				dealerHand.getChildren().remove(blankView);
				dealerHand.getChildren().add(card.displayCardView());
				dealerHand.getChildren().add(blankView);
				
				// DEALER OVER 21
				if(dealerScore > 21)
				{
					playerWins++;
					betWinnings += betAmount;
					updateLabels(playerWins,playerLoses,betWinnings);
					System.out.println("Player won");
					
					if(playAgainPrompt())
					{
						try {
							clearAll();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				// PLAYER OVER 21
				if(playerScore > 21)
				{
					playerLoses++;
					betWinnings -= betAmount;
					updateLabels(playerWins, playerLoses, betWinnings);
					System.out.println("Player lost");
					
					if(playAgainPrompt())
					{
						try {
							clearAll();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				// PLAYER > DEALER AND UNDER/AT 21
				if((playerScore > dealerScore) && (playerScore <= 21))
				{
					playerWins++;
					betWinnings += betAmount;
					System.out.println("Player won");
					updateLabels(playerWins, playerLoses, betWinnings);
					
					if(playAgainPrompt())
					{
						try {
							
							clearAll();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				// DEALERSCORE > PLAYERS AND DEALER UNDER/AT 21
				if((playerScore < dealerScore) && (dealerScore <= 21))
				{
					playerLoses++;
					betWinnings -= betAmount;
					updateLabels(playerWins,playerLoses,betWinnings);
					System.out.println("Dealer won");
					if(playAgainPrompt())
					{
						try {
							clearAll();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
				// PUSH
				if(playerScore == dealerScore)
				{
					System.out.println();
					System.out.println("Push");
					if(playAgainPrompt())
					{
						try {
							clearAll();
						} catch (FileNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				
			}
		});
		
		// QUIT
		quitButton.getButton().setOnAction(new EventHandler<ActionEvent>() {
			
			public void handle(ActionEvent event)
			{
				System.exit(0);
			}
		});
		
		// ###############################################################################
		// ###############################################################################
		// GAME LOGIC
		// ###############################################################################
		// ###############################################################################
		if(overallRound == 0)
		{ 
			canDeal = true;
		}
		
		
		// ###############################################################################
		// ###############################################################################
		// EXECUTE FIRST 2 ROUNDS, DEAL BUTTON STARTS GAME, AFTER 2 ROUNDS SWITCH TO REGULAR
		// PLAY
		// ###############################################################################
		// ###############################################################################
		
		
		// DEAL BUTTON EVENT HANDLER
		// anytime dealer deals itself a card
		// put blank card in front of cards
		
		dealButton.getButton().setOnAction(new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event)
			{
				// First two rounds and then make canDeal false until player hits or stands
				if(!firstTwoRoundsOver)
				{
					handleFirstTwoRounds();
					firstTwoRoundsOver = true;
					canDeal = false;
				}
				
			}
		
		});
		
		
		// ###############################################################################
		// HIT 
		
		// Allow player to bet before starting
		if(overallRound == 0)
		{
			if(overallRound == 0 || canBet == true)
			{
				betForLabel = bet();
				playerBetLabel.setText("Current Bet: " + betForLabel);
				
			}
		}
		
		
		hitButton.getButton().setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{

				if(canHit)
				{
					System.out.println(cardDeck.getDeckSize());
					handleHitButton();
					// ###############################################################################
					// Check for Winner - WORKING
					if(gameStatus)
					{
						updateLabels(scoreChecker.getPlayerWins(),scoreChecker.getPlayerLoses(),scoreChecker.getPlayerWinnings());
						if(playAgainPrompt())
						{
							try {
								clearAll();
								String newBet = bet();
								playerBetLabel.setText("Current Bet: "+newBet);
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {
						singleDealerTurn();
					}
					
					System.out.println("playerscore: "+playerScore);
				}
				
						
			}
		});
		
		winLabel.setText("Wins: " + playerWins);
		loseLabel.setText("Loses: " + playerLoses);
		
		
		// ###############################################################################
		// ###############################################################################
		// GAME LOGIC - END
		// ###############################################################################
		// ###############################################################################
		
		
		// ###############################################################################
		// STAGE
		primaryStage.setTitle("Black Jack by Roman Campbell");
		primaryStage.setResizable(false);
		primaryStage.setScene(scene);
		primaryStage.show();
	}
	
	
	// ###############################################################################
	// ###############################################################################
	// FUNCTIONS
	// ###############################################################################
	// ###############################################################################
	
	
	// ###############################################################################
	// UPDATE LABELS
	public void updateLabels(int wins, int loses, double betWinnings)
	{
		winLabel.setText("Wins: " + wins);
		loseLabel.setText("Loses: " + loses);
		playerWinningsLabel.setText("Total Winnings: " + currencyConverter(betWinnings));
	}
	
	// ###############################################################################
	// CLEAR PREVIOUS CARDS
	// Used to prevent the same card being displayed from previous round
	public void clearAllHands()
	{
		// Clear visible cards
		playerHand.getChildren().clear();
		dealerHand.getChildren().clear();
	}
	
	
	
	// ###############################################################################
	// BET FUNCTION
	public String bet()
	{
		// SET UP BET PROMPT, CLEAR LAST TEXT FROM PROMPT
		prompt.setTitle("Bet");
    	prompt.setHeaderText("Place your bet of $2 to $500");
    	prompt.setContentText("Enter amount:");
    	prompt.getEditor().clear();
    	Optional<String> betStr = prompt.showAndWait();
    	
    	// Convert prompt string into dollar amount
    	try {
    		if(betStr.isPresent())
    		{
    			String betString = betStr.get();
    			betAmount += betAmount;
    			betAmount = Double.parseDouble(betString);
    			playerBetLabel.setText(currencyConverter(betAmount));
    		} else {
    			System.out.println("No amount entered");
    			betAmount = 0.0;
    		}
    	} catch (NumberFormatException e) {
    		System.out.println("Not a representation of a dollar amount");
    		betAmount = 0.0;
    	}
    	
    	// Format into US currency
    	currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
    	betFormatted = currencyFormat.format(betAmount);
    	
    	return betFormatted;
	}
	
	// ###############################################################################
	// METHOD TO HANDLE HIT ACTION FROM PLAYER
	int count = 0;
	public void handleHitButton()
	{
		
		// Take card
		try {
			card = cardDeck.getCards(1).get(0);
			cardPlayerHand.add(card);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		// Remove cards used
		cardDeck.removeCard(1);
		
		// Card is drawn and displayed
		playerHand.getChildren().add(card.displayCardView());
		
		// Prevent random null pointer exceptions i was getting
		if (card.getCardValue() != null) 
		{
			playerScore += card.getCardValue();
		}
		
		gameStatus = scoreChecker.checkScore(card, dealerScore, playerScore, cardDealerHand, cardDeck, dealerHand, playerStood, betAmount);

		cardDeck.clearGameCards();
		
		cardPlayerHand.clear();
		cardDealerHand.clear();
		
		// At end of hit, make dealing possible and hitting not possible
		canHit = false;
		canDeal = true;
	}
	
	// ###############################################################################
	// METHOD TO HANDLE FIRST FEW ROUNDS
	public void handleFirstTwoRounds()
	{
		
		cardDeck.clearGameCards();
		cardPlayerHand.clear();
		cardDealerHand.clear();
		
		if(canDeal)
		{
			while(loop)
			{
				
				if((overallRound > 2 && playerCardCount == 1) && (overallRound > 2 && dealerCardCount == 1)) 
				{
					canHit = true;
					canDeal = false;
					loop = false;
				}
				
				// ###############################################################################
				// PLAYER CARDS SECTION 
		
				try {
					cardPlayerHand.addAll(cardDeck.getCards(1));
					
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				// Remove cards used
				cardDeck.removeCard(1);

				for (Cards card : cardPlayerHand)
				{
				    playerHand.getChildren().add(card.displayCardView());

				    
				    if (card.getCardValue() != null) 
				    {
				    	// Get the first two cards to test for Natural Black Jack
				        playerScore += card.getCardValue();
				    }
				
				
				}
				
				
				// ###############################################################################
			    // NATURAL BLACKJACK TEST
				
				
				if(overallRound == 0)
				{
					cardOne = cardPlayerHand.get(0).getCardValue();
				}
				
				if(overallRound == 1)
				{
					cardTwo = cardPlayerHand.get(0).getCardValue();
					if(isNaturalBlackJack(cardOne, cardTwo, playerWins, betAmount, betWinnings))
					{
						if(playAgainPrompt())
						{
							try {
								updateLabels(playerWins, playerLoses, betWinnings);
								clearAll();
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					System.out.println("cardOne: "+cardOne);
					System.out.println("cardTwo: "+cardTwo);
				}
					
				// ###############################################################################
				
				
				// ###############################################################################
				// CHECK FOR WINNER
				gameStatus = scoreChecker.checkScore(card, dealerScore, playerScore, cardDealerHand, cardDeck, dealerHand, playerStood, betAmount);
				
				winCheck(gameStatus);
				System.out.println(firstRound);
				
				System.out.println("Player score: "+playerScore);
				System.out.println("Dealer score: "+dealerScore);
				cardDeck.clearGameCards();
				
				cardPlayerHand.clear();
				cardDealerHand.clear();
				
				// ###############################################################################
				// DEALER CARDS SECTION
				try {
					cardDealerHand.addAll(cardDeck.getCards(1));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Remove cards from deck that are used
				cardDeck.removeCard(1);
				
				for(Cards card : cardDealerHand)
				{
					if(overallRound == 1)
					{
						if(isBlankCardAdded)
						{
							dealerHand.getChildren().remove(blankView);
						}
						dealerHand.getChildren().add(blankView);
						isBlankCardAdded = true;
						break;
					}
					
					dealerHand.getChildren().add(card.displayCardView());
					
					// Handle random NullpointExceptions
					if(card.getCardValue() != null)
					{
						dealerScore += card.getCardValue();
					}
					
					dealerCardCount++;
					
				}
				
				// ###############################################################################
				// CHECK FOR WINNER
				gameStatus = scoreChecker.checkScore(card, dealerScore, playerScore, cardDealerHand, cardDeck, dealerHand, playerStood, betAmount);
				winCheck(gameStatus);
				
				cardDeck.clearGameCards();
				
				// Cards for dealer face down after 1st round
				overallRound++;
				firstRound++;
				System.out.println("DealerScore: " + dealerScore);
				
				// DISPLAY BLANK CARD AT END OF HAND OF DEALER
				if(overallRound > 1)
				{
					if(isBlankCardAdded)
					{
						dealerHand.getChildren().remove(blankView);
					}
					dealerHand.getChildren().add(blankView);
					isBlankCardAdded = true;
				}
			
				if(firstRound == 2)
				{
					dealerCardCount = 0;
					canHit = true;
					canDeal = false;
					break;
				}
			}
		}
	}
	
	// ###############################################################################
	// REGULAR ROUND, EACH PLAYER GETS CARD FACE UP
	public void regularRoundDeal()
	{
		try {
			card = cardDeck.getCards(1).get(0);
			cardPlayerHand.add(card);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		// Remove cards used
		cardDeck.removeCard(1);
		
		// Card is drawn and displayed
		playerHand.getChildren().add(card.displayCardView());
		playerScore += card.getCardValue();
		
		
		
		// ###############################################################################
		// CHECK FOR WINNER
		gameStatus = scoreChecker.checkScore(card, dealerScore, playerScore, cardDealerHand, cardDeck, dealerHand, playerStood, betAmount);
		winCheck(gameStatus);
		
		cardDeck.clearGameCards();
		
		cardPlayerHand.clear();
		cardDealerHand.clear();
		
		// ###############################################################################
		// DEALER CARDS SECTION
		try {
			card = cardDeck.getCards(1).get(0);
			cardDealerHand.add(card);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Remove cards from deck that are used
		cardDeck.removeCard(1);
		
		// DISPLAY BLANK CARD AT END OF HAND OF DEALER
		dealerHand.getChildren().remove(blankView);
		dealerHand.getChildren().add(card.displayCardView());
		dealerHand.getChildren().add(blankView);
		dealerScore += card.getCardValue();
		dealerCardCount++;
		
		System.out.println("Player score: "+playerScore);
		System.out.println("Dealer score: "+dealerScore);
		
		
		// Check for winner
		gameStatus = scoreChecker.checkScore(card, dealerScore, playerScore, cardDealerHand, cardDeck, dealerHand, playerStood, betAmount);
		winCheck(gameStatus);
						
	
		cardDeck.clearGameCards();
		canHit = true;
	}
	
	// ###############################################################################
	// DEALER DRAW ONE CARD
	public void singleDealerTurn()
	{
		try {
			card = cardDeck.getCards(1).get(0);
			cardDealerHand.add(card);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Remove cards from deck that are used
		cardDeck.removeCard(1);
		
		// DISPLAY BLANK CARD AT END OF HAND OF DEALER
		dealerHand.getChildren().remove(blankView);
		dealerHand.getChildren().add(card.displayCardView());
		
		if (dealerHand != null) {
		    dealerHand.getChildren().add(blankView);
		} 
		
		// Handle random null pointer exception
		if(card.getCardValue() != null)
		{
			dealerScore += card.getCardValue();
		}
		
		dealerCardCount++;
		
		// Check for winner
		gameStatus = scoreChecker.checkScore(card, dealerScore, playerScore, cardDealerHand, cardDeck, dealerHand, playerStood, betAmount);
		winCheck(gameStatus);
				
		System.out.println("Player score: "+playerScore);
		System.out.println("Dealer score: "+dealerScore);
		cardDeck.clearGameCards();
		canHit = true;
	}
	
	// ###############################################################################
	// CLEAR ALL VARIABLE FOR NEW GAME
	public void clearAll() throws FileNotFoundException
	{
		firstTwoRoundsOver = false;
		canDeal = true;
		canHit = false;
		loop = true;
		firstRound = 0;
		cardDealerHand = null;
		cardPlayerHand = null;
		cardDealerHand = new ArrayList<Cards>();
		cardPlayerHand = new ArrayList<Cards>();
		dealerHand.getChildren().clear();
		playerHand.getChildren().clear();
		cardPlayerHand.clear();
		cardDealerHand.clear();
		dealerScore = 0;
		playerScore = 0;
		overallRound = 0;
		dealerCardCount = 0;
		cardDeck.clearGameCards();
		cardDeck = null;
		cardDeck = new DeckCards();
		cardDeck.shuffleDeck();
		String newBet = bet();
		playerBetLabel.setText("Current Bet: "+newBet);
		dealButton.getButton().fire();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// ###############################################################################
	// PLAY AGAIN PROMPT
	public boolean playAgainPrompt()
	{
		prompt.setTitle("Black Jack");
    	prompt.setHeaderText("Play Again?");
    	prompt.setContentText("Enter Y or N");
    	prompt.getEditor().clear();
    	
    	Optional<String> result = prompt.showAndWait();
    	
    	if(result.isPresent() && result.get().toLowerCase().equals("y"))
    	{
    		return true;
    	} else {
    		System.exit(0);
    	}
    	return false;
	}
	
	
	
	// ###############################################################################
	// CURRENCY CONVERTER
	public String currencyConverter(Double amount)
	{
		Currency currency = Currency.getInstance(Locale.US);
		NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.US);
		currencyFormat.setCurrency(currency);
		
		String formattedAmount = currencyFormat.format(amount);
		
		return formattedAmount;
	}
	
	// ###############################################################################
	// WIN CHECKER
	public void winCheck(boolean gameStatus)
	{
		if(gameStatus)
		{
			updateLabels(scoreChecker.getPlayerWins(),scoreChecker.getPlayerLoses(),scoreChecker.getPlayerWinnings());
			if(playAgainPrompt())
			{
				try {
					updateLabels(scoreChecker.getPlayerWins(),scoreChecker.getPlayerLoses(),scoreChecker.getPlayerWinnings());
					clearAll();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	// ###############################################################################
	// NATURAL BLACK JACK CONDITION
	// Test for Natural BlackJack, one card is face or ten card and other is ace, vice versa
	public boolean isNaturalBlackJack(Integer cardOne, Integer cardTwo, Integer playerWins, Double betAmount, Double betWinnings)
	{
		if((cardOne == 10 && cardTwo == 11) || (cardTwo == 10 && cardOne == 11))
		{
			System.out.println("Natural Black Jack!");
			playerWins++;
			betWinnings += (betAmount + (betAmount / 2));
			
			return true;
		}
		
		return false;
	}

	public static void main(String[] args)
	{
		launch(args);
	}
	
}
