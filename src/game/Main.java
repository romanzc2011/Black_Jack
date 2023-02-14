package game;

// Name: Roman Campbell
// Project: Black_Jack
// A basic game of blackjack, 21 makes a winner, anything above is a bust

import java.io.FileNotFoundException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Currency;
import java.util.Locale;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Main extends Application 
{
	private static ArrayList<Cards> cardPlayerHand;
	private static ArrayList<Cards> cardDealerHand;
	private static ArrayList<Cards> cardDealerList;
	private static GameButton dealButton;
	private static Cards card;
	private static ScoreChecker scoreChecker;
	private static HBox playerHand;
	private static HBox dealerHand;
	private static Label playerWinningsLabel;
	private static Label playerBetLabel;
	private TextInputDialog prompt;
	private boolean playAgain = false;
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
		cardDealerList 					= new ArrayList<Cards>();
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
		dealButton 		= new GameButton("Deal", "20", 50, 100, "deal", cardDeck);
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
		playerBetLabel 			= new Label("Current Bet:");
		playerWinningsLabel 	= new Label("Total Winnings:");
		
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
		winLabel = new Label("Wins: " + playerWins);
		loseLabel = new Label("Loses: " + playerLoses);
		
		winLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
		loseLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));

		HBox middleRightHBox = new HBox();
		middleRightHBox.setPadding(new Insets(10,10,10,10));
		middleRightHBox.setSpacing(20);
		
		middleRightHBox.getChildren().addAll(winLabel,loseLabel);
		
		root.setRight(middleRightHBox);
		BorderPane.setAlignment(middleRightHBox, javafx.geometry.Pos.TOP_CENTER);
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
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		});
		
		// STAND
		standButton.getButton().setOnAction(new EventHandler<ActionEvent>() 
		{
			public void handle(ActionEvent event)
			{
				System.out.println("Dealer must stand now");
				standDealerLastTurn();
				
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
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
		// When dealing cards, if passed round one, anytime dealer deals itself a card
		// put blank card in front of cards
		
		// Is it dealer's turn
		dealButton.getButton().setOnAction(new EventHandler<ActionEvent>() 
		{
			
			public void handle(ActionEvent event)
			{
				// Make sure all variables are updated before continuing
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// First two rounds and then make canDeal false until player hits or stands
				if(!firstTwoRoundsOver)
				{
					handleFirstTwoRounds();
					firstTwoRoundsOver = true;
					canDeal = false;
				}
				
				if(canDeal && firstTwoRoundsOver)
				{
					regularRoundDeal();
				}
				
			}
		
		});
		
		
		// ###############################################################################
		// HIT 
		
		// Allow player to bet before starting
		if(overallRound == 0 || canBet == true)
		{
			betForLabel = bet();
			playerBetLabel.setText("Current Bet: " + betForLabel);
			
		}
		
		hitButton.getButton().setOnAction(new EventHandler<ActionEvent>()
		{
			public void handle(ActionEvent event)
			{
				// Make sure all variables are updated before continuing
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(canHit)
				{
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
	
	public void updateLabels(int wins, int loses, double betWinnings)
	{
		winLabel.setText("Wins: " + wins);
		loseLabel.setText("Loses: " + loses);
		playerWinningsLabel.setText("Total Winnings" + currencyConverter(betWinnings));
	}
	
	public void clearAllHands()
	{
		// Clear visible cards
		playerHand.getChildren().clear();
		dealerHand.getChildren().clear();
	}
	
	public boolean checkGameStatus(boolean gameOver)
	{
		System.out.println("checkGameStatus");
		boolean playerWonOrLost = false;
		// Did someone win?
		if(gameOver)
		{
			// Was it the player and if so ask to play again
			if(scoreChecker.getDidPlayerWin() || scoreChecker.getDidPlayerLose())
			{
				updateLabels(scoreChecker.getPlayerWins(),scoreChecker.getPlayerLoses(),scoreChecker.getPlayerWinnings());
				
				return playerWonOrLost;
			}
		} 
		return playerWonOrLost;
	}
	
	
	public String bet()
	{
		prompt.setTitle("Bet");
    	prompt.setHeaderText("Place your bet of $2 to $500");
    	prompt.setContentText("Enter amount:");
    	
    	Optional<String> betStr = prompt.showAndWait();
    	
    	// Convert prompt string into dollar amount
    	try {
    		if(betStr.isPresent())
    		{
    			String betString = betStr.get();
    			betAmount += betAmount;
    			betAmount = Double.parseDouble(betString);
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
		gameStatus = scoreChecker.checkScore(card, dealerScore, playerScore, cardDealerList, cardDeck, dealerHand, playerStood, betAmount);
		if(gameStatus)
		{
			if(scoreChecker.getDidPlayerWin())
			{
				betWinnings += betAmount;
				updateLabels(scoreChecker.getPlayerWins(),scoreChecker.getPlayerLoses(),scoreChecker.getPlayerWinnings());
				if(scoreChecker.playAgainPrompt())
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
			}
		}
		cardDeck.clearGameCards();
		
		cardPlayerHand.clear();
		cardDealerList.clear();
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
		cardDealerList.clear();
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

				for (Cards card : cardPlayerHand) {
				    playerHand.getChildren().add(card.displayCardView());

				    if (card.getCardValue() != null) {
				        playerScore += card.getCardValue();
				    }

				    gameStatus = scoreChecker.checkScore(card, dealerScore, playerScore, cardDealerList, cardDeck, dealerHand, playerStood, betAmount);
				    if (gameStatus) {
				        if (scoreChecker.getDidPlayerWin()) {
				            betWinnings += betAmount;
				            updateLabels(scoreChecker.getPlayerWins(), scoreChecker.getPlayerLoses(), scoreChecker.getPlayerWinnings());
				            if (scoreChecker.playAgainPrompt()) {
				                try {
				                    clearAll();
				                    String newBet = bet();
				                    playerBetLabel.setText("Current Bet: "+newBet);
				                } catch (FileNotFoundException e) {
				                    e.printStackTrace();
				                }
				            }
				            clearAllHands();
				        }
				    }
				}
				
				System.out.println("Player score: "+playerScore);
				System.out.println("Dealer score: "+dealerScore);
				cardDeck.clearGameCards();
				
				cardPlayerHand.clear();
				cardDealerList.clear();
				
				// ###############################################################################
				// DEALER CARDS SECTION
				try {
					cardDealerList.addAll(cardDeck.getCards(1));
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// Remove cards from deck that are used
				cardDeck.removeCard(1);
				
				for(Cards card : cardDealerList)
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
					
					gameStatus = scoreChecker.checkScore(card, dealerScore, playerScore, cardDealerList, cardDeck, dealerHand, playerStood, betAmount);
				}
			
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
		// Check for Winner - WORKING
		gameStatus = scoreChecker.checkScore(card, dealerScore, playerScore, cardDealerList, cardDeck, dealerHand, playerStood, betAmount);
		if(gameStatus)
		{
			updateLabels(scoreChecker.getPlayerWins(),scoreChecker.getPlayerLoses(),scoreChecker.getPlayerWinnings());
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
		
		cardDeck.clearGameCards();
		
		cardPlayerHand.clear();
		cardDealerList.clear();
		
		// ###############################################################################
		// DEALER CARDS SECTION
		try {
			card = cardDeck.getCards(1).get(0);
			cardDealerList.add(card);
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
		}
	
		cardDeck.clearGameCards();
		canHit = true;
	}
	
	public void singleDealerTurn()
	{
		try {
			card = cardDeck.getCards(1).get(0);
			cardDealerList.add(card);
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
		
		gameStatus = scoreChecker.checkScore(card, dealerScore, playerScore, cardDealerList, cardDeck, dealerHand, playerStood, betAmount);
		
		// ###############################################################################
		// Check for Winner - WORKING
		if(gameStatus)
		{
			updateLabels(scoreChecker.getPlayerWins(),scoreChecker.getPlayerLoses(),scoreChecker.getPlayerWinnings());
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
		System.out.println("Player score: "+playerScore);
		System.out.println("Dealer score: "+dealerScore);
		cardDeck.clearGameCards();
		canHit = true;
	}
	
	public DeckCards clearAll() throws FileNotFoundException
	{
		firstTwoRoundsOver = false;
		canDeal = true;
		canHit = false;
		loop = true;
		firstRound = 0;
		cardDealerList = null;
		cardPlayerHand = null;
		cardDealerList = new ArrayList<Cards>();
		cardPlayerHand = new ArrayList<Cards>();
		dealerHand.getChildren().clear();
		playerHand.getChildren().clear();
		cardPlayerHand.clear();
		cardDealerList.clear();
		dealerScore = 0;
		playerScore = 0;
		overallRound = 0;
		dealerCardCount = 0;
		cardDeck.clearGameCards();
		cardDeck = null;
		cardDeck = new DeckCards();
		cardDeck.shuffleDeck();
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return cardDeck;
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
	// STAND DEALER LAST TURN 
	public void standDealerLastTurn()
	{
		System.out.println("standDealerLastTurn");
		playerStood = true;
		// LAST TURN FOR DEALER, FIND OUT WHO WON
		try {
			card = cardDeck.getCards(1).get(0);
			cardDealerList.add(card);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Remove cards from deck that are used
		cardDeck.removeCard(1);
		
		// DISPLAY BLANK CARD AT END OF HAND OF DEALER
		dealerHand.getChildren().remove(blankView);
		dealerHand.getChildren().add(card.displayCardView());
		
		// Handle NullPointException that show up 
		if (dealerHand != null) {
		    dealerHand.getChildren().add(blankView);
		} else {
			System.out.println("Something else is wrong");
		}
		
		// Handle NullPointException that show up
		if(card.getCardValue() != null)
		{
			dealerScore += card.getCardValue();
		}
		
		dealerCardCount++;
		
		gameStatus = scoreChecker.checkScore(card, dealerScore, playerScore, cardDealerList, cardDeck, dealerHand, playerStood, betAmount);
		updateLabels(scoreChecker.getPlayerWins(),scoreChecker.getPlayerLoses(),scoreChecker.getPlayerWinnings());
		
		// Execute play againt prompt
		if(playAgainPrompt())
		{
			try {
				clearAll();
				String newBet = bet();
				playerBetLabel.setText("Current Bet: "+newBet);;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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

	public static void main(String[] args)
	{
		launch(args);
	}
	
}
